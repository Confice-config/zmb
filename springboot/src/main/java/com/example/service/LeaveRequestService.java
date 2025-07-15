package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.enetity.Employee;
import com.example.enetity.LeaveRequest;
import com.example.enetity.User;
import com.example.mapper.EmployeeMapper;
import com.example.mapper.LeaveRequestMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveRequestService {
    @Resource
    LeaveRequestMapper leaveRequestMapper;
    @Resource
    EmployeeMapper employeeMapper;

    // 新增请假
    public void add(LeaveRequest leaveRequest) {
        User currentUser = TokenUtils.getCurrentUser();
        leaveRequest.setEmployeeId(currentUser.getEmployeeId());
        leaveRequest.setStatus("待审批");
        leaveRequestMapper.insert(leaveRequest);
    }

    // 单个删除
    public void deleteById(Integer id) {
        leaveRequestMapper.deleteById(id);
    }

    // 批量删除
    public void deleteBatch(List<LeaveRequest> leaveRequestsList) {
        for (LeaveRequest leaveRequest : leaveRequestsList) {
         this.deleteById(leaveRequest.getId());
        }
    }

    // 更新请假记录
    public void update(LeaveRequest leaveRequest) {
        leaveRequestMapper.updateById(leaveRequest);
    }

    public List<LeaveRequest> selectAll() {
        return leaveRequestMapper.selectAll(null);
    }

    // 分页查询
    public PageInfo<LeaveRequest> selectPage(Integer pageNum, Integer pageSize, LeaveRequest leaveRequest) {
        User currentUser = TokenUtils.getCurrentUser();
        if (3 == currentUser.getRoleId()){
            leaveRequest.setEmployeeId(currentUser.getEmployeeId());
        }
        // 开启分页并查询
        PageHelper.startPage(pageNum, pageSize);
        List<LeaveRequest> list = leaveRequestMapper.selectPage(leaveRequest);
        for (LeaveRequest dbLeaveRequest : list){
            Integer  employeeId = dbLeaveRequest.getEmployeeId();
            Employee employee = employeeMapper.selectEeById(employeeId);
            if (ObjectUtil.isNotEmpty(employee)){
                dbLeaveRequest.setEmployeeName(employee.getName());
            }

        }
        return PageInfo.of(list);
    }

    // 根据ID查询单条记录
    public LeaveRequest selectById(Long id) {
        return leaveRequestMapper.selectById(id);
    }
}