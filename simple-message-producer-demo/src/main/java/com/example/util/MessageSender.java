package com.example.util;

import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Component
@Slf4j
public class MessageSender {
@Resource
    private RestTemplate restTemplate;

    private final static String MESSAGE_QUEUE_HOST_ADDR="http://localhost:8888/message";

    public boolean sendMessage(String topic,String message){
        Assert.notEmpty(topic,"topic cannot be empty");
        Assert.notEmpty(message,"message cannot be empty");

        String url = MESSAGE_QUEUE_HOST_ADDR+"/" + topic;
        log.info("url:{}",url);
        log.info("message:{}",message);
        return restTemplate.postForObject(url,message,Boolean.class);
    }
}
