package com.example.mapper;

import com.example.enetity.Training;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

public interface TrainingMapper {

    void insert(Training training);

    @Delete("delete from training where id = #{id}")
    void deleteById(Long id);

    void updateById(Training training);

    List<Training> selectAll(
            @Param("titleCondition") String titleCondition,
            @Param("dateCondition") String dateCondition
    );
}