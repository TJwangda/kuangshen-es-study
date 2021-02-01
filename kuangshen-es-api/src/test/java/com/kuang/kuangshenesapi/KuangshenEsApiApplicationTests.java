package com.kuang.kuangshenesapi;

import com.alibaba.fastjson.JSON;
import com.kuang.kuangshenesapi.pojo.User;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * es 7.6.x 高级api测试
 */
@SpringBootTest
class KuangshenEsApiApplicationTests {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    void contextLoads() {
    }

    /**
     * 创建索引
     */
    @Test
    void testCreateIndex() throws IOException {
        //创建索引请求
        CreateIndexRequest  request= new CreateIndexRequest("kuang_index");
        //执行请求，获得响应
        CreateIndexResponse createIndexResponse =  restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse);
    }

    /**
     * 获取索引,判断是否存在
     */
    @Test
    void testGetIndex() throws IOException {
        //创建请求
        GetIndexRequest request = new GetIndexRequest("kuang_index2");
        //执行，获取响应
        boolean bool = restHighLevelClient.indices().exists(request,RequestOptions.DEFAULT);
        System.out.println("==="+bool);
    }

    /**
     * 删除索引
     */
    @Test
    void testDelIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("kuang_index");
        AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices().delete(request,RequestOptions.DEFAULT);
        System.out.println("======"+acknowledgedResponse.isAcknowledged());
    }

    /**
     * 添加文档
     */
    @Test
    void testAddDoc() throws IOException {
        //创建对象
        User user = new User("狂神说",3);
        //创建请求
        IndexRequest request = new IndexRequest("kuang_index");
        //规则 put /kuang_index/_doc/1
        request.id("1");
        request.timeout(TimeValue.timeValueSeconds(1));
        //数据放入请求
        request.source(JSON.toJSONString(user), XContentType.JSON);
        //发送请求
        IndexResponse response = restHighLevelClient.index(request,RequestOptions.DEFAULT);
        System.out.println("==="+response.toString());
    }

    /**
     * 获取文档,判断是否存在
     */
    @Test
    void testGetDoc() throws IOException {
        GetRequest getRequest = new GetRequest("kuang_index", "1");
        //不获取返回的_source上下文
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");

        boolean oob = restHighLevelClient.exists(getRequest,RequestOptions.DEFAULT);
        System.out.println("======="+oob);
    }

    /**
     * 获取文档信息
     */
    @Test
    void testGetDocInfo() throws IOException {
        GetRequest getRequest = new GetRequest("kuang_index", "1");

        GetResponse oob = restHighLevelClient.get(getRequest,RequestOptions.DEFAULT);
        System.out.println("======="+oob.getSourceAsString());
        System.out.println("======="+oob);
    }

    /**
     * 更新文档信息
     */
    @Test
    void testUpdateDocInfo() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("kuang_index", "1");
        updateRequest.timeout("1s");
        User user = new User("狂神说java", 18);
        updateRequest.doc(JSON.toJSONString(user),XContentType.JSON);
        UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println("======="+updateResponse);
        System.out.println("======="+updateResponse.status());
    }

    /**
     * 删除文档信息
     */
    @Test
    void testDelDocInfo() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("kuang_index", "1");
        deleteRequest.timeout("1s");
        DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println("======="+deleteResponse);
        System.out.println("======="+deleteResponse.status());
    }

    /**
     * 批量插入文档信息
     */
    @Test
    void testBulkAddDocInfo() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("10s");
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("kuangshenq1",3));
        users.add(new User("kuangshenq2",3));
        users.add(new User("kuangshenq3",3));
        users.add(new User("kuangshenq4",3));
        users.add(new User("qinjiang1",3));
        users.add(new User("qinjiang1",3));
        users.add(new User("qinjiang1",3));
        users.add(new User("qinjiang2",3));
        users.add(new User("qinjiang2",3));
        //批处理请求
        for(int i = 0;i<users.size();i++){
            bulkRequest.add(
                    new IndexRequest("kuang_index")
                            .id(""+(i+1))
                            .source(JSON.toJSONString(users.get(i)),XContentType.JSON)
            );
        }
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println("======="+bulkResponse);
        System.out.println("======="+bulkResponse.hasFailures());//是否失败
    }

    /**
     * 查询
     */
    @Test
    void testSearch() throws IOException {
        SearchRequest searchRequest = new SearchRequest("kuang_index");
        //构建查询条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //查询条件，可以使用QueryBuilders工具实现
        //QueryBuilders.termQuery  精确匹配
        //QueryBuilders.matchAllQuery()  匹配所有
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "qinjiang1");

        sourceBuilder.query(termQueryBuilder);
        sourceBuilder.from();//分页
        sourceBuilder.size();
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(searchResponse.getHits()));
        System.out.println("=========================");
        for(SearchHit documentFields : searchResponse.getHits().getHits()){
            System.out.println(documentFields.getSourceAsMap());
        }
    }

}
