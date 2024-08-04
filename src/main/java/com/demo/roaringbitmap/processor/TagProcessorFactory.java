package com.demo.roaringbitmap.processor;

import com.demo.roaringbitmap.annotation.TagProcessorSpringBean;
import com.demo.roaringbitmap.util.SpringContextUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @Description: 处理器工程
 * 解析配置文件，实例化处理器流程
 * @Author: baiye <baiye_21@163.com>
 * @CreateTime: 2024/7/3
 */
@Slf4j
@Component
public class TagProcessorFactory {

    public ITagProcessor createTagProcessorFlow(String config)
            throws JsonProcessingException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Map<String, Object> configMap = new ObjectMapper().readValue(config, new TypeReference<Map<String, Object>>() {
        });
        // processor_create
        return createProcessor(configMap);
    }

    private ITagProcessor createProcessor(Map<String, Object> processorConfig)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, JsonProcessingException {
        // 实例化
        ITagProcessor processor = createProcessorByType((String) processorConfig.get("class_name"));
        // 初始化 - 属性赋值
        populateFields(processor, processorConfig);
        // 处理器初始化校验
        processor.initCheck();
        return processor;
    }

    private ITagProcessor createProcessorByType(String className)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> clazz = Class.forName(className);
        if (!ITagProcessor.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(clazz.getTypeName() + " class is not implemented ClueDataProcessor");
        }
        return (ITagProcessor) clazz.newInstance();
    }

    private void populateFields(ITagProcessor processor, Map<String, Object> configMap)
            throws IllegalAccessException, JsonProcessingException, ClassNotFoundException, InstantiationException {
        Field[] fields = processor.getClass().getDeclaredFields();
        Map<String, Object> params = (Map<String, Object>) configMap.get("params");
        for (Field field : fields) {
            field.setAccessible(Boolean.TRUE);
            // spring_bean注入
            if (field.isAnnotationPresent(TagProcessorSpringBean.class)) {
                injectSpringBean(processor, field);
                // 属性赋值
            } else {
                injectFieldValue(processor, field, params);
            }
        }
    }

    /**
     * 注入spring_bean
     *
     * @param processor processor
     * @param field     field
     * @throws IllegalAccessException ..
     */
    private void injectSpringBean(ITagProcessor processor, Field field) throws IllegalAccessException {
        // 如果有spring_bean 注解
        TagProcessorSpringBean springBeanAnnotation = AnnotationUtils.getAnnotation(
                field, TagProcessorSpringBean.class);
        String beanName = springBeanAnnotation.beanName();
        if (StringUtils.isBlank(beanName)) {
            beanName = field.getName();
        }
        field.set(processor, SpringContextUtil.getBean(beanName));
    }

    private void injectFieldValue(ITagProcessor processor, Field field, Map<String, Object> paramMap)
            throws IllegalAccessException, JsonProcessingException, ClassNotFoundException, InstantiationException {
        // 集合类型
        if (Collection.class.isAssignableFrom(field.getType())) {
            Collection<Object> paramCollection = initParamCollection(field);
            // 判断是否是 tag_process
            if (checkProcessCollectionParam(field)) {
                List<Map<String, Object>> paramMapList = (List<Map<String, Object>>) paramMap.get(field.getName());
                for (Map<String, Object> tempMap : paramMapList) {
                    paramCollection.add(createProcessor(tempMap));
                }
            } else {
                List<Object> paramMapList = (List<Object>) paramMap.get(field.getName());
                for (Object obj : paramMapList) {
                    paramCollection.add(obj);
                }
            }
            field.set(processor, paramCollection);
            // 其他
        } else {
            if (ITagProcessor.class.isAssignableFrom(field.getType())) {
                Map<String, Object> tempParamMap = (Map<String, Object>) paramMap.get(field.getName());
                field.set(processor, createProcessor(tempParamMap));
            } else {
                field.set(processor, paramMap.get(field.getName()));
                // TODO 校验指定了枚举的filed是否取值正常
                // checkFieldEnumValue(processor, field);
            }
        }
    }


    /**
     * 返回集合类型的初始化集合
     * TODO: 当前只有list,Set类型
     *
     * @param field field
     * @return Collection
     */
    private Collection initParamCollection(Field field) {
        if (List.class.isAssignableFrom(field.getType())) {
            return new ArrayList();
        } else if (Set.class.isAssignableFrom(field.getType())) {
            return new HashSet();
        } else {
            throw new IllegalArgumentException("Unsupported Collection type");
        }
    }

    /**
     * 判断 Collection 类型参数 是否为 processor类型
     *
     * @param field Field
     * @return true/false
     */
    private boolean checkProcessCollectionParam(Field field) {
        Type genericType = field.getGenericType();
        if (!(genericType instanceof ParameterizedType)) {
            throw new IllegalArgumentException("Field genericType Source Value " + genericType);
        }
        // List<T>
        ParameterizedType parameterizedType = (ParameterizedType) genericType;
        Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
        if (actualTypeArgument instanceof Class) {
            Class<?> actualClass = (Class<?>) actualTypeArgument;
            if (ITagProcessor.class.isAssignableFrom(actualClass)) {
                return true;
            }
        }
        try {
            // 基础类型无需进一步转换
            ParameterizedType rowType = (ParameterizedType) actualTypeArgument;
            log.info("rowType " + rowType.getClass());
            Class<?> typeClass = (Class<?>) rowType.getRawType();
            return ITagProcessor.class.isAssignableFrom(typeClass);
        } catch (ClassCastException e) {
            log.error("checkProcessCollectionParam ClassCastException type:{}", actualTypeArgument, e);
        }
        return false;
    }
}