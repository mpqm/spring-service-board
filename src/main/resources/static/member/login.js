$(document).ready(() => {
    $('#login-form').on('submit', handleLogin);
});

// 로그인 처리 함수
const handleLogin = (event) => {
    event.preventDefault();
    const loginData = {
        id: $.trim($('#id').val()),
        password: $.trim($('#password').val()),
    };
    $.ajax({
        type: 'POST',
        url: '/login',
        contentType: 'application/json',
        data: JSON.stringify(loginData),
        success: (res) => {
            if (res.success) {
                showAlert('success', getMessage(res));
                setTimeout(() => window.location.href = "/test", 1);
            } else {
                showAlert('danger', getMessage(res));
            }
        },
        error: (e) => {
            const errorResponse = e.responseJSON || { message: '서버와의 통신 중 문제가 발생했습니다.', result: [] };
            showAlert('danger', getMessage(errorResponse));
        }
    });
};
