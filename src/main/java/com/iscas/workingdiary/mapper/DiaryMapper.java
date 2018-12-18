package com.iscas.workingdiary.mapper;

import org.apache.ibatis.annotations.Param;

public interface DiaryMapper {
    void deleteDiaryByName(@Param("userName") String userName);
}
