package com.example.mapper;

import com.example.enetity.Contract;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ContractMapper {

    void insert(Contract contract);

    @Delete("delete from contract where id = #{id}")
    void deleteById(Long id);

    void updateById(Contract contract);

    List<Contract> selectAll(Contract  contract);
}