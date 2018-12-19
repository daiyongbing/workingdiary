package com.iscas.workingdiary.mapper;

import com.iscas.workingdiary.bean.Diary;
import com.iscas.workingdiary.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminMapper {

    void deleteUser(@Param("userName") String userName, @Param("userId") String userId);

    void updateUser(User user);

    void resetPassword(@Param("userName") String userName, @Param("crypted") String crypted);

    List<User> selectAll();

}
