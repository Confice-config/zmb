package com.example.mapper;

import com.example.enetity.Attendance;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AttendanceMapper {

    // 插入考勤记录
    void insert(Attendance attendance);

    // 按ID删除
    @Delete("delete from `attendance` where id= #{id}")
    void delectById(Integer id);

    // 更新考勤
    void updateById(Attendance attendance);

    // 按员工ID和日期查询（唯一性校验）
   List<Attendance> selectAll(Attendance attendance);

   @Select("select * from `attendance`")
    List<Attendance> selectEEAll(Object o);
}