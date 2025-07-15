package com.example.mapper;

import com.example.enetity.Employee;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface EmployeeMapper {
    List<Employee> selectAll(Employee employee);

    void insert(Employee employee);

    @Select("select * from `employee` where phone= #{phone}")
    Employee selectByPhone(String phone);
    @Select("select * from `employee` where email= #{email}")
    Employee selectByemail(String email);

    @Delete("delete from `employee` where id= #{id}")
    void delectById(Integer id);

    void updateById(Employee employee);

    List<Employee> selectByPage(Employee employee);

    @Select("select name from `employee` where id= #{id}")
    String selectById(Integer id);

    @Select("select name from `employee` where id= #{employeeId}")
    Employee selectEeById(Integer employeeId);
}
