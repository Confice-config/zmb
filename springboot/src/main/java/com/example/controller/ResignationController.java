package com.example.controller;

import com.example.common.Result;
import com.example.enetity.Resignation;
import com.example.service.ResignationService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resign")
public class ResignationController {
    @Resource
    private ResignationService resignationService;

    // 新增离职记录
    @PostMapping("/add")
    public Result add(@RequestBody Resignation resignation) {
        resignationService.add(resignation);
        return Result.success();
    }

    // 删除单个
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        resignationService.deleteById(id);
        return Result.success();
    }

    // 批量删除（接收ID列表）
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Resignation> resignations) {
        resignationService.deleteBatch(resignations);
        return Result.success();
    }

    // 更新离职记录
    @PutMapping("/update")
    public Result update(@RequestBody Resignation resignation) {
        resignationService.update(resignation);
        return Result.success();
    }

    // 分页查询（带搜索）
    @GetMapping("/selectPage")
    public Result selectPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "15") Integer pageSize,
            Resignation resignation
    ) {
        PageInfo<Resignation> pageInfo = resignationService.selectPage(pageNum, pageSize, resignation);
        return Result.success(pageInfo);
    }
}
