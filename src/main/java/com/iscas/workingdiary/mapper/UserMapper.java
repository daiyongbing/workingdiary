package com.iscas.workingdiary.mapper;

import com.iscas.workingdiary.bean.CustomUserDetails;
import com.iscas.workingdiary.bean.Integral;
import com.iscas.workingdiary.bean.User;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    void insertUser(User user);

    void deleteUser(@Param(value = "userId") String userId);

    /**
     * 仅用于用户登录时的验证
     * @param userName
     * @return
     */
    CustomUserDetails findByUserName(@Param(value = "userName") String userName);

    User findByUserId(@Param(value = "userId") String userId);

    void updateByName(User user);

    User selectByUserName(@Param(value = "userName") String userName);

    void deleteUserByName(@Param("userName") String userName);

    void modifyPassword(@Param("userName") String userName, @Param("password") String newPassword);

    Integer selectTotalIntegral(@Param("userName") String userName);

    List<Integral> selectIntegralList(@Param("userName") String userName);
}
