package com.demo.roaringbitmap.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description: 标签类型枚举
 * @Author: baiye <baiye_21@163.com>
 * @CreateTime: 2024/7/3
 */
@Getter
public enum TagTypeEnum {

    /**
     * 客户参与活动：
     * 活动id
     */
    CUS_ACTIVITY("cus_activity", "客户参与活动"),

    /**
     * 客户等级：
     * A～F
     */
    CUS_CLASS("cus_class", "客户等级"),

    /**
     * 客户风险承受能力：
     * R1～R5
     */
    CUS_RISK_LEVEL("cus_risk_level", "风险承受能力"),
    ;

    private final String code;
    private final String desc;

    private final static Map<String, TagTypeEnum> MAP_SELF = Arrays.stream(TagTypeEnum.values())
            .collect(Collectors.toMap(TagTypeEnum::getCode, Function.identity()));

    TagTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
