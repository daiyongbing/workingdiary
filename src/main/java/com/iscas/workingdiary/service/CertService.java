package com.iscas.workingdiary.service;

import com.iscas.workingdiary.bean.Cert;
import com.iscas.workingdiary.mapper.CertMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service()
public interface CertService {

    // 添加用户证书到数据库，此时的证书没有上链
    void insertCert(Cert cert);

    // 从数据库中删除证书(根据证书编号)
    void deleteCertByCertNo(String certNo);

    // 从数据库中删除证书（根据用户id）
    void deleteCertByUserId(Integer user_id);

    // 更新数据库证书
    void updateCert(Cert cert);

    // 查询证书信息
    Cert queryCert(String certNo);

    // 查询所有证书
    List<Cert> selectAll();
}
