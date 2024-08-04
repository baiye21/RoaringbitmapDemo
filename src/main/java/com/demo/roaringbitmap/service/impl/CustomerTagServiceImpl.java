package com.demo.roaringbitmap.service.impl;

import com.demo.roaringbitmap.db.mysql.model.TableAccoBitmapDO;
import com.demo.roaringbitmap.db.mysql.repo.TableAccoBitmapRepo;
import com.demo.roaringbitmap.enums.BitmapOperateEnum;
import com.demo.roaringbitmap.processor.ITagProcessor;
import com.demo.roaringbitmap.processor.TagProcessorFactory;
import com.demo.roaringbitmap.service.CustomerTagService;
import com.demo.roaringbitmap.util.BitOperationUtil;
import lombok.extern.slf4j.Slf4j;
import org.roaringbitmap.RoaringBitmap;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Objects;

/**
 * @Description:
 * @Author: baiye <baiye_21@163.com>
 * @CreateTime: 2024/7/7
 */
@Slf4j
@Service
public class CustomerTagServiceImpl implements CustomerTagService {

    @Resource
    private TableAccoBitmapRepo tableAccoBitmapRepo;

    @Resource
    private TagProcessorFactory tagProcessorFactory;

    @Override
    public boolean saveOrUpdateBitmap(String tagType, String tagId, RoaringBitmap bitmap) {
        TableAccoBitmapDO accoBitmapDO = tableAccoBitmapRepo.getByUniqueKey(tagType, tagId);
        if (Objects.isNull(accoBitmapDO)) {
            accoBitmapDO = new TableAccoBitmapDO();
            accoBitmapDO.setVcTagType(tagType);
            accoBitmapDO.setVcTagId(tagId);
        } else {
            // 忽略更新mtime
            accoBitmapDO.setMtime(null);
            accoBitmapDO.setNiVersion(accoBitmapDO.getNiVersion() + 1);
        }
        accoBitmapDO.setNiCardinality(bitmap.getCardinality());
        accoBitmapDO.setVcByte(BitOperationUtil.roaringBitMapToString(bitmap));
        return tableAccoBitmapRepo.saveOrUpdate(accoBitmapDO);
    }

    @Override
    public RoaringBitmap roaringBitMapClass(String tagType, Collection<String> tagIds, BitmapOperateEnum operateEnum) {
        RoaringBitmap initBitmap = new RoaringBitmap();
        if (!CollectionUtils.isEmpty(tagIds)) {
            boolean notInitBaseBitmap = true;
            for (String tagId : tagIds) {
                TableAccoBitmapDO bitmapInfo = tableAccoBitmapRepo.getByUniqueKey(tagType, tagId);
                if (Objects.nonNull(bitmapInfo) && notInitBaseBitmap) {
                    initBitmap = BitOperationUtil.stringToRoaringBitMap(bitmapInfo.getVcByte());
                    notInitBaseBitmap = false;
                } else if (Objects.nonNull(bitmapInfo)) {
                    initBitmap = BitOperationUtil.roaringBitMapOperate(initBitmap,
                            BitOperationUtil.stringToRoaringBitMap(bitmapInfo.getVcByte()), operateEnum);
                } else {
                    log.info("标签不存在 ：{},{}", tagType, tagId);
                }
            }
        }
        return initBitmap;
    }

    @Override
    public RoaringBitmap bitmapProcessByConfig(String config) {
        try {
            ITagProcessor tagProcessorFlow =
                    tagProcessorFactory.createTagProcessorFlow(config);
            return tagProcessorFlow.process();
        } catch (Exception e) {
            log.error("bitmapProcessByConfig error config:{}", config, e);
            throw new RuntimeException(e);
        }
    }
}
