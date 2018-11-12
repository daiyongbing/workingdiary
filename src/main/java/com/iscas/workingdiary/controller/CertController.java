package com.iscas.workingdiary.controller;

import com.iscas.workingdiary.bean.Cert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 证书管理类
 */

@RestController
@RequestMapping("/cert")
public class CertController {

    public Boolean submitCert(Cert cert){
        return true;
    }
}
