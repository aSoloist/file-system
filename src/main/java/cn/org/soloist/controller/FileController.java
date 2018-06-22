package cn.org.soloist.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * @author Soloist
 * @version 1.0
 * @createtime 2018/5/28 18:45
 * @email ly@soloist.top
 * @description
 */
@RestController
@RequestMapping("/files")
public class FileController {

    private final static String UPLOAD_PATH = "WEB-INF/upload/";

    private final static String DOWNLOAD_PATH = "WEB-INF/download/";

    /**
     * 上传文件
     *
     * @param request
     * @param file
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public JSONObject upload(HttpServletRequest request,
                             @RequestParam MultipartFile file) {
        String realPath = request.getSession().getServletContext().getRealPath("/") + UPLOAD_PATH;
        String filename = file.getOriginalFilename();
        File uploadFile = new File(realPath, filename);
        JSONObject result = new JSONObject();
        try {
            file.transferTo(uploadFile);
            result.put("result", "保存成功");
        } catch (IOException e) {
            e.printStackTrace();
            result.put("result", "保存失败");
        }

        return result;
    }

    /**
     * 下载文件
     *
     * @param request
     * @param filename
     * @return
     */
    @RequestMapping(value = "/{filename}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> download(HttpServletRequest request, @PathVariable String filename) {
        String realPath = request.getSession().getServletContext().getRealPath("/") + DOWNLOAD_PATH;
        File file = new File(realPath, filename);
        try {
            //下载显示的文件名，解决中文名称乱码问题  
            String downloadFileName = new String(filename.getBytes("UTF-8"), "iso-8859-1");
            HttpHeaders headers = new HttpHeaders();
            //通知浏览器以attachment（下载方式）打开图片
            headers.setContentDispositionFormData("attachment", downloadFileName);
            //application/octet-stream ： 二进制流数据（最常见的文件下载）。
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<>(FileUtils.readFileToByteArray(file),
                    headers, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
