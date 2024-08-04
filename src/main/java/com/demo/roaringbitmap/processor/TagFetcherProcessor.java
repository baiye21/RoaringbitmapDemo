package com.demo.roaringbitmap.processor;

import com.demo.roaringbitmap.annotation.TagProcessorSpringBean;
import com.demo.roaringbitmap.enums.BitmapOperateEnum;
import com.demo.roaringbitmap.service.CustomerTagService;
import com.google.common.base.Preconditions;
import lombok.Data;
import org.roaringbitmap.RoaringBitmap;

import java.util.List;

/**
 * @Description: 取数处理器
 * @Author: baiye <baiye_21@163.com>
 * @CreateTime: 2024/7/3
 */
@Data
public class TagFetcherProcessor implements ITagProcessor {
    /**
     * 标签类型
     */
    private String tagType;

    /**
     * 标签ID列表
     */
    private List<String> tagTypeIds;

    @TagProcessorSpringBean(beanName = "customerTagServiceImpl")
    private CustomerTagService customerTagService;

    /**
     * 返回取数结果
     *
     * @return RoaringBitmap
     */
    @Override
    public RoaringBitmap process() {
        // 改为缓存时，此处需要做复制处理
        return customerTagService.roaringBitMapClass(tagType, tagTypeIds, BitmapOperateEnum.OR);
    }

    @Override
    public void initCheck() {
        Preconditions.checkNotNull(tagType, "tagType不能为空");
        Preconditions.checkNotNull(tagTypeIds, "tagTypeIds不能为空");
        Preconditions.checkNotNull(customerTagService, "customerTagService不能为空");
    }
}
