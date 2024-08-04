package com.demo.roaringbitmap.service;

import java.util.List;

/**
 * @Description: 测试数据相关
 * @Author: baiye <baiye_21@163.com>
 * @CreateTime: 2024/7/7
 */
public interface TestDataService {

    /**
     * 初始化测试数据
     */
    void initTestData();

    List<Integer> testProcessConfig(String configName);

}
