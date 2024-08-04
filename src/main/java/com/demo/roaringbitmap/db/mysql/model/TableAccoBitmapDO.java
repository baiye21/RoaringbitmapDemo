package com.demo.roaringbitmap.db.mysql.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:
 * @Author: baiye <baiye_21@163.com>
 * @CreateTime: 2024/7/7
 */
@Data
@TableName("t_acco_bitmap")
public class TableAccoBitmapDO implements Serializable {
    private static final long serialVersionUID = 5710433459032172947L;
    /**
     * 自增id
     */
    @TableId(value = "seq", type = IdType.AUTO)
    private Long seq;

    /**
     * 记录创建时间
     */
    private Date ctime;

    /**
     * 记录修改时间
     */
    private Date mtime;

    /**
     * 类型
     */
    private String vcTagType;

    /**
     * 类型id
     */
    private String vcTagId;

    /**
     * 标签当前容量
     */
    private Integer niCardinality;

    /**
     * bitmap
     */
    private String vcByte;

    /**
     * 版本号
     */
    private Long niVersion;
}
