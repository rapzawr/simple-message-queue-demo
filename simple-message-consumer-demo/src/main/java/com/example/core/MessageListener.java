package com.example.core;

import com.example.util.SpringContextUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.lang.reflect.Method;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor

public class MessageListener {
   private Class targetClass;
   private String targetMethodName;

   public void invoke (SimpleMessage simpleMessage) {
       Assert.notNull(targetClass,"targetClass is Null");
       Assert.notNull(targetMethodName,"targetMethodName is Null");

       try{
           Method method = targetClass.getDeclaredMethod(targetMethodName,SimpleMessage.class);
           if(method == null) {
               log.warn("No such method with annotation SimpleMessageConsumer,targetClass:{} , targetMethodName:{}",
                       targetClass,targetMethodName);

           }
           Object targetObject = SpringContextUtils.getBean(targetClass);
           method.invoke(targetObject,simpleMessage);
       } catch (Exception e){
           log.error(e.getMessage(),e);
       }
    }
}
