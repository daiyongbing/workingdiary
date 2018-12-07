package com.iscas.workingdiary.mapper;

import com.iscas.workingdiary.bean.User;
import org.apache.ibatis.annotations.Param;

public interface AdminMapper {

    void deleteUser(@Param("userName") String userName, @Param("userId") Integer userId);

    void updateUser(User user);
}
