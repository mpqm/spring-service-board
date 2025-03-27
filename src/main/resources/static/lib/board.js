// [알림]
// 페이지 로드 시 sessionStorage에서 알림 메시지 확인 및 표시
$(document).ready(() => {
    // SessionStorage에서 알림 데이터 확인
    const alertType = sessionStorage.getItem('alertType');
    const alertMessage = sessionStorage.getItem('alertMessage');

    // 알림 데이터가 있으면 표시 후 삭제
    if (alertType && alertMessage) {
        showAlert(alertType, alertMessage);
        // 표시 후 세션스토리지에서 제거
        sessionStorage.removeItem('alertType');
        sessionStorage.removeItem('alertMessage');
    }
});

// 알림 메시지를 표시하는 함수
const showAlert = (type, message) => {
    const alertHtml = `<div class="alert alert-${type} alert-dismissible fade show header-alert" role="alert"><span>${message}</span></div>`;
    $('#alert-container').html(alertHtml);

    // 3초 후 알림 닫기
    setTimeout(() => {
        $('#alert-container').empty();
    }, 3000);
};

// 서버 응답을 분석하여 성공 및 오류 메시지를 추출하는 함수
const getMessage = (response) => {
    // 기본 메시지 설정
    let message = response.message || '알 수 없는 오류가 발생했습니다.';

    // 결과(result)가 배열인 경우 상세 메시지 추가
    if (Array.isArray(response.result) && response.result.length > 0) {
        message += '<br>';
        response.result.forEach(err => {
            const errorMsg = err.split(':')[1]?.trim() || err;
            message += `${errorMsg}<br>`;
        });
    }

    return message;
};

// 로그아웃 함수
const handleLogoutForm = () => {
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

// 프로필 이미지 미리보기
const handleProfileImagePreview = (event) => {
    const file = event.target.files[0];
    const preview = $('#profilePreview');

    if (file) {
        // 파일 크기 체크 (5MB 제한)
        if (file.size > 5 * 1024 * 1024) {
            showAlert('danger', '파일 크기는 5MB를 초과할 수 없습니다.');
            event.target.value = ''; // 파일 선택 초기화
            return;
        }

        // 파일 타입 체크
        if (!file.type.startsWith('image/')) {
            showAlert('danger', '이미지 파일만 업로드 가능합니다.');
            event.target.value = ''; // 파일 선택 초기화
            return;
        }
        const reader = new FileReader();
        reader.onload = (e) => preview.attr('src', e.target.result).show();
        reader.readAsDataURL(file);
    } else {
        preview.hide();
    }
};

// 날짜 포맷팅 함수
const formatDate = (date) => {
    if (!(date instanceof Date) || isNaN(date)) return '';
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${year}-${month}-${day} ${hours}:${minutes}`;
}
