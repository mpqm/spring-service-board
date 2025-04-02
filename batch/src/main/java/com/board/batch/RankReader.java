package com.board.batch;

import com.board.common.post.dao.PostDao;
import com.board.common.post.dto.BatchPostReq;
import com.board.common.post.dto.BatchPostRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RankReader implements ItemReader<List<BatchPostRes>>, StepExecutionListener {

    private final PostDao postDao; // 데이터베이스에서 게시글 정보를 가져오는 DAO
    private int chunkSize; // 한 번에 읽어올 데이터의 크기
    private int currentOffset; // 현재 읽고 있는 데이터의 오프셋
    private boolean noMoreData; // 더 이상 읽을 데이터가 없는지 여부

    @Override
    public void beforeStep(StepExecution stepExecution) {
        chunkSize = 1000; // 청크 크기 초기화
        currentOffset = 0; // 오프셋 초기화
        noMoreData = false; // 데이터가 더 있는지 초기화
    }

    @Override
    public List<BatchPostRes> read() {

        // 더 이상 읽을 데이터가 없으면 null 반환 (배치 작업 종료)
        if (noMoreData) return null;

        // 현재 오프셋부터 chunkSize 만큼 데이터 읽기
        BatchPostReq batchPostReq = BatchPostReq.builder()
                .offset(currentOffset) // 현재 오프셋 설정
                .limit(chunkSize) // 청크 크기 설정
                .build();
        List<BatchPostRes> postChunk = postDao.batchPost(batchPostReq); // 데이터 읽기

        // 데이터가 없는 경우 noMoreData 설정 및 null 반환
        if (postChunk == null || postChunk.isEmpty()) {
            noMoreData = true; // 더 이상 데이터 없음
            return null;
        }

        // 다음 청크를 위해 오프셋 업데이트
        currentOffset += chunkSize; // 오프셋 증가

        // 가져온 데이터 크기가 chunkSize 보다 작으면 마지막 청크
        if (postChunk.size() < chunkSize) noMoreData = true; // 마지막 청크 여부 설정

        // 로그 출력, 청크 반환
        log.info("데이터 읽기 청크 크기: {}", postChunk.size()); // 읽은 데이터 크기 로그
        return postChunk; // 읽은 데이터 반환
    }

    @Override
    @Transactional
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("RankReader 완료");
        return ExitStatus.COMPLETED; // 배치 작업 완료 상태 반환

    }

}
