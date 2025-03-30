$(document).ready(() => {
    $('#loginForm').on('submit', handleLogin);
});

// 로그인
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
                setSessionAlert('success', "게시판에 오신걸 환영합니다.");
                location.href = "/";
            } else {
                setSessionAlert('danger', res);
            }
        },
        error: (e) => setInstantAlert('danger', e.responseJSON)
    });
};
