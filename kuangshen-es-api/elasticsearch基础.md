> ElasticSearch 7.6.1



下载慢的小伙伴们可以到 华为云的镜像去下载
速度很快，自己找对应版本就可以
ElasticSearch: https://mirrors.huaweicloud.com/elasticsearch/?C=N&O=D
logstash: https://mirrors.huaweicloud.com/logstash/?C=N&O=D
kibana: https://mirrors.huaweicloud.com/kibana/?C=N&O=D

[TOC]

## 基础

### es安装

1. ES解压就可以使用
2. 双击elasticseaerch.bat启动
3. 测试访问9200端口![image-20201223165829665](E:\dev\picture\image-20201223165829665.png)



___

### es可视化界面  elasticsearch head

1. 下载elasticsearch head压缩包

2. 解压进入文件，执行cnpm install 安装依赖

3. 启动：npm run start 

4. 无法跨域，修改elasticsearch.yml

   ~~~shell
   http.cors.enabled: true
   http.cors.allow-origin: "*"
   ~~~

5. 重启es![image-20201223165816440](E:\dev\picture\image-20201223165816440.png)

___

### 安装kibana

1. 解压压缩包
2. 双击kibana.bat启动
3. 测试 http://localhost:5601
4. 官方可汉化



___

### ik分词器

1. 下载：https://github.com/medcl/elasticsearch-analysis-ik/releases

2. 解压到es安装目录plugins文件夹下ik目录

3. 重启es![image-20201224102951073](E:\dev\picture\image-20201224102951073.png)

4. kibana测试---------字典？自己需要的关键词需要自己加到字典中才能不被切分。![image-20201224103746834](E:\dev\picture\image-20201224103746834.png)![image-20201224103814078](E:\dev\picture\image-20201224103814078.png)

5. 自定义关键词,mydic.dic为自定义的字典文件，内容为要查的关键词，测试为“狂神说”。

   ![image-20201224144422299](E:\dev\picture\image-20201224144422299.png)

6. 重启es![image-20201224144753802](E:\dev\picture\image-20201224144753802.png)

___

### Rest风格

> Rest风格

![image-20201224145138996](E:\dev\picture\image-20201224145138996.png)



### 索引的基本操作

1. 创建索引

   ~~~shell
   PUT /索引名称/类型名/文档id
   {请求体}
   ~~~

   ![image-20201224145424178](E:\dev\picture\image-20201224145424178.png)

2. 指定字段类型测试![image-20201224150819413](E:\dev\picture\image-20201224150819413.png)

3. 测试默认信息![image-20201224151423840](E:\dev\picture\image-20201224151423840.png)

4. DELETE  索引库

### 文档的基本操作(重点)

1. 添加数据

   ~~~shell
   PUT /kuangshen/user/1
   {
     "name":"狂神说",
     "age":23,
     "desc":"一顿操作猛如虎",
     "tags":["技术宅","nuan","直男"] 
   }
   ~~~

   ![image-20201224153842419](E:\dev\picture\image-20201224153842419.png)

2. 查询  GET命令

   ![image-20201224154224732](E:\dev\picture\image-20201224154224732.png)

3. 更新数据 PUT，必须全部数据赋值，否则没有的字段会赋空![image-20201224154539431](E:\dev\picture\image-20201224154539431.png)

4. POST 更新（推荐），路径后跟 **_update** 可以任意更新哪个字段；不加update后缀，没包含的字段也会置空。![image-20201224155244928](E:\dev\picture\image-20201224155244928.png)

   

5. 简单查询

   ![image-20201224161941481](E:\dev\picture\image-20201224161941481.png)

6. 复杂查询 排序、分页、高亮、模糊查询。。。

   ~~~json
   GET /kuangshen/user/_search
   {
     "query": {
       "match": {
         "name": "狂神"
       }
     }
   }
   ~~~

   **过滤查询结果字段**![image-20201224163338968](E:\dev\picture\image-20201224163338968.png)

   **排序**![image-20201224163732029](E:\dev\picture\image-20201224163732029.png)

   **分页**![image-20201224163905485](E:\dev\picture\image-20201224163905485.png)

   **多条件查询**  must 相当于mysql的where后的and，should相当于or，must_not相当于not

   ![image-20201224164636231](E:\dev\picture\image-20201224164636231.png)

   **should 条件**![image-20201224164852437](E:\dev\picture\image-20201224164852437.png)

   **过滤条件**![image-20201224170351408](E:\dev\picture\image-20201224170351408.png)

   **高亮**![image-20201224175604873](E:\dev\picture\image-20201224175604873.png)**自定义高亮标签**![image-20201224175745251](E:\dev\picture\image-20201224175745251.png)

7. sd



## 实战测试

### es集成springboot

> 步骤

1. 导入maven依赖

   ~~~xml
   <dependency>
       <groupId>org.elasticsearch.client</groupId>
       <artifactId>elasticsearch-rest-high-level-client</artifactId>
       <version>7.10.1</version>
   </dependency>
   ~~~

2. 找对象初始化

   ~~~java
   //初始化
   RestHighLevelClient client = new RestHighLevelClient(
           RestClient.builder(
                   new HttpHost("localhost", 9200, "http"),
                   new HttpHost("localhost", 9201, "http")));
   
   //用完关闭
   client.close();
   ~~~

3. 分析这个类的方法

4. **注意**![image-20210111170116398](E:\dev\picture\image-20210111170116398.png)自定义依赖

   ![image-20210119163233132](E:\dev\picture\image-20210119163233132.png)

5. sd

