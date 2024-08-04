package com.demo.roaringbitmap.processor;

import org.roaringbitmap.RoaringBitmap;

/**
 * @Description: 标签处理器接口
 * @Author: baiye <baiye_21@163.com>
 * @CreateTime: 2024/7/3
 */
public interface ITagProcessor {

    /**
     * 标签处理
     *
     * @return RoaringBitmap
     */
    RoaringBitmap process();

    /**
     * 处理器初始化校验
     */
    void initCheck();
}
