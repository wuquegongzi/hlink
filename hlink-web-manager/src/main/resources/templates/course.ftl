<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>教程freemarker</title>
</head>
<body>
<h1>数据类型</h1>
<h2>1、基础类型</h2>
<p>字符串：${strVal}</p>
<p>日期：${dateVal?date}</p>
<p>日期：${sqlDateVal}</p>
<p>整数：${intVal}</p>
<p>长整数：${longVal}</p>
<p>双精度：${doubleVal}</p>
<p>布尔值：${booleanVal?string('yes','no')}</p>
<p>null：${nullVal!'我是空'}</p>
<p>missing：${sssVal!}</p>

<h1>逻辑</h1>
<h2>1、取基本值</h2>
<div>
    <ul>
        <li>赋值&运算</li>
        <#assign a=100/>
        a = <font>${a}</font><br/>
        a + 100 = ${a+100}<br/>
    </ul>
</div>

<h2>2、封装对象</h2>
<div>
    <ul>
        <li>对象</li>
        ${user.id} <br/>
        ${(user.email)!'默认'} <br/>
        ${(user.email)!?html} <br/>
    </ul>
</div>

<h2>3、集合遍历</h2>
<div>
    <ul>
        <li>list</li>
        <#if list??> <!-- ?? 双问号代表 如果集合不存在或者为空，则不执行 -->
            <#list list as item>
                <font>ID: ${item.id}</font> 年龄：${item.age} <br/>
            </#list>
        </#if>

        <li>map</li>
        <if map?exists> <!-- 判断非空 或者用 ?exists-->
            <#list map?keys as key>
                ${key}:${map[key]} <br/>
            </#list>
        </if>
    </ul>
</div>

<h2>4、if</h2>
<div>
    <ul>
        <li>if</li>
       <#assign ss=100/>
       <#if ss==99>
           ss=99
       <#else>
           ss!=99
       </#if>

        <br/>

        <#if ss&gt;99>
            ss 大于99
        <#elseif ss ==99>
            ss 等于99
        <#else >
            ss 小于99
        </#if>
        <br/>

        <li>if 多条件 ||,&&,!</li> <!-- 多条件可以使用括号 -->
        <#assign var = 'java'/>
        <if var =='java' || var =='flink' >
            java or flink
        </if>
        <br/>
        <if var =='java' && var?length ==4 > <!-- ?length 判断长度-->
            java length is 4
        </if>
    </ul>
</div>


<h2>5、switch</h2>
<div>
    <ul>
        <li>switch</li>
        <#assign b=10/>

        <#switch b>
            <#case 1>
            1 <br/>
            <#break >
            <#case 10>
            10 <br/>
            <#break >
            <#default >
            100 <br/>
        </#switch>
    </ul>
</div>

<h1>字符串</h1>
<h2>1、字符串常用内建函数</h2>
<div>
    <ul>
        <#assign a='HELLO'>
        <#assign b='world'>
        <li>连接</li>
        ${a+b} <br/>
        <li>截取</li>
        ${(a+b)?substring(5,8)} <br/>
        <li>长度</li>
        ${(a+b)?length} <br/>
        <li>大写</li>
        ${(a+b)?upper_case} <br/>
        <li>小写</li>
        ${(a+b)?lower_case} <br/>
        <li>index_of</li>
        ${(a+b)?index_of("w")} <br/>
        <li>last_index_of</li>
        ${(a+b)?last_index_of("o")} <br/>
        <li>replace</li>
        ${(a+b)?replace("o","xx")} <br/>

    </ul>
</div>

<h1>自定义函数</h1>
<h2>1、自定义函数-整数排序 sort_int</h2>
<div>
    <ul>
        <#assign myList=[2,1,6,4,3,5,7]>
        <li>未排序</li>
         <#list myList as item>
             ${item},
         </#list>
        <li>排序后</li>
        <#list sort_int(myList) as item>
            ${item},
        </#list>
    </ul>
</div>
<h2>2、自定义函数-整数排序 sort_int</h2>
<div>
    <ul>
        <#assign myList=[2,1,6,4,3,5,7]>
        <li>未排序</li>
        <#list myList as item>
            ${item},
        </#list>
        <li>排序后</li>
        <#list sort_int(myList) as item>
            ${item},
        </#list>
    </ul>
</div>
</body>
</html>