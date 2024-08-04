package com.demo.roaringbitmap.processor;

import com.demo.roaringbitmap.enums.BitmapOperateEnum;
import com.demo.roaringbitmap.util.BitOperationUtil;
import com.google.common.base.Preconditions;
import lombok.Data;
import org.roaringbitmap.RoaringBitmap;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 或处理器
 * @Author: baiye <baiye_21@163.com>
 * @CreateTime: 2024/7/3
 */
@Data
public class TagOrLogicProcessor extends AbstractAndOrTagLogic {
    /**
     * 处理标签处理器
     */
    private List<ITagProcessor> processorList;

    @Override
    public void addTagProcess(ITagProcessor tagProcess) {
        if (CollectionUtils.isEmpty(processorList)) {
            processorList = new ArrayList<>();
        }
        processorList.add(tagProcess);
    }

    @Override
    RoaringBitmap andOrLogic() {
        RoaringBitmap result = initResult();
        int i = 1;
        while (i < processorList.size()) {
            result = BitOperationUtil.roaringBitMapOperate(result, processorList.get(i).process(),
                    BitmapOperateEnum.OR);
            i++;
        }
        return result;
    }

    private RoaringBitmap initResult() {
        return processorList.get(0).process();
    }

    @Override
    public void initCheck() {
        Preconditions.checkNotNull(processorList, "processorList不能为空");
        Preconditions.checkArgument(processorList.size() >= 1,
                "processorList至少有一个元素");
    }

}
