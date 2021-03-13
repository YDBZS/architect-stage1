package org.architect.controller;

import org.architect.service.StuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

/**
 * 测试接口
 *
 * @author 多宝
 * @since 2021/3/7 16:34
 */
@ApiIgnore
@RestController
public class StuFooController {

    @Resource
    private StuService stuService;

    @GetMapping("/getStu")
    public Object hello(int id) {
        return stuService.getStuInfo(id);
    }

    @PostMapping("/saveStu")
    public void save() {
        stuService.saveStu();
    }

    @PostMapping("/updateStu")
    public void updateStu(int id) {
        stuService.updateStu(id);
    }

    @PostMapping("/deleteStu")
    public void delete(int id) {
        stuService.deleteStu(id);
    }



}
