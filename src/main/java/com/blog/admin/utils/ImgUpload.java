package com.blog.admin.utils;

import com.blog.admin.config.Constant;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class ImgUpload {


    public static String upload(String fileName, String filePath, InputStream is) {
        String src = null;
        boolean flag = uploadFile(filePath, fileName, is);
        if (!flag) {
            return src;
        }
        src = Constant.IMAGE_BASE_URL +  filePath + "/" + fileName;
        return src;
    }


    /**
     * @service
     * @dec
     * @author liushuai
     * @date 2019/3/1 15:48
     * @param filePath 文件存放目录
     * @param fileName 文件名
     * @param is 输入流
     * @return boolean
     */
    public static boolean uploadFile(String filePath, String fileName, InputStream is) {
        boolean result = false;
        FTPClient ftpClient = new FTPClient();
        try {
            int reply;
            ftpClient.connect(Constant.FTP_HOST, Constant.FTP_PORT);
            ftpClient.login(Constant.FTP_USERNAME, Constant.FTP_PASSWORD);
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                return result;
            }
            ftpClient.changeWorkingDirectory(Constant.FTP_REALPATH + filePath);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();//这个设置允许被动连接--访问远程ftp时需要
            ftpClient.setControlEncoding("GBK");
            if (!ftpClient.storeFile(fileName, is)) {
                return result;
            }
            is.close();
            ftpClient.logout();
            result = true;
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 调用上传操作
     * @param file
     * @param path
     * @return
     */
    public static Map<String, String> uploadImg(MultipartFile file, String path) {
        if ("cover".equals(path)) {
            path = Constant.COVER_IMG_PAHT;
        } else if ("content".equals(path)){
            path = Constant.ARTICLE_CONTENT_IMG_PATH;
        }
        String originalFilename = file.getOriginalFilename();
        //获取文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String fileName = DateTimeUtil.getDateString() + "." + suffix;
        String resPath = null;
        try {
            resPath = ImgUpload.upload(fileName, path, file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> map = new HashMap<>();
        map.put("name", fileName);
        map.put("url", resPath);
        return map;
    }
}
