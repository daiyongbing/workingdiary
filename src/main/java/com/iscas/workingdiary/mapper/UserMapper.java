package com.iscas.workingdiary.mapper;

import com.iscas.workingdiary.bean.User;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    void updateUser(User user);

    void insertUser(User user);

    void deleteUser(@Param(value = "userId") String userId);

    User findByUserName(@Param(value = "userName") String userName);

    User findByUserId(@Param(value = "userId") Integer userId);
}
