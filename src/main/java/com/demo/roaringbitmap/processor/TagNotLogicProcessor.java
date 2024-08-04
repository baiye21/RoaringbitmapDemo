package com.demo.roaringbitmap.processor;

import com.demo.roaringbitmap.enums.BitmapOperateEnum;
import com.demo.roaringbitmap.util.BitOperationUtil;
import com.google.common.base.Preconditions;
import lombok.Data;
import org.roaringbitmap.RoaringBitmap;

/**
 * @Description: 非处理器
 * @Author: baiye <baiye_21@163.com>
 * @CreateTime: 2024/7/3
 */
@Data
public class TagNotLogicProcessor implements ITagProcessor {
    /**
     * 处理标签处理器Source
     */
    private ITagProcessor processorSource;
    /**
     * 处理标签处理器Target
     */
    private ITagProcessor processorTarget;

    @Override
    public RoaringBitmap process() {
        return BitOperationUtil.roaringBitMapOperate(processorSource.process(), processorTarget.process(),
                BitmapOperateEnum.NOT);
    }

    @Override
    public void initCheck() {
        Preconditions.checkNotNull(processorSource, "processorSource不能为空");
        Preconditions.checkNotNull(processorTarget, "processorTarget不能为空");
    }
}
