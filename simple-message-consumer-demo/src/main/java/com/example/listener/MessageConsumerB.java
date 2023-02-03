package com.example.listener;

import com.example.annotation.SimpleMessageConsumer;
import com.example.core.SimpleMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class MessageConsumerB {
    /**
     * Use java reflection to invoke consumer method,
     * make sure only one parameter - simpleMessage is allowed in consumer method
     * see com.example.core.MessageListener
     */

    @SimpleMessageConsumer(topics ={"user-deposit-topic"})
    public void consumeUserDepositTopic(SimpleMessage simpleMessage){
        List<String> messages = simpleMessage.getMessages();
        for(String msg:messages){
            log.info("will consume message : {}",msg);
        }
    }
}
