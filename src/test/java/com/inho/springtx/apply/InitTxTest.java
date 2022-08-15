package com.inho.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.PostConstruct;

@Slf4j
@SpringBootTest
public class InitTxTest {

    @Autowired
    Hello hello;

    @Test
    void test()
    {
        //hello.initV1();
    }

    @TestConfiguration
    static class InitTxTestConfig
    {
        @Bean
        Hello hello() {
            return  new Hello();
        }
    }

    static class Hello
    {
        /**
         * 초기화 코드가 먼저 실행되고, 트랜잭션 AOP가 적용되기 때문에 헤서드에서 트랜잭션을 획득할 수 없다.
         * 대안) @EventListener(AppllicationReadyEvent.class)
         */
        @PostConstruct
        @Transactional
        public void initV1(){
            boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("initV1 @PostConstruct tx active={}", isActive);
        }



        /**
         * 초기화 코드가 먼저 실행되고, 트랜잭션 AOP가 적용되기 때문에 헤서드에서 트랜잭션을 획득할 수 없다.
         * 대안) @EventListener(AppllicationReadyEvent.class)
         */
        @EventListener(ApplicationReadyEvent.class)
        @Transactional
        public void initV2(){
            boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("initV2 @PostConstruct tx active={}", isActive);
        }
    }
}
