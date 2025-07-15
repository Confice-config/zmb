package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.enetity.Department;
import com.example.enetity.Employee;
import com.example.enetity.Resignation;
import com.example.enetity.User;
import com.example.mapper.DepartmentMapper;
import com.example.mapper.EmployeeMapper;
import com.example.mapper.ResignationMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResignationService {
    @Resource
    ResignationMapper resignationMapper;
    @Resource
    EmployeeMapper employeeMapper;
    @Resource
    DepartmentMapper departmentMapper;

    // 新增
    public void add(Resignation resignation) {
        User currentUser = TokenUtils.getCurrentUser();
        resignation.setEmployeeId(currentUser.getEmployeeId());
        resignation.setStatus("待审批");
        resignationMapper.insert(resignation);
    }

    // 删除单个
    public void deleteById(Integer id) {
        resignationMapper.deleteById(id);
    }

    // 批量删除
    public void deleteBatch(List<Resignation> resignationList) {
        for (Resignation resignation : resignationList) {
            this.deleteById(resignation.getId());
        }
    }

    // 更新
    public void update(Resignation resignation) {
        resignationMapper.updateById(resignation);
    }

    // 分页查询（与员工/请假表逻辑一致）
    public PageInfo<Resignation> selectPage(Integer pageNum, Integer pageSize, Resignation resignation) {
        User currentUser = TokenUtils.getCurrentUser();
        if (3 == currentUser.getRoleId()){
            resignation.setEmployeeId(currentUser.getEmployeeId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Resignation> list = resignationMapper.selectPage(resignation);
        for (Resignation dbResignation : list){
            Integer employeeId = dbResignation.getEmployeeId();
            Long departmentId = dbResignation.getApproverId();
            Employee employee = employeeMapper.selectEeById(employeeId);
            Department department = departmentMapper.selectById(departmentId);
            if (ObjectUtil.isNotEmpty(employee)) {
                    dbResignation.setEmployeeName(employee.getName());
                    if (ObjectUtil.isNotEmpty(department)) {
                        dbResignation.setDepartmentName(department.getManager());
                    }else {
                        dbResignation.setDepartmentName("无");
                    }
            }
        }
        return PageInfo.of(list);
    }
}