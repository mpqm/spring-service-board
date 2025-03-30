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
    loadPosts();
    $('.dropdown-item[data-category-idx]').on('click', handleCategoryDropDown);
    $('.dropdown-item[data-order-idx]').on('click', handleOrderDropDown);
    $('.dropdown-item[data-search-idx]').on('click', handleSearchTypeDropDown);
    $('#searchBtn').on('click', handleSearchBtn);
    $('#searchKeyword').on('keypress', handleSearchInput);
    $(document).on('click', '.pagination .page-link', handlePageClick);
});

// 게시물 목록 가져오기
const loadPosts = () => {
    // AJAX 요청
    $.ajax({
        url: '/post-list',
        type: 'GET',
        dataType: 'json',
        data: queryParams,
        success: (res) => {
            if (res.success) {
                $('#postTableBody').empty();
                if (res.result.data.length < 0) return;
                res.result.data.forEach(post => {
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
                    $('#postTableBody tr').on('click', (event) => location.href = `/post-detail?postIdx=${$(event.currentTarget).data('post-idx')}`);
                    createPagination({
                        containerId: 'pagination-container',
                        pageInfo: {
                            currentPage: res.result.currentPage,
                            totalPages: res.result.totalPages
                        },
                        callback: (page) => {
                            queryParams.page = page;
                            loadPosts();
                            $('html, body').animate({ scrollTop: 0 }, 'slow');
                        },
                        size: 'md',
                        showPageInput: true
                    });
                });
            } else {
                setInstantAlert('danger', res);
            }
        },
        error: (e) => setInstantAlert('danger', e.responseJSON)
    });
}

// 카테고리 필터
const handleCategoryDropDown = (event) => {
    event.preventDefault();
    const categoryIdx = parseInt($(event.currentTarget).data('category-idx'));
    $('#categoryDropdown').text($(event.currentTarget).text());
    queryParams.categoryIdx = categoryIdx
    queryParams.page = 1;
    loadPosts();
}

// 정렬 필터
const handleOrderDropDown = (event) => {
    event.preventDefault();
    const orderIdx = parseInt($(event.currentTarget).data('order-idx'));
    $('#sortDropdown').text($(event.currentTarget).text());
    queryParams.orderIdx = orderIdx;
    queryParams.page = 1;
    loadPosts();
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
        loadPosts();
    }
}

const handleSearchInput = (event) => {
    if (event.key === 'Enter') {
        event.preventDefault();
        $('#searchBtn').click();
    }
}

// 검색 버튼 이벤트
const handleSearchBtn = () => {
    queryParams.keyword = $('#searchKeyword').val().trim();
    queryParams.page = 1;
    loadPosts();
}
