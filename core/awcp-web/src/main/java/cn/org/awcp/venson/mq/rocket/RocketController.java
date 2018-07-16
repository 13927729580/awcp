package cn.org.awcp.venson.mq.rocket;

import cn.org.awcp.venson.controller.base.ReturnResult;
import cn.org.awcp.venson.controller.base.StatusCode;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.websocket.server.PathParam;

//@RestController
@RequestMapping("rocket")
public class RocketController {

    @Autowired
    private DefaultMQProducer rocketmqProduct;

    @RequestMapping(value = "send/{topic}",method = RequestMethod.GET)
    public ReturnResult send(@PathVariable("topic") String topic, @PathParam("tags") String tags,@PathParam("message") String message){
        ReturnResult result=ReturnResult.get();
        Message msg = new Message(topic, tags, message.getBytes());
        SendResult sendResult = null;
        try {
            sendResult = rocketmqProduct.send(msg);
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        result.setStatus(StatusCode.SUCCESS).setData(sendResult);
        return result;
    }
}
