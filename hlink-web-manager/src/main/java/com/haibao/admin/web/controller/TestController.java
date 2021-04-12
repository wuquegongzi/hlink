package com.haibao.admin.web.controller;

import com.haibao.admin.web.entity.User;
import com.haibao.flink.utils.GsonUtils;
import freemarker.template.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 *  @author: ml.c
 *  @Date: 2020/1/2 5:31 下午
 *  @Description: 测试专用，技术教程
 */
@ApiIgnore
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    FreeMarkerConfigurer configurer;

    @GetMapping("/")
    String index(){

        return "hello world!";
    }

    @GetMapping("/http_test")
    String httpTest(String a){

        System.out.println("http_test: a="+a);

        Map map = new HashMap();
        map.put("a","leon");
        map.put("b","t1");

        Map map2 = new HashMap();
        map.put("a","haibao");
        map.put("b","t2");

        if("ml.c".equals(a)){
            return GsonUtils.gsonString(map);
        }else {
            return GsonUtils.gsonString(map2);
        }
    }

    @GetMapping("/row_test")
    String rowTest(String a){
        System.out.println("a = "+a);
        Map obj1 = new HashMap();
        obj1.put("address","cn");
        obj1.put("company","t1银行");

        Map map = new HashMap();
        map.put("a","leon");
        map.put("b",obj1);

        return GsonUtils.gsonString(map);
    }

    /**
     *
     * @return
     */
    @GetMapping("/array_test")
    String arrayTest(String a){
        System.out.println("a = "+a);
        Map obj1 = new HashMap();
        obj1.put("address","cn");
        obj1.put("company","t1银行");

        Map obj2 = new HashMap();
        obj2.put("address","com");
        obj2.put("company","t2银行");

       List<Map> list = new ArrayList<>();
        list.add(obj1);
        list.add(obj2);

        Map map = new HashMap();
        map.put("a","ml.c");
        map.put("b",list);

        return GsonUtils.gsonString(map);
    }

//    @GetMapping("/")
//    IPage<User> welcome(){
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("age", 18);
//
//        //      不返回总记录数 设置false
//        Page<User> page = new Page<>(1, 2);
//
//        List<User> list = userService.lambdaQuery().eq(User::getAge, 20).list();
//        list.forEach(System.out::println);
//
//        IPage<User> users =  userService.page(page,queryWrapper);
//        return users;
//    }

    /**
     * freemarker 教程
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    @GetMapping("/freemarker")
    String freeMarker() throws IOException, TemplateException {
        Map map = new HashMap(2);
        map.put("strVal","六六六");
        map.put("dateVal",new Date());
        map.put("sqlDateVal",new java.sql.Date(1));
        map.put("intVal",666);
        map.put("longVal",10000L);
        map.put("doubleVal",3.45);
        map.put("booleanVal",true);
        map.put("nullVal",null);

        User user = new User();
        user.setId(1L);
        user.setEmail("<font color='red'>html 字符串</font>");
        map.put("user",user);

        List list = new ArrayList();
        for (int i = 0; i < 3 ; i++) {
            User user2 = new User();
            user2.setId((long)i);
            user2.setAge(i);
            list.add(user2);
        }
        map.put("list",list);

        Map<String,String> myMap = new HashMap<String,String>();
        myMap.put("flink","hello flink");
        myMap.put("spark","hello spark");
        map.put("map",myMap);

        //自定义函数
        map.put("sort_int",new SortMethod());

        //自定义指令 ...
//        todo


        Template template = configurer.getConfiguration().getTemplate("course.ftl");
        String resp = FreeMarkerTemplateUtils.processTemplateIntoString(template,map);


        System.out.println(resp);
        return resp;
    }

}

/**
 * 自定义freemark函数
 */
class SortMethod implements TemplateMethodModelEx{

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        //获取第一个参数
        SimpleSequence arg0 = (SimpleSequence)arguments.get(0);
        List<BigDecimal> list = arg0.toList();

        Collections.sort(list, new Comparator<BigDecimal>() {
            @Override
            public int compare(BigDecimal o1, BigDecimal o2) {
                //升序
                return o1.intValue() - o2.intValue();
            }
        });

        return list;
    }
}

