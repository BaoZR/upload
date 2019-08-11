package com.bob.upload;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/*
   学到了MultipartFile类是上传来的文件
   HttpServletRequest.getServletContext().getRealPath()可以获取服务器真实路径
   HttpServletRequest.getScheme获取协议
   HttpServletRequest.getServerName获取服务器名字
   HttpServletRequest.getServerPort获取服务器端口
   file new一个对象的方法有传入文件地址String 或父文件路径加自己的路径（一般为文件名）
   file.mkdirs新建文件夹
   file.getOriginalFileName获取文件名
   file.transferTo(file)复制一个文件到参数给定的文件路径中（包括文件名）
   UUID.randomUUID生成随机内容别忘toString
   SimpleDateFormat 设置一种时间格式 有个format方法可将传入的Date转化为符合格式的String
   File一般带有文件名
 */

@RestController
public class UploadController {

    SimpleDateFormat sdf =  new SimpleDateFormat("/yyyy/MM/dd/");
    @PostMapping("/upload")
    public String upload(MultipartFile file, HttpServletRequest request){

        System.out.println(request.getContextPath());
        System.out.println(request.getServletPath());
        System.out.println(request.getServletContext());

        String format = sdf.format(new Date());
        String realPath = request.getServletContext().getRealPath("/img")+format;

        File  folder = new File(realPath);
        if(!folder.exists()){
            folder.mkdirs();
        }

        String oldName = file.getOriginalFilename();
        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));


        try {
            System.out.println(new File(folder,newName));

            file.transferTo(new File(folder,newName));
            String url = request.getScheme() + "//" + request.getServerName() + ":" +request.getServerPort() + "/img" + format + newName;
            return url;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }

}
