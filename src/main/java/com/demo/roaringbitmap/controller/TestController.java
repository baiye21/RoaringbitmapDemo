package com.demo.roaringbitmap.controller;

import com.demo.roaringbitmap.common.DemoResponse;
import com.demo.roaringbitmap.db.mysql.model.TableAccoBitmapDO;
import com.demo.roaringbitmap.db.mysql.repo.TableAccoBitmapRepo;

import com.demo.roaringbitmap.service.TestDataService;
import com.demo.roaringbitmap.util.BitOperationUtil;
import com.demo.roaringbitmap.util.ClassPathHelp;
import com.demo.roaringbitmap.util.MyFileUtil;
import org.roaringbitmap.RoaringBitmap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private TestDataService testDataService;

    @Resource
    private TableAccoBitmapRepo tableAccoBitmapRepo;

    /**
     * 测试读取文件
     *
     * @return DemoResponse
     */
    @GetMapping("/test/config/file")
    public DemoResponse<String> testMethod() {
        String fileLine = MyFileUtil.fileLoafToStr(ClassPathHelp.CLASS_PATH + "file/test_config_1.txt");
        return DemoResponse.ofSuc(fileLine);
    }

    /**
     * 初始化测试数据
     *
     * @return DemoResponse
     */
    @GetMapping("/test/data")
    public DemoResponse<String> testData() {
        testDataService.initTestData();
        return DemoResponse.ofSuc();
    }

    @GetMapping("/test/processor/config")
    public DemoResponse<List<Integer>> testProcessor(@RequestParam("config_name")String configName) {
        return DemoResponse.ofSuc(testDataService.testProcessConfig(configName));
    }

    /**
     * 测试接口 - 验证客户是否有标签
     *
     * @param tagType 标签类型
     * @param tagId   标签id
     * @param cusId   客户id
     * @return DemoResponse
     */
    @GetMapping("/test/check/customer/tag")
    public DemoResponse<Boolean> checkTag(@RequestParam("tag_type") String tagType,
                                          @RequestParam("tag_Id") String tagId,
                                          @RequestParam("cus_id") Integer cusId) {
        TableAccoBitmapDO accoBitmapDO = tableAccoBitmapRepo.getByUniqueKey(tagType, tagId);
        RoaringBitmap roaringBitmap = Optional.ofNullable(accoBitmapDO)
                .map(t -> BitOperationUtil.stringToRoaringBitMap(t.getVcByte()))
                .orElse(new RoaringBitmap());
        return DemoResponse.ofSuc(roaringBitmap.contains(cusId));
    }
}
