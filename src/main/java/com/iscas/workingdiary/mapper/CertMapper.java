package com.iscas.workingdiary.mapper;

import com.iscas.workingdiary.bean.Cert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CertMapper {

    void insertCert(Cert cert);

    void updateCert(Cert cert);

    void deleteCertByCertNo(@Param("certNo") String certNo);

    void deleteCertByUserId(@Param("userId") Integer userId);

    Cert queryCert(@Param("certNo") String CertNo);

    List<Cert> selectAll();

    Cert verifyCert(@Param("userId") Integer userId);
}
