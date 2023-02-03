package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.example.util.MessageSender;
import com.example.vo.UserRegisterVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    @Resource
    private MessageSender messageSender;
    private static final String USER_REGISTER_TOPIC_NAME = "user-register-topic";

    @PostMapping
    public boolean register(@RequestBody UserRegisterVo userRegisterVo){
        //save user info to database
        saveUserInfoToDatabase(userRegisterVo);
        //send message to message queue
        return messageSender.sendMessage(USER_REGISTER_TOPIC_NAME,JSON.toJSONString(userRegisterVo));

    }

    private void saveUserInfoToDatabase(UserRegisterVo userRegisterVo){
        log.info("You have saved user info in database successfully..");
    }
}
