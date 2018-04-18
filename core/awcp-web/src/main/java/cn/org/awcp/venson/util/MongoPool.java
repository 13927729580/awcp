package cn.org.awcp.venson.util;

import com.mongodb.MongoClient;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * MongoDB连接池
 *
 * @author venson
 * @version 20180326
 */
public class MongoPool {


    /**
     * 日志对象
     */
    private final Log logger = LogFactory.getLog(getClass());

    private static final long DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS = 2*60 * 1000L;
    /**
     * 最大数量
     */
    private final static int DEFAULT_MAX = 20;
    /**
     * 最小数量
     */
    private final static int DEFAULT_MIN = 5;
    /**
     * 初始化大小
     */
    private final static int DEFAULT_INIT = DEFAULT_MIN;
    /**
     * mongo主机
     */
    private final static String DEFAULT_HOST = "127.0.0.1";
    /**
     * mongo端口
     */
    private final static int DEFAULT_PORT = 27017;


    /**
     * 用数组的方式存储
     */
    private MongoClientHolder[] mongoClientHolders = new MongoClientHolder[DEFAULT_MAX];

    /**
     * 当前计数
     */
    private volatile int count = 0;

    /**
     * 当前活动数
     */
    private volatile int active = 0;

    private int min = DEFAULT_MIN;
    private int max = DEFAULT_MAX;
    private int init = DEFAULT_INIT;
    private String host = DEFAULT_HOST;
    private int port = DEFAULT_PORT;


    /**
     * 锁
     */
    private ReentrantLock lock = new ReentrantLock();
    private Condition empty = lock.newCondition();
    private Condition notEmpty = lock.newCondition();


    public MongoPool() {
        this.init();
    }

    public MongoPool(String host, int port) {
        this.init();
        this.host = host;
        this.port = port;
    }

    /**
     * 初始化
     */
    private void init() {
        lock.lock();
        try {
            for (int i = count; i < init; i++) {
                put();
            }
            createAndStartCreatorThread();
            createAndStartDestoryThread();
        } finally {
            lock.unlock();
        }
    }

    private void createAndStartCreatorThread() {
        String threadName = "Mongo-ConnectionPool-Creator-" + System.identityHashCode(this);
        CreateConnectionThread createConnectionThread = new CreateConnectionThread(threadName);
        createConnectionThread.start();
    }

    private void createAndStartDestoryThread() {
        String threadName = "Mongo-ConnectionPool-Destory-" + System.identityHashCode(this);
        DestoryConnectionThread destoryConnectionThread = new DestoryConnectionThread(threadName);
        destoryConnectionThread.start();
    }

    public class DestoryConnectionThread extends Thread {


        private DestoryConnectionThread(String name) {
            super.setName(name);
            super.setDaemon(true);
        }

        @Override
        public void run() {
            for (; ; ) {
                try {
                    Thread.sleep(DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS);
                } catch (InterruptedException e) {
                    logger.debug("ERROR", e);
                }
                shrink();
            }
        }
    }

    public class CreateConnectionThread extends Thread {


        private CreateConnectionThread(String name) {
            super.setName(name);
            super.setDaemon(true);
        }

        @Override
        public void run() {
            for (; ; ) {
                produce();
            }
        }
    }

    /**
     * 根据活动数最大最小数进行生产
     */
    private void produce() {
        lock.lock();
        try {
            // 必须存在线程等待并且活动数和连接数大于最小数，才创建连接
            while (count >= active && active + count >= min) {
                logger.debug("必须存在线程等待并且活动数和连接数大于最小数，才创建连接" + this);
                empty.await();
            }
            int produce;
            if (active > max) {
                produce = max;
            } else {
                produce = active > min ? active : min;
            }
            for (; count < produce; ) {
                put();
            }
        } catch (InterruptedException e) {
            logger.debug("ERROR", e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 进行缩放，保持最小个数
     */
    private void shrink() {
        lock.lock();
        logger.debug("开始缩放");
        try {
            //清除空闲资源
            for (int shrink = active > min ? active : min; shrink < count; ) {
                MongoClientHolder mongoClientHolder = taskMongoClient();
                close(mongoClientHolder);
            }
            //清除过期资源
            for (int i = count - 1; i > -1; i--) {
                MongoClientHolder mongoClientHolder = mongoClientHolders[i];
                if (mongoClientHolder.isTimeout()) {
                    decrementCount();
                    Object[] temp = ArrayUtils.remove(mongoClientHolders, i);
                    System.arraycopy(temp, 0, mongoClientHolders, 0, temp.length);
                    Arrays.fill(mongoClientHolders, temp.length, max, null);
                    mongoClientHolders[i] = null;
                    close(mongoClientHolder);
                    empty.signal();
                }
            }

        } finally {
            lock.unlock();
        }
    }

    /**
     * 增加池中连接
     */
    private void put() {
        lock.lock();
        try {
            //如果生产已经超过最大值，则等待消耗后再生产
            while (count >= max) {
                logger.debug("当前使用的太多了，进行阻塞" + this);
                empty.await();
            }
            mongoClientHolders[count] = bulid();
            incrementCount();
            logger.debug("已经增加了，通知可以消耗" + this);
            notEmpty.signal();
        } catch (InterruptedException e) {
            logger.debug("ERROR", e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取池中连接
     *
     * @return MongoClient
     */
    public MongoClient get() {
        lock.lock();
        try {
            MongoClientHolder mongoClientHolder = MongoClientContext.get();
            if (mongoClientHolder != null) {
                return mongoClientHolder.getMongoClient();
            }
            incrementActive();
            mongoClientHolder = taskMongoClient();
            //判断是否已经过期
            while (mongoClientHolder.isTimeout()) {
                mongoClientHolder = taskMongoClient();
            }
            MongoClientContext.set(mongoClientHolder);
            return mongoClientHolder.getMongoClient();
        } finally {
            lock.unlock();
        }
    }

    private MongoClientHolder taskMongoClient() {
        lock.lock();
        try {
            //如果已经消耗没有了，则等待生产后再消耗
            while (count == 0) {
                logger.debug("池中已经没有了，进行阻塞" + this);
                try {
                    notEmpty.await();
                } catch (InterruptedException e) {
                    logger.debug("ERROR", e);
                }
            }
            decrementCount();
            MongoClientHolder mongoClientHolder = mongoClientHolders[count];
            mongoClientHolders[count] = null;
            logger.debug("已经消耗了，通知可以生产" + this);
            empty.signal();
            return mongoClientHolder;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 创建对象
     *
     * @return MongoClient
     */
    private MongoClientHolder bulid() {
        lock.lock();
        try {
            return new MongoClientHolder(new MongoClient(host, port), System.currentTimeMillis());
        } finally {
            lock.unlock();
        }
    }

    /**
     * 增加计数
     */
    private void incrementCount() {
        count++;
    }

    /**
     * 减少计数
     */
    private void decrementCount() {
        count--;
    }

    /**
     * 增加计数
     */
    private void incrementActive() {
        active++;
    }

    /**
     * 减少计数
     */
    private void decrementActive() {
        active--;
    }

    /**
     * 获取当前计数
     *
     * @return int
     */
    private int getCount() {
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取当前计数
     *
     * @return int
     */
    private int getActive() {
        lock.lock();
        try {
            return active;
        } finally {
            lock.unlock();
        }
    }


    /**
     * 关闭资源
     */
    public void close() {
        lock.lock();
        try {
            MongoClientHolder mongoClientHolder = MongoClientContext.get();
            if (mongoClientHolder != null) {
                decrementActive();
                close(mongoClientHolder);
                MongoClientContext.remove();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 关闭资源
     *
     * @param mongoClient 客户端
     */
    public void close(MongoClient mongoClient) {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    /**
     * 关闭资源
     *
     * @param mongoClientHolder 客户端持有类
     */
    private void close(MongoClientHolder mongoClientHolder) {
        if (mongoClientHolder != null) {
            close(mongoClientHolder.getMongoClient());
        }
    }

    private static class MongoClientContext {
        private final static ThreadLocal<MongoClientHolder> MONGO_CLIENT_HOLDER_THREAD_LOCAL = new ThreadLocal<>();

        private static void set(MongoClientHolder mongoClientHolder) {
            MONGO_CLIENT_HOLDER_THREAD_LOCAL.set(mongoClientHolder);
        }

        private static MongoClientHolder get() {
            return MONGO_CLIENT_HOLDER_THREAD_LOCAL.get();
        }

        private static void remove() {
            MONGO_CLIENT_HOLDER_THREAD_LOCAL.remove();
        }


    }

    private class MongoClientHolder {
        private MongoClient mongoClient;
        private long currentTime;
        /**
         * 过期时间30分钟
         */
        private final static long TIMEOUT = 30 * 60 * 1000L;

        private MongoClientHolder(MongoClient mongoClient, long currentTime) {
            this.mongoClient = mongoClient;
            this.currentTime = currentTime;
        }

        private boolean isTimeout() {
            return System.currentTimeMillis() > TIMEOUT + currentTime;
        }

        private MongoClient getMongoClient() {
            return mongoClient;
        }


    }

    @Override
    public String toString() {
        return "当前活动数：" + getActive() + "，连接数：" + getCount();
    }
}


