package com.board.batch;

import com.board.common.rank.dao.RankDao;
import com.board.common.rank.dto.CreateRankReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RankWriter implements ItemWriter<List<CreateRankReq>>, StepExecutionListener {

    private final RankDao rankDao;
    private List<CreateRankReq> lastRankings;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        lastRankings = null;
    }

    @Override
    @Transactional
    public void write(Chunk<? extends List<CreateRankReq>> chunk) {
        // 청크가 null이거나 빈 경우 처리하지 않음
        if (chunk == null || chunk.isEmpty()) return;

        // 모든 랭킹 항목을 하나의 목록으로 합침
        List<CreateRankReq> allRankings = new ArrayList<>();
        
        // 청크 내 모든 항목 처리
        for (List<CreateRankReq> rankings : chunk.getItems()) {
            if (rankings == null || rankings.isEmpty()) {
                log.info("Found empty item in chunk");
                continue; // 빈 항목은 건너뜀
            }
            
            // 내부 CreateRankReq 객체들 중 null이 아닌 항목만 추가
            List<CreateRankReq> validRankings = rankings.stream()
                .filter(rank -> rank != null && rank.getPostIdx() != null)
                .collect(Collectors.toList());
            
            if (validRankings.isEmpty()) {
                log.info("All items in the list were invalid");
            } else {
                allRankings.addAll(validRankings);
                log.info("Added {} valid rankings from chunk", validRankings.size());
            }
        }
        
        // 합쳐진 랭킹이 없으면 무시
        if (allRankings.isEmpty()) {
            log.warn("No valid rankings found in this chunk");
            return;
        }
        
        // 중요: 마지막 랭킹 데이터로 저장 (깊은 복사)
        this.lastRankings = new ArrayList<>(allRankings);

        // 랭킹 유형 확인 (로깅 목적으로만 사용)
        boolean hasV = false, hasL = false, hasU = false, hasC = false;
        for (CreateRankReq rank : allRankings) {
            if (rank.getFlag() == null) continue;
            
            switch (rank.getFlag()) {
                case "V": hasV = true; break;
                case "L": hasL = true; break;
                case "U": hasU = true; break;
                case "C": hasC = true; break;
            }
        }
        
        // 유형별 존재 여부 로깅
        log.info("랭킹 유형 존재 여부: 조회수(V)={}, 좋아요(L)={}, 싫어요(U)={}, 댓글수(C)={}", 
                 hasV, hasL, hasU, hasC);
                 
        // 유효한 랭킹이 있으면 무조건 저장 시도 (모든 유형이 없어도 저장)
        try {
            log.info("유효한 랭킹 {}개 발견, 기존 랭킹 삭제 후 새로운 랭킹 저장을 시작합니다.", allRankings.size());
            rankDao.deleteRanks();
            int savedCount = 0;
            for (CreateRankReq rank : allRankings) {
                rankDao.createRank(rank);
                savedCount++;
            }
            log.info("write 메서드에서 {}개의 랭킹 저장 완료", savedCount);
        } catch (Exception e) {
            log.error("랭킹 저장 중 오류 발생: {}", e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public ExitStatus afterStep(StepExecution stepExecution) {
        if (lastRankings != null && !lastRankings.isEmpty()) {
            try {
                // write 메서드에서 이미 저장했지만, 혹시 실패했을 경우를 대비해 한번 더 시도
                log.info("afterStep에서 최종 저장 시도: {}개의 항목", lastRankings.size());
                
                // 기존 랭킹 삭제
                rankDao.deleteRanks();

                // 새 랭킹 데이터 저장
                int savedCount = 0;
                for (CreateRankReq rank : lastRankings) {
                    rankDao.createRank(rank);
                    savedCount++;
                }

                log.info("랭킹 정보 최종 저장 완료: {}개의 항목", savedCount);
                return ExitStatus.COMPLETED;
            } catch (Exception e) {
                log.error("랭킹 최종 저장 중 오류 발생: {}", e.getMessage(), e);
                return ExitStatus.FAILED;
            }
        } else {
            // 종료 상태에 문제 설명 추가
            log.error("어떤 랭킹도 배치 프로세스 중에 저장되지 않았습니다!");
            return ExitStatus.COMPLETED.addExitDescription("No rankings to save");
        }
    }
} 