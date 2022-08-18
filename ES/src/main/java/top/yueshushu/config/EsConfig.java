package top.yueshushu.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-08-18
 */
@EqualsAndHashCode(callSuper = true)
@Component
@ConfigurationProperties("elasticsearch")
@Data
public class EsConfig extends AbstractElasticsearchConfiguration {
	private String host;
	private Integer port;
	
	@Override
	public RestHighLevelClient elasticsearchClient() {
		RestClientBuilder restClientBuilder = RestClient.builder(new HttpHost(host, port));
		return new RestHighLevelClient(restClientBuilder);
	}
}
