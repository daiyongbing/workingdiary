package com.iscas.workingdiary.service.impl;

import com.github.pagehelper.PageHelper;
import com.iscas.workingdiary.bean.*;
import com.iscas.workingdiary.mapper.CertMapper;
import com.iscas.workingdiary.mapper.DiaryMapper;
import com.iscas.workingdiary.mapper.UserMapper;
import com.iscas.workingdiary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service()
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private CertMapper certMapper;

    @Resource
    private DiaryMapper diaryMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void userRegister(User user, Cert cert) {
        // 加密密码
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        if (cert != null){
            certMapper.insertCert(cert);
        }
        userMapper.insertUser(user);
    }

    @Override
    public User selectUserByName(String userName) {
        return userMapper.selectByUserName(userName);
    }

    @Override
    public User findUserById(String userId) {
        return userMapper.findByUserId(userId);
    }

    @Override
    public void updateByName(User user) {
        userMapper.updateByName(user);
    }

    @Override
    public void destoryAccount(String userName){
        try {
            certMapper.deleteCertByName(userName);
            diaryMapper.deleteDiaryByName(userName);
            userMapper.deleteIntegralByName(userName);
            userMapper.deleteUserByName(userName);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void modifyPassword(String userName, String newPassword) {
        userMapper.modifyPassword(userName, bCryptPasswordEncoder.encode(newPassword));
    }

    @Override
    public Integer queryTotalIntegral(String userName) {
        return userMapper.selectTotalIntegral(userName);
    }

    @Override
    public List<Integral> queryIntegralList(String userName) {
        return userMapper.selectIntegralList(userName);
    }

    @Override
    public void pushDairy(Diary diary, Integral integral) {
        diaryMapper.insertDiary(diary);
        userMapper.insertIntegral(integral);
    }

    @Override
    public List<Diary> queryDiaryList(String userName) {
        return diaryMapper.selectDiaryByName(userName);
    }

    @Override
    public List<User> selectUserByPage(int currentPage, int pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        List<User> userList = userMapper.selectAll();
        int count = userMapper.userCount();
        PageBean<User> pageBean = new PageBean<>(currentPage, pageSize, count);
        pageBean.setItems(userList);
        return pageBean.getItems();
    }

    @Override
    public void deleteUserById(String userId) {
        userMapper.deleteUser(userId);
    }

}
