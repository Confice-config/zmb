package com.example.service;


import cn.hutool.core.util.ObjectUtil;
import com.example.enetity.Department;
import com.example.enetity.Employee;
import com.example.enetity.User;
import com.example.exception.CustomerEeception;
import com.example.mapper.DepartmentMapper;
import com.example.mapper.EmployeeMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Resource
    EmployeeMapper employeeMapper;
    @Resource
    DepartmentMapper departmentMapper;

    public void add(Employee employee) {
        Employee abphone =employeeMapper.selectByPhone(employee.getPhone());
        Employee abemail =employeeMapper.selectByemail(employee.getEmail());
        if (abphone != null) {
            throw new CustomerEeception("手机号重复");
        }else if (abemail != null){
            throw new CustomerEeception("邮箱重复");
        }
        employeeMapper.insert(employee);
    }

    public void delectById(Integer id) {
        employeeMapper.delectById(id);
    }



    public void delectBatch(List<Employee> employeeList) {
        for (Employee employee:employeeList ) {
            this.delectById(employee.getId());
        }
    }
    public String selectById(Integer id) {
        String name = employeeMapper.selectById(id);
        if (name == null) {
            throw new RuntimeException("员工不存在 ID: " + id);
        }
        return name;
    }

    public void update(Employee employee) {
        employeeMapper.updateById(employee);
    }

    public List<Employee> selectAll() {

        return employeeMapper.selectAll(null);
    }
    public PageInfo<Employee> selectPage(Integer pageNum, Integer pageSize,Employee employee) {
//        User currentUser = TokenUtils.getCurrentUser();
//        if (1 == currentUser.getRoleId()){
//            employee.setId(currentUser.getEmployeeId());
//        }

        //        开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Employee> list = employeeMapper.selectAll(employee);
        for (Employee dbEmployee : list){
             Long  departmentId = dbEmployee.getDepartmentId();
             Department department = departmentMapper.selectById(departmentId);
             if (ObjectUtil.isNotEmpty(department)){
                 dbEmployee.setDepartmentName(department.getName());
             }

        }
        return  PageInfo.of(list);
    }


    public List<Employee> selectDao(Employee employee) {
        return employeeMapper.selectByPage(employee);
    }


}
