package com.demo.roaringbitmap.db.mysql.repo;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.roaringbitmap.db.mysql.dao.TableAccoBitmapDao;
import com.demo.roaringbitmap.db.mysql.model.TableAccoBitmapDO;
import org.springframework.stereotype.Repository;

/**
 * @Description:
 * @Author: baiye <baiye_21@163.com>
 * @CreateTime: 2024/7/7
 */
@Repository
public class TableAccoBitmapRepo extends ServiceImpl<TableAccoBitmapDao, TableAccoBitmapDO> {

    /**
     * 唯一键查询标签数据
     *
     * @param tagType 标签类型
     * @param tagId   标签id
     * @return TableAccoBitmapDO
     */
    public TableAccoBitmapDO getByUniqueKey(String tagType, String tagId) {
        return lambdaQuery().eq(TableAccoBitmapDO::getVcTagType, tagType)
                .eq(TableAccoBitmapDO::getVcTagId, tagId)
                .one();
    }
}
