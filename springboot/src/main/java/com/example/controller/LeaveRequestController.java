package com.example.controller;

import com.example.common.Result;
import com.example.enetity.LeaveRequest;
import com.example.service.LeaveRequestService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leave")
public class LeaveRequestController {
    @Resource
    LeaveRequestService leaveService;

    // 新增
    @PostMapping("/add")
    public Result add(@RequestBody LeaveRequest leaveRequest) {
        leaveService.add(leaveRequest);
        return Result.success();
    }

    // 删除单个
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        leaveService.deleteById(id);
        return Result.success();
    }

    // 批量删除（接收ID列表）
    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<LeaveRequest> leaveRequests) {
        leaveService.deleteBatch(leaveRequests);
        return Result.success();
    }

    // 更新
    @PutMapping("/update")
    public Result update(@RequestBody LeaveRequest leaveRequest) {
        leaveService.update(leaveRequest);
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll(LeaveRequest leaveRequest) {
        List<LeaveRequest> leaveRequestList = leaveService.selectAll();
        return Result.success(leaveRequestList);
    }

    // 分页查询（带搜索，模糊匹配所有字段）
    @GetMapping("/selectPage")
    public Result selectPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "15") Integer pageSize,
            LeaveRequest leaveRequest
    ) {
        PageInfo<LeaveRequest> pageInfo = leaveService.selectPage(pageNum, pageSize, leaveRequest);
        return Result.success(pageInfo);
    }
}