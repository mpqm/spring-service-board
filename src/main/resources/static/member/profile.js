$(document).ready(() => {
    $('#edit-profile-form').on('submit', handleEditProfile);
    $('#edit-pw-form').on('submit', handleEditPw);
    $('#in-active-form').on('submit', handleInActive);
});

const handleEditProfile = (event) => {

    // 기본 제출 방지, 데이터 셋팅
    event.preventDefault();
    const editProfileData = {
        nickName: $.trim($('#nickName').val()),
        phoneNumber: $.trim($('#phoneNumber').val())
    };

    const formData = new FormData();
    formData.append("dto", new Blob([JSON.stringify(editProfileData)], { type: "application/json" }));

    // 파일 추가 (파일이 선택되었는지 확인)
    const fileInput = $('#profileImage')[0].files[0];
    if (fileInput) formData.append("file", fileInput);

    // AJAX 요청
    $.ajax({
        url: "/editProfile",
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        success: (res) => {
            if (res.success) {
                showAlert('success', getMessage(res));
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

const handleEditPw = (event) => {
    event.preventDefault();
    const editPwData = {
        oldPassword: $.trim($('#oldPassword').val()),
        newPassword: $.trim($('#newPassword').val()),
    };
    $.ajax({
        type: 'POST',
        url: '/editPw',
        contentType: 'application/x-www-form-urlencoded',  // 폼 데이터를 URL 인코딩 형식으로 전송
        data: $.param(editPwData),
        success: (res) => {
            if (res.success) {
                showAlert('success', getMessage(res));
                setTimeout(() => window.location.href = "/login", 200);
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

const handleInActive = (event) => {
    event.preventDefault();
    $.ajax({
        type: 'GET',
        url: '/inActive',
        success: (res) => {
            if (res.success) {
                showAlert('success', res.message);
                setTimeout(() => window.location.href = "/login", 2000);
            } else {
                showAlert('danger', res.message);
            }
        },
        error: (e) => {
            const errorResponse = e.responseJSON || { message: '서버와의 통신 중 문제가 발생했습니다.', result: [] };
            showAlert('danger', errorResponse.message);
        }
    });
};