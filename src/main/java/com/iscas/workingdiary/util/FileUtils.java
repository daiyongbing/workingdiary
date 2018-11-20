package com.iscas.workingdiary.util;

import com.utils.certUtil;

import java.io.*;
import java.security.cert.Certificate;
import java.util.List;

public class FileUtils extends Thread {
    public static String readStringFromFile(String url){
        String encoding = "UTF-8";
        File file = new File(url);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }
}
