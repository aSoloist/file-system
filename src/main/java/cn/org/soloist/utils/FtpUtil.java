package cn.org.soloist.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Soloist
 * @version 1.0
 * @createtime 2018/5/28 15:57
 * @email ly@soloist.top
 * @description
 */
public class FtpUtil {

    private final static String ENCODING = "GBK";
    // 默认账号
    private final static String USERNAME = "anonymous";
    private final static String PASSWORD = "";

    public static FTPClient getClient(String url) {
        return getClient(url, FTPClient.DEFAULT_PORT);
    }

    public static FTPClient getClient(String url, Integer port) {
        FTPClient ftpClient = new FTPClient();
        // 设置编码
        ftpClient.setControlEncoding(ENCODING);
        try {
            ftpClient.connect(url, port);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 匿名登陆
            login(ftpClient, USERNAME, PASSWORD);
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                ftpClient.disconnect();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("获取FTP客户端失败", e);
        }

        return ftpClient;
    }

    public static Boolean login(FTPClient ftpClient, String username, String password) {
        Boolean login;
        try {
            login = ftpClient.login(username, password);
            ftpClient.enterLocalPassiveMode();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("登陆失败", e);
        }

        return login;
    }

    /**
     * 获取指定路径下的目录
     *
     * @param ftpClient ftp对象
     * @param pathname  路径名
     * @return key 文件名 value 文件或目录 0 目录 1 文件
     */
    public static Map<String, Integer> getResources(FTPClient ftpClient, String pathname) {
        Map<String, Integer> resources = new HashMap<>();
        try {
            FTPFile[] ftpFiles = ftpClient.listFiles(pathname);
            for (FTPFile ftpFile : ftpFiles) {
                int i;
                if (ftpFile.isDirectory()) {
                    i = 0;
                } else {
                    i = 1;
                }
                resources.put(ftpFile.getName(), i);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("获取目录失败", e);
        }
        return resources;
    }

    public static void changeWorkDir(FTPClient ftpClient, String pathname) {
        try {
            ftpClient.changeWorkingDirectory(pathname);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("改变工作目录失败", e);
        }
    }

    public static Boolean uploadFile(FTPClient ftpClient, String filename, InputStream inputStream) {
        Boolean b;
        try {
            b = ftpClient.storeFile(filename, inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("上传失败", e);
        }
        return b;
    }

    public static Boolean downloadFile(FTPClient ftpClient, String filename, OutputStream outputStream) {
        Boolean b;
        try {
            b = ftpClient.retrieveFile(filename, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("下载失败", e);
        }

        return b;
    }
    
    public static Boolean deleteFile(FTPClient ftpClient, String pathname) {
        Boolean b;
        try {
            b = ftpClient.deleteFile(pathname);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("删除失败", e);
        }
        
        return b;
    }

    public static void close(FTPClient ftpClient) {
        try {
            if (ftpClient.isConnected()) {
                ftpClient.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("关闭失败", e);
        }
    }

    public static void main(String[] args) {
        
        /*FTPClient client = getClient("222.22.49.188");
        Map<String, Integer> resources = getResources(client, "/2017-2018学年第二学期/WEB开发框架");
        resources.forEach((key, value) -> System.out.println(key + " - " + value));*/
        
        /*
         下载测试
         */
        /*FTPClient client = getClient("222.22.49.188");
        System.out.println("链接" + (client != null));
        Boolean login = login(client, "upload", "soft17922");
        System.out.println("登陆" + login);
        String filename = "201677I1311黄孟辉第九次试验.rar";
        String pathname = System.getProperty("user.dir") + "\\src\\main\\java\\cn\\org\\soloist\\utils\\" + filename;
        File file = new File(pathname);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            
            OutputStream outputStream = new FileOutputStream(file);
            Boolean downloadFile = downloadFile(client, "/2017-2018学年第二学期/WEB开发框架/" + filename, outputStream);
            System.out.println("下载" + downloadFile);
            close(client);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        /*
         上传测试 
         */
        /*FTPClient client = getClient("222.22.49.188");
        System.out.println("链接" + (client != null));
        assert client != null;
        Boolean login = login(client, "upload", "soft17922");
        System.out.println("登陆" + login);
        String filename = "新建文本文档.txt";
        String pathname = System.getProperty("user.dir") + "\\src\\main\\java\\cn\\org\\soloist\\utils\\" + filename;
        File file = new File(pathname);
        try {
            InputStream inputStream = new FileInputStream(file);
            Boolean uploadFile = uploadFile(client, "/2017-2018学年第二学期/WEB开发框架/" + filename, inputStream);
            System.out.println(client.getReplyString());
            System.out.println("上传" + uploadFile);
            close(client);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
