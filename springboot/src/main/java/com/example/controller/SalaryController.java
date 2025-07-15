package com.example.controller;

import com.example.common.Result;
import com.example.enetity.Salary;
import com.example.service.SalaryService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salary")
public class SalaryController {

    @Resource
    private SalaryService salaryService;

    @PostMapping("/add")
    public Result add(@RequestBody Salary salary) {
        salaryService.add(salary);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        salaryService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Long> ids) {
        salaryService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Salary salary) {
        salaryService.update(salary);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll(Salary salary) {
        List<Salary> salaryList = salaryService.selectAll();
        return Result.success(salaryList);
    }

    @GetMapping("/selectPage")
    public Result selectPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "15") Integer pageSize,
            Salary salary
    ) {
        PageInfo<Salary> pageInfo = salaryService.selectPage(pageNum, pageSize, salary);
        return Result.success(pageInfo);
    }
}