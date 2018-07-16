package cn.org.awcp.venson.mq.rocket;

import cn.org.awcp.unit.message.PunNotification;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 *
 */
public class MessageListenerImpl implements MessageListenerConcurrently {

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        for (MessageExt msg : msgs) {
            if (PunNotification.KEY_FLOW.equals(msg.getTopic())) {
                if (PunNotification.IMPORTANT.equals(msg.getTags())) {
                    System.out.println("important：" + new String(msg.getBody()));
                } else if (PunNotification.UNIMPORTANT.equals(msg.getTags())) {
                    System.out.println("unimportant：" + new String(msg.getBody()));
                } else {
                    System.out.println("whatever：" + new String(msg.getBody()));
                }

            }else{
                System.out.println("not flow：" + new String(msg.getBody()));
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}