package com.blog.admin.config;


/**
 * 常量配置
 *
 * @author liushuai
 */
public final class Constant {

    private Constant() {
    }

    //----------------------------------------------------
    //确定删除
    public static final String IS_DELETE = "1";
    //取消删除
    public static final String UN_DELETE = "0";

    //--------------------评论--------------------------------
    //审核允许通过
    public static final String IS_ALLOW = "yes";
    //审核不允许通过
    public static final String UN_ALLOW = "no";
    //审核状态
    //审核已通过
    public static final String AUDIT_PASS = "1";
    //待审核
    public static final String AUDIT_WAIT = "2";
    //审核没通过
    public static final String AUDIT_NOT_PASS = "0";
    //评论来自留言板
    public static final String COMMENT_FROM_MSGBOARD = "1";
    //评论来自文章
    public static final String COMMENT_FROM_ARTICLE = "2";

    //--------------------文章--------------------------------
    //推荐
    public static final String IS_REC = "1";
    //不推荐
    public static final String UN_REC = "0";
    //主页显示
    public static final String IS_MAIN = "1";
    //主页不显示
    public static final String UN_MAIN = "0";
    //原创
    public static final String IS_OWN = "1";
    //转载
    public static final String UN_OWN = "0";

    //--------------------FTP图片服务器配置--------------------------------

    //服务器地址
    public static final String FTP_HOST = "47.100.15.146";
    //端口
    public static final Integer FTP_PORT = 21;
    //ftp服务器用户名
    public static final String FTP_USERNAME = "ftpuser";
    //ftp服务器密码
    public static final String FTP_PASSWORD = "272829";
    //图片存储路径
    public static final String FTP_REALPATH = "/home/ftpimages/blogimg";
    //返回图片地址前缀
    public static final String IMAGE_BASE_URL = "http://lsblog.vip";
    //文章封面图片存储地址
    public static final String COVER_IMG_PAHT = "/coverImg";
    //文章内容图片存储地址
    public static final String ARTICLE_CONTENT_IMG_PATH = "/articleImg";

}
