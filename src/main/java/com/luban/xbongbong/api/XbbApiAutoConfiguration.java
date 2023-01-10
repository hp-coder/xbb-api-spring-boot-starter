package com.luban.xbongbong.api;

import cn.hutool.extra.spring.EnableSpringUtil;
import com.luban.xbongbong.api.biz.XbbLubanBizApi;
import com.luban.xbongbong.api.biz.config.XbbBizConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author HP 2022/12/27
 */
@Configuration
@EnableConfigurationProperties
@Import({XbbBizConfig.class})
public class XbbApiAutoConfiguration {

    @ConditionalOnMissingBean
    @Bean
    public ThreadPoolExecutor executor() {
        return new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(10000), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
