package com.example.agricultureFederation.config;

import com.example.agricultureFederation.controller.*;
import com.example.agricultureFederation.datasource.DataSourceConfig;
import com.example.agricultureFederation.repository.*;
import com.example.agricultureFederation.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

@Configuration
public class AppConfig {

    private final DataSource dataSource;

    public AppConfig(DataSourceConfig dataSourceConfig) {
        this.dataSource = dataSourceConfig.dataSource();
    }

    @Bean public CollectiveRepository collectiveRepository() { return new CollectiveRepository(dataSource); }
    @Bean public MemberRepository memberRepository() { return new MemberRepository(dataSource); }
    @Bean public MembershipFeeRepository membershipFeeRepository() { return new MembershipFeeRepository(dataSource); }
    @Bean public FinancialAccountRepository financialAccountRepository() { return new FinancialAccountRepository(dataSource); }
    @Bean public MemberPaymentRepository memberPaymentRepository() { return new MemberPaymentRepository(dataSource); }
    @Bean public CollectivityTransactionRepository transactionRepository() { return new CollectivityTransactionRepository(dataSource); }

    @Bean public CollectiveService collectiveService() { return new CollectiveService(collectiveRepository(), memberRepository()); }
    @Bean public MemberService memberService() { return new MemberService(memberRepository(), collectiveRepository()); }
    @Bean public MembershipFeeService membershipFeeService() { return new MembershipFeeService(membershipFeeRepository(), collectiveRepository()); }
    @Bean public MemberPaymentService memberPaymentService() { return new MemberPaymentService(memberPaymentRepository(), memberRepository(), membershipFeeRepository(), financialAccountRepository(), transactionRepository()); }
    @Bean public CollectivityTransactionService transactionService() { return new CollectivityTransactionService(transactionRepository(), collectiveRepository(), memberRepository(), financialAccountRepository()); }
    @Bean public FinancialAccountService financialAccountService() { return new FinancialAccountService(financialAccountRepository(), collectiveRepository()); }

    @Bean public CollectiveController collectiveController() { return new CollectiveController(collectiveService()); }
    @Bean public MemberController memberController() { return new MemberController(memberService()); }
    @Bean public MembershipFeeController membershipFeeController() { return new MembershipFeeController(membershipFeeService()); }
    @Bean public MemberPaymentController memberPaymentController() { return new MemberPaymentController(memberPaymentService()); }
    @Bean public CollectivityTransactionController transactionController() { return new CollectivityTransactionController(transactionService()); }
    @Bean public FinancialAccountController financialAccountController() { return new FinancialAccountController(financialAccountService()); }
}