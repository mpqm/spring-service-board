// 전역 변수로 선언하여 중복 선언 방지
let queryParams = {
    page: 1,
    size: 10,
    categoryIdx: null,
    searchIdx: null,
    orderIdx: null,
    keyword: ''
};

$(document).ready(() => {
    // cursor-pointer 스타일 추가
    $('<style>.cursor-pointer { cursor: pointer; }</style>').appendTo('head');
    
    // 페이지 로드 시 게시물 목록 가져오기
    fetchPosts();
    
    // 카테고리 필터 이벤트
    $('.dropdown-item[data-category-idx]').on('click', function(e) {
        e.preventDefault();
        const categoryIdx = $(this).data('category-idx');

        // 버튼 텍스트 업데이트
        $('#categoryDropdown').text($(this).text());

        // 쿼리 매개변수 업데이트
        queryParams.categoryIdx = categoryIdx === 'all' ? null : categoryIdx;
        queryParams.page = 1; // 페이지 리셋

        // 게시물 목록 다시 가져오기
        fetchPosts();
    });

    // 정렬 필터 이벤트
    $('.dropdown-item[data-order-idx]').on('click', function(e) {
        e.preventDefault();
        // 원시 데이터 속성 값 확인
        const rawOrderIdx = $(this).data('order-idx');
        // 파싱 시도
        const orderIdx = parseInt(rawOrderIdx);

        // 버튼 텍스트 업데이트
        $('#sortDropdown').text($(this).text());

        // 쿼리 매개변수 업데이트
        queryParams.orderIdx = orderIdx;
        queryParams.page = 1; // 페이지 리셋

        // 게시물 목록 다시 가져오기
        fetchPosts();
    });

    // 검색 조건 이벤트
    $('.dropdown-item[data-search-idx]').on('click', function(e) {
        e.preventDefault();
        // 원시 데이터 속성 값 확인
        const rawSearchIdx = $(this).data('search-idx');
        // 파싱 시도
        const searchIdx = parseInt(rawSearchIdx);

        // 버튼 텍스트 업데이트
        $('#searchTypeDropdown').text($(this).text());

        // 쿼리 매개변수 업데이트
        queryParams.searchIdx = searchIdx;
    });

    // 검색 버튼 이벤트
    $('#searchBtn').on('click', function() {
        const keyword = $('#searchKeyword').val().trim();

        // 쿼리 매개변수 업데이트
        queryParams.keyword = keyword;
        queryParams.page = 1; // 페이지 리셋

        // 게시물 목록 다시 가져오기
        fetchPosts();
    });

    // 검색어 입력 필드에서 엔터 키 이벤트
    $('#searchKeyword').on('keypress', function(e) {
        if (e.key === 'Enter') {
            e.preventDefault();
            $('#searchBtn').click();
        }
    });

    // 페이지네이션 클릭 이벤트를 동적으로 처리하기 위한 이벤트 위임
    $(document).on('click', '.pagination .page-link', function(e) {
        e.preventDefault();
        const page = $(this).data('page');
        if (page && !$(this).parent().hasClass('disabled')) {
            queryParams.page = parseInt(page);
            fetchPosts();
            // 페이지 상단으로 스크롤
            $('html, body').animate({ scrollTop: 0 }, 'slow');
        }
    });
});

// 게시물 목록 가져오기
const fetchPosts = () => {
    // 로딩 상태 표시
    $('#postTableBody').html('<tr><td colspan="9" class="text-center">로딩 중...</td></tr>');
    
    // 유효한 파라미터만 포함하여 URL 쿼리스트링 직접 구성
    let url = '/post-list?';
    url += `page=${queryParams.page || 1}&`;
    url += `size=${queryParams.size || 10}&`;
    if (queryParams.categoryIdx !== null && queryParams.categoryIdx !== undefined) 
        url += `categoryIdx=${queryParams.categoryIdx}&`;
    if (queryParams.searchIdx !== null && queryParams.searchIdx !== undefined) 
        url += `searchIdx=${queryParams.searchIdx}&`;
    if (queryParams.orderIdx !== null && queryParams.orderIdx !== undefined) 
        url += `orderIdx=${queryParams.orderIdx}&`;
    if (queryParams.keyword) 
        url += `keyword=${encodeURIComponent(queryParams.keyword)}&`;
    // 마지막 & 제거
    url = url.slice(0, -1);

    // AJAX 요청
    $.ajax({
        url: url,
        type: 'GET',
        // data 파라미터 제거 (이미 URL에 포함됨)
        dataType: 'json',
        success: (data) => {
            if (data.success) {
                displayPosts(data.result);
            } else {
                alert(data.message || '게시물을 불러올 수 없습니다.');
            }
        },
        error: (error) => {
            $('#postTableBody').html('<tr><td colspan="9" class="text-center">게시물을 불러오는 중 오류가 발생했습니다.</td></tr>');
        }
    });
}

// 게시물 목록 표시
const displayPosts = (result) => {
    if (result.data && result.data.length > 0) {
        $('#postTableBody').empty();

        $.each(result.data, (index, post) => {
            // null 체크
            if (!post) {
                return; // continue와 동일
            }
            
            // 생성 날짜 포맷팅
            let formattedDate = '';
            if (post.createdAt) {
                const createdDate = new Date(post.createdAt);
                formattedDate = formatDate(createdDate);
            }

            const row = `
                <tr class="text-center align-middle cursor-pointer" data-post-idx="${post.idx}">
                    <td class="small">${post.idx || ''}</td>
                    <td>
                        <span class="badge bg-dark rounded-pill">${post.categoryName || '분류없음'}</span>
                    </td>
                    <td>${post.nickName || '익명'}</td>
                    <td class="text-start">
                        <span class="text-decoration-none text-dark">${post.title || '제목 없음'}</span>
                        ${post.commentCount > 0 ? `<span class="badge bg-danger rounded-pill ms-1">${post.commentCount}</span>` : ''}
                    </td>
                    <td class="small">${post.viewCount || 0}</td>
                    <td class="small">${post.likeCount || 0}</td>
                    <td class="small">${post.unlikeCount || 0}</td>
                    <td class="small">${post.commentCount || 0}</td>
                    <td class="text-muted small">${formattedDate}</td>
                </tr>
            `;

            $('#postTableBody').append(row);
        });

        // 게시글 행 클릭 이벤트 추가
        $('#postTableBody tr').on('click', function() {
            const postIdx = $(this).data('post-idx');
            window.location.href = `/post-detail?postIdx=${postIdx}`;
        });

        // 페이지네이션 업데이트
        updatePagination(result);
    } else {
        $('#postTableBody').html('<tr><td colspan="9" class="text-center py-5 text-muted">게시물이 없습니다.</td></tr>');
        
        // 데이터가 없는 경우에만 페이지네이션 비우기
        clearPagination();
    }
}

// 페이지네이션 초기화
const clearPagination = () => {
    $('#pagination-container').empty();
}

// 페이지네이션 업데이트
const updatePagination = (result) => {
    // 페이지네이션 컨테이너 초기화
    $('#pagination-container').empty();
    
    // 전체 페이지가 0인 경우에만 페이지네이션 표시하지 않음
    if (!result.totalPages || result.totalPages <= 0) {
        return;
    }

    // 페이지네이션 HTML 생성
    let paginationHtml = `
        <div class="d-flex align-items-center justify-content-center flex-wrap">
            <nav aria-label="Page navigation" class="me-3">
                <ul class="pagination justify-content-center mb-0">
                    <li class="page-item ${result.currentPage <= 1 ? 'disabled' : ''}">
                        <a class="page-link nav-btn bg-white text-black border-dark" href="#" data-page="1" aria-label="First">
                            <span aria-hidden="true">&laquo;&laquo;</span>
                        </a>
                    </li>
                    <li class="page-item ${result.currentPage === 1 ? 'disabled' : ''}">
                        <a class="page-link nav-btn bg-white text-black border-dark" href="#" data-page="${result.currentPage - 1}" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
    `;
    
    // 페이지 번호 버튼 - 현재 페이지 주변에 표시할 페이지 수
    const maxVisiblePages = 5;
    const halfVisible = Math.floor(maxVisiblePages / 2);
    let startPage = Math.max(1, result.currentPage - halfVisible);
    let endPage = Math.min(result.totalPages, startPage + maxVisiblePages - 1);
    
    // 표시할 페이지 범위 조정
    if (endPage - startPage + 1 < maxVisiblePages) {
        startPage = Math.max(1, endPage - maxVisiblePages + 1);
    }
    
    // 페이지 번호 버튼
    for (let i = startPage; i <= endPage; i++) {
        if (i === result.currentPage) {
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
                <li class="page-item ${result.currentPage === result.totalPages ? 'disabled' : ''}">
                    <a class="page-link nav-btn bg-white text-black border-dark" href="#" data-page="${result.currentPage + 1}" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>

                <li class="page-item ${result.currentPage >= result.totalPages ? 'disabled' : ''}">
                    <a class="page-link nav-btn bg-white text-black border-dark" href="#" data-page="${result.totalPages}" aria-label="Last">
                        <span aria-hidden="true">&raquo;&raquo;</span>
                    </a>
                </li>
            </ul>
        </nav>
        
        <!-- 페이지 직접 입력 폼 -->
        <div class="input-group " style="width: auto;">
            <input type="number" id="pageInput" class="form-control bg-white text-black border-dark" min="1" max="${result.totalPages}" value="${result.currentPage}" style="width: 70px;">
            <span class="input-group-text bg-white border-dark border">/ ${result.totalPages}</span>
            <button class="btn btn-outline-dark" type="button" id="goToPageBtn">이동</button>
        </div>
    </div>
    `;
    
    // 페이지네이션 HTML 추가
    $('#pagination-container').html(paginationHtml);
    
    // 페이지 직접 입력 이벤트 처리
    $('#goToPageBtn').on('click', function() {
        const pageNum = parseInt($('#pageInput').val());
        if (pageNum && pageNum >= 1 && pageNum <= result.totalPages) {
            queryParams.page = pageNum;
            fetchPosts();
            // 페이지 상단으로 스크롤
            $('html, body').animate({ scrollTop: 0 }, 'slow');
        } else {
            alert('유효한 페이지 번호를 입력해주세요.');
            $('#pageInput').val(result.currentPage);
        }
    });
    
    // 입력 필드에서 엔터키 처리
    $('#pageInput').on('keypress', function(e) {
        if (e.key === 'Enter') {
            e.preventDefault();
            $('#goToPageBtn').click();
        }
    });
}

// 날짜 포맷팅 함수
const formatDate = (date) => {
    if (!(date instanceof Date) || isNaN(date)) {
        return '';
    }

    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');

    return `${year}-${month}-${day} ${hours}:${minutes}`;
}
