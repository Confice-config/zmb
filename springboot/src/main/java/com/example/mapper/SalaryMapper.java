package com.example.mapper;

import com.example.enetity.Salary;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SalaryMapper {

    void insert(Salary salary);

    @Delete("delete from salary where id = #{id}")
    void deleteById(Long id);

    void updateById(Salary salary);

    List<Salary> selectAll(Salary salary);

    List<Salary> selectEeAll(Salary salary);
}