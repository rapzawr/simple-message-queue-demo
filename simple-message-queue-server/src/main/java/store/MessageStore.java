package store;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MessageStore {
    private ConcurrentHashMap<String, Queue<String>>messageQueueMap=new ConcurrentHashMap<>();

    public boolean addMessage(String topic, String message){
        messageQueueMap.putIfAbsent(topic,new ArrayBlockingQueue<>(1024));
        return messageQueueMap.get(topic).add(message);
    }

    public List<String> getMessage(String topic){
        Queue<String> queue = messageQueueMap.get(topic);
        if(!CollectionUtils.isEmpty(queue)){
            List<String> messageList = new ArrayList<>();
            String msg;
            while((msg=queue.poll()) != null ) {
                messageList.add(msg);
            }
            return messageList;
        }
        return Collections.emptyList();
    }

    public Map<String,List<String>> getAllMessages() {
        Enumeration <String> keys = messageQueueMap.keys();
        Map<String,List<String>> msgMap = new HashMap<>();

        while(keys.hasMoreElements()){
            String topicName = keys.nextElement();
            List<String> msgList = getMessage(topicName);
            //no need to send messages to client , if list is empty.
            if(!CollectionUtils.isEmpty(msgList)){
                msgMap.put(topicName,msgList);
            }
        }

        return msgMap;
    }


}
