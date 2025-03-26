// 로그아웃 함수
const logout = () => {
    $.ajax({
        type: 'POST',
        url: '/logout',
        success: (data) => {
            if (data.success) {
                // 세션 스토리지 비우기
                sessionStorage.clear();
                window.location.href = '/';
            } else {
                alert(data.message || '로그아웃에 실패했습니다.');
            }
        },
        error: () => {
            alert('로그아웃 처리 중 오류가 발생했습니다.');
        }
    });
}; 