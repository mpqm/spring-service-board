// 전역 변수 선언
let postIdx = null;
let isEdit = false;

$(document).ready(() => {
    // URL에서 게시물 IDX 가져오기
    const urlParams = new URLSearchParams(window.location.search);
    postIdx = urlParams.get('postIdx');
    
    // 수정 모드인 경우 기존 데이터 로드
    if (postIdx) {
        isEdit = true;
        loadPostDetail(postIdx);
    }
    
    // Summernote 에디터 초기화
    $('#content').summernote({
        height: 400,
        lang: 'ko-KR',
        toolbar: [
            ['style', ['style']],
            ['font', ['bold', 'underline', 'clear']],
            ['color', ['color']],
            ['para', ['ul', 'ol', 'paragraph']],
            ['table', ['table']],
            ['insert', ['link', 'picture']],
            ['view', ['fullscreen', 'codeview', 'help']]
        ],
        callbacks: {
            onImageUpload: function(files) {
                uploadImage(files);
            }
        }
    });
    
    // 갤러리 이미지 미리보기 이벤트 바인딩
    $('#postImage').on('change', handleImagePreview);
    
    // 폼 제출 이벤트
    $('#post-create-form').on('submit', (e) => {
        e.preventDefault();
        
        const title = $('#title').val();
        const content = $('#content').summernote('code');
        const categoryIdx = $('#categoryIdx').val();
        const rangeIdx = $('#rangeIdx').val();
        
        if (!title || !content) {
            alert('제목과 내용을 모두 입력해주세요.');
            return;
        }
        
        if (!categoryIdx || !rangeIdx) {
            alert('카테고리와 공개범위를 모두 선택해주세요.');
            return;
        }
        
        const formData = new FormData();
        formData.append("dto", new Blob([JSON.stringify({
            title: title,
            content: content,
            categoryIdx: parseInt(categoryIdx),
            rangeIdx: parseInt(rangeIdx)
        })], { type: "application/json" }));
        
        // 파일 추가 (파일이 선택되었는지 확인)
        const fileInput = $('#postImage')[0].files;
        if (fileInput && fileInput.length > 0) {
            for (let i = 0; i < fileInput.length; i++) {
                formData.append("file", fileInput[i]);
            }
        }
        
        const url = isEdit ? `/post?postIdx=${postIdx}` : '/post';
        const method = isEdit ? 'PUT' : 'POST';
        
        $.ajax({
            type: method,
            url: url,
            data: formData,
            processData: false,
            contentType: false,
            success: (res) => {
                if (res.success) {
                    sessionStorage.setItem('alertType', 'success');
                    sessionStorage.setItem('alertMessage', getMessage(res));
                    if(isEdit) location.href = `/post-detail?postIdx=${postIdx}`;
                    else location.href = '/';
                } else {
                    showAlert('danger', getMessage(res));
                }
            },
            error: () => {
                alert('게시물 저장 중 오류가 발생했습니다.');
            }
        });
    });
});

// 게시물 상세 정보 로드 함수
const loadPostDetail = (postIdx) => {
    $.ajax({
        type: 'GET',
        url: `/post?postIdx=${postIdx}`,
        success: (res) => {
            if (res.success) {
                const post = res.result;
                
                if (!post) {
                    showAlert('danger', '게시물 데이터를 불러올 수 없습니다.');
                    window.location.href = '/';
                    return;
                }
                
                // 제목과 내용 설정
                $('#title').val(post.title || '');
                $('#content').summernote('code', post.content || '');
                
                // 카테고리와 공개범위 설정
                if (post.categoryIdx) {
                    $('#categoryIdx').val(post.categoryIdx);
                    $('#categoryIdx option[value="' + post.categoryIdx + '"]').prop('selected', true);
                }
                if (post.rangeIdx) {
                    $('#rangeIdx').val(post.rangeIdx);
                    $('#rangeIdx option[value="' + post.rangeIdx + '"]').prop('selected', true);
                }
                
                // 이미지가 있는 경우 표시
                if (post.postImages && post.postImages.length > 0) {
                    renderExistingImages(post.postImages);
                }
            } else {
                showAlert('danger', getMessage(res));
                window.location.href = '/';
            }
        },
        error: () => {
            showAlert('danger', '게시물을 불러올 수 없습니다.');
        }
    });
};

// 이미지 업로드 함수
const uploadImage = (files) => {
    const file = files[0];
    const reader = new FileReader();
    
    reader.onload = function(e) {
        // 먼저 에디터에 이미지 삽입
        $('#content').summernote('insertImage', e.target.result);
        
        // 그 다음 서버에 업로드
        const formData = new FormData();
        formData.append('file', file);
        
        $.ajax({
            type: 'POST',
            url: '/upload-image',
            data: formData,
            processData: false,
            contentType: false,
            success: (res) => {
                if (!res.success) {
                    showAlert('danger', getMessage(res));
                }
            },
            error: () => {
                showAlert('danger', '이미지 업로드 중 오류가 발생했습니다.');
            }
        });
    };
    
    reader.readAsDataURL(file);
};

// 이미지 미리보기 처리
const handleImagePreview = () => {
    const files = event.target.files; // 선택한 파일 목록
    const $container = $('#imagePreviewContainer').empty();
    $container.append('<p class="mt-3 mb-2">새 이미지: &nbsp;&nbsp;</p>');
    // 모든 이미지 처리
    for (let i = 0; i < files.length; i++) {
        const file = files[i];
        if (!file.type.startsWith('image/')) continue;
        const reader = new FileReader();
        reader.onload = function(e) {
            // 이미지 생성
            const $img = $('<img>')
                .attr('src', e.target.result)
                .attr('alt', '새 이미지')
                .addClass('img-thumbnail me-2 mb-2')
                .css('max-height', '150px');
            $container.append($img);
        };
        reader.readAsDataURL(file);
    }
}

// 기존 이미지 렌더링 (수정 모드)
const renderExistingImages = (images) => {
    const $container = $('#existingImages');
    $container.empty();

    if (images && images.length > 0) {
        // 제목 추가
        $container.append('<p class="mt-3 mb-2">기존 이미지:</p>');
        
        images.forEach(function(image) {
            // 이미지 파일 경로 처리
            const imagePath = image.imageUrl || image.filePath || (image.storedFileName ? `/upload/${image.storedFileName}` : null);
            if (!imagePath) return;

            // 이미지 생성
            const $img = $('<img>')
                .attr('src', imagePath)
                .attr('alt', '기존 이미지')
                .attr('data-idx', image.idx)
                .addClass('img-thumbnail me-2 mb-2')
                .css('max-height', '150px');
            $container.append($img);
        });
    }
}

// 페이지 이동
const cancelBtn = (event) => {
    event.preventDefault();
    if (isEdit) location.href = `/post-detail?postIdx=${postIdx}`;
    else location.href = '/';
}
