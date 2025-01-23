// 알림 메시지를 표시하는 함수
const showAlert = (type, message) => {
    const alertHtml = `<div class="alert alert-${type} alert-dismissible fade show" role="alert"><span>${message}</span></div>`;
    $('#alert-container').html(alertHtml);
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

