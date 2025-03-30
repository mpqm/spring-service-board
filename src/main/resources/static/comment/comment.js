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
    setInitialCommentSort();
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

// 초기 댓글 정렬 설정
const setInitialCommentSort = () => {
    const firstSortItem = $('.comment-sort-item').first();
    if (firstSortItem.length) {
        const firstOrderName = firstSortItem.text();
        $('#commentSortDropdown').text(firstOrderName);
    }
}

// 댓글 목록 토글 함수
const toggleCommentsList = () => {
    // 현재 버튼 상태 확인 (보기/접기)
    if ($('#commentsList').children().length > 0 && $('#commentsList').is(':visible')) {
        $('#commentsList').hide();
        $('#commentPagination').hide();
        $('.comment-sort-container').hide();
        $('#commentEditor').hide();
        $('#submitComment').hide();
        $('#loadCommentsBtn').html('<i class="bi bi-chat-dots"></i> 댓글 보기');
    } else {
        if (!queryParam.comment.orderIdx && $('.comment-sort-item').first().length) {
            queryParam.comment.orderIdx = $('.comment-sort-item').first().data('order-idx');
            $('#commentSortDropdown').text($('.comment-sort-item').first().text());
        }
        loadCommentItems({isParent: true, itemIdx: queryParam.postIdx});
        $('#commentsList').show();
        $('#commentPagination').show();
        $('.comment-sort-container').show();
        $('#commentEditor').show();
        $('#submitComment').show();
        $('#loadCommentsBtn').html('<i class="bi bi-chat-dots-fill"></i> 댓글 접기');
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
    if (!parentCommentIdx) {
        return queryParam.comment.orderIdx;
    }
    
    // 대댓글의 경우
    const activeSortItem = $(`.reply-sort-item[data-parent-idx="${parentCommentIdx}"]`).filter((index, element) => {
        return $(element).text() === $(`#replySortDropdown-${parentCommentIdx}`).text();
    });
    return activeSortItem.data('order-idx');
};

// 대댓글 정렬 드롭다운 생성 함수
const createReplySortDropdown = (parentCommentIdx) => {
    // 코드 테이블에서 정렬 옵션 가져오기
    const $dropdown = $(`
        <div class="dropdown">
            <button class="btn btn-sm btn-outline-dark dropdown-toggle" type="button" id="replySortDropdown-${parentCommentIdx}" data-bs-toggle="dropdown" aria-expanded="false">
                정렬순서
            </button>
            <ul class="dropdown-menu" aria-labelledby="replySortDropdown-${parentCommentIdx}"></ul>
        </div>
    `);
    
    const $dropdownMenu = $dropdown.find('ul');
    
    // 댓글 정렬 옵션과 동일한 항목 추가 (조회수순, 댓글수순 제외)
    $('.comment-sort-item').each(function() {
        const orderIdx = $(this).data('order-idx');
        const orderName = $(this).text();
        
        // 조회수순과 댓글수순 제외
        if (orderName !== '조회수순' && orderName !== '댓글수순') {
            const $item = $(`<li><a class="dropdown-item reply-sort-item" href="#" data-order-idx="${orderIdx}" data-parent-idx="${parentCommentIdx}">${orderName}</a></li>`);
            $dropdownMenu.append($item);
        }
    });
    
    // 정렬 항목 클릭 이벤트
    $dropdown.find('.reply-sort-item').on('click', function() {
        const parentIdx = $(this).data('parent-idx');
        const orderIdx = $(this).data('order-idx');
        const orderText = $(this).text();
        
        // 현재 스크롤 위치 저장 (대댓글 컨테이너의 ID로 키를 구성)
        saveScrollPosition(`replies-${parentIdx}`);
        
        // 드롭다운 버튼 텍스트 변경
        $(`#replySortDropdown-${parentIdx}`).text(orderText);
        
        // 대댓글 다시 불러오기
        loadCommentItems({
            isParent: false,
            itemIdx: parentIdx,
            page: 1,
            orderIdx: orderIdx
        });
        
        // 대댓글 페이지 초기화
        queryParam.reply.page = 1;
    });
    
    return $dropdown;
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
                
                // 부모 댓글인지 확인하여 해당하는 목록 새로고침
                const isParentComment = $(`#comment-${commentIdx}`).length > 0;
                
                if (isParentComment) {
                    loadCommentItems({
                        isParent: true,
                        itemIdx: queryParam.postIdx,
                        page: queryParam.comment.page
                    });
                } else if (queryParam.reply.lastParentIdx) {
                    // 현재 선택된 orderIdx 가져오기
                    const orderIdx = getSelectedSortOrder(queryParam.reply.lastParentIdx);
                    loadCommentItems({
                        isParent: false,
                        itemIdx: queryParam.reply.lastParentIdx,
                        page: queryParam.reply.page,
                        orderIdx: orderIdx
                    });
                }
                
                showAlertMaintence('success', res);
            } else {
                showAlertMaintence('danger', res);
            }
        },
        error: (e) => {
            showAlertMaintence('danger', e.responseJSON);
        }
    });
};

// 댓글/대댓글 로드 통합 함수
const loadCommentItems = (options) => {
    const {
        isParent = true,
        itemIdx, // postIdx 또는 parentCommentIdx
        page = 1,
        orderIdx = null,
        size = 5
    } = options;
    
    // 상태 업데이트
    if (isParent) {
        queryParam.comment.page = page;
        if (orderIdx !== null && orderIdx !== undefined) {
            queryParam.comment.orderIdx = orderIdx;
        }
    } else {
        queryParam.reply.page = page;
        queryParam.reply.lastParentIdx = itemIdx;
        
        // 대댓글 첫 로드 시 정렬 옵션 설정
        if (orderIdx === null || orderIdx === undefined) {
            // 댓글 정렬과 동일한 첫번째 옵션 찾기
            const firstCommentSortItem = $('.comment-sort-item').first();
            if (firstCommentSortItem.length) {
                const firstOrderIdx = firstCommentSortItem.data('order-idx');
                const firstSortText = firstCommentSortItem.text();
                $(`#replySortDropdown-${itemIdx}`).text(firstSortText);
                options.orderIdx = firstOrderIdx;
            }
        }
    }
    
    // URL 구성
    let url = isParent 
        ? `/comment-list?postIdx=${itemIdx}&parentOnly=true&page=${page}&size=${size}` 
        : `/reply-list?parentCommentIdx=${itemIdx}&page=${page}&size=${size}`;
    
    // 정렬 옵션 추가
    const effectiveOrderIdx = isParent ? queryParam.comment.orderIdx : options.orderIdx;
    if (effectiveOrderIdx !== null && effectiveOrderIdx !== undefined) url += `&orderIdx=${effectiveOrderIdx}`;
    
    $.ajax({
        type: 'GET',
        url: url,
        success: (res) => {
            if (res.success) {
                // 표시 관련 함수 호출
                if (isParent) {
                    displayCommentItems(res.result.data, $('#commentsList'), true);
                    createPagination({
                        containerId: 'commentPagination',
                        pageInfo: {
                            currentPage: res.result.currentPage,
                            totalPages: res.result.totalPages
                        },
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
                        pageInfo: {
                            currentPage: res.result.currentPage,
                            totalPages: res.result.totalPages
                        },
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
                        if (selectedSortItem.length) {
                            const selectedSortText = selectedSortItem.text();
                            $(`#replySortDropdown-${itemIdx}`).text(selectedSortText);
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
            // 댓글 항목 컨테이너
            const $itemDiv = $(`<div class="${isParent ? 'comment-item' : 'reply-item'} p-3 mb-2 border rounded"></div>`);
            $itemDiv.attr('id', `${isParent ? 'comment' : 'reply'}-${item.idx}`);
            $itemDiv.attr('data-id', item.idx);
            
            // 헤더 (작성자, 날짜)
            const $header = $('<div class="d-flex justify-content-between"></div>');
            const authorName = item.nickName || '익명';
            const $author = $(`<strong ${!isParent ? 'class="fs-6"' : ''}></strong>`).text(authorName);
            const $date = $('<small class="text-muted"></small>').text(formatDate(new Date(item.createdAt)));
            
            $header.append($author).append($date);
            
            // 내용
            const $content = $(`<div class="${isParent ? 'mt-2' : 'mt-1'} comment-content"></div>`).html(item.content);
            
            // 댓글 수정/삭제 버튼 및 반응 버튼을 한 행에 배치
            const $actions = $('<div class="mt-2 d-flex justify-content-end align-items-center"></div>');
            
            // 좋아요 버튼
            let $likeButton;
            item.isLiked 
            ? $likeButton = $(`<button class="btn btn-sm me-2 btn-dark like-btn" data-id="${item.idx}"><i class="bi bi-hand-thumbs-up-fill"></i> <span>${item.likeCount || 0}</span></button>`)
            : $likeButton = $(`<button class="btn btn-sm me-2 btn-outline-dark like-btn" data-id="${item.idx}"><i class="bi bi-hand-thumbs-up"></i> <span>${item.likeCount || 0}</span></button>`);
            
            $likeButton.on('click', () => {
                reactToComment('like', item.idx);
            });
            
            // 싫어요 버튼
            let $unlikeButton;
            item.isUnliked 
            ? $unlikeButton = $(`<button class="btn btn-sm me-2 btn-dark unlike-btn" data-id="${item.idx}"><i class="bi bi-hand-thumbs-down-fill"></i> <span>${item.unlikeCount || 0}</span></button>`)
            : $unlikeButton = $(`<button class="btn btn-sm me-2 btn-outline-dark unlike-btn" data-id="${item.idx}"><i class="bi bi-hand-thumbs-down"></i> <span>${item.unlikeCount || 0}</span></button>`);
            
            $unlikeButton.on('click', () => {
                reactToComment('unlike', item.idx);
            });
            
            // 부모 댓글에는 대댓글 버튼과 대댓글 보기 버튼 추가
            if (isParent) {
                // 대댓글 버튼
                const $replyButton = $('<button class="btn btn-sm btn-outline-dark me-2"><i class="bi bi-pencil"></i> 댓글 작성</button>');
                $replyButton.on('click', () => {
                    $('#parentCommentIdx').val(item.idx);
                    $('#replyEditor').summernote('code', '');
                    $('#replyModal').modal('show');
                });
                
                // 대댓글 보기 버튼 (서버에서 받은 replyCount 사용)
                const replyCount = item.replyCount || 0;
                const $viewRepliesButton = $(`<button class="btn btn-sm btn-outline-dark me-2"><i class="bi bi-chat-dots"></i> 댓글 보기(${replyCount})</button>`);
                $viewRepliesButton.on('click', () => {
                    const $repliesContainer = $(`#replies-${item.idx}`);
                    if ($repliesContainer.is(':visible')) {
                        $repliesContainer.hide();
                        $viewRepliesButton.html(`<i class="bi bi-chat-dots"></i> 댓글 보기(${replyCount})`);
                    } else {
                        $repliesContainer.show();
                        loadCommentItems({isParent: false, itemIdx: item.idx});
                        $viewRepliesButton.html(`<i class="bi bi-chat-dots-fill"></i> 댓글 닫기(${replyCount})`);
                    }
                });
                
                $actions.append($replyButton).append($viewRepliesButton);
            }
            
            // 버튼들을 $actions에 추가
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
            $deleteBtn.on('click', () => {
                deleteComment(item.idx, !isParent ? parentCommentIdx : null);
            });
            
            $actions.append($editBtn).append($deleteBtn);
            
            // 헤더, 내용, 액션 버튼들 추가
            $itemDiv.append($header).append($content).append($actions);
            
            // 부모 댓글에만 대댓글 컨테이너 추가
            if (isParent) {
                // 대댓글 컨테이너
                const $repliesContainer = $(`<div id="replies-${item.idx}" class="replies-container mt-3 ps-4" style="display: none;"></div>`);
                
                // 대댓글 정렬 드롭다운을 컨테이너 내부 상단에 추가
                const $replySortDropdown = createReplySortDropdown(item.idx);
                const $replyControlRow = $('<div class="d-flex justify-content-end mb-2"></div>').append($replySortDropdown);
                $repliesContainer.append($replyControlRow);
                
                // 대댓글이 표시될 컨테이너
                const $repliesContent = $('<div class="replies-content"></div>');
                $repliesContainer.append($repliesContent);
                
                // 대댓글 페이지네이션 컨테이너
                const $replyPaginationContainer = $(`<div id="replyPagination-${item.idx}" class="mt-2"></div>`);
                $repliesContainer.append($replyPaginationContainer);
                
                $itemDiv.append($repliesContainer);
            }
            
            $container.append($itemDiv);
        });
        
        // 부모 댓글 목록에만 페이지네이션 컨테이너 추가
        if (isParent) {
            const $paginationContainer = $('<div id="commentPagination" class="mt-3"></div>');
            $container.append($paginationContainer);
        }
    } else {
        // 댓글이 없는 경우
        const message = isParent ? '댓글이 없습니다. 첫 댓글을 작성해보세요!' : '대댓글이 없습니다.';
        const $noItems = $(`<p class="text-center text-muted ${!isParent ? 'mb-2' : ''}">${message}</p>`);
        $container.append($noItems);
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
    const selector = `.${type}-btn[data-id="${commentIdx}"]`;
    const $reactButton = $(selector);
    const isParentComment = $(`#comment-${commentIdx}`).length > 0;
    if ($reactButton && $reactButton.length) {
        $reactButton.prop('disabled', true);
        
        // 반대 버튼도 일시적으로 비활성화
        const oppositeType = type === 'like' ? 'unlike' : 'like';
        const oppositeSelector = `.${oppositeType}-btn[data-id="${commentIdx}"]`;
        const $oppositeButton = $(oppositeSelector);
        if ($oppositeButton && $oppositeButton.length) {
            $oppositeButton.prop('disabled', true);
        }
    }
    
    $.ajax({
        type: 'GET',
        url: `${url}?commentIdx=${commentIdx}`,
        success: (res) => {
            // 버튼 다시 활성화
            if ($reactButton && $reactButton.length) {
                $reactButton.prop('disabled', false);
                
                // 반대 버튼도 활성화
                const oppositeType = type === 'like' ? 'unlike' : 'like';
                const oppositeSelector = `.${oppositeType}-btn[data-id="${commentIdx}"]`;
                const $oppositeButton = $(oppositeSelector);
                if ($oppositeButton && $oppositeButton.length) {
                    $oppositeButton.prop('disabled', false);
                }
            }
            
            if (res.success) {
                setTimeout(() => {
                    if (isParentComment) {
                        loadCommentItems({
                            isParent: true,
                            itemIdx: queryParam.postIdx,
                            page: queryParam.comment.page
                        });
                    } else if (queryParam.reply.lastParentIdx) {
                        const orderIdx = getSelectedSortOrder(queryParam.reply.lastParentIdx);
                        loadCommentItems({
                            isParent: false,
                            itemIdx: queryParam.reply.lastParentIdx,
                            page: queryParam.reply.page,
                            orderIdx: orderIdx
                        });
                    }
                }, 300);
                
                setInstantAlert('success', res);
            } else {
                setInstantAlert('danger', res);
            }
        },
        error: (e) => {
            // 오류 발생 시 버튼 다시 활성화
            if ($reactButton && $reactButton.length) {
                $reactButton.prop('disabled', false);
                
                // 반대 버튼도 활성화
                const oppositeType = type === 'like' ? 'unlike' : 'like';
                const oppositeSelector = `.${oppositeType}-btn[data-id="${commentIdx}"]`;
                const $oppositeButton = $(oppositeSelector);
                if ($oppositeButton && $oppositeButton.length) {
                    $oppositeButton.prop('disabled', false);
                }
            }
            setInstantAlert('danger', e.responseJSON);
        }
    });
};