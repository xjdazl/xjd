package com.gome.iuv.python.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * User: 夏加冬
 * Date: 2018/3/26  19:02
 */
@RestController
@RequestMapping("/pythonImageRest")
public class PythonImageRest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/operate", method = RequestMethod.POST)
    public void operate(@RequestParam("pic") MultipartFile file, HttpServletResponse response) {
        logger.info("---python处理开始---");
        String outStr = "";
        // 判断文件是否为空
        if (!file.isEmpty()) {
            try {
                logger.info("---开始处理文件---"+file.getOriginalFilename());
                // 文件保存路径
                String filePath = "/root/cls_demo/" + file.getOriginalFilename();
                //String filePath = "D:\\" + file.getOriginalFilename();
                // 转存文件
                file.transferTo(new File(filePath));
                Process pr = Runtime.getRuntime().exec("python /root/cls_demo/cls1.py "+filePath);

                BufferedReader in = new BufferedReader(new
                        InputStreamReader(pr.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    logger.info(line);
                    outStr = outStr + line;
                }
                in.close();
                pr.waitFor();
                PrintWriter out = response.getWriter();
                out.write(outStr);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.info("---python处理结束---");
        }
    }
}
