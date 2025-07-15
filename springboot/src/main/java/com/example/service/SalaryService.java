package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.enetity.Employee;
import com.example.enetity.Salary;
import com.example.enetity.User;
import com.example.mapper.EmployeeMapper;
import com.example.mapper.SalaryMapper;
import com.example.utils.SearchFormatUtils;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class SalaryService {

    @Resource
    SalaryMapper salaryMapper;
    @Resource
    EmployeeMapper employeeMapper;

    public void add(Salary salary) {

        salaryMapper.insert(salary);
    }

    public void deleteById(Long id) {
        salaryMapper.deleteById(id);
    }

    public void deleteBatch(List<Long> ids) {
        ids.forEach(this::deleteById);
    }

    public void update(Salary salary) {
        salaryMapper.updateById(salary);
    }

    public PageInfo<Salary> selectPage(Integer pageNum, Integer pageSize, Salary salary) {
        User currentUser = TokenUtils.getCurrentUser();
        if (3 == currentUser.getRoleId()){
            salary.setEmployeeId(currentUser.getEmployeeId());
        }

        // 启动分页
        PageHelper.startPage(pageNum, pageSize);
        List<Salary> list = salaryMapper.selectAll(salary);
        for (Salary dbsalary : list) {
            Integer  employeId = dbsalary.getEmployeeId();
            Employee employee = employeeMapper.selectEeById(employeId);
            if (ObjectUtil.isNotEmpty(employee)){
                dbsalary.setEmployeeName(employee.getName());
            }
        }
        return PageInfo.of(list);
    }

    public List<Salary> selectAll() {
        return salaryMapper.selectEeAll(null);
    }
}
