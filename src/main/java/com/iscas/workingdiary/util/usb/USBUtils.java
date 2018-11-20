package com.iscas.workingdiary.util.usb;

import com.iscas.workingdiary.util.CertUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.cert.Certificate;
import java.util.List;

/**
 * 从U盘读取jks的愚蠢实现，后续会使用多线程完善
 * 正式应用建议采用JSR-80 API实现U盘的实时监听，这里只是做个简单demo
 */
public class USBUtils {
    private static File[] disk;
    private static int diskNum;
    private static String usbName;
    private static byte[] jksBytes;
    private static FileInputStream fileInputStream;

    public static byte[] readFileBytesFromUSB() throws IOException {
        String path = usbName+"jks/mykeystore_1.jks";
        File file = new File(path);
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            System.out.println("file too big...");
        }
        FileInputStream fi = null;
        try {
            fi = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] buffer = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        if (fi != null) {
            while (offset < buffer.length
                    && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
                offset += numRead;
            }
        }
        if (offset != buffer.length) {
            throw new IOException("Could not completely read file "
                    + file.getName());
        }
        fi.close();
        return buffer;
    }

    public static FileInputStream readFisFromUSB(){
        String path = usbName+"jks/mykeystore_1.jks";
        File file = new File(path);
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            System.out.println("file too big...");
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fis;
    }

    public static Boolean findUSB(){
        Boolean isUSB = false;
        File[] disk = File.listRoots();
        int len = disk.length;
        if (len > diskNum){
            System.out.println("U盘已插入:"+ disk[len-1].getPath());
            usbName = (disk[len-1].getPath());
            isUSB = true;
        }
        return isUSB;
    }

    private static void init(){
        System.out.println("系统初始化中，请在系统初始化完成后插入U盘...");
        File[] files = File.listRoots();
        diskNum = files.length;
    }
    public static void main(String[] args){
        init();
        Boolean f = true;
        while (f){
            if (findUSB()){
                f = false;
            }
        }
        fileInputStream = readFisFromUSB();
        List list = CertUtils.getCertFromInputStram(fileInputStream,"123","1");
        Certificate cert = (Certificate) list.get(0);
        System.out.println(cert.toString());
    }
}
