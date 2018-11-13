package com.iscas.workingdiary.service;

import com.iscas.workingdiary.bean.Cert;
import com.iscas.workingdiary.mapper.CertMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CertServiceImpl implements CertService {

    @Resource
    CertMapper certMapper;

    @Override
    public void insertCert(Cert cert) {
        certMapper.insertCert(cert);
    }

    @Override
    public void deleteCertByCertNo(String certNo) {
        certMapper.deleteCertByCertNo(certNo);
    }

    @Override
    public void deleteCertByUserId(Integer userId) {
        certMapper.deleteCertByUserId(userId);
    }


    @Override
    public void updateCert(Cert cert) {
        certMapper.updateCert(cert);
    }

    @Override
    public Cert queryCert(String certNo) {
        Cert cert = new Cert();
        cert =  certMapper.queryCert(certNo);
        return cert;
    }

    @Override
    public List<Cert> selectAll() {
        return certMapper.selectAll();
    }

}
