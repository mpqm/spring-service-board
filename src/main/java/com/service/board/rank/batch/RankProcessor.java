package com.service.board.rank.batch;

import com.service.board.post.dto.BatchPostRes;
import com.service.board.rank.dto.CreateRankReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Component
public class RankProcessor implements ItemProcessor<List<BatchPostRes>, List<CreateRankReq>>, StepExecutionListener {

    private int rankLimit; // 랭킹 제한 개수
    private int processedChunks; // 처리된 청크 개수
    private int chunkSize; // 청크 크기
    private boolean rankingsGenerated; // 랭킹 생성 여부

    // 정렬된 게시물 저장을 위한 TreeSet (각 기준별 내림차순 정렬)
    private final SortedSet<BatchPostRes> viewPosts;
    private final SortedSet<BatchPostRes> likePosts;
    private final SortedSet<BatchPostRes> unlikePosts;
    private final SortedSet<BatchPostRes> commentPosts;

    public RankProcessor() {
        // 조회수 기준 정렬 (조회수 내림차순, 동일하면 ID 오름차순)
        this.viewPosts = new TreeSet<>((p1, p2) -> {
            int result = p2.getViewCount().compareTo(p1.getViewCount());
            return result != 0 ? result : p1.getIdx().compareTo(p2.getIdx());
        });

        // 좋아요 수 기준 정렬
        this.likePosts = new TreeSet<>((p1, p2) -> {
            int result = p2.getLikeCount().compareTo(p1.getLikeCount());
            return result != 0 ? result : p1.getIdx().compareTo(p2.getIdx());
        });

        // 싫어요 수 기준 정렬
        this.unlikePosts = new TreeSet<>((p1, p2) -> {
            int result = p2.getUnlikeCount().compareTo(p1.getUnlikeCount());
            return result != 0 ? result : p1.getIdx().compareTo(p2.getIdx());
        });

        // 댓글 수 기준 정렬
        this.commentPosts = new TreeSet<>((p1, p2) -> {
            int result = p2.getCommentCount().compareTo(p1.getCommentCount());
            return result != 0 ? result : p1.getIdx().compareTo(p2.getIdx());
        });
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        rankLimit = 10; // 랭킹 제한 10개
        chunkSize = 1000; // 한 번에 처리할 게시물 수
        rankingsGenerated = false; // 랭킹이 생성되지 않음
        processedChunks = 0; // 처리된 청크 개수 초기화

        // 기존 데이터 초기화
        viewPosts.clear();
        likePosts.clear();
        unlikePosts.clear();
        commentPosts.clear();
    }

    @Override
    public List<CreateRankReq> process(List<BatchPostRes> batchPostResList) {
        if (batchPostResList == null) {
            // 게시물이 없고, 아직 랭킹을 생성하지 않았다면 랭킹 생성
            if (!rankingsGenerated && (!viewPosts.isEmpty() || !likePosts.isEmpty() || !unlikePosts.isEmpty() || !commentPosts.isEmpty())) {
                rankingsGenerated = true;
                List<CreateRankReq> result = new ArrayList<>();
                generateRankings(result, "V", viewPosts);
                generateRankings(result, "L", likePosts);
                generateRankings(result, "U", unlikePosts);
                generateRankings(result, "C", commentPosts);
                return result;
            }
            return null;
        }

        if (batchPostResList.isEmpty()) return Collections.emptyList();

        processedChunks++; // 처리한 청크 수 증가
        log.info("청크 처리: {} (size: {})", processedChunks, batchPostResList.size());

        // 게시물을 기준에 맞게 TreeSet에 추가
        for (BatchPostRes post : batchPostResList) {
            if (post.getViewCount() != null && post.getViewCount() > 0) {
                viewPosts.add(post);
                if (viewPosts.size() > rankLimit) viewPosts.remove(viewPosts.last());
            }
            if (post.getLikeCount() != null && post.getLikeCount() > 0) {
                likePosts.add(post);
                if (likePosts.size() > rankLimit) likePosts.remove(likePosts.last());
            }
            if (post.getUnlikeCount() != null && post.getUnlikeCount() > 0) {
                unlikePosts.add(post);
                if (unlikePosts.size() > rankLimit) unlikePosts.remove(unlikePosts.last());
            }
            if (post.getCommentCount() != null && post.getCommentCount() > 0) {
                commentPosts.add(post);
                if (commentPosts.size() > rankLimit) commentPosts.remove(commentPosts.last());
            }
        }

        if (rankingsGenerated) return Collections.emptyList();

        if ((batchPostResList.size() < chunkSize) || (processedChunks % 5 == 0)) {
            rankingsGenerated = true;
            List<CreateRankReq> rankings = new ArrayList<>();
            generateRankings(rankings, "V", viewPosts);
            generateRankings(rankings, "L", likePosts);
            generateRankings(rankings, "U", unlikePosts);
            generateRankings(rankings, "C", commentPosts);
            return rankings;
        }
        return Collections.emptyList();
    }

    // 랭킹 데이터를 생성하여 리스트에 추가하는 메서드
    private void generateRankings(List<CreateRankReq> resultList, String flag, SortedSet<BatchPostRes> sortedPosts) {
        if (sortedPosts.isEmpty()) return;
        List<BatchPostRes> topPosts = new ArrayList<>(sortedPosts);
        for (int i = 0; i < topPosts.size(); i++) {
            BatchPostRes post = topPosts.get(i);
            int rank = i + 1;
            // 주어진 기준(flag)에 따라 CreateRankReq 객체 생성
            CreateRankReq rankItem = CreateRankReq.builder()
                    .ranking(rank)
                    .flag(flag)
                    .title(post.getTitle())
                    .postIdx(post.getIdx())
                    .viewCount(post.getViewCount() != null ? post.getViewCount().intValue() : 0)
                    .likeCount(post.getLikeCount() != null ? post.getLikeCount().intValue() : 0)
                    .unlikeCount(post.getUnlikeCount() != null ? post.getUnlikeCount().intValue() : 0)
                    .commentCount(post.getCommentCount() != null ? post.getCommentCount().intValue() : 0)
                    .build();
            resultList.add(rankItem);
        }
    }

    @Override
    @Transactional
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED; // 배치 완료 상태 반환
    }
}