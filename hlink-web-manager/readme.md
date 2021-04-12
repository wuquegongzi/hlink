# Flink WEB 管理模块
## 接口访问地址：
- http://localhost:8080/doc.html 

## 版本
- springboot2
- [mybatis plus](https://mp.baomidou.com/)

    mybatis plus 带分页插件，内置通用 Mapper、通用 Service

- 工具包 [hutool](https://hutool.cn/)

## 代码实例
- 分页例子： TestController  welcome方法
- 测试接口： localhost:8080/  会打印user测试表的数据

## 准备工作

1、进入lib目录，执行oracle jar到本地maven，需要配置maven环境变量

    cd doc/lib
    sh oracle2mvn.sh

2、如果是生产环境 修改数据库配置时，可设置加密

    cd doc/jasypt

修改 jasypt.sh 文件，input为你的数据库密码，password为加密盐
然后执行

     sh jasypt.sh

获取output,修改 application-prod.yml, spring.datasource.password = ENC( xxx )
jasypt.encryptor.password 为加密盐，生产环境最好写入环境变量，增加安全性
或者启动时加上参数  jasypt.encryptor.password 即：
java -jar target/flink-job-admin.jar --jasypt.encryptor.password=password

文档地址：https://github.com/ulisesbocchio/jasypt-spring-boot


## 启动
1、编译代码

    source ~/.bash_profile 
    mvn clean package -Dmaven.test.skip=true

2、启动 AdminApplication 即可


##开发手册

### json工具类

1、字符串 转 JsonObject
JsonObject jsonObject = JsonParser.parseString(jsonStr).getAsJsonObject();

2、字符串 转 JsonArray
JsonArray array = JsonParser.parseString(json).getAsJsonArray();

3、JsonObject 取value字符串 ,需要.getAsString()，否则会出现双引号，不能使用toString
String type = typesJsonObject.get("type").getAsString()

4、JsonObject 取value Json对象
JsonObject properties = jsonObject.getAsJsonObject("properties");

5、springboot 内置json解析器是jackson, 接口返回对象数据类型如果是JsonArray，可能会有异常
 
 不可使用：Response r = Response.success(jsonArray);

 建议使用 Response.success(GsonUtils.GsonToListMaps(jsonStr))； 将字符串序列化List<Map>

6、建议：小json 使用GSON，大数据量json使用jackson
