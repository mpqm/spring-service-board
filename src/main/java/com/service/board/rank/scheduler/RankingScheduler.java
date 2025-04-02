package com.service.board.rank.scheduler;

import java.time.LocalDateTime;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.service.board.rank.batch.RankProcessor;
import com.service.board.rank.batch.RankReader;
import com.service.board.rank.batch.RankWriter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RankingScheduler {

    private final JobLauncher jobLauncher;
    private final Job calculateRankingJob;
    private final RankReader postReader;
    private final RankProcessor rankProcessor;
    private final RankWriter rankWriter;

    // 오전 6시에 실행
    @Scheduled(cron = "0 0 6 * * *")
    public void executeRankingJob() {
        log.info("랭킹 집계 작업 시작: {}", LocalDateTime.now());
        
        try {
            // 배치 작업을 위한 고유 파라미터 생성 (동일 파라미터로 중복 실행 방지)
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            // 배치 작업 실행
            log.info("배치 작업 실행 시작 (Job: {})", calculateRankingJob.getName());
            JobExecution jobExecution = jobLauncher.run(calculateRankingJob, jobParameters);
            
            // 작업 실행 결과 확인
            log.info("랭킹 집계 작업 완료: {} (상태: {})", 
                    LocalDateTime.now(), jobExecution.getStatus());
            
            // 실행 결과에 따른 상세 로깅
            if (jobExecution.getExitStatus().getExitCode().equals("COMPLETED")) {
                log.info("배치 작업이 성공적으로 완료되었습니다.");
                
                // 추가 설명이 있는 경우 로깅
                String exitDescription = jobExecution.getExitStatus().getExitDescription();
                if (exitDescription != null && !exitDescription.isEmpty()) {
                    log.info("작업 종료 설명: {}", exitDescription);
                }
            } else {
                log.warn("배치 작업이 비정상적으로 종료되었습니다. 상태: {}, 설명: {}", 
                        jobExecution.getStatus(), 
                        jobExecution.getExitStatus().getExitDescription());
            }
        } catch (JobExecutionAlreadyRunningException e) {
            log.error("랭킹 집계 작업이 이미 실행 중입니다.", e);
        } catch (JobRestartException e) {
            log.error("랭킹 집계 작업 재시작 에러", e);
        } catch (JobInstanceAlreadyCompleteException e) {
            log.error("이미 완료된 랭킹 집계 작업입니다.", e);
        } catch (JobParametersInvalidException e) {
            log.error("랭킹 집계 작업 파라미터 에러", e);
        } catch (Exception e) {
            log.error("랭킹 집계 작업 중 예상치 못한 오류 발생", e);
        }
    }

} 