let postIdx;
let memberIdx;

$(document).ready(() => {
    // URL에서 게시물 IDX 가져오기
    const urlParams = new URLSearchParams(window.location.search);
    postIdx = urlParams.get('postIdx');
    
    // 세션 스토리지에서 memberIdx 가져오기
    memberIdx = sessionStorage.getItem('memberIdx');
    
    // 게시물 상세 정보 로드
    loadPostDetail(postIdx);
    
    // 페이지 로드 시 댓글 목록 바로 로드 - 주석 처리
    // loadComments(postIdx);
    
    // 댓글 목록 보기 버튼 숨기기
    $('#loadCommentsBtn').hide();
    
    // 수정 버튼 이벤트
    $('#editButton').on('click', () => {
        window.location.href = `/post-edit?postIdx=${postIdx}`;
    });
    
    // 삭제 버튼 이벤트
    $('#deleteButton').on('click', () => {
        if (confirm('정말로 이 게시물을 삭제하시겠습니까?')) {
            deletePost(postIdx);
        }
    });
    
    // 좋아요 버튼 이벤트
    $('#likeButton').on('click', () => {
        reactToPost('like', postIdx);
    });
    
    // 싫어요 버튼 이벤트
    $('#unlikeButton').on('click', () => {
        reactToPost('unlike', postIdx);
    });
    
    // 목록으로 버튼 이벤트
    $('#listButton').on('click', () => {
        window.location.href = '/';
    });
    
    // 댓글 작성 버튼 이벤트 - 주석 처리
    /*
    $('#submitComment').on('click', () => {
        if (!window.memberIdx) {
            alert('로그인이 필요합니다.');
            return;
        }
        
        const content = $.trim($('#commentContent').val());
        if (!content) {
            alert('댓글 내용을 입력해주세요.');
            return;
        }
        
        submitComment(postIdx, content);
    });
    */
});

// 게시물 상세 정보 로드 함수
const loadPostDetail = (postIdx) => {
    $.ajax({
        type: 'GET',
        url: `/post?postIdx=${postIdx}`,
        success: (data) => {
            if (!data.success) {
                alert(data.message || '게시물을 불러올 수 없습니다.');
                window.location.href = '/';
                return;
            }
            
            displayPostDetail(data.result);
        },
        error: () => {
            alert('게시물을 불러올 수 없습니다.');
        }
    });
};

// 게시물 상세 정보 표시 함수
const displayPostDetail = (postDetail) => {
    if (!postDetail) {
        return;
    }
    
    const post = postDetail.idx ? postDetail : postDetail.post;
    
    if (!post) {
        return;
    }
    
    // 기본 정보 표시
    if (post.categoryName) $('#categoryName').text(post.categoryName);
    else $('#categoryName').hide();
    
    $('#postTitle').text(post.title || '제목 없음');
    $('#author').text(`${post.nickName || '알 수 없음'}`);
    $('#viewCount').text(`${post.viewCount || 0}`);
    $('#likeCount').text(`${post.likeCount || 0}`);
    $('#unlikeCount').text(`${post.unlikeCount || 0}`);
    
    if (post.createdAt) $('#createdAt').text(`${formatDate(new Date(post.createdAt))}`);
    else $('#createdAt').text('작성일 알 수 없음');

    
    $('#content').html(post.content || '');
    
    // 이미지 표시
    const $imageGallery = $('#imageGallery');
    $imageGallery.empty();
    
    const images = postDetail.postImages || post.postImages;
    
    if (images && images.length > 0) {
        const $row = $('<div class="row"></div>');
        
        $.each(images, (index, image) => {
            const $col = $('<div class="col-md-4 mb-3"></div>');
            const $img = $('<img class="img-fluid rounded" alt="게시물 이미지">').attr('src', image.imageUrl);
            
            $col.append($img);
            $row.append($col);
        });
        
        $imageGallery.append($row);
    }
    
    // 좋아요/싫어요 버튼 업데이트
    const $likeButton = $('#likeButton');
    const $unlikeButton = $('#unlikeButton');
    
    $likeButton.html(`<i class="bi bi-hand-thumbs-up"></i> 좋아요 ${post.likeCount || 0}`);
    $unlikeButton.html(`<i class="bi bi-hand-thumbs-down"></i> 싫어요 ${post.unlikeCount || 0}`);
    
    if (postDetail.isLiked || post.isLiked) {
        $likeButton.removeClass('btn-outline-primary').addClass('btn-primary');
    } else {
        $likeButton.removeClass('btn-primary').addClass('btn-outline-primary');
    }
    
    if (postDetail.isUnliked || post.isUnliked) {
        $unlikeButton.removeClass('btn-outline-danger').addClass('btn-danger');
    } else {
        $unlikeButton.removeClass('btn-danger').addClass('btn-outline-danger');
    }
    
    // 수정/삭제 버튼 항상 표시
    $('#editButton').show();
    $('#deleteButton').show();
};

// 게시물 반응 (좋아요/싫어요) 함수
const reactToPost = (type, postIdx) => {
    const url = type === 'like' ? '/react-like' : '/react-unlike';
    
    $.ajax({
        type: 'GET',
        url: `${url}?postIdx=${postIdx}`,
        success: (data) => {
            if (data.success) {
                loadPostDetail(postIdx);
            } else {
                alert(data.message || '요청 처리 중 오류가 발생했습니다.');
            }
        },
        error: () => {
            alert('요청 처리 중 오류가 발생했습니다.');
        }
    });
};

// 날짜 포맷팅 함수
const formatDate = (date) => {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    
    return `${year}-${month}-${day} ${hours}:${minutes}`;
};

// 게시물 삭제 함수
const deletePost = (postIdx) => {
    $.ajax({
        type: 'DELETE',
        url: `/post?postIdx=${postIdx}`,
        success: (data) => {
            if (data.success) {
                alert('게시물이 삭제되었습니다.');
                window.location.href = '/';
            } else {
                alert(data.message || '게시물 삭제 중 오류가 발생했습니다.');
            }
        },
        error: () => {
            alert('게시물 삭제 중 오류가 발생했습니다.');
        }
    });
};

// 댓글 작성 함수
const submitComment = (postIdx, content) => {
    $.ajax({
        type: 'POST',
        url: '/comment',
        contentType: 'application/json',
        data: JSON.stringify({
            postIdx: postIdx,
            content: content
        }),
        success: (data) => {
            if (data.success) {
                $('#commentContent').val('');
                loadComments(postIdx);
            } else {
                alert(data.message || '댓글 작성 중 오류가 발생했습니다.');
            }
        },
        error: () => {
            alert('댓글 작성 중 오류가 발생했습니다.');
        }
    });
};

// 댓글 로드 함수
const loadComments = (postIdx, page = 1) => {
    $.ajax({
        type: 'GET',
        url: `/comment-list?postIdx=${postIdx}&page=${page}`,
        success: (data) => {
            if (data.success) {
                displayComments(data.result.data, postIdx);
                displayPagination(data.result, postIdx);
            } else {
                console.error('댓글을 불러올 수 없습니다.');
            }
        },
        error: () => {
            console.error('댓글을 불러올 수 없습니다.');
        }
    });
};

// 페이지네이션 표시 함수
const displayPagination = (pageInfo, postIdx) => {
    const $commentsList = $('#commentsList');
    const $paginationDiv = $('<div class="pagination justify-content-center mt-3"></div>');
    
    const totalPages = pageInfo.totalPages;
    const currentPage = pageInfo.currentPage;
    
    if (totalPages > 1) {
        // 이전 페이지 버튼
        if (currentPage > 1) {
            const $prevButton = $('<button class="btn btn-outline-primary mx-1">이전</button>');
            $prevButton.on('click', () => {
                loadComments(postIdx, currentPage - 1);
            });
            $paginationDiv.append($prevButton);
        }
        
        // 페이지 번호 버튼
        for (let i = 1; i <= totalPages; i++) {
            const buttonClass = i === currentPage ? 'btn btn-primary mx-1' : 'btn btn-outline-primary mx-1';
            const $pageButton = $(`<button class="${buttonClass}">${i}</button>`);
            
            $pageButton.on('click', () => {
                if (i !== currentPage) {
                    loadComments(postIdx, i);
                }
            });
            
            $paginationDiv.append($pageButton);
        }
        
        // 다음 페이지 버튼
        if (currentPage < totalPages) {
            const $nextButton = $('<button class="btn btn-outline-primary mx-1">다음</button>');
            $nextButton.on('click', () => {
                loadComments(postIdx, currentPage + 1);
            });
            $paginationDiv.append($nextButton);
        }
        
        $commentsList.append($paginationDiv);
    }
};

// 댓글 표시 함수
const displayComments = (comments, postIdx) => {
    const $commentsList = $('#commentsList');
    $commentsList.empty();
    
    if (comments && comments.length > 0) {
        $.each(comments, (index, comment) => {
            const $commentDiv = $('<div class="comment-item p-3 mb-2 border rounded"></div>');
            
            // 헤더 (작성자, 날짜)
            const $header = $('<div class="d-flex justify-content-between"></div>');
            const $author = $('<strong></strong>').text(comment.author);
            const $date = $('<small class="text-muted"></small>').text(formatDate(new Date(comment.createdAt)));
            
            $header.append($author).append($date);
            
            // 내용
            const $content = $('<p class="mt-2"></p>').text(comment.content);
            
            // 좋아요/싫어요 버튼 영역
            const $reactButtons = $('<div class="d-flex mt-2"></div>');
            
            // 좋아요 버튼
            const likeButtonClass = 'btn btn-sm me-2 ' + (comment.isLiked ? 'btn-primary' : 'btn-outline-primary');
            const $likeButton = $(`<button class="${likeButtonClass}"><i class="bi bi-hand-thumbs-up"></i> <span>${comment.likeCount || 0}</span></button>`);
            
            $likeButton.on('click', () => {
                if (!window.memberIdx) {
                    alert('로그인이 필요합니다.');
                    return;
                }
                reactToComment('like', comment.idx);
            });
            
            // 싫어요 버튼
            const unlikeButtonClass = 'btn btn-sm ' + (comment.isUnliked ? 'btn-danger' : 'btn-outline-danger');
            const $unlikeButton = $(`<button class="${unlikeButtonClass}"><i class="bi bi-hand-thumbs-down"></i> <span>${comment.unlikeCount || 0}</span></button>`);
            
            $unlikeButton.on('click', () => {
                if (!window.memberIdx) {
                    alert('로그인이 필요합니다.');
                    return;
                }
                reactToComment('unlike', comment.idx);
            });
            
            $reactButtons.append($likeButton).append($unlikeButton);
            
            $commentDiv.append($header).append($content).append($reactButtons);
            
            // 댓글 수정/삭제 버튼 (자신의 댓글일 경우에만 표시)
            // 문자열로 비교하기 위해 toString() 추가
            if (window.memberIdx && window.memberIdx.toString() === comment.memberIdx.toString()) {
                const $actions = $('<div class="mt-2 d-flex justify-content-end"></div>');
                
                const $editBtn = $('<button class="btn btn-sm btn-outline-secondary me-2">수정</button>');
                $editBtn.on('click', () => {
                    editComment(comment, postIdx);
                });
                
                const $deleteBtn = $('<button class="btn btn-sm btn-outline-danger">삭제</button>');
                $deleteBtn.on('click', () => {
                    deleteComment(comment.idx, postIdx);
                });
                
                $actions.append($editBtn).append($deleteBtn);
                $commentDiv.append($actions);
            }
            
            $commentsList.append($commentDiv);
        });
    } else {
        const $noComments = $('<p class="text-center text-muted">댓글이 없습니다. 첫 댓글을 작성해보세요!</p>');
        $commentsList.append($noComments);
    }
};

// 댓글 수정
const editComment = (comment, postIdx) => {
    if (!postIdx) {
        const urlParams = new URLSearchParams(window.location.search);
        postIdx = urlParams.get('postIdx');
    }
    
    const newContent = prompt('댓글을 수정하세요:', comment.content);
    if (newContent && newContent !== comment.content) {
        $.ajax({
            type: 'PUT',
            url: '/comment',
            contentType: 'application/json',
            data: JSON.stringify({
                idx: comment.idx,
                content: newContent
            }),
            success: (data) => {
                if (data.success) {
                    loadComments(postIdx);
                } else {
                    alert(data.message || '댓글 수정 중 오류가 발생했습니다.');
                }
            },
            error: () => {
                alert('댓글 수정 중 오류가 발생했습니다.');
            }
        });
    }
};

// 댓글 삭제
const deleteComment = (commentIdx, postIdx) => {
    if (!postIdx) {
        const urlParams = new URLSearchParams(window.location.search);
        postIdx = urlParams.get('postIdx');
    }
    
    if (confirm('정말 이 댓글을 삭제하시겠습니까?')) {
        $.ajax({
            type: 'DELETE',
            url: `/comment?idx=${commentIdx}`,
            success: (data) => {
                if (data.success) {
                    loadComments(postIdx);
                } else {
                    alert(data.message || '댓글 삭제 중 오류가 발생했습니다.');
                }
            },
            error: () => {
                alert('댓글 삭제 중 오류가 발생했습니다.');
            }
        });
    }
};

// 댓글 좋아요/싫어요 함수
const reactToComment = (type, commentIdx) => {
    if (!window.memberIdx) {
        alert('로그인이 필요합니다.');
        return;
    }
    
    const urlParams = new URLSearchParams(window.location.search);
    const postIdx = urlParams.get('postIdx');
    
    const url = type === 'like' ? '/react-like' : '/react-unlike';
    
    $.ajax({
        type: 'GET',
        url: `${url}?commentIdx=${commentIdx}`,
        success: (data) => {
            if (data.success) {
                loadComments(postIdx);
            } else {
                alert(data.message || '요청 처리 중 오류가 발생했습니다.');
            }
        },
        error: () => {
            alert('요청 처리 중 오류가 발생했습니다.');
        }
    });
}; 