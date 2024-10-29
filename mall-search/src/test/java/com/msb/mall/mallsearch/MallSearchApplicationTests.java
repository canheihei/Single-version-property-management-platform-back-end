package com.msb.mall.mallsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msb.mall.mallsearch.config.MallElasticSearchConfiguration;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MallSearchApplicationTests {
    @Autowired
    private RestHighLevelClient client;

    @Test
    void contextLoads() {
        System.out.println("--->"+client);
    }

    @Test
    void saveIndex() throws Exception {
        IndexRequest indexRequest = new IndexRequest("system");
        indexRequest.id("1");
        // indexRequest.source("name","bobokaoya","age",18,"gender","男");
        User user = new User();
        user.setName("chhei");
        user.setAge(21);
        user.setGender("男");
        // 用Jackson中的对象转json数据
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);
        indexRequest.source(json, XContentType.JSON);
        // 执行操作
        IndexResponse index = client.index(indexRequest, MallElasticSearchConfiguration.COMMON_OPTIONS);
        // 提取有用的返回信息
        System.out.println(index);
    }
    @Data
    class User{
        private String name;
        private Integer age;
        private String gender;
    }

}
