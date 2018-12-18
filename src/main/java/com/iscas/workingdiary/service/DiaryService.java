package com.iscas.workingdiary.service;

import org.springframework.stereotype.Service;

@Service
public interface DiaryService {
    void deleteDiaryByName(String userName);
}
