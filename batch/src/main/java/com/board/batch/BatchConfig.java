package com.board.batch;

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

import com.board.common.post.dto.BatchPostRes;
import com.board.common.rank.dto.CreateRankReq;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final RankReader rankReader;
    private final RankProcessor rankProcessor;
    private final RankWriter rankWriter;
    
    @Value("${spring.batch.chunk-size:100}")
    private int chunkSize;

    // 랭킹 계산 Step 생성
    @Bean
    public Step rankingCalculationStep() {
        return new StepBuilder("rankingCalculationStep", jobRepository)
                .<List<BatchPostRes>, List<CreateRankReq>>chunk(chunkSize, transactionManager)
                .reader(rankReader)
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
