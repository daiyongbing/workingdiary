package com.iscas.workingdiary.mapper;

import com.iscas.workingdiary.bean.Admin;
import com.iscas.workingdiary.bean.User;
import org.apache.ibatis.annotations.Insert;

/**
 * User数据库访问接口
 */
public interface IUserMapper {

    // db test code
    @Insert("INSERT INTO admin(username, password) VALUES(#{username}, #{password})")
    int insertAdim(Admin admin);


    User login(User user);
}
