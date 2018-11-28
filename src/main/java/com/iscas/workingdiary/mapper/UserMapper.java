package com.iscas.workingdiary.mapper;

import com.iscas.workingdiary.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    void updateUser(User user);

    String userLogin(@Param(value = "userName") String userName, @Param(value = "password") String password);

    void insertUser(User user);

    void deleteUser(@Param(value = "userId") Integer userId);

    String validate(@Param(value = "userId") Integer userId);
}
