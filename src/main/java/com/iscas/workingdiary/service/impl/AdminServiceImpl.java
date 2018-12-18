package com.iscas.workingdiary.service.impl;

import com.iscas.workingdiary.bean.User;
import com.iscas.workingdiary.mapper.AdminMapper;
import com.iscas.workingdiary.service.AdminService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AdminServiceImpl implements AdminService {
    @Resource
    AdminMapper adminMapper;

    @Override
    public void deleteUser(String userName, int userId) {
        adminMapper.deleteUser(userName, userId);
    }

    @Override
    public void updateUser(User user) {
        adminMapper.updateUser(user);
    }

}
