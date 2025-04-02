$(document).ready(() => {
    loadActivityPosts(1);
    $('#activityTab button').on('click', loadActivity);
});

// 활동 탭 클릭 시 해당 탭 로드
const loadActivity = (event) => {
    const tabId = $(event.target).attr('id');
    if (tabId === 'posts-tab') loadActivityPosts(1);
    else if (tabId === 'comments-tab') loadActivityComments(1);
    else if (tabId === 'likes-tab') loadActivityLikes(1);
    else if (tabId === 'unlikes-tab') loadActivityUnlikes(1);
}

// 내가 작성한 게시물 로드
const loadActivityPosts = (page) => {
    $.ajax({
        url: '/activity-posts',
        type: 'GET',
        success: (res) => {
            if (res.success) displayPosts(res.result, page);
            else setInstantAlert('danger', res);
        },
        error: (e) => setInstantAlert('danger', e.responseJSON)
    });
};

// 게시물 목록 표시
const displayPosts = (posts, currentPage) => {
    const tableBody = $('#postsTableBody').empty();
    $('#postsPagination').empty();
    if (!posts || posts.length === 0) {
        $('#noPostsMessage').removeClass('d-none');
        return;
    }
    $('#noPostsMessage').addClass('d-none');
    const totalPages = Math.ceil(posts.length / 10);
    const startIdx = (currentPage - 1) * 10;
    const endIdx = Math.min(startIdx + 10, posts.length);
    posts.slice(startIdx, endIdx).forEach((post) => {
        const row = `
            <tr class="text-center align-middle cursor-pointer" data-post-idx="${post.idx}">
                <td class="small">${post.postIdx}</td>
                <td><span class="badge bg-dark rounded-pill">${post.categoryName}</span></td>
                <td><span class="badge bg-dark rounded-pill">${post.rangeName}</span></td>
                <td class="text-start">
                    <a href="/post-detail?postIdx=${post.postIdx}" class="text-decoration-none text-dark">${post.title || '제목 없음'}</a>
                </td>
                <td class="small">${post.viewCount}</td>
                <td class="small">${post.likeCount || 0}</td>
                <td class="small">${post.unlikeCount || 0}</td>
                <td class="small">${post.commentCount || 0}</td>
                <td class="text-muted small">${formatDate(new Date(post.createdAt))}</td>
            </tr>
        `;
        tableBody.append(row);
    });    
    createPagination({ containerId: 'postsPagination', pageInfo: {currentPage: currentPage, totalPages: totalPages}, callback: loadActivityPosts });
};

// 내가 작성한 댓글 로드
const loadActivityComments = (page) => {
    $.ajax({
        url: '/activity-comments',
        type: 'GET',
        success: (res) => {
            if (res.success) displayComments(res.result, page);
            else setInstantAlert('danger', res);
        },
        error: (e) => setInstantAlert('danger', e.responseJSON)
    });
};

// 댓글 목록 표시
const displayComments = (comments, currentPage) => {
    const tableBody = $('#commentsTableBody').empty();
    $('#commentsPagination').empty();
    if (!comments || comments.length === 0) {
        $('#noCommentsMessage').removeClass('d-none');
        return;
    }
    $('#noCommentsMessage').addClass('d-none');
    const totalPages = Math.ceil(comments.length / 10);
    const startIdx = (currentPage - 1) * 10;
    const endIdx = Math.min(startIdx + 10, comments.length);
    comments.slice(startIdx, endIdx).forEach((comment, index) => {
        const truncatedTitle = comment.postTitle && comment.postTitle.length > 15 
            ? comment.postTitle.substring(0, 15) + '...' 
            : comment.postTitle || '(제목 없음)';
            
        const truncatedContent = comment.content && comment.content.length > 30
            ? comment.content.substring(0, 30) + '...'
            : comment.content || '(내용 없음)';
            
        const row = `
            <tr class="text-center align-middle cursor-pointer" data-post-idx="${comment.postIdx}">
                <td class="small">${comment.commentIdx}</td>
                <td>
                    <span class="text-center text-truncate-custom">
                        <a href="/post-detail?postIdx=${comment.postIdx}" class="fw-bold link-no-style">${truncatedTitle}</a>
                    </span>
                </td>
                <td class="text-start">
                    <a href="/post-detail?postIdx=${comment.postIdx}" class="link-no-style text-dark">
                        <span class="text-truncate-custom">${truncatedContent}</span>
                    </a>
                </td>
                <td class="small">${comment.likeCount || 0}</td>
                <td class="small">${comment.unlikeCount || 0}</td>
                <td class="text-muted small">${formatDate(new Date(comment.createdAt))}</td>
            </tr>
        `;
        tableBody.append(row);
    });
    createPagination({ containerId: 'commentsPagination', pageInfo: {currentPage: currentPage, totalPages: totalPages}, callback: loadActivityComments });
};

// 내가 좋아요한 항목 로드
const loadActivityLikes = (page) => {
    $.ajax({
        url: '/activity-likes',
        type: 'GET',
        success: (res) => {
            if (res.success) displayReactions(res.result, page, 'likes');
            else setInstantAlert("danger", res);
        },
        error: (e) => setInstantAlert('danger', e.responseJSON)
    });
};

// 내가 싫어요한 항목 로드
const loadActivityUnlikes = (page) => {
    $.ajax({
        url: '/activity-unlikes',
        type: 'GET',
        success: (res) => {
            if (res.success) displayReactions(res.result, page, 'unlikes');
            else setInstantAlert("danger", res);
        },
        error: (e) => setInstantAlert('danger', e.responseJSON)
    });
};

// 반응(좋아요/싫어요) 목록 표시
const displayReactions = (reactions, currentPage, type) => {
    const tableBody = $(`#${type}TableBody`);
    const paginationContainer = $(`#${type}Pagination`);
    const noReactionsMessage = $(`#no${type.charAt(0).toUpperCase() + type.slice(1)}Message`);
    
    tableBody.empty();
    paginationContainer.empty();
    
    if (!reactions || reactions.length === 0) {
        noReactionsMessage.removeClass('d-none');
        return;
    }
    
    noReactionsMessage.addClass('d-none');
    
    // 페이지당 항목 수
    const itemsPerPage = 10;
    const totalPages = Math.ceil(reactions.length / itemsPerPage);
    
    // 현재 페이지 항목 계산
    const startIdx = (currentPage - 1) * itemsPerPage;
    const endIdx = Math.min(startIdx + itemsPerPage, reactions.length);
    const currentPageItems = reactions.slice(startIdx, endIdx);
    
    // 테이블 행 생성
    currentPageItems.forEach((reaction, index) => {
        const displayIndex = startIdx + index + 1;
        let content, link, category;
        
        // 게시글 제목 또는 댓글 내용 15자 이상이면 자르기
        let displayContent;
        
        if (reaction.commentIdx) {
            // 댓글 반응인 경우
            content = reaction.commentContent || "내용 없음";
            displayContent = content && content.length > 15 ? content.substring(0, 15) + '...' : content;
            // postIdx가 null인 경우 처리
            if (reaction.postIdx) {
                link = `/post-detail?postIdx=${reaction.postIdx}`;
            } else {
                console.warn("댓글에 연결된 postIdx가 없습니다:", reaction);
                link = "#";
            }
            category = "댓글";
        } else {
            content = reaction.postTitle || "제목 없음";
            displayContent = content && content.length > 15 ? content.substring(0, 15) + '...' : content;
            link = reaction.postIdx ? `/post-detail?postIdx=${reaction.postIdx}` : "#";
            category = "게시글";
        }
        
        // 날짜 처리
        let createdAt;
        if (reaction.commentIdx && reaction.commentCreatedAt) createdAt = formatDate(new Date(reaction.commentCreatedAt));
        else if (reaction.postCreatedAt) createdAt = formatDate(new Date(reaction.postCreatedAt));
        else createdAt = "-";
        
        const row = `
            <tr>
                <td>${reaction.reactIdx}</td>
                <td class="text-center"><span class="badge bg-dark rounded-pill">${category}</span></td>
                <td class="text-start">
                    <a href="${link}" class="link-no-style text-dark">${displayContent}</a>
                </td>
                <td class="text-muted small text-center">${createdAt}</td>
                <td class="text-center items-center">
                    <button class="btn btn-sm btn-outline-dark cancel-reaction-btn py-0 px-2"
                            data-type="${reaction.reactType}" 
                            data-idx="${reaction.reactIdx}"
                            data-post-idx="${reaction.postIdx || ''}"
                            data-comment-idx="${reaction.commentIdx || ''}">
                        취소
                    </button>
                </td>
            </tr>
        `;
        tableBody.append(row);
    });
    
    // 취소 버튼 이벤트 등록
    $(`.cancel-reaction-btn`).on('click', function(e) {
        e.stopPropagation();
        const type = $(this).data('type');
        const idx = $(this).data('idx');
        cancelReaction(type, idx);
    });
    
    // 페이지네이션 생성
    createPagination({ containerId: `${type}Pagination`, pageInfo: {currentPage: currentPage, totalPages: totalPages}, callback: type === 'likes' ? loadActivityLikes : loadActivityUnlikes });
};

// 좋아요/싫어요 취소
const cancelReaction = (type, reactIdx) => {
    // 버튼에서 게시글과 댓글 인덱스 정보 가져오기
    const button = $(`.cancel-reaction-btn[data-idx="${reactIdx}"]`);
    const postIdx = button.data('post-idx');
    const commentIdx = button.data('comment-idx');
    
    // 요청할 데이터 준비
    const data = { reactIdx: reactIdx };
    
    // 게시글이나 댓글 인덱스가 있으면 추가
    if (postIdx) data.postIdx = postIdx;
    if (commentIdx) data.commentIdx = commentIdx;
    
    // API 엔드포인트 설정 (API 경로를 정확히 확인하세요)
    const endpoint = type === 'like' ? '/react-like' : '/react-unlike';
    
    $.ajax({
        url: endpoint,
        type: 'GET',
        data: data,
        success: (res) => {
            if (res.success) {
                // 성공 후 해당 탭 리로드
                if (type === 'like') {
                    loadActivityLikes(1);
                    setInstantAlert('success', "좋아요가 취소되었습니다.");
                } else {
                    loadActivityUnlikes(1);
                    setInstantAlert('success', "싫어요가 취소되었습니다.");
                }
            } else {
                setInstantAlert('danger', res);
            }
        },
        error: (e) => setInstantAlert('danger', e.responseJSON)
    });
};
