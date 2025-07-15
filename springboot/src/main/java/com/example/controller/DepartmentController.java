package com.example.controller;

import com.example.common.Result;
import com.example.enetity.Department;
import com.example.service.DepartmentService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
public class DepartmentController {
    @Resource
    DepartmentService departmentService;

    @PostMapping("/add")
    public Result add(@RequestBody Department department) {
        departmentService.add(department);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        departmentService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Department> departmentList) {
        departmentService.deleteBatch(departmentList);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Department department) {
        departmentService.update(department);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll() {
        List<Department> departmentList = departmentService.selectAll();
        return Result.success(departmentList);
    }

    @GetMapping("/selectPage")
    public Result selectPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String searchText
    ) {
        PageInfo<Department> pageInfo = departmentService.selectPage(pageNum, pageSize, searchText);
        return Result.success(pageInfo);
    }
}