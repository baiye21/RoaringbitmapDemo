package com.demo.roaringbitmap.util;

import com.demo.roaringbitmap.enums.BitmapOperateEnum;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.roaringbitmap.RoaringBitmap;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @Description:
 * @Author: baiye <baiye_21@163.com>
 * @CreateTime: 2024/7/3
 */
@Slf4j
@UtilityClass
public class BitOperationUtil {

    /**
     * roaringBitmap 转 string
     *
     * @param roaringBitmap 入参
     * @return str
     */
    public static String roaringBitMapToString(RoaringBitmap roaringBitmap) {
        // RoaringBitMap 转为byte数组
        byte[] array = new byte[roaringBitmap.serializedSizeInBytes()];
        roaringBitmap.serialize(ByteBuffer.wrap(array));
        // byte转为字符串
        return new String(array, StandardCharsets.ISO_8859_1);
    }

    /**
     * string 转 roaringBitmap
     *
     * @param bitMapStr bitmap序列化字符串
     * @return RoaringBitmap
     */
    public static RoaringBitmap stringToRoaringBitMap(String bitMapStr) {
        RoaringBitmap roaringBitmap = new RoaringBitmap();
        try {
            // str 转为 byte数组
            byte[] redisArray = bitMapStr.getBytes(StandardCharsets.ISO_8859_1);
            roaringBitmap.deserialize(ByteBuffer.wrap(redisArray));
        } catch (IOException e) {
            log.error("stringToRoaringBitMap {} Error", bitMapStr.length(), e);
        }
        return roaringBitmap;
    }

    /**
     * bitMap操作
     *
     * @param oldBitMap    bitMap_1
     * @param newSubBitMap bitMap_2
     * @param operate      操作符
     * @return RoaringBitmap
     */
    public static RoaringBitmap roaringBitMapOperate(
            RoaringBitmap oldBitMap, RoaringBitmap newSubBitMap, BitmapOperateEnum operate) {
        switch (operate) {
            case ADD:
            case OR:
                oldBitMap.or(newSubBitMap);
                break;
            case REMOVE:
            case NOT:
                oldBitMap.andNot(newSubBitMap);
                break;
            case REPLACE:
                oldBitMap = newSubBitMap;
                break;
            case AND:
                oldBitMap.and(newSubBitMap);
                break;
            default:
                throw new IllegalArgumentException("operate 异常" + operate);
        }
        return oldBitMap;
    }


}
