$(document).ready(() => { 
    $('#signupForm').on('submit', handleSignup);
    $('#profileImage').on('change', handleProfileImagePreview);
});

const handleSignup = (event) => {

    // 기본 제출 방지, 데이터 셋팅
    event.preventDefault();
    const signupData = {
        id: $.trim($('#id').val()),
        password: $.trim($('#password').val()),
        nickName: $.trim($('#nickName').val()),
        userName: $.trim($('#userName').val()),
        email: $.trim($('#email').val()),
        phoneNumber: $.trim($('#phoneNumber').val())
    };

    const formData = new FormData();
    formData.append("dto", new Blob([JSON.stringify(signupData)], { type: "application/json" }));

    // 파일 추가 (파일이 선택되었는지 확인)
    const fileInput = $('#profileImage')[0].files[0];
    if (fileInput) formData.append("file", fileInput);

    // AJAX 요청
    $.ajax({
        url: "/signup",
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        success: (res) => {
            if (res.success) {
                sessionStorage.setItem('alertType', 'success');
                sessionStorage.setItem('alertMessage', getMessage(res));
                window.location.href = "/login";
            } else {
                showAlert('danger', getMessage(res));
            }
        },
        error: (e) => {
            const errorResponse = e.responseJSON || { message: '서버와의 통신 중 문제가 발생했습니다.', result: [] };
            showAlert('danger', getMessage(errorResponse));
        }
    });
}