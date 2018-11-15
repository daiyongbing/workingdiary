package com.iscas.workingdiary.bean;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * 用于xml解析，非签名交易接口使用
 */

@JacksonXmlRootElement(localName = "CSpec")
public class CSpec {

    @JacksonXmlProperty(localName = "stype")
    private String stype;

    @JacksonXmlProperty(localName = "idPath")
    private String idPath;

    @JacksonXmlProperty(localName = "idName")
    private String name;

    @JacksonXmlProperty(localName = "iptFunc")
    private String iptFunc;

    @JacksonXmlProperty(localName = "iptArgs")
    private String iptArgs;

    @JacksonXmlProperty(localName = "timeout")
    private String timeout;

    @JacksonXmlProperty(localName = "secureContext")
    private String secureContext;

    @JacksonXmlProperty(localName = "code")
    private Integer code;

    @JacksonXmlProperty(localName = "ctype")
    private Integer ctype;
}
