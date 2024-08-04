package com.demo.roaringbitmap.common;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 通用返回体(简版)
 * @Author: baiye <baiye_21@163.com>
 * @CreateTime: 2024/7/3
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DemoResponse<T> {
    /**
     * 返回状态码
     */
    private Integer statusCode;
    /**
     * 信息
     */
    private String statusMsg;
    /**
     * 数据
     */
    private T data;

    private DemoResponse(Integer statusCode, String statusMsg) {
        this.statusCode = statusCode;
        this.statusMsg = statusMsg;
    }

    public static <T> DemoResponse<T> ofSuc() {
        return new DemoResponse<T>(0, "success");
    }

    public static <T> DemoResponse<T> ofSuc(T data) {
        return new DemoResponse<T>(0, "success", data);
    }

    public static <T> DemoResponse<T> ofErr(String statusMsg) {
        return new DemoResponse<T>(500, "error");
    }

}
