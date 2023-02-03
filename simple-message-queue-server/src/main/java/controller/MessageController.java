package controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import store.MessageStore;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/message")

public class MessageController {
    @Resource
    private MessageStore messageStore;
    private final static String SUCCESS = "success";

    @PostMapping("/{topic}")
    public boolean publish (@RequestBody String message , @PathVariable ("topic") String topic){
        log.info("publish message: {} to topic : {}", message,topic);
        return messageStore.addMessage(topic,message);
    }

    @GetMapping("{topic}")
    public List<String> getMessages(@PathVariable("topic")String topic){
        log.info("trying to get message from topic :{}", topic);
        return messageStore.getMessage(topic);
    }
    @GetMapping
    public Map<String,List<String>> getAllMessages(){ return messageStore.getAllMessages();}


}
