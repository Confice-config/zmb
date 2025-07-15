package com.example.mapper;

import com.example.enetity.Resignation;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.stream.Collectors;

public interface ResignationMapper {
    // 新增
    @Insert("INSERT INTO resignation (employee_id, resignation_type, apply_date, last_work_date, reason, status, approver_id, approval_comment) " +
            "VALUES (#{employeeId}, #{resignationType}, #{applyDate}, #{lastWorkDate}, #{reason}, #{status}, #{approverId}, #{approvalComment})")
    void insert(Resignation resignation);

    // 删除单个
    @Delete("DELETE FROM resignation WHERE id = #{id}")
    void deleteById(Integer id);

    // 更新
    @Update("UPDATE resignation SET " +
            "employee_id=#{employeeId}, resignation_type=#{resignationType}, apply_date=#{applyDate}, " +
            "last_work_date=#{lastWorkDate}, reason=#{reason}, status=#{status}, " +
            "approver_id=#{approverId}, approval_comment=#{approvalComment} " +
            "WHERE id=#{id}")
    void updateById(Resignation resignation);

    // 分页查询（带搜索）
    List<Resignation> selectPage(Resignation resignation);

    // 根据ID查询（编辑回显）
    @Select("SELECT * FROM resignation WHERE id = #{id}")
    Resignation selectById(Long id);
}

// 动态SQL提供类（批量删除）
class ResignSqlProvider {
    public String deleteBatchSql(@Param("ids") List<Long> ids) {
        return "DELETE FROM resignation WHERE id IN (" +
                ids.stream().map(id -> "#{ids[" + ids.indexOf(id) + "]}").collect(Collectors.joining(",")) +
                ")";
    }
}