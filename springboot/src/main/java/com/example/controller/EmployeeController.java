package com.example.controller;


import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.enetity.Employee;
import com.example.service.EmployeeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Resource
    EmployeeService employeeService;

    @PostMapping("/add")
    public Result add(@RequestBody Employee employee){
        employeeService.add(employee);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        employeeService.delectById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Employee> employeeList){
        employeeService.delectBatch( employeeList);
        return Result.success();
    }

    @GetMapping("/select/{id}")
    public Result selectById(@PathVariable Integer id){
        String name =employeeService.selectById(id);
        String displayName = name != null ? name : "未知用户";
        return Result.success(displayName);
    }

    @PutMapping("/update")
    public Result update(@RequestBody Employee employee){
        employeeService.update(employee);
        return Result.success();
    }


    @GetMapping("/selectAll")
    public Result selectAll(Employee employee){
        List<Employee> employeeList=employeeService.selectAll();
        return Result.success(employeeList);
    }
    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Employee employee
                             ){
        PageInfo<Employee> pageInfo = employeeService.selectPage(pageNum,pageSize,employee);
        return Result.success(pageInfo);
    }

    @GetMapping("/export")
    public void exportDAta(Employee employee,HttpServletResponse response) throws Exception {
        String ids = employee.getIds();
        if (StrUtil.isNotBlank(ids)){
            String[] idsArr = ids.split(",");
            employee.setIdsArr(idsArr);
        }
        List<Employee> list = employeeService.selectDao(employee);
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.addHeaderAlias("name","姓名");
        writer.addHeaderAlias("gender","性别");
        writer.addHeaderAlias("age","年龄");
        writer.addHeaderAlias("phone","电话");
        writer.addHeaderAlias("email","邮箱");
        writer.addHeaderAlias("departmentId","部门编号");
        writer.addHeaderAlias("hireDate","入职日期");
        writer.addHeaderAlias("position","职位");
        writer.addHeaderAlias("status","状态");
        writer.setOnlyAlias(true);
        writer.write(list);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("员工信息", StandardCharsets.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream out = response.getOutputStream();
        writer.flush(out);
        writer.close();
        out.close();
    }

    @PostMapping("/import")
    public Result importDAta(MultipartFile file) throws Exception {
//        拿到输入流，构建reader
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);

        reader.addHeaderAlias("姓名","name");
        reader.addHeaderAlias("性别","gender");
        reader.addHeaderAlias("年龄","age");
        reader.addHeaderAlias("电话","phone");
        reader.addHeaderAlias("邮箱","email");
        reader.addHeaderAlias("部门编号","departmentId");
        reader.addHeaderAlias("入职日期","hireDate");
        reader.addHeaderAlias("职位","position");
        reader.addHeaderAlias("状态","status");
        List<Employee> list = reader.readAll(Employee.class);

        for (Employee employee : list) {
            employeeService.add(employee);
        }
        return Result.success();
    }
}
