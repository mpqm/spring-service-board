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

