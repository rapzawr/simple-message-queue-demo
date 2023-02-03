package com.example.core;

import com.example.annotation.SimpleMessageConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class MessageTopicMapper {
    /**
     * Key: topic,value : consumers , it`s allow to have more than  1 consumers
     * for each topic
     */

    private ConcurrentHashMap<String, List<MessageListener>> messageListenerCache = new ConcurrentHashMap<>();

    private final String BASE_PACKAGE = "com.example";
    private final String RESOURCE_PATTERN = "/**/*.class";

    public MessageTopicMapper() {
        init();
    }

    public List<MessageListener> getMessageListeners(String topic) {
        return messageListenerCache.get(topic);
    }

    /**
     * load all consumers (all methods with annotation SimpleMessageConsumer)
     */
    private void init() {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

        try {
            String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    ClassUtils.convertClassNameToResourcePath(BASE_PACKAGE) + RESOURCE_PATTERN;
            Resource[] resources = resourcePatternResolver.getResources(pattern);
            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
            for (Resource resource : resources) {
                //read class info
                MetadataReader reader = readerFactory.getMetadataReader(resource);
                //scan classes
                String className = reader.getClassMetadata().getClassName();
                Class<?> clazz = Class.forName(className);
                //get all methods from a clazz

                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    SimpleMessageConsumer annotation = method.getAnnotation(SimpleMessageConsumer.class);
                    if (annotation == null) {
                        continue;
                    }

                    String[] topics = annotation.topics();
                    if (topics == null || topics.length == 0) {
                        continue;
                    }
                    for (String topic : topics) {
                        messageListenerCache.putIfAbsent(topic, new ArrayList<>());
                        log.info("add class:{},method:{} to topic : {}", clazz, method.getName(), topic);
                        messageListenerCache.get(topic).add(new MessageListener(clazz, method.getName()));
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            log.error("Failed to init listner cache..", e);
        }
    }
    }

