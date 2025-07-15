package com.example.service;

import com.example.enetity.Training;
import com.example.mapper.TrainingMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class TrainingService {

    @Resource
    private TrainingMapper trainingMapper;

    public void add(Training training) {
        trainingMapper.insert(training);
    }

    public void deleteById(Long id) {
        trainingMapper.deleteById(id);
    }

    public void deleteBatch(List<Long> ids) {
        ids.forEach(this::deleteById);
    }

    // 修复：原update方法错误调用add，改为直接更新
    public void update(Training training) {
        trainingMapper.updateById(training);
    }

    public PageInfo<Training> selectPage(Integer pageNum, Integer pageSize, String searchText) {
        // 初始化查询条件：允许同时匹配标题或日期（模糊查询）
        String titleCondition = null;
        String dateCondition = null;

        if (StringUtils.hasText(searchText)) {
            // 不严格区分格式，直接将搜索词作为标题或日期的模糊查询条件
            titleCondition = "%" + searchText + "%";  // 标题模糊查询
            dateCondition = "%" + searchText + "%";   // 日期模糊查询（支持任意日期格式片段）
        }

        // 启动分页（必须在执行查询前调用）
        PageHelper.startPage(pageNum, pageSize);
        // 执行查询（传入模糊查询条件）
        List<Training> list = trainingMapper.selectAll(titleCondition, dateCondition);
        // 构造PageInfo（自动包含total总记录数）
        return new PageInfo<>(list);
    }
}
