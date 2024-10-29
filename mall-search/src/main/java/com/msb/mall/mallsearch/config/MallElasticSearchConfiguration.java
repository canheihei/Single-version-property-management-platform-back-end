package com.msb.mall.mallsearch.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MallElasticSearchConfiguration {
	public static final RequestOptions COMMON_OPTIONS;
	static {
		RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
//        builder.addHeader("Authorization", "Bearer " + TOKEN);
//        builder.setHttpAsyncResponseConsumerFactory(
//                new HttpAsyncResponseConsumerFactory
//                        .HeapBufferedResponseConsumerFactory(30 * 1024 * 1024 * 1024));
		COMMON_OPTIONS = builder.build();
	}

	@Bean
	public RestHighLevelClient restHighLevelClient(){
		RestClientBuilder builder = RestClient.builder(new HttpHost("192.168.56.100", 9200, "http"));
		RestHighLevelClient client = new RestHighLevelClient(builder);
		return client;
	}
}
