package com.example.controller;

import com.example.common.Result;
import com.example.enetity.Attendance;
import com.example.service.AttendanceService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Resource
    AttendanceService attendanceService;

    @PostMapping("/add")
    public Result add(@RequestBody Attendance attendance) {
        attendanceService.add(attendance);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        attendanceService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Attendance> AttendanceIdList) {
        attendanceService.deleteBatch(AttendanceIdList);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Attendance attendance) {
        attendanceService.update(attendance);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll(Attendance attendance) {
        List<Attendance> attendanceList = attendanceService.selectAll();
        return Result.success(attendanceList);
    }


    @GetMapping("/selectPage")
    public Result selectPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "15") Integer pageSize,
            Attendance attendance
    ) {
        PageInfo<Attendance> pageInfo = attendanceService.selectPage(pageNum, pageSize, attendance);
        return Result.success(pageInfo);
    }
}