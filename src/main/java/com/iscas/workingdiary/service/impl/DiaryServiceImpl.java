package com.iscas.workingdiary.service.impl;

import com.iscas.workingdiary.mapper.DiaryMapper;
import com.iscas.workingdiary.service.DiaryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DiaryServiceImpl implements DiaryService {
    @Resource
    private DiaryMapper diaryMapper;

    @Override
    public void deleteDiaryByName(String userName) {
        diaryMapper.deleteDiaryByName(userName);
    }
}
