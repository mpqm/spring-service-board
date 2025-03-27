$(document).ready(() => {
    $('#loginForm').on('submit', handleLoginForm);
});

// 로그인 처리 함수
const handleLoginForm = (event) => {
    event.preventDefault();
    const formData = {
        id: $.trim($('#id').val()),
        password: $.trim($('#password').val()),
    };
    $.ajax({
        type: 'POST',
        url: '/login',
        contentType: 'application/json',
        data: JSON.stringify(formData),
        success: (res) => {
            if (res.success) {
                // 성공 메시지를 sessionStorage에 저장하고 페이지 이동
                sessionStorage.setItem('alertType', 'success');
                sessionStorage.setItem('alertMessage', "게시판에 오신걸 환영합니다.");
                window.location.href = "/";
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
