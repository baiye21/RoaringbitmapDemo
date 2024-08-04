package com.demo.roaringbitmap.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @Description:
 * @Author: baiye <baiye_21@163.com>
 * @CreateTime: 2024/7/7
 */
@Slf4j
@UtilityClass
public final class MyFileUtil {

    /**
     * 读取文件转为String(无换行)
     * @param filePath 文件露肩
     * @return String
     */
    public static String fileLoafToStr(String filePath) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
          log.error("fileLoafToStr error filePath:{}", filePath,e);
          throw new RuntimeException(e);
        }
    }

}
