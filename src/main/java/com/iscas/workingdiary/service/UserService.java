package com.iscas.workingdiary.service;

import com.iscas.workingdiary.bean.Cert;
import com.iscas.workingdiary.bean.Diary;
import com.iscas.workingdiary.bean.Integral;
import com.iscas.workingdiary.bean.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service()
public interface UserService {

    // register
    void userRegister(User user, Cert cert);

    // delete user
    void deleteUserById(String userId);

    User selectUserByName(String userName);

    User findUserById(String userId);

    void updateByName(User user);

    void destoryAccount(String userName);

    void modifyPassword(String userName, String newPassword);

    Integer queryTotalIntegral(String userName);

    List<Integral> queryIntegralList(String userName);

    void pushDairy(Diary diary, Integral integral);

    List<Diary> queryDiaryList(String userName);
}
