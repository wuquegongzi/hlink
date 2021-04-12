package com.haibao.admin.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import com.haibao.admin.web.common.result.Response;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * @ClassName TDFileUtils
 * @Description 文件操作方法
 * @Author ml.c
 * @Date 2020/2/14 3:34 下午
 * @Version 1.0
 */
public class TDFileUtils {

    /**
     * 校验文件类型
     * @param oldname
     * @param s
     * @return
     */
    public static boolean checkFileType(String oldname, String s) {

        return ".json".equals(oldname.substring(oldname.lastIndexOf("."),oldname.length()));

    }

    /**
     * jsonSchema 验证
     * @param jsonSchema
     * @throws JSONException
     * @throws ValidationException
     */
    public static void jsonSchemaValidate(String jsonSchema) throws JSONException,ValidationException{

        InputStream inputStream = TDFileUtils.class.getResourceAsStream("/templates/ddl/schema.json");

        JSONObject schemaJsonObj = new JSONObject(new JSONTokener(inputStream));

        SchemaLoader loader = SchemaLoader.builder()
                .schemaJson(schemaJsonObj)
                .draftV7Support()
                .build();
        org.everit.json.schema.Schema schema = loader.load().build();

        JSONObject data = new JSONObject(jsonSchema);

        schema.validate(data);
    }

    /**
     * 上传json文件到临时目录
     * @param file
     * @param req
     * @return
     */
    public static Response uplaodFileToTemp(MultipartFile file, HttpServletRequest req) {

        String oldname=file.getOriginalFilename();

        String format = DateUtil.format(new Date(),"/yyyy/MM/dd/");
        //创建保存路径
        String realpath = req.getServletContext().getRealPath("/templates/json_field")+ format;
        File files=new File(realpath);
        if (!FileUtil.exist(realpath)) {
            FileUtil.mkdir(realpath);
        }

        String newname= UUID.randomUUID().toString() + oldname.substring(oldname.lastIndexOf("."),oldname.length());
        try {
            file.transferTo(new File(files,newname));
        } catch (IOException e) {
            e.printStackTrace();
            return Response.error(500,"上传失败，IO异常");
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return Response.error(500,"上传失败，IllegalStateException");
        }

        String schemaPath = realpath+newname;

        return Response.success(schemaPath);
    }


    /*
     * 读取配置文件
     */
    public static String readFile(InputStream inputStream) throws IOException {

        StringBuilder builder = new StringBuilder();
        try {
            InputStreamReader reader = new InputStreamReader(inputStream , "UTF-8" );
            BufferedReader bfReader = new BufferedReader( reader );
            String tmpContent = null;
            while ( ( tmpContent = bfReader.readLine() ) != null ) {
                builder.append( tmpContent );
            }
            bfReader.close();
        } catch ( UnsupportedEncodingException e ) {
            // 忽略
        }
        return filter( builder.toString() );
    }
    // 过滤输入字符串, 剔除多行注释以及替换掉反斜杠
    private static String filter(String input) {

        return input.replaceAll( "/\\*[\\s\\S]*?\\*/", "" );
    }


    /**
     * @author: c.zh
     * @param fileName
     * @return
     */
    public static  String getFileType(String fileName) {
        String[] strArray = fileName.split("\\.");
        int suffixIndex = strArray.length -1;
        return strArray[suffixIndex];
    }

    /**
     * 获取文件字节大小转换成 kb、M、G 等单元
     * @param size
     * @return
     */
    public static String readableFileSize(long size) {
        if (size <= 0){ return "0"; }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
