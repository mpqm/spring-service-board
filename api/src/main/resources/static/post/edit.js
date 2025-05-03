// 전역 변수 선언
let postIdx = new URLSearchParams(window.location.search).get('postIdx');
let isEdit = postIdx ? true : false;

$(document).ready(() => {
    if(isEdit) loadPostDetail(postIdx);
    initSummernote('#content', 200);
    $('#postImage').on('change', handleImageGalleryPreview);
    $('#cancelBtn').on('click', () => location.href = isEdit ? `/post-detail?postIdx=${postIdx}` : '/');
    $('#postCreateForm').on('submit', handleCreatePost);
    $('#rangeIdx').on('change', showPwField)
});

// 게시물 상세 정보 로드 함수
const loadPostDetail = (postIdx) => {
    $('#pageTitle').text('게시글 수정');
    $.ajax({
        type: 'GET',
        url: `/post?postIdx=${postIdx}`,
        success: (res) => {
            if (res.success) {
                const post = res.result;
                $('#title').val(post.title || '');
                $('#content').summernote('code', post.content || '');
                $('#categoryIdx').val(post.categoryIdx);
                $('#rangeIdx').val(post.rangeIdx);
                if (post.rangeIdx == 17) $('#passwordField').show();
                if (post.postImages && post.postImages.length > 0) {
                    const oldImageGallery = $('<div class="d-flex flex-wrap gap-2"></div>');
                    post.postImages.forEach(image => {
                        const imageElement = $(`<img class="img-thumbnail rounded mxh-150" alt="게시물 이미지" src="${image.imageUrl}">`);
                        oldImageGallery.append(imageElement);
                    });
                    $('#existingImages').append(oldImageGallery);
                }
            } else {
                setInstantAlert('danger', res);
            }
        },
        error: (e) => setInstantAlert('danger', e.responseJSON)
    });
};

// 이미지 미리보기 처리
const handleImageGalleryPreview = (event) => {
    const $container = $('#imagePreviewContainer').empty().append('<p class="mt-3 mb-2">NEW IMAGE: </p>');
    for (let i = 0; i < event.target.files.length; i++) {
        if (!event.target.files[i].type.startsWith('image/')) continue;
        const reader = new FileReader();
        reader.onload = (e) => {
            const $img = $('<img>').attr('src', e.target.result).addClass('img-thumbnail rounded mxh-150');
            $container.append($img);
        };
        reader.readAsDataURL(event.target.files[i]);
    }
}

// 게시물 생성 함수
const handleCreatePost = (event) => {
    event.preventDefault();
    const postData = {
        title: $.trim($('#title').val()),
        content: $.trim($('#content').summernote('code')),
        categoryIdx: parseInt($('#categoryIdx').val()),
        rangeIdx: parseInt($('#rangeIdx').val()),
        password: $.trim($('#password').val())
    }
    const formData = new FormData();
    formData.append("dto", new Blob([JSON.stringify(postData)], { type: "application/json" }));
    const fileInput = $('#postImage')[0].files;
    if (fileInput && fileInput.length > 0) for (let i = 0; i < fileInput.length; i++) formData.append("file", fileInput[i]);
    $.ajax({
        type: isEdit ? 'PUT' : 'POST',
        url: isEdit ? `/post?postIdx=${postIdx}` : '/post',
        data: formData,
        processData: false,
        contentType: false,
        success: (res) => {
            if (res.success) {
                setSessionAlert('success', res);
                location.href = isEdit ? `/post-detail?postIdx=${postIdx}` : '/';
            } else {
                setInstantAlert('danger', res);
            }
        },
        error: (e) => setInstantAlert('danger', e.responseJSON)
    });
}

// 패스워드 입력 필드
const showPwField = (event) => {
    if ($(event.currentTarget).val() == 17) {
        $('#passwordField').show();
        $('#password').prop('required', true);
    } else {
        $('#passwordField').hide();
        $('#password').prop('required', false);
        $('#password').val(''); // 비밀번호 필드 초기화
    }
}