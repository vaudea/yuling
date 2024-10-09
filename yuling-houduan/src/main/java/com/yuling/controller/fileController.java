package com.yuling.controller;

import cn.hutool.core.io.FileUtil;
import com.yuling.common.Result;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
@CrossOrigin
@RestController
@RequestMapping("/files")
public class fileController {


    @GetMapping("/test")
    public Result test(){
        return Result.success("测试成功");
    }

    // 文件上传存储路径，获取当前用户工作目录并在其下创建 avatar 目录
    private static final String AVATAR_PATH = System.getProperty("user.dir") + "/avatar/";

    //上传头像
    @PostMapping("/user/avatar")
    public Result uploadAvatar(@RequestParam("avatarFile") MultipartFile avatarFile, @RequestParam("jobNumber") String jobNumber) {
        synchronized (fileController.class) {
//           //获取的当前unix时间戳
            String flag = System.currentTimeMillis() + "";
            //这个是表示路径 ， 我的路径是 /avatar/工号/头像名称
            String filePath = jobNumber + "/";
            //获取文件原始名称 + 扩展名
            String originalFilename = avatarFile.getOriginalFilename();
            // 使用时间戳拼接原始文件名和扩展名
            String fileName = flag + "-" + originalFilename;
            System.out.println(avatarFile.getOriginalFilename());
            try {
                if (!FileUtil.isDirectory(AVATAR_PATH+filePath)) {
                    FileUtil.mkdir(AVATAR_PATH+filePath);
                }
                FileUtil.writeBytes(avatarFile.getBytes(), AVATAR_PATH + filePath + fileName);
                System.out.println(fileName + "--头像上传成功");
                Thread.sleep(1L);
            } catch (Exception e) {
                System.err.println(fileName + "--头像上传失败");
            }
            return Result.success(filePath + fileName);
        }
    }

    /**
     * 获取头像
     */
    @GetMapping("/getAvatar/{jobNumber}/{avatarPath}")
    public void avatarPath(@PathVariable String jobNumber, @PathVariable String avatarPath, HttpServletResponse response)  {
        OutputStream os;
        try {
            // 组合文件的完整路径
            String fullPath = AVATAR_PATH +jobNumber +"/"+ avatarPath;

            // 检查文件是否存在
            if (FileUtil.exist(fullPath)) {
                // 设置响应头，文件将以附件形式下载
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(avatarPath, "UTF-8"));
                response.setContentType("application/octet-stream");
                // 读取文件字节并写入响应输出流
                byte[] bytes = FileUtil.readBytes(fullPath);
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
            } else {
                // 文件不存在时的处理
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "文件不存在");
            }
        } catch (Exception e) {
            System.out.println("文件下载失败: " + e.getMessage());
        }
    }
}
