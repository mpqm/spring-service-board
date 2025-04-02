package com.service.board.global.config;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.service.board.post.dto.BatchPostRes;
import com.service.board.rank.batch.RankReader;
import com.service.board.rank.batch.RankProcessor;
import com.service.board.rank.batch.RankWriter;
import com.service.board.rank.dto.CreateRankReq;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final RankReader postReader;
    private final RankProcessor rankProcessor;
    private final RankWriter rankWriter;
    
    @Value("${spring.batch.chunk-size:100}")
    private int chunkSize;

    // 랭킹 계산 Step 생성
    @Bean
    public Step rankingCalculationStep() {
        return new StepBuilder("rankingCalculationStep", jobRepository)
                .<List<BatchPostRes>, List<CreateRankReq>>chunk(chunkSize, transactionManager)
                .reader(postReader)
                .processor(rankProcessor)
                .writer(rankWriter)
                .listener(rankWriter)
                .build();
    }

    // 랭킹 계산 Job 생성
    @Bean
    public Job calculateRankingJob() {
        return new JobBuilder("calculateRankingJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(rankingCalculationStep())
                .build();
    }

}
