package com.iscas.workingdiary.service.impl;

import com.iscas.workingdiary.bean.Cert;
import com.iscas.workingdiary.mapper.CertMapper;
import com.iscas.workingdiary.service.CertService;
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
    public void deleteCertByName(String userName) {
        certMapper.deleteCertByName(userName);
    }


    @Override
    public void updateCert(Cert cert) {
        certMapper.updateCert(cert);
    }

    @Override
    public Cert queryCert(String certNo) {
        return certMapper.queryCert(certNo);
    }

    @Override
    public List<Cert> selectAll() {
        return certMapper.selectAll();
    }

    @Override
    public String getPemCert(String userName) {
        return certMapper.getPemCert(userName);
    }

    @Override
    public Cert getCertByName(String userName) {
        return certMapper.getCertByName(userName);
    }

}
