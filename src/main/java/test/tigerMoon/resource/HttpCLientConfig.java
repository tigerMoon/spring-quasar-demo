package test.tigerMoon.resource;

import co.paralleluniverse.fibers.httpclient.FiberHttpClientBuilder;
import org.apache.http.client.HttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.*;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiger on 16-7-28.
 */

@Configuration
public class HttpCLientConfig {

    @Bean
    public HttpClient httpClient() {
        return FiberHttpClientBuilder.
                create(100). // use 100 io threads
                setMaxConnPerRoute(1000).
                setMaxConnTotal(10000).build();
    }

    @Bean
    public ClientHttpRequestFactory httpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

    private RestTemplate getRestTemplateByConverts(List<HttpMessageConverter<?>> converters) {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.setMessageConverters(converters);

        restTemplate.setRequestFactory(new InterceptingClientHttpRequestFactory(
                new BufferingClientHttpRequestFactory(httpRequestFactory()), interceptors
        ));
        return restTemplate;
    }

    private List<HttpMessageConverter<?>> addSelfHttpMessageConverters(HttpMessageConverter<?> converter) {
        List<HttpMessageConverter<?>> httpMessageConverters = new ArrayList<>();
        httpMessageConverters.add(new ByteArrayHttpMessageConverter());
        httpMessageConverters.add(new StringHttpMessageConverter());
        httpMessageConverters.add(new ResourceHttpMessageConverter());
        httpMessageConverters.add(new SourceHttpMessageConverter<>());
        httpMessageConverters.add(new AllEncompassingFormHttpMessageConverter());
        httpMessageConverters.add(converter);
        return httpMessageConverters;
    }

    @Bean
    public RestTemplate restTemplate() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON);
        mediaTypes.add(MediaType.ALL);
        converter.setSupportedMediaTypes(mediaTypes);

        return getRestTemplateByConverts(addSelfHttpMessageConverters(converter));
    }
}
