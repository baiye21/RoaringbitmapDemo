package com.demo.roaringbitmap.service;

import com.demo.roaringbitmap.enums.BitmapOperateEnum;
import org.roaringbitmap.RoaringBitmap;

import java.util.Collection;

/**
 * @Description:
 * @Author: baiye <baiye_21@163.com>
 * @CreateTime: 2024/7/7
 */
public interface CustomerTagService {

    /**
     * 保存或者更新 标签
     *
     * @param tagType 标签类型
     * @param tagId   标签id
     * @param bitmap  bitmap
     * @return boolean 更新结果
     */
    boolean saveOrUpdateBitmap(String tagType, String tagId, RoaringBitmap bitmap);

    /**
     * 批量获取标签数据(需指定批量逻辑 and or)
     *
     * @param tagType     标签类型
     * @param tagIds      标签id
     * @param operateEnum 操作枚举
     * @return RoaringBitmap
     */
    RoaringBitmap roaringBitMapClass(String tagType, Collection<String> tagIds, BitmapOperateEnum operateEnum);


    RoaringBitmap bitmapProcessByConfig(String config);
}
