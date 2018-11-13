package com.iscas.workingdiary.mapper;

import com.iscas.workingdiary.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    void updateUser(User user);

    String userLogin(User user);

    void insertUser(User user);

    void deleteUser(@Param(value = "userId") Integer userId);
}
