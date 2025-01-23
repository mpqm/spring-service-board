$(document).ready(() => {
    $('#find-id-form').on('submit', handleFindIdForm);
    $('#find-pw-form').on('submit', handleFindPwForm);
});

const handleFindIdForm = (event) => {
    event.preventDefault();
    const formData = {
        email: $.trim($('#email').val()),
    };
    $.ajax({
        type: 'POST',
        url: '/findIdPw',
        contentType: 'application/x-www-form-urlencoded',  // 변경된 부분
        data: $.param(formData),  // JSON 대신 URL-encoded 형식으로 변환
        success: (res) => {
            // 서버에서 받은 응답의 상태 확인
            if (res.success) {
                showAlert('success', getMessage(res));
            } else {
                showAlert('danger', getErrorMessage(res));
            }
        },
        error: (e) => {
            const errorResponse = e.responseJSON || { message: '서버와의 통신 중 문제가 발생했습니다.', result: [] };
            showAlert('danger', getErrorMessage(errorResponse));
        }
    });
}

const handleFindPwForm = (event) => {
    event.preventDefault();
    const formData = {
        id: $.trim($('#id').val()),
    };
    $.ajax({
        type: 'POST',
        url: '/findIdPw',
        contentType: 'application/x-www-form-urlencoded',  // 변경된 부분
        data: $.param(formData),  // JSON 대신 URL-encoded 형식으로 변환
        success: (res) => {
            // 서버에서 받은 응답의 상태 확인
            if (res.success) {
                showAlert('success', getMessage(res));
            } else {
                showAlert('danger', getErrorMessage(res));
            }
        },
        error: (e) => {
            const errorResponse = e.responseJSON || { message: '서버와의 통신 중 문제가 발생했습니다.', result: [] };
            showAlert('danger', getErrorMessage(errorResponse));
        }
    });
}