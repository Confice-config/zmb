package com.example.controller;

import com.example.common.Result;
import com.example.enetity.Training;
import com.example.service.TrainingService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/training")
public class TrainingController {

    @Resource
    private TrainingService trainingService;

    @PostMapping("/add")
    public Result add(@RequestBody Training training) {
        trainingService.add(training);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        trainingService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Long> ids) {
        trainingService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Training training) {
        trainingService.update(training);
        return Result.success();
    }

    @GetMapping("/selectPage")
    public Result selectPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "15") Integer pageSize,
            @RequestParam(required = false) String searchText
    ) {
        PageInfo<Training> pageInfo = trainingService.selectPage(pageNum, pageSize, searchText);
        return Result.success(pageInfo);
    }
}