package com.blog.admin.utils;

import java.security.MessageDigest;
import java.util.UUID;

/**
 * 账号工具类
 * 1、生成id
 * 2、密码加密
 */
public class AccountUtil {

    /**
     * 获取一个uuid
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 获取指定数量的uuid
     *
     * @param number
     * @return
     */
    public static String[] getSomeUUID(int number) {
        String[] res = new String[number];
        for (int i = 0; i < number; i++) {
            res[i] = UUID.randomUUID().toString().replaceAll("-", "");
        }
        return res;
    }

    /**
     * 当uuid无法生成的时候，调用此方法生成一个32位的字符串id
     *
     * @return
     */
    public static String get32Stirng() {
        String chars = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz123456789";
        int length = chars.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            sb.append(chars.charAt((int) (Math.random() * length)));
        }
        return sb.toString();
    }

    /**
     * 密码加密——32位大写
     * @param pwd 密码
     * @return String
     */
    public static String MD5(String pwd) {
        // 用于加密的字符
        char md5String[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F' };
        try {
            // 使用平台的默认字符集将此 String 编码为 byte序列，并将结果存储到一个新的 byte数组中
            byte[] btInput = pwd.getBytes();

            // 信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
            MessageDigest mdInst = MessageDigest.getInstance("MD5");

            // MessageDigest对象通过使用 update方法处理数据， 使用指定的byte数组更新摘要
            mdInst.update(btInput);

            // 摘要更新之后，通过调用digest（）执行哈希计算，获得密文
            byte[] md = mdInst.digest();

            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) { // i = 0
                byte byte0 = md[i]; // 95
                str[k++] = md5String[byte0 >>> 4 & 0xf]; // 5
                str[k++] = md5String[byte0 & 0xf]; // F
            }
            // 返回经过加密后的字符串
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }





}
