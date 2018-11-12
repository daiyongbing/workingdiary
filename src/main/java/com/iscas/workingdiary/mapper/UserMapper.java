package com.iscas.workingdiary.mapper;

import com.iscas.workingdiary.bean.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    void updateById(User user);
}
