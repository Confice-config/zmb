package com.example.service;

import com.example.enetity.Department;
import com.example.exception.CustomerEeception;
import com.example.mapper.DepartmentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    @Resource
    DepartmentMapper departmentMapper;

    public void add(Department department) {
        // 校验部门名称唯一性
        Department existDepartment = departmentMapper.selectByName(department.getName());
        if (existDepartment != null) {
            throw new CustomerEeception("部门名称已存在");
        }
        departmentMapper.insert(department);
    }

    public void deleteById(Integer id) {
        departmentMapper.deleteById(id);
    }

    public void deleteBatch(List<Department> departmentList) {

        for (Department department : departmentList) {
            this.deleteById(department.getId());
        }
    }

    public void update(Department department) {
        departmentMapper.updateById(department);
    }

    public List<Department> selectAll() {
        return departmentMapper.selectAll(null, null);
    }

    public PageInfo<Department> selectPage(Integer pageNum, Integer pageSize, String searchText) {
        String name = null;
        String manager = null;

        // 解析搜索文本：支持名称或负责人模糊搜索
        if (searchText != null && !searchText.isEmpty()) {
            name = searchText;
            manager = searchText; // 同时匹配负责人（与员工模块搜索逻辑保持一致）
        }

        PageHelper.startPage(pageNum, pageSize);
        List<Department> list = departmentMapper.selectAll(name, manager);
        return PageInfo.of(list);
    }
}