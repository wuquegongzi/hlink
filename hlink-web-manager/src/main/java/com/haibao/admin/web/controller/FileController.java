package com.haibao.admin.web.controller;

import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.io.*;

/*
 * @Author ml.c
 * @Description 文件上传下载操作接口
 * @Date 11:31 2020-04-24
 **/
@Api(tags = "文件上传下载操作接口",position=22)
@Validated
@RestController
@RequestMapping("/file")
public class FileController extends BaseController {

    private Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    @ApiOperationSupport(author = "ml.c")
    @ApiOperation(value = "通用下载")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "filePath",value = "文件路径",paramType = "query"),
                    @ApiImplicitParam(name = "fileName",value = "下载的文件名",paramType = "query")})
    @GetMapping("down")
    public void downFlie(@NotEmpty(message = "文件名路径不可为空")
                                         String filePath,
                             String fileName,
                             HttpServletResponse response){

        String[] s2 = filePath.split("/");
        fileName = StrUtil.isEmpty(fileName)?s2[s2.length -1]:fileName;

        if (fileName != null) {
            //设置文件路径
            File file = new File(filePath);

            if (!file.exists()) {
                LOGGER.error("该文件不存在：{}",filePath);
//                throw new CustomException(CodeEnum.NOT_FOUND_ERROR,"文件不存在");
                return;
            }
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment;fileName="
                    + fileName);
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    bis.close();
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        String filePath = "/data01/hlink/app/udf/b128ff99-0c2b-4fbe-9980-babdd75dd96doriginal-quickstart-0.2.jar";
       String[] s2 = filePath.split("/");
//        for (int i = 0; i < s2.length; i++) {
//            System.out.println(s2[i]);
//        }

        System.out.println(s2[s2.length - 1]);
    }
}
