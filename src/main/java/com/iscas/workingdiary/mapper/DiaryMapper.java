package com.iscas.workingdiary.mapper;

import com.iscas.workingdiary.bean.Diary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiaryMapper {
    void deleteDiaryByName(@Param("userName") String userName);

    List<Diary> selectDiaryByName(@Param("userName") String userName);

    void insertDiary(Diary diary);
}
