package com.example.mapper;

import com.example.enetity.LeaveRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface LeaveRequestMapper {
     List<LeaveRequest> selectAll(LeaveRequest leaveRequest);

    // 新增请假记录（与员工表insert方法风格一致）
    @Insert("INSERT INTO leave_request (employee_id, leave_type, start_date, end_date, reason, status) " +
            "VALUES (#{employeeId}, #{leaveType}, #{startDate}, #{endDate}, #{reason}, #{status})")
    void insert(LeaveRequest leaveRequest);

    // 根据ID删除请假记录（修正拼写错误，使用delete而非delect）
    @Delete("DELETE FROM leave_request WHERE id = #{id}")
    void deleteById(Integer id);

    // 批量删除（接收ID列表，与员工管理批量删除逻辑一致）
    @DeleteProvider(type = LeaveSqlProvider.class, method = "deleteBatchSql")
    void deleteBatch(@Param("ids") List<Long> ids);

    // 更新请假记录（字段与数据库完全对应）
    @Update("UPDATE leave_request SET " +
            "employee_id=#{employeeId}, leave_type=#{leaveType}, start_date=#{startDate}, " +
            "end_date=#{endDate}, reason=#{reason}, status=#{status} " +
            "WHERE id=#{id}")
    void updateById(LeaveRequest leaveRequest);

    // 分页查询（带搜索，与员工表selectAll方法逻辑一致）
    List<LeaveRequest> selectPage(LeaveRequest leaveRequest);

    // 根据ID查询单条记录（编辑时回显数据）
    @Select("SELECT * FROM leave_request WHERE id = #{id}")
    LeaveRequest selectById(Long id);
}

// 动态SQL提供类（用于批量删除）
class LeaveSqlProvider {
    public String deleteBatchSql(@Param("ids") List<Long> ids) {
        StringBuilder sql = new StringBuilder("DELETE FROM leave_request WHERE id IN (");
        for (int i = 0; i < ids.size(); i++) {
            sql.append("#{ids[").append(i).append("]}");
            if (i != ids.size() - 1) sql.append(",");
        }
        sql.append(")");
        return sql.toString();
    }
}