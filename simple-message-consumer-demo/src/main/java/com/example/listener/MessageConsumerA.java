package com.example.listener;

import com.alibaba.fastjson.JSON;
import com.example.annotation.SimpleMessageConsumer;
import com.example.core.SimpleMessage;
import com.example.message.UserRegisterMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class MessageConsumerA {
    /**
     * Use java reflection to invoke consumer method,
     * make sure only one parameter - simpleMessage is allowed in consumer method
     * see com.example.core.MessageListener
     */
    @SimpleMessageConsumer(topics={"user-register-topic"})
    public void consumeUserRegisterMessage(SimpleMessage simpleMessage){
        //....
        List<String> messages = simpleMessage.getMessages();

        for(String msg : messages) {
            UserRegisterMessage userRegisterMessage= JSON.parseObject(msg, UserRegisterMessage.class);
            //if this logic belongs to the activity group...
            log.info("[Activity Group Business] send welcome site message to user : {}", userRegisterMessage.getName());
        }
    }
}
