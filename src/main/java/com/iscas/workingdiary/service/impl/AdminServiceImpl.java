package com.iscas.workingdiary.service.impl;

import com.iscas.workingdiary.bean.Diary;
import com.iscas.workingdiary.bean.User;
import com.iscas.workingdiary.mapper.AdminMapper;
import com.iscas.workingdiary.mapper.DiaryMapper;
import com.iscas.workingdiary.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Resource
    AdminMapper adminMapper;

    @Resource
    DiaryMapper diaryMapper;

    @Override
    public void deleteUser(String userName, String userId) {
        adminMapper.deleteUser(userName, userId);
    }

    @Override
    public void updateUser(User user) {
        adminMapper.updateUser(user);
    }

    @Override
    public void resetPassword(String userName) {
        String defaultPassword = userName+"@123";
        String crypted = bCryptPasswordEncoder.encode(defaultPassword);
        adminMapper.resetPassword(userName, crypted);
    }

    @Override
    public List<User> selectAllUser() {
        return adminMapper.selectAll();
    }

    @Override
    public List<Diary> selectDiaryByName(String userName) {
        return diaryMapper.selectDiaryByName(userName);
    }

}
