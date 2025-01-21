$(document).ready(() => { $('#login-form').on('submit', handleLogin); });

const handleLogin = (event) => {
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
            // 서버에서 받은 응답의 상태 확인
            if (res.success) {
                alert("로그인 성공!");
                window.location.href = "/test";  // 성공 시 페이지 이동
            } else {
                alert("로그인 실패: " + res.message);
            }
        },
        error: (e) => { alert("로그인 실패: " + e.responseJSON.message); }
    });
}
