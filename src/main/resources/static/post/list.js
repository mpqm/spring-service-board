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

    // 페이지 로드 시 게시물 목록 가져오기
    fetchPosts();

    // 카테고리 필터 이벤트
    $('.dropdown-item[data-category-idx]').on('click', handleCategoryDropDown);

    // 정렬 필터 이벤트
    $('.dropdown-item[data-order-idx]').on('click', handleOrderDropDown);

    // 검색 조건 이벤트 설정
    $('.dropdown-item[data-search-idx]').on('click', handleSearchTypeDropDown);

    // 검색 버튼 이벤트
    $('#searchBtn').on('click', handleSearchBtn);

    // 검색어 입력 필드에서 엔터 키 이벤트
    $('#searchKeyword').on('keypress', handleInputEnter);

    // 페이지네이션 클릭 이벤트를 동적으로 처리하기 위한 이벤트 위임
    $(document).on('click', '.pagination .page-link', handlePageClick);

});

// 카테고리 필터
const handleCategoryDropDown = (event) => {
    event.preventDefault();
    const categoryIdx = parseInt($(event.currentTarget).data('category-idx'));
    $('#categoryDropdown').text($(event.currentTarget).text());
    queryParams.categoryIdx = categoryIdx
    queryParams.page = 1;
    fetchPosts();
}

// 정렬 필터
const handleOrderDropDown = (event) => {
    event.preventDefault();
    const orderIdx = parseInt($(event.currentTarget).data('order-idx'));
    $('#sortDropdown').text($(event.currentTarget).text());
    queryParams.orderIdx = orderIdx;
    queryParams.page = 1;
    fetchPosts();
}

// 검색 조건 설정
const handleSearchTypeDropDown = (event) => {
    event.preventDefault();
    const searchIdx = parseInt($(event.currentTarget).data('search-idx'));
    $('#searchTypeDropdown').text($(event.currentTarget).text());
    queryParams.searchIdx = searchIdx;
}

// 페이지네이션 클릭 이벤트
const handlePageClick = (event) => {
    event.preventDefault();
    const page = $(event.currentTarget).data('page');
    if (page && !$(event.currentTarget).parent().hasClass('disabled')) {
        queryParams.page = parseInt(page);
        fetchPosts();
    }
}

// 검색 버튼 이벤트
const handleSearchBtn = () => {
    queryParams.keyword = $('#searchKeyword').val().trim();
    queryParams.page = 1;
    fetchPosts();
}

// 검색 필드 엔터 이벤트
const handleInputEnter = (event) => {
    if (event.key === 'Enter') {
        event.preventDefault();
        $('#searchBtn').click();
    }
}

// 게시물 목록 가져오기
const fetchPosts = () => {
    // AJAX 요청
    $.ajax({
        url: '/post-list',
        type: 'GET',
        dataType: 'json',
        data: queryParams,  // 객체를 직접 전달하면 자동으로 쿼리스트링으로 변환됨
        success: (res) => {
            if (res.success) displayPosts(res.result);
            else showAlert("danger", getMessage(res));
        },
        error: (e) => {
            const errorResponse = e.responseJSON || { message: '서버와의 통신 중 문제가 발생했습니다.', result: [] };
            showAlert('danger', getMessage(errorResponse));
        }
    });
}

// 게시물 목록 테이블에 표시
const displayPosts = (result) => {
    if (result.data && result.data.length > 0) {
        $('#postTableBody').empty();
        // 데이터 삽입
        $.each(result.data, (index, post) => {
            if (!post) return;
            const row = `
                <tr class="text-center align-middle cursor-pointer" data-post-idx="${post.idx}">
                    <td class="small">${post.idx || ''}</td>
                    <td><span class="badge bg-dark rounded-pill">${post.categoryName || '분류없음'}</span></td>
                    <td>${post.nickName || '익명'}</td>
                    <td class="text-start"><span class="text-decoration-none text-dark">${post.title || '제목 없음'}</span></td>
                    <td class="small">${post.viewCount || 0}</td>
                    <td class="small">${post.likeCount || 0}</td>
                    <td class="small">${post.unlikeCount || 0}</td>
                    <td class="small">${post.commentCount || 0}</td>
                    <td class="text-muted small">${formatDate(new Date(post.createdAt))}</td>
                </tr>
            `;
            $('#postTableBody').append(row);
            // 게시글 행 클릭 이벤트 추가
            $('#postTableBody tr').on('click', (event) => {
               const postIdx = $(event.currentTarget).data('post-idx');
               window.location.href = `/post-detail?postIdx=${postIdx}`;
            });

            // 페이지네이션 업데이트
            updatePagination(result);
        });
    } else {
        $('#pagination-container').empty();
    }
}

// 페이지네이션 업데이트
const updatePagination = (result) => {
    $('#pagination-container').empty();
    if (!result.totalPages || result.totalPages <= 0) return;

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
    if (endPage - startPage + 1 < maxVisiblePages) startPage = Math.max(1, endPage - maxVisiblePages + 1);

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
    $('#goToPageBtn').on('click', () => {
        const pageNum = parseInt($('#pageInput').val());
        if (pageNum && pageNum >= 1 && pageNum <= result.totalPages) {
            queryParams.page = pageNum;
            fetchPosts();
            $('html, body').animate({ scrollTop: 0 }, 'slow');
        } else {
            showAlert("danger", '유효한 페이지 번호를 입력해주세요.');
            $('#pageInput').val(result.currentPage);
        }
    });

    // 입력 필드에서 엔터키 처리
    $('#pageInput').on('keypress', (event) => {
        if (event.key === 'Enter') {
            event.preventDefault();
            $('#goToPageBtn').click();
        }
    });
}


