package com.iscas.workingdiary.controller;

import com.iscas.workingdiary.bean.Cert;
import com.iscas.workingdiary.service.CertService;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 证书管理类
 */

@RestController
@RequestMapping("/cert")
public class CertController {

    @Autowired
    private CertService certService;

    @PostMapping("insert")
    public void insertCert(Cert cert){
        certService.insertCert(cert);
    }

    @GetMapping("deleteByCertNo")
    public void deleteCertByCertNo(String certNo){
        certService.deleteCertByCertNo(certNo);
    }

    @GetMapping("deleteByUserId")
    public void deleteCertByCertNo(Integer userId){
        certService.deleteCertByUserId(userId);
    }

    @GetMapping("queryByCertNo")
    public Cert queryCertByNo(String certNo){
        Cert cert = new Cert();
        cert =  certService.queryCert(certNo);
        return cert;
    }

    @GetMapping("queryAll")
    public List<Cert> queryAllCert(){
        List<Cert> certList = new ArrayList<>();
        certList = certService.selectAll();
        return certList;
    }

    @PostMapping("update")
    public void updateCert(Cert cert){
        certService.updateCert(cert);
    }
}
