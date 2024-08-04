package com.demo.roaringbitmap.processor;

import com.demo.roaringbitmap.enums.BitmapOperateEnum;
import org.roaringbitmap.RoaringBitmap;

/**
 * @Description: 与或处理器抽象类
 * @Author: baiye <baiye_21@163.com>
 * @CreateTime: 2024/7/3
 */
public abstract class AbstractAndOrTagLogic implements ITagProcessor {

    abstract RoaringBitmap andOrLogic();

    public abstract void addTagProcess(ITagProcessor tagProcess);

    public static AbstractAndOrTagLogic getAndOrLogicProcess(String operate) {
        if (BitmapOperateEnum.AND.getCode().equals(operate)) {
            return new TagAndLogicProcessor();
        } else if (BitmapOperateEnum.OR.getCode().equals(operate)) {
            return new TagOrLogicProcessor();
        } else {
            throw new IllegalArgumentException("异常标签操作类型" + operate);
        }
    }

    @Override
    public RoaringBitmap process() {
        return andOrLogic();
    }

}
