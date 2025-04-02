// 댓글 관련 전역 변수를 객체로 통합
const queryParam = {
    postIdx: new URLSearchParams(window.location.search).get('postIdx'),
    comment: {
        page: 1,
        orderIdx: null
    },
    reply: {
        page: 1,
        lastParentIdx: null
    }
};

$(document).ready(() => {
    initSummernote('#commentEditor', 200);
    $('#replyModal').on('show.bs.modal', () => initSummernote('#replyEditor', 200));
    $('#editCommentModal').on('show.bs.modal', () => initSummernote('#editCommentEditor', 200));
    $('#loadCommentsBtn').on('click', toggleCommentsList);
    $(document).on('click', '.comment-sort-item', handleCommentSortChange);
    $('#submitComment').on('click', () => handleCommentSubmit('comment'));
    $('#submitReply').on('click', () => handleCommentSubmit('reply'));
    $('#submitEditComment').on('click', handleCommentUpdate);
    $(document).on('comment:added comment:deleted', () => loadPostDetail(queryParam.postIdx));
});


// 댓글 목록 보기/접기 토글 함수
const toggleCommentsList = () => {
    if ($('#commentsList').children().length > 0 && $('#commentsList').is(':visible')) {
        $('#commentsList').hide();
        $('#commentPagination').hide();
        $('.comment-sort-container').hide();
        $('#commentEditor').hide();
        $('#submitComment').hide();
        $('#loadCommentsBtn').html('<i class="bi bi-chat-dots"></i> 댓글 보기');
    } else {
        queryParam.comment.orderIdx = $('.comment-sort-item').first().data('order-idx');
        $('#commentSortDropdown').text($('.comment-sort-item').first().text());
        loadCommentItems({isParent: true, itemIdx: queryParam.postIdx});
        $('#commentsList').show();
        $('#commentPagination').show();
        $('.comment-sort-container').show();
        $('#commentEditor').show();
        $('#submitComment').show();
        $('#loadCommentsBtn').html('<i class="bi bi-chat-dots-fill"></i> 댓글 접기');
        
        // 댓글 보기 버튼을 누를 때 댓글 입력창에 포커스
        $('#commentEditor').summernote('focus');
    }
};
        
// 정렬 옵션 변경 처리 함수
const handleCommentSortChange = (event) => {
    event.preventDefault();
    const orderIdx = $(event.currentTarget).data('order-idx');
    const orderName = $(event.currentTarget).text();
    saveScrollPosition('comments');
    $('#commentSortDropdown').text(orderName);
    loadCommentItems({isParent: true, itemIdx: queryParam.postIdx, page: 1, orderIdx}); // 정렬 변경 시 첫 페이지로 돌아감
};

// 현재 선택된 정렬 순서 가져오기
const getSelectedSortOrder = (parentCommentIdx) => {
    // 댓글의 경우
    if (!parentCommentIdx) return queryParam.comment.orderIdx;
    // 대댓글의 경우
    const activeSortItem = $(`.reply-sort-item[data-parent-idx="${parentCommentIdx}"]`).filter((index, element) => {
        return $(element).text() === $(`#replySortDropdown-${parentCommentIdx}`).text();
    });
    return activeSortItem.data('order-idx');
};

// 댓글, 대댓글 제출 처리 함수
const handleCommentSubmit = (type) => {
    let commentData;
    if(type == 'reply'){
        commentData = {
            postIdx: queryParam.postIdx,
            parentCommentIdx: $('#parentCommentIdx').val(),
            content: $('#replyEditor').summernote('code')
        }
    } else if(type == 'comment') {
        commentData = {
            postIdx: queryParam.postIdx,
            content: $('#commentEditor').summernote('code')
        }
    } else {
        return;
    }
    $.ajax({
        type: 'POST',
        url: `/comment`,
        data: JSON.stringify(commentData),
        contentType: 'application/json',
        success: (res) => {
            if (res.success) {
                if(type == 'reply'){
                    $('#replyModal').modal('hide');
                    $(`#replyContent-${commentData.parentCommentIdx}`).val('');
                    const orderIdx = getSelectedSortOrder(commentData.parentCommentIdx);
                    loadCommentItems({isParent: false, itemIdx: commentData.parentCommentIdx, page: 1, orderIdx});
                } else if(type == 'comment'){
                    $('#commentEditor').summernote('reset');
                    const orderIdx = queryParam.comment.orderIdx;
                    loadCommentItems({isParent: true, itemIdx: queryParam.postIdx, page: 1, orderIdx});
                    queryParam.comment.page = 1;
                } else {
                    return;
                }
                $(document).trigger('comment:added');
                setInstantAlert('success', res);
            } else {
                setInstantAlert('danger', res);
            }
        },
        error: (e) => setInstantAlert('danger', e.responseJSON)
    });
};

// 댓글/대댓글 수정 함수
const handleCommentUpdate = () => {
    const content = $('#editCommentEditor').summernote('code');
    const commentIdx = $('#editCommentIdx').val();
    $.ajax({
        type: 'PUT',
        url: `/comment?commentIdx=${commentIdx}`,
        contentType: 'application/json',
        data: JSON.stringify({
            content: $('#editCommentEditor').summernote('code')
        }),
        success: (res) => {
            if (res.success) {
                $('#editCommentModal').modal('hide');
                if ($(`#comment-${commentIdx}`).length > 0) {
                    loadCommentItems({ isParent: true, itemIdx: queryParam.postIdx, page: queryParam.comment.page });
                } else if (queryParam.reply.lastParentIdx) {
                    loadCommentItems({ isParent: false, itemIdx: queryParam.reply.lastParentIdx, page: queryParam.reply.page, orderIdx: getSelectedSortOrder(queryParam.reply.lastParentIdx) });
                }
                setInstantAlert('success', res);
            } else {
                setInstantAlert('danger', res);
            }
        },
        error: (e) => setInstantAlert('danger', e.responseJSON)
    });
};

// 댓글/대댓글 로드 통합 함수
const loadCommentItems = (options) => {
    const { isParent = true, itemIdx,  page = 1, orderIdx = null, size = 5 } = options;
    
    if (isParent) {
        queryParam.comment.page = page;
        if (orderIdx !== null && orderIdx !== undefined) queryParam.comment.orderIdx = orderIdx;
    } else {
        queryParam.reply.page = page;
        queryParam.reply.lastParentIdx = itemIdx;
        if (orderIdx === null || orderIdx === undefined) {
            if ($('.comment-sort-item').first().length) {
                $(`#replySortDropdown-${itemIdx}`).text($('.comment-sort-item').first().data('order-idx'));
                options.orderIdx = $('.comment-sort-item').first().data('order-idx');
            }
        }
    }
    let url = isParent 
        ? `/comment-list?postIdx=${itemIdx}&parentOnly=true&page=${page}&size=${size}` 
        : `/reply-list?parentCommentIdx=${itemIdx}&page=${page}&size=${size}`;
    const effectiveOrderIdx = isParent ? queryParam.comment.orderIdx : options.orderIdx;
    if (effectiveOrderIdx !== null && effectiveOrderIdx !== undefined) url += `&orderIdx=${effectiveOrderIdx}`;
    
    $.ajax({
        type: 'GET',
        url: url,
        success: (res) => {
            if (res.success) {
                if (isParent) {
                    displayCommentItems(res.result.data, $('#commentsList'), true);
                    createPagination({
                        containerId: 'commentPagination',
                        pageInfo: { currentPage: res.result.currentPage, totalPages: res.result.totalPages },
                        callback: (page, params) => {
                            saveScrollPosition('comments');
                            loadCommentItems({ isParent: isParent, itemIdx: params.itemIdx, page: page, orderIdx: params.orderIdx });
                        },
                        params: { itemIdx, effectiveOrderIdx },
                        size: 'md',
                        showPageInput: false
                    });
                    restoreScrollPosition('comments', null, true, 100);
                } else {
                    displayCommentItems(res.result.data, $(`#replies-${itemIdx}`).find('.replies-content'), false, itemIdx);
                    createPagination({
                        containerId: `replyPagination-${itemIdx}`,
                        pageInfo: { currentPage: res.result.currentPage, totalPages: res.result.totalPages },
                        callback: (page, params) => {
                            saveScrollPosition(`replies-${itemIdx}`);
                            loadCommentItems({ isParent: false, itemIdx: params.itemIdx, page: page, orderIdx: params.orderIdx });
                        },
                        params: { itemIdx, effectiveOrderIdx },
                        size:'sm',
                        showPageInput: false
                    });
                    // 현재 선택된 정렬 옵션 텍스트 업데이트
                    if (effectiveOrderIdx !== null && effectiveOrderIdx !== undefined) {
                        const selectedSortItem = $(`.reply-sort-item[data-parent-idx="${itemIdx}"][data-order-idx="${effectiveOrderIdx}"]`);
                        if ($(`.reply-sort-item[data-parent-idx="${itemIdx}"][data-order-idx="${effectiveOrderIdx}"]`).length) {
                            $(`#replySortDropdown-${itemIdx}`).text($(`.reply-sort-item[data-parent-idx="${itemIdx}"][data-order-idx="${effectiveOrderIdx}"]`).text());
                        }
                    }
                    $(`#replyCount-${itemIdx}`).text(res.result.totalElements);
                    restoreScrollPosition(`replies-${itemIdx}`, null, true, 100);
                }
            } else {
                setInstantAlert('danger', res);
            }
        },
        error: (e) => setInstantAlert('danger', e.responseJSON)
    });
};

// 댓글/대댓글 공통 표시 함수
const displayCommentItems = (items, container, isParent = true, parentCommentIdx = null) => {
    if (!items || !container) return;
    
    // 컨테이너가 제이쿼리 객체가 아닌 경우 변환
    const $container = container instanceof jQuery ? container : $(container);
    $container.empty();
    
    if (items.length > 0) {
        $.each(items, (index, item) => {
            const $itemDiv = $(`<div class="${isParent ? 'comment-item' : 'reply-item'} p-3 mb-2 border rounded"></div>`)
                        .attr('id', `${isParent ? 'comment' : 'reply'}-${item.idx}`)
                        .attr('data-id', item.idx);
            const $header = $('<div class="d-flex justify-content-between"></div>');
            const authorName = item.nickName || '익명';
            const $author = $(`<strong ${!isParent ? 'class="fs-6"' : ''}></strong>`).text(authorName);
            const $date = $('<small class="text-muted"></small>').text(formatDate(new Date(item.createdAt)));
            $header.append($author).append($date);
            const $content = $(`<div class="${isParent ? 'mt-2' : 'mt-1'} comment-content"></div>`).html(item.content);
            const $actions = $('<div class="mt-2 d-flex justify-content-end align-items-center"></div>');
            
            let $likeButton;
            item.isLiked 
            ? $likeButton = $(`<button class="btn btn-sm me-2 btn-dark like-btn" data-id="${item.idx}"><i class="bi bi-hand-thumbs-up-fill"></i> <span>${item.likeCount || 0}</span></button>`)
            : $likeButton = $(`<button class="btn btn-sm me-2 btn-outline-dark like-btn" data-id="${item.idx}"><i class="bi bi-hand-thumbs-up"></i> <span>${item.likeCount || 0}</span></button>`);
            
            $likeButton.on('click', () => reactToComment('like', item.idx));
            
            let $unlikeButton;
            item.isUnliked 
            ? $unlikeButton = $(`<button class="btn btn-sm me-2 btn-dark unlike-btn" data-id="${item.idx}"><i class="bi bi-hand-thumbs-down-fill"></i> <span>${item.unlikeCount || 0}</span></button>`)
            : $unlikeButton = $(`<button class="btn btn-sm me-2 btn-outline-dark unlike-btn" data-id="${item.idx}"><i class="bi bi-hand-thumbs-down"></i> <span>${item.unlikeCount || 0}</span></button>`);
            
            $unlikeButton.on('click', () => reactToComment('unlike', item.idx));
            if (isParent) {
                // 대댓글 작성 버튼
                const $replyButton = $('<button class="btn btn-sm btn-outline-dark me-2"><i class="bi bi-pencil"></i> 댓글 작성</button>');
                $replyButton.on('click', () => {
                    $('#parentCommentIdx').val(item.idx);
                    $('#replyEditor').summernote('code', '');
                    $('#replyModal').modal('show');
                });
                
                // 대댓글 보기 버튼 
                const replyCount = item.replyCount || 0;
                const $viewRepliesButton = $(`<button class="btn btn-sm btn-outline-dark me-2"><i class="bi bi-chat-dots"></i> 댓글 보기(${replyCount})</button>`);
                $viewRepliesButton.on('click', () => {
                    if ($(`#replies-${item.idx}`).is(':visible')) {
                        $(`#replies-${item.idx}`).hide();
                        $viewRepliesButton.html(`<i class="bi bi-chat-dots"></i> 댓글 보기(${replyCount})`);
                    } else {
                        $(`#replies-${item.idx}`).show();
                        loadCommentItems({isParent: false, itemIdx: item.idx});
                        $viewRepliesButton.html(`<i class="bi bi-chat-dots-fill"></i> 댓글 닫기(${replyCount})`);
                    }
                });
                $actions.append($replyButton).append($viewRepliesButton);
            }
            $actions.append($likeButton).append($unlikeButton);
            // 수정 버튼
            const $editBtn = $('<button class="btn btn-sm btn-outline-dark me-2">수정</button>');
            $editBtn.on('click', () => {
                $('#editCommentIdx').val(item.idx);
                $('#editCommentEditor').summernote('code', item.content);
                $('#editCommentModal').modal('show');
            });
            
            // 삭제 버튼
            const $deleteBtn = $('<button class="btn btn-sm btn-outline-dark">삭제</button>');
            $deleteBtn.on('click', () => deleteComment(item.idx, !isParent ? parentCommentIdx : null));
            $actions.append($editBtn).append($deleteBtn);
            $itemDiv.append($header).append($content).append($actions);
            
            if (isParent) {
                const $repliesContainer = $(`<div id="replies-${item.idx}" class="replies-container mt-3 ps-4" style="display: none;"></div>`);
                const $replySortDropdown = $(`
                    <div class="dropdown">
                        <button class="btn btn-sm btn-outline-dark dropdown-toggle" type="button" id="replySortDropdown-${item.idx}" data-bs-toggle="dropdown" aria-expanded="false">
                            정렬순서
                        </button>
                        <ul class="dropdown-menu" aria-labelledby="replySortDropdown-${item.idx}"></ul>
                    </div>
                `);
                
                const $dropdownMenu = $replySortDropdown.find('ul');
                $('.comment-sort-item').each(function() {
                    const orderIdx = $(this).data('order-idx');
                    const orderName = $(this).text();
                    if (orderName !== '조회수순' && orderName !== '댓글수순') {
                        const $item = $(`<li><a class="dropdown-item reply-sort-item" href="#" data-order-idx="${orderIdx}" data-parent-idx="${item.idx}">${orderName}</a></li>`);
                        $dropdownMenu.append($item);
                    }
                });
                
                // 정렬 항목 클릭 이벤트
                $replySortDropdown.find('.reply-sort-item').on('click', function() {
                    const parentIdx = $(this).data('parent-idx');
                    const orderIdx = $(this).data('order-idx');
                    const orderText = $(this).text();
                    saveScrollPosition(`replies-${parentIdx}`);
                    $(`#replySortDropdown-${parentIdx}`).text(orderText);
                    loadCommentItems({ isParent: false, itemIdx: parentIdx, page: 1, orderIdx: orderIdx });
                    queryParam.reply.page = 1;
                });
                $repliesContainer.append($('<div class="d-flex justify-content-end mb-2"></div>').append($replySortDropdown));
                $repliesContainer.append($('<div class="replies-content"></div>'));
                $repliesContainer.append($(`<div id="replyPagination-${item.idx}" class="mt-2"></div>`));   
                $itemDiv.append($repliesContainer);
            }
            $container.append($itemDiv);
        });
        if (isParent) $container.append($('<div id="commentPagination" class="mt-3"></div>'));
    } else {
        const message = isParent ? '댓글이 없습니다. 첫 댓글을 작성해보세요!' : '대댓글이 없습니다.';
        $container.append($(`<p class="text-center text-muted ${!isParent ? 'mb-2' : ''}">${message}</p>`));
    }
};

// 댓글/대댓글 삭제 함수
const deleteComment = (commentIdx, parentCommentIdx = null) => {
    if (confirm('정말 이 댓글을 삭제하시겠습니까?')) {
        $.ajax({
            type: 'DELETE',
            url: `/comment?commentIdx=${commentIdx}`,
            success: (res) => {
                if (res.success) {
                    if (parentCommentIdx) loadCommentItems({ isParent: false, itemIdx: parentCommentIdx, page: queryParam.reply.page, orderIdx: orderIdx });
                    else  loadCommentItems({ isParent: true, itemIdx: queryParam.postIdx, page: queryParam.comment.page });
                    $(document).trigger('comment:deleted');
                    setInstantAlert('success', res);
                } else {
                    setInstantAlert('danger', res);
                }
            },
            error: (e) => setInstantAlert('danger', e.responseJSON)
        });
    }
};

// 댓글/대댓글 좋아요/싫어요 함수
const reactToComment = (type, commentIdx) => {
    const url = type === 'like' ? '/react-like' : '/react-unlike';
    const isParentComment = $(`#comment-${commentIdx}`).length > 0;
    $.ajax({
        type: 'GET',
        url: `${url}?commentIdx=${commentIdx}`,
        success: (res) => {
            if (res.success) {
                if (isParentComment) loadCommentItems({ isParent: true, itemIdx: queryParam.postIdx, page: queryParam.comment.page });
                else if (queryParam.reply.lastParentIdx) loadCommentItems({ isParent: false, itemIdx: queryParam.reply.lastParentIdx, page: queryParam.reply.page, orderIdx: getSelectedSortOrder(queryParam.reply.lastParentIdx) });
                setInstantAlert('success', res);
            } else {
                setInstantAlert('danger', res);
            }
        },
        error: (e) => setInstantAlert('danger', e.responseJSON)
    });
};