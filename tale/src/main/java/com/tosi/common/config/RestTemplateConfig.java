package com.tosi.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    /**
     * RestTemplate를 빈으로 등록하여 스프링에서 관리되도록 합니다.
     * 스프링 컨텍스트에서 하나의 인스턴스만 생성(싱글턴)되며, 의존성 주입을 통해 여러 곳에서 재사용할 수 있습니다.
     */

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
