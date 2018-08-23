package dango.rabbit;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author: DANGO
 * @date 2018/7/12 16:24
 * @Description:
 */

@Service
public class ConsumerService {

    public void getMessage(Map<String, Object> message) {
        System.out.println("消息消费者 = " + message);
    }
}
