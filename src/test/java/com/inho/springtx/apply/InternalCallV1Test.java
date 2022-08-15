package com.inho.springtx.apply;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class InternalCallV1Test {

    @Autowired
    CallService callService;

    @Test
    void callExternal()
    {
        log.info("callService class={}", callService.getClass());
        callService.external();
    }


    @TestConfiguration
    static class InternalCallV1TestConfig
    {
        @Bean
        CallService callService(){
            return new CallService(InternalService());
        }

        @Bean
        InternalService InternalService(){
            return new InternalService();
        }
    }

    @RequiredArgsConstructor
    static class CallService{

        private final InternalService internalService;

        public void external(){
            log.info("call external");
            internalService.internal();
            printTxInfo();
        }



        private void printTxInfo(){
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            log.info("[tx] active={}, readOnly={}", txActive, readOnly);
        }
    }

    static class InternalService
    {
        @Transactional
        public void internal(){
            log.info("call internal");
            printTxInfo();
        }

        private void printTxInfo(){
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            log.info("[tx] active={}, readOnly={}", txActive, readOnly);
        }
    }

}
