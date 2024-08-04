package com.demo.roaringbitmap.enums;

import lombok.Getter;

/**
 * @Description: 标签操作符
 * @Author: baiye <baiye_21@163.com>
 * @CreateTime: 2024/7/3
 */
@Getter
public enum BitmapOperateEnum {

    OR("or", "逻辑或"),

    AND("and", "逻辑与"),

    NOT("NOT", "逻辑非"),

    ADD("or", "追加元素"),

    REMOVE("not", "移除元素"),

    REPLACE("replace", "替换元素"),
    ;

    private String code;
    private String desc;

    BitmapOperateEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
