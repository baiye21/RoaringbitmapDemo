package com.demo.roaringbitmap.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * @Description:
 * @Author: baiye <baiye_21@163.com>
 * @CreateTime: 2024/7/7
 */
@Slf4j
public class ClassPathHelp {

    private ClassPathHelp(){
    }

    private static final String BOOT = "BOOT-INF";
    private static final String BOOT_PATH = "BOOT-INF/classes/";
    public static final String CLASS_PATH;
    static {
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        log.info("Class path: {}", path);
        if (path.contains(".")) {
            path = "/home/libs/";
        }
        File file = new File(path);
        if (file.exists()) {
            String[] files = file.list();
            for (String fileName : files) {
                if (BOOT.equals(fileName)) {
                    File boot = new File(path.concat(BOOT));
                    if (boot.exists() && boot.isDirectory()) {
                        path = path.concat(BOOT_PATH);
                        break;
                    }
                }
            }
        }
        CLASS_PATH = path;
    }
}
