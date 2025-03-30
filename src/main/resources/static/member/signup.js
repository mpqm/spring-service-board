$(document).ready(() => { 
    $('#signupForm').on('submit', handleSignup);
    $('#profileImage').on('change', handleProfileImagePreview);
});

const handleSignup = (event) => {
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
    const fileInput = $('#profileImage')[0].files[0];
    if (fileInput) formData.append("file", fileInput);
    $.ajax({
        url: "/signup",
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        success: (res) => {
            if (res.success) {
                setSessionAlert('success', "회원가입이 완료되었습니다.");
                window.location.href = "/login";
            } else {
                setInstantAlert('danger', res);
            }
        },
        error: (e) => setInstantAlert('danger', e.responseJSON)
    });
}

// 프로필 이미지 미리보기
const handleProfileImagePreview = (event) => {
    const file = event.target.files[0];
    const preview = $('#profilePreview');
    if (file) {
        // 파일 크기 체크 (5MB 제한)
        if (file.size > 5 * 1024 * 1024) {
            showAlertMaintence('danger', '파일 크기는 5MB를 초과할 수 없습니다.');
            event.target.value = ''; // 파일 선택 초기화
            return;
        }

        // 파일 타입 체크
        if (!file.type.startsWith('image/')) {
            showAlertMaintence('danger', '이미지 파일만 업로드 가능합니다.');
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