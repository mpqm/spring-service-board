// 페이지 로드 시 랭킹 데이터 로드
$(document).ready(() => {
    loadRankData();
    // 더보기 버튼 클릭 이벤트 등록
    $('.rank-more-btn').on('click', toggleRankView);
    // 랭킹 아이템 클릭 시 해당 게시글로 이동
    $(document).on('click', '.rank-item', movePostDetail);
});

// 랭킹 아이템 클릭시 해당 게시글로 이동
const movePostDetail = () => {
    const postIdx = $(this).data('post-idx');
    if (postIdx) location.href = `/post-detail?postIdx=${postIdx}`
};

// 랭킹 데이터 로드
const loadRankData = () => {
    $.ajax({
        url: '/rank-list',
        type: 'GET',
        dataType: 'json',
        success: (res) => {
            if (res.success) {
                renderRankData('view', res.result.viewRanks);
                renderRankData('like', res.result.likeRanks);
                renderRankData('unlike', res.result.unlikeRanks);
                renderRankData('comment', res.result.commentRanks);
            } else {
                setInstantAlert('danger', res);
            }
        },
        error: (e) => setInstantAlert('danger', e.responseJSON)
    });
};

// 랭킹 데이터 표시 함수
const renderRankData = (type, data) => {
    if (!data || !Array.isArray(data)) return;
    
    const $listContainer = $(`#${type}RankList`);
    $listContainer.empty();
    
    // 처음에는 상위 5개만 표시
    const initialCount = 5;
    const displayData = data.slice(0, initialCount);
    
    // 각 랭킹 항목 생성
    displayData.forEach((item, index) => {
        const $rankItem = createRankItem(item, index + 1);
        $listContainer.append($rankItem);
    });
    
    // 추가 데이터가 있는 경우 hidden 클래스로 저장
    if (data.length > initialCount) {
        const additionalData = data.slice(initialCount);
        additionalData.forEach((item, index) => {
            const $rankItem = createRankItem(item, index + initialCount + 1);
            $rankItem.addClass('d-none additional-rank');
            $listContainer.append($rankItem);
        });
    } else {
        $(`.rank-more-btn[data-rank-type="${type}"]`).attr('disabled', true);
    }
};

// 랭킹 아이템 생성 함수
const createRankItem = (item, rank) => {

    return $(`
        <li class="list-group-item p-2 rank-item" data-post-idx="${item.postIdx}">
            <div class="d-flex align-items-center">
                <span class="badge bg-dark me-2">${rank}</span>
                <div class="rank-content">
                    <span class="rank-title">${truncateText(item.title, 80)}</span>
                    <div class="rank-info text-muted small">
                        <span class="badge bg-light text-dark">${item.categoryName || '분류없음'}</span>
                        <span class="ms-1"><i class="bi bi-person-fill"></i> ${item.nickName || '익명'}</span>
                        <span class="ms-1">
                            ${getCountIcon(item)} ${getCountValue(item)}
                        </span>
                    </div>
                </div>
            </div>
        </li>
    `);
};

// 텍스트 길이 제한 함수
const truncateText = (text, maxLength) => {
    if (!text) return '';
    return text.length > maxLength ? text.substring(0, maxLength) + '...' : text;
};

// 랭킹 타입에 따른 아이콘 설정
const getCountIcon = (item) => {
    if (item.flag == ('V')) return '<i class="bi bi-eye-fill"></i>';
    if (item.flag == ('L')) return '<i class="bi bi-hand-thumbs-up-fill"></i>';
    if (item.flag == ('U')) return '<i class="bi bi-hand-thumbs-down-fill"></i>';
    if (item.flag == ('C')) return '<i class="bi bi-chat-left-dots-fill"></i>';
    return '';
};

// 랭킹 타입에 따른 카운트 값 설정
const getCountValue = (item) => {
    if (item.flag == ('V')) return item.viewCount;
    if (item.flag == ('L')) return item.likeCount;
    if (item.flag == ('U')) return item.unlikeCount;
    if (item.flag == ('C')) return item.commentCount;
    return 0;
};

// 더보기/접기 토글 함수
const toggleRankView = (event) => {
    const $btn = $(event.currentTarget);
    const type = $btn.data('rank-type');
    const $container = $(`#${type}RankList`);
    const $additionalItems = $container.find('.additional-rank');
    const $rankCount = $btn.closest('.card-header').find('.rank-count');
    
    if ($additionalItems.hasClass('d-none')) {
        $additionalItems.removeClass('d-none');
        $btn.text('접기');
        $rankCount.text('10');
    } else {
        $additionalItems.addClass('d-none');
        $btn.text('더보기');
        $rankCount.text('5');
    }
};

