package com.demo.roaringbitmap.service.impl;

import com.demo.roaringbitmap.db.mysql.model.TableAccoBitmapDO;
import com.demo.roaringbitmap.db.mysql.repo.TableAccoBitmapRepo;
import com.demo.roaringbitmap.enums.TagTypeEnum;
import com.demo.roaringbitmap.service.CustomerTagService;
import com.demo.roaringbitmap.service.TestDataService;
import com.demo.roaringbitmap.util.BitOperationUtil;
import com.demo.roaringbitmap.util.ClassPathHelp;
import com.demo.roaringbitmap.util.MyFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.roaringbitmap.RoaringBitmap;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.swing.*;
import javax.swing.text.html.parser.Entity;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: baiye <baiye_21@163.com>
 * @CreateTime: 2024/7/30 23:50
 */
@Slf4j
@Service
public class TestDataServiceImpl implements TestDataService {

    @Resource
    private CustomerTagService customerTagService;

    @Override
    public void initTestData() {
        // 客户id是1～100万的随机
        Random random = new Random();
        // 客户活动标签数据初始化，活动id为1～5的5个活动(可交叉)
        List<String> activityIds = Arrays.asList("1", "2", "3", "4", "5");
        for (String activityId : activityIds) {
            RoaringBitmap activityBitmap = new RoaringBitmap();
            for (int i = 0; i < Integer.parseInt(activityId) * 100000; i++) {
                activityBitmap.add(random.nextInt(1000000) + 1);
            }
            customerTagService.saveOrUpdateBitmap(
                    TagTypeEnum.CUS_ACTIVITY.getCode(), activityId, activityBitmap);
        }

        // 客户等级标签数据初始化
        List<String> cusClassIds = Arrays.asList("A", "B", "C", "D", "E", "F");
        Map<String, RoaringBitmap> cusClassMap = new HashMap<>();
        for (int i = 0; i < 1000000; i++) {
            int randomIndex = random.nextInt(cusClassIds.size());
            RoaringBitmap cusClassBitmap = cusClassMap.computeIfAbsent(
                    cusClassIds.get(randomIndex), value -> new RoaringBitmap());
            cusClassBitmap.add(i);
        }
        for (Map.Entry<String, RoaringBitmap> entry : cusClassMap.entrySet()) {
            customerTagService.saveOrUpdateBitmap(
                    TagTypeEnum.CUS_CLASS.getCode(), entry.getKey(), entry.getValue());
        }

        // 客户风险等级标签数据初始化
        List<String> cusRiskIds = Arrays.asList("R1", "R2", "R3", "R4", "R5");
        Map<String, RoaringBitmap> cusRiskMap = new HashMap<>();
        for (int i = 0; i < 500000; i++) {
            int randomIndex = random.nextInt(cusRiskIds.size());
            RoaringBitmap cusClassBitmap = cusRiskMap.computeIfAbsent(
                    cusRiskIds.get(randomIndex), value -> new RoaringBitmap());
            cusClassBitmap.add(i);
        }
        for (Map.Entry<String, RoaringBitmap> entry : cusRiskMap.entrySet()) {
            customerTagService.saveOrUpdateBitmap(
                    TagTypeEnum.CUS_RISK_LEVEL.getCode(), entry.getKey(), entry.getValue());
        }
    }

    @Override
    public List<Integer> testProcessConfig(String configName) {
        String fileLine = MyFileUtil.fileLoafToStr(ClassPathHelp.CLASS_PATH + "file/" + configName);
        RoaringBitmap roaringBitmap = customerTagService.bitmapProcessByConfig(fileLine);
        int[] array = roaringBitmap.stream().toArray();
        return Arrays.stream(array).boxed().collect(Collectors.toList());
    }

}
