package com.example.mapper;

import com.example.enetity.Department;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DepartmentMapper {
    List<Department> selectAll(
            @Param("name") String name,
            @Param("manager") String manager
    );

    void insert(Department department);

    @Select("select * from department where name = #{name}")
    Department selectByName(String name);

    @Delete("delete from department where id = #{id}")
    void deleteById(Integer id);

    void updateById(Department department);

    @Select("select * from department where id  = #{id}")
    Department selectById(Long departmentId);
}