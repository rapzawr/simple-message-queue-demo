package com.example.core;


import com.example.util.SpringContextUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.lang.reflect.Method;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleMessagePullingTask {
    private Class targetClass;
    private String targetMethodName;

    public void invoke (SimpleMessage simpleMessage) {
        Assert.notNull(targetClass,"targetClass is null");
        Assert.notNull(targetMethodName,"targetMethodName is null");

        try {
            Method method = targetClass.getDeclaredMethod(targetMethodName,SimpleMessage.class);
            if(method == null){
                log.warn("No such method with annotation SimpleMessageConsumer,targetClass:{},targetMethodName:{}",
                        targetClass,targetMethodName);
            }
            //get instance of callee
            Object targetObject = SpringContextUtils.getBean(targetClass);

            if(targetObject==null){
                log.warn("No such bean registered, targetClass:{},targetMethod:{}",targetClass,targetMethodName);
                return;
            }
            method.invoke(targetObject,simpleMessage);
        } catch(Exception e) {
            log.error(e.getMessage(),e);
        }

    }
}
