package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.enetity.Attendance;

import com.example.enetity.Employee;
import com.example.enetity.User;
import com.example.mapper.AttendanceMapper;
import com.example.mapper.EmployeeMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendanceService {

    @Resource
    AttendanceMapper attendanceMapper;
    @Resource
    EmployeeMapper employeeMapper;

    public void add(Attendance attendance) {

        attendanceMapper.insert(attendance);
    }

    public void deleteById(Integer id) {
        attendanceMapper.delectById(id);
    }

    // 批量删除
    public void deleteBatch(List<Attendance> Attendances) {
        for (Attendance attendance : Attendances) {
            this.deleteById(attendance.getId());
        }
    }

    public void update(Attendance attendance) {

        attendanceMapper.updateById(attendance);
    }

    public PageInfo<Attendance> selectPage(Integer pageNum, Integer pageSize, Attendance attendance) {
        User currentUser = TokenUtils.getCurrentUser();
        if (3 == currentUser.getRoleId()){
            attendance.setEmployeeId(currentUser.getEmployeeId());
        }
        // 开启分页
        PageHelper.startPage(pageNum, pageSize);
        List<Attendance> list = attendanceMapper.selectAll(attendance);
        for (Attendance dbAttendance : list) {
            Integer  employeeId = dbAttendance.getEmployeeId();
            Employee employee = employeeMapper.selectEeById(employeeId);
            if (ObjectUtil.isNotEmpty(employee)){
                dbAttendance.setEmployeeName(employee.getName());
            }
        }
        return PageInfo.of(list);
    }

    public List<Attendance> selectAll() {
        return attendanceMapper.selectEEAll(null);
    }
}