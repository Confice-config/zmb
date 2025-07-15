package com.example.controller;

import com.example.common.Result;
import com.example.enetity.Contract;
import com.example.service.ContractService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contract")
public class ContractController {

    @Resource
    private ContractService contractService;

    @PostMapping("/add")
    public Result add(@RequestBody Contract contract) {
        contractService.add(contract);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        contractService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Long> ids) {
        contractService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Contract contract) {
        contractService.update(contract);
        return Result.success();
    }

    @GetMapping("/selectPage")
    public Result selectPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "15") Integer pageSize,
            Contract contract
    ) {
        PageInfo<Contract> pageInfo = contractService.selectPage(pageNum, pageSize, contract);
        return Result.success(pageInfo);
    }
}