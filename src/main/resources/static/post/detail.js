let postIdx = new URLSearchParams(window.location.search).get('postIdx');
let passwordVerified = false;

$(document).ready(() => {
    loadPostDetail(postIdx);
    initSummernote('#commentEditor', 200);
    $('#editButton').on('click', () => location.href = `/post-edit?postIdx=${postIdx}`);
    $('#deleteButton').on('click', () => handleDeleteBtn());
    $('#likeButton').on('click', () => reactToPost('/react-like'));
    $('#unlikeButton').on('click', () => reactToPost('/react-unlike'));
    $('#passwordConfirmBtn').on('click', checkPostPassword);
});

// 게시물 상세 정보 로드 함수
const loadPostDetail = (postIdx) => {
    $.ajax({
        type: 'GET',
        url: `/post?postIdx=${postIdx}`,
        success: (res) => {
            if (res.success) {
                // 비공개 게시물 처리
                if(res.result.rangeIdx == 15){
                    setSessionAlert('danger', "비공개 게시물입니다.");
                    window.location.href = '/';
                }
                // 보호된 게시물 처리
                if(res.result.rangeIdx == 17 && !passwordVerified) {
                    $('#passwordModal').modal('show');
                    return;
                }
                // 게시글 데이터 표시
                const post = res.result;
                $('#categoryName').text(post.categoryName || '카테고리 없음');
                $('#postTitle').text(post.title || '제목 없음');
                $('#author').text(`${post.nickName || '알 수 없음'}`);
                $('#viewCount').text(`${post.viewCount || 0}`);
                $('#likeCount').text(`${post.likeCount || 0}`);
                $('#unlikeCount').text(`${post.unlikeCount || 0}`);
                $('#commentCount').text(`${post.commentCount || 0}`);
                $('#createdAt').text(`${formatDate(new Date(post.createdAt)) || '작성일 알 수 없음'}`);
                $('#content').html(post.content || '');
                // 이미지 표시
                $('#imageGallery').empty();
                if (post.postImages && post.postImages.length > 0) {
                    const imageGallery = $('<div class="d-flex flex-wrap gap-2"></div>');
                    post.postImages.forEach(image => {
                        const imageElement = $(`
                            <img class="img-thumbnail rounded mxh-150" alt="게시물 이미지" src="${image.imageUrl}">
                        `);
                        imageGallery.append(imageElement);
                    });
                    $('#imageGallery').append(imageGallery);
                }
                // 좋아요/싫어요 버튼 업데이트
                post.isLiked 
                ? $('#likeButton').removeClass('btn-outline-dark').addClass('btn-dark').html(`<i class="bi bi-hand-thumbs-up-fill"></i> 좋아요 <span>${post.likeCount || 0}</span>`)
                : $('#likeButton').html(`<i class="bi bi-hand-thumbs-up"></i> 좋아요 <span>${post.likeCount || 0}</span>`);
                post.isUnliked 
                ? $('#unlikeButton').removeClass('btn-outline-dark').addClass('btn-dark').html(`<i class="bi bi-hand-thumbs-down-fill"></i> 싫어요 <span>${post.unlikeCount || 0}</span>`)
                : $('#unlikeButton').html(`<i class="bi bi-hand-thumbs-down"></i> 싫어요 <span>${post.unlikeCount || 0}</span>`);
                
            } else {
                setSessionAlert('danger', res);
                window.location.href = '/';
            }
        },
        error: (e) => setSessionAlert('danger', e.responseJSON)
    });
};

// 게시물 반응 (좋아요/싫어요) 함수
const reactToPost = (url) => {
    $.ajax({
        type: 'GET',
        url: `${url}?postIdx=${postIdx}`,
        success: (res) => {
            if (res.success) location.reload();
            else location.reload();
        },
        error: (e) => setInstantAlert('danger', e.responseJSON)
    });
}; 

// 게시물 삭제 함수
const handleDeleteBtn = () => {
    if (!confirm('정말로 이 게시물을 삭제하시겠습니까?')) return;
    $.ajax({
        type: 'DELETE',
            url: `/post?postIdx=${postIdx}`,
            success: (res) => {
                if (res.success) {
                    setSessionAlert('success', res);
                    window.location.href = '/';
                } else {
                    setInstantAlert('danger', res);
                }
            },
            error: (e) => setInstantAlert('danger', e.responseJSON)
        });
};

const checkPostPassword = () => {
    const password = $('#postPassword').val();
    if (!password) {
        alert('비밀번호를 입력해주세요.');
        return;
    }

    $.ajax({
        url: `/post-auth?postIdx=${postIdx}&password=${password}`,
        type: 'GET',
        success: (res) => {
            if (res.success) {
                passwordVerified = true;
                $('#passwordModal').modal('hide');
                loadPostDetail(postIdx);
            } else {
                setSessionAlert('danger', '비밀번호가 일치하지 않습니다.');
                window.location.href = '/';
            }
        },
        error: (e) => setSessionAlert('danger', e.responseJSON)
    });
}
