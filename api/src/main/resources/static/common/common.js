$(document).ready(() => {
    showSessionAlert();
});

// 알림 메시지 표시
const showSessionAlert = () => {
    const alertType = sessionStorage.getItem('alertType');
    const alertMessage = sessionStorage.getItem('alertMessage');
    if (alertType && alertMessage) {
        const alertHtml = `<div class="alert alert-${alertType} alert-dismissible fade show header-alert" role="alert"><span>${alertMessage}</span></div>`;
        $('#alert-container').html(alertHtml);
        setTimeout(() => { $('#alert-container').empty(); }, 3000);
        sessionStorage.removeItem('alertType');
        sessionStorage.removeItem('alertMessage');
    }
}

// 세션용 알림 메시지 설정하는 함수
const setSessionAlert = (type, res) => {
    let message;
    if (res == null) message = '알 수 없는 오류가 발생했습니다.';
    else if(typeof res === "string") message = res || '알 수 없는 오류가 발생했습니다.';
    else message = res.message || '알 수 없는 오류가 발생했습니다.';
    if (Array.isArray(res.result) && res.result.length > 0) {
        message += '<br>';
        res.result.forEach(err => message += `${err.split(':')[1]?.trim() || err}<br>`);
    }
    sessionStorage.setItem('alertType', type);
    sessionStorage.setItem('alertMessage', message);
}

// 알림 메시지를 표시하는 함수
const setInstantAlert = (type, res) => {
    let message;
    if (res == null) res = { message: '서버와의 통신 중 문제가 발생했습니다.', result: [] };
    else if(typeof res === "string") message = res || '알 수 없는 오류가 발생했습니다.';
    else message = res.message || '알 수 없는 오류가 발생했습니다.';
    if (Array.isArray(res.result) && res.result.length > 0) {
        message += '<br>';
        res.result.forEach(err => message += `${err.split(':')[1]?.trim() || err}<br>`);
    }
    const alertHtml = `<div class="alert alert-${type} alert-dismissible fade show header-alert" role="alert"><span>${message}</span></div>`;
    $('#alert-container').html(alertHtml);
    setTimeout(() => { $('#alert-container').empty(); }, 3000);
};

// 스크롤 위치 저장
const saveScrollPosition = (key, containerId = null) => {
    let scrollPosition;
    
    if (containerId) {
        // 특정 컨테이너의 스크롤 위치 저장
        const container = document.getElementById(containerId);
        if (container) {
            scrollPosition = {
                top: container.scrollTop,
                left: container.scrollLeft
            };
        }
    } else {
        // 윈도우 스크롤 위치 저장
        scrollPosition = {
            top: window.scrollY || document.documentElement.scrollTop,
            left: window.scrollX || document.documentElement.scrollLeft
        };
    }
    
    if (scrollPosition) sessionStorage.setItem('scrollPos_' + key, JSON.stringify(scrollPosition));
}

// 스크롤 위치 복원
const restoreScrollPosition = (key, containerId = null, clearAfterRestore = false, delay = 0) => {
    const savedPosition = sessionStorage.getItem('scrollPos_' + key);
    if (!savedPosition) return;
    
    const scrollPosition = JSON.parse(savedPosition);
    
    const applyScroll = () => {
        if (containerId) {
            const container = document.getElementById(containerId);
            if (container) container.scrollTo(scrollPosition.left, scrollPosition.top);
        } else {
            window.scrollTo(scrollPosition.left, scrollPosition.top);
        }
        
        if (clearAfterRestore) clearScrollPosition(key);
    };
    
    delay > 0 ? setTimeout(applyScroll, delay) : applyScroll();
}

// 스크롤 위치 삭제
const clearScrollPosition = (key) => {
    sessionStorage.removeItem('scrollPos_' + key);
}

// 날짜 포맷팅 함수
const formatDate = (date) => {
    if (!date || !(date instanceof Date) || isNaN(date)) return '-';
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${year}-${month}-${day} ${hours}:${minutes}`;
}

// 에디터 초기화
const initSummernote = (selector, height) => {
    $(selector).summernote({
        placeholder: '내용을 입력하세요',
        tabsize: 2,
        height: height,
        lang: 'ko-KR',
        toolbar: [
            ['style'],
            ['font', ['bold', 'underline', 'clear']],
            ['color', ['color']],
            ['para', ['ul', 'ol', 'paragraph']],    
            ['table', ['table']],
            ['insert', ['link', 'picture']],
            ['view', ['fullscreen', 'codeview', 'help']]
        ],
        callbacks: {
            onImageUpload: (files) => uploadSummernoteImage(files[0], $(selector), null)
        }
    });
};

// 이미지 업로드 함수
const uploadSummernoteImage = (file, $editor, $imageInput) => {
    if (!file) return;
    const reader = new FileReader();
    reader.onload = function(e) {
        $editor.summernote('insertImage', e.target.result);
        const formData = new FormData();
        formData.append('file', file);
        $.ajax({
            type: 'POST',
            url: '/upload-image',
            data: formData,
            processData: false,
            contentType: false,
            success: (res) => !res.success && showAlertMaintence('danger', res),
            error: (e) => showAlertMaintence('danger', e.responseJSON),
            complete: () => { if ($imageInput) $imageInput.val(''); }
        });
    };
    reader.readAsDataURL(file);
};

// 페이지네이션 생성
const createPagination = (options) => {
    // 필수 옵션 확인
    if (!options.containerId || !options.pageInfo || !options.callback) return;
    const {
        containerId,
        pageInfo,
        callback,
        params = {},
        size = 'md',
        showPageInput = true
    } = options;

    // 페이지 정보 유효성 검사
    if (!pageInfo.totalPages || pageInfo.totalPages <= 0) {
        $(`#${containerId}`).empty();
        return;
    }

    // 페이지네이션 크기에 따른 클래스 결정
    let sizeClass = '';
    if (size === 'sm') sizeClass = 'pagination-sm';
    else if (size === 'lg') sizeClass = 'pagination-lg';

    // 페이지네이션 HTML 생성
    let paginationHtml = `
        <div class="d-flex align-items-center justify-content-center flex-wrap">
            <nav aria-label="Page navigation" class="${showPageInput ? 'me-3' : ''}">
                <ul class="pagination ${sizeClass} justify-content-center mb-0">
                    <li class="page-item ${pageInfo.currentPage <= 1 ? 'disabled' : ''}">
                        <a class="page-link nav-btn bg-white text-black border-dark" href="#" data-page="1" aria-label="First">
                            <span aria-hidden="true">&laquo;&laquo;</span>
                        </a>
                    </li>
                    <li class="page-item ${pageInfo.currentPage === 1 ? 'disabled' : ''}">
                        <a class="page-link nav-btn bg-white text-black border-dark" href="#" data-page="${pageInfo.currentPage - 1}" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
    `;

    // 페이지 번호 버튼 - 현재 페이지 주변에 표시할 페이지 수
    const maxVisiblePages = 5;
    const halfVisible = Math.floor(maxVisiblePages / 2);
    let startPage = Math.max(1, pageInfo.currentPage - halfVisible);
    let endPage = Math.min(pageInfo.totalPages, startPage + maxVisiblePages - 1);

    // 표시할 페이지 범위 조정
    if (endPage - startPage + 1 < maxVisiblePages) startPage = Math.max(1, endPage - maxVisiblePages + 1);

    // 페이지 번호 버튼
    for (let i = startPage; i <= endPage; i++) {
        if (i === pageInfo.currentPage) {
            // 현재 활성화된 페이지
            paginationHtml += `
                <li class="page-item active">
                    <a class="page-link bg-dark text-white border-dark" href="#" data-page="${i}">${i}</a>
                </li>
            `;
        } else {
            // 비활성화 페이지
            paginationHtml += `
                <li class="page-item">
                    <a class="page-link text-dark bg-white border-dark" href="#" data-page="${i}">${i}</a>
                </li>
            `;
        }
    }

    // 다음 버튼들
    paginationHtml += `
                <li class="page-item ${pageInfo.currentPage === pageInfo.totalPages ? 'disabled' : ''}">
                    <a class="page-link nav-btn bg-white text-black border-dark" href="#" data-page="${pageInfo.currentPage + 1}" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>

                <li class="page-item ${pageInfo.currentPage >= pageInfo.totalPages ? 'disabled' : ''}">
                    <a class="page-link nav-btn bg-white text-black border-dark" href="#" data-page="${pageInfo.totalPages}" aria-label="Last">
                        <span aria-hidden="true">&raquo;&raquo;</span>
                    </a>
                </li>
            </ul>
        </nav>
    `;

    // 페이지 직접 입력 폼 (선택 사항)
    if (showPageInput) {
        paginationHtml += `
            <!-- 페이지 직접 입력 폼 -->
            <div class="input-group" style="width: auto;">
                <input type="number" id="pageInput-${containerId}" class="form-control bg-white text-black border-dark" min="1" max="${pageInfo.totalPages}" value="${pageInfo.currentPage}" style="width: 70px;">
                <span class="input-group-text bg-white border-dark border">/ ${pageInfo.totalPages}</span>
                <button class="btn btn-outline-dark" type="button" id="goToPageBtn-${containerId}">이동</button>
            </div>
        `;
    }

    paginationHtml += `</div>`;

    // 페이지네이션 HTML 추가
    $(`#${containerId}`).html(paginationHtml);

    // 페이지 번호 버튼 클릭 이벤트
    $(`#${containerId} .page-link[data-page]`).on('click', function(e) {
        e.preventDefault();
        const page = parseInt($(this).data('page'));
        if (page === pageInfo.currentPage) return; // 같은 페이지면 무시
        callback(page, params);
    });

    // 페이지 직접 입력 이벤트 처리 (선택 사항)
    if (showPageInput) {
        $(`#goToPageBtn-${containerId}`).on('click', () => {
            const pageNum = parseInt($(`#pageInput-${containerId}`).val());
            if (pageNum && pageNum >= 1 && pageNum <= pageInfo.totalPages) {
                if (pageNum === pageInfo.currentPage) return; // 같은 페이지면 무시
                callback(pageNum, params);
            } else {
                showAlertMaintence("danger", '유효한 페이지 번호를 입력해주세요.');
                $(`#pageInput-${containerId}`).val(pageInfo.currentPage);
            }
        });

        // 입력 필드에서 엔터키 처리
        $(`#pageInput-${containerId}`).on('keypress', (event) => {
            if (event.key === 'Enter') {
                event.preventDefault();
                $(`#goToPageBtn-${containerId}`).click();
            }
        });
    }
};
