package com.iscas.workingdiary.service;

import com.iscas.workingdiary.bean.Admin;
import com.iscas.workingdiary.bean.User;

public interface IUserService {

    public Admin addUser(Admin admin);

    User login(User user);
}
