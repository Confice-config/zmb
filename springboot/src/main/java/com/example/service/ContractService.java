package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.enetity.Contract;
import com.example.enetity.Employee;
import com.example.enetity.User;
import com.example.mapper.ContractMapper;
import com.example.mapper.EmployeeMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ContractService {

    @Resource
    ContractMapper contractMapper;
    @Resource
    EmployeeMapper employeeMapper;

    public void add(Contract contract) {

        contractMapper.insert(contract);
    }

    public void deleteById(Long id) {
        contractMapper.deleteById(id);
    }

    public void deleteBatch(List<Long> ids) {
        ids.forEach(this::deleteById);
    }

    public void update(Contract contract) {
        // 复用新增校验逻辑
        add(contract);
        contractMapper.updateById(contract);
    }

    public PageInfo<Contract> selectPage(Integer pageNum, Integer pageSize, Contract contract) {
        User currentUser = TokenUtils.getCurrentUser();
        if (3 == currentUser.getRoleId()){
            contract.setEmployeeId(currentUser.getEmployeeId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Contract> list = contractMapper.selectAll(contract);
        for (Contract dbcontract : list) {
            Integer  employeId = dbcontract.getEmployeeId();
            Employee employee = employeeMapper.selectEeById(employeId);
            if (ObjectUtil.isNotEmpty(employee)){
                dbcontract.setEmployeeName(employee.getName());
            }
        }
        return PageInfo.of(list);
    }
}