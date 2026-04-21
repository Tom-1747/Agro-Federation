package com.example.agricultureFederation.config;

import com.example.agricultureFederation.controller.CollectiveController;
import com.example.agricultureFederation.controller.MemberController;
import com.example.agricultureFederation.datasource.DataSourceConfig;
import com.example.agricultureFederation.repository.CollectiveRepository;
import com.example.agricultureFederation.repository.MemberRepository;
import com.example.agricultureFederation.service.CollectiveService;
import com.example.agricultureFederation.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

import javax.sql.DataSource;

@Configuration
public class AppConfig {

    private final DataSource dataSource;

    public AppConfig(DataSourceConfig dataSourceConfig) {
        this.dataSource = dataSourceConfig.dataSource();
    }

    @Bean
    public CollectiveRepository collectiveRepository() {
        return new CollectiveRepository(dataSource);
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemberRepository(dataSource);
    }

    @Bean
    public CollectiveService collectiveService() {
        return new CollectiveService(collectiveRepository());
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public CollectiveController collectiveController() {
        return new CollectiveController(collectiveService());
    }

    @Bean
    public MemberController memberController() {
        return new MemberController(memberService());
    }
}
