$(document).ready(() => {
    // 페이지 로딩 시 회원 정보 가져오기
    loadMemberInfo();
    
    // 이벤트 핸들러 등록
    $('#editProfileForm').on('submit', handleEditProfileForm);
    $('#editPwForm').on('submit', handleEditPasswordForm);
    $('#inActiveForm').on('submit', handleInActiveForm);
    $('#profileImage').on('change', handleProfileImagePreview);
});

// 회원 정보 로드
const loadMemberInfo = () => {
    $.ajax({
        url: "/find-member",
        type: "GET",
        success: (res) => {
            if (res.success) {
                const member = res.result;
                $('#nickName').val(member.nickName);
                $('#phoneNumber').val(member.phoneNumber);
                // 프로필 이미지가 있는 경우에만 미리보기 설정
                if (member.profileImageUrl) $('#profilePreview').attr('src', member.profileImageUrl);
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

// 프로필 수정
const handleEditProfileForm = (event) => {
    event.preventDefault();
    const profileData = {
        nickName: $.trim($('#nickName').val()),
        phoneNumber: $.trim($('#phoneNumber').val())
    }
    const formData = new FormData();
    formData.append("dto", new Blob([JSON.stringify(profileData)], { type: "application/json" }));
    
    const fileInput = $('#profileImage')[0].files[0];
    if (fileInput) formData.append("file", fileInput);

    $.ajax({
        url: "/edit-profile",
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
};

// 비밀번호 변경
const handleEditPasswordForm = (event) => {
    event.preventDefault();
    const data = {
        oldPassword: $('#oldPassword').val(),
        newPassword: $('#newPassword').val()
    };

    $.ajax({
        url: "/edit-pw",
        type: "POST",
        data: JSON.stringify(data),
        contentType: "application/json",
        success: (res) => {
            if (res.success) {
                showAlert('success', getMessage(res));
                $('#oldPassword').val('');
                $('#newPassword').val('');
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

// 회원 탈퇴
const handleInActiveForm = (event) => {
    event.preventDefault();
    if (confirm('정말로 탈퇴하시겠습니까?')) {
        $.ajax({
            url: "/in-active",
            type: "GET",
            success: (res) => {
                if (res.success) {
                    showAlert('success', getMessage(res));
                    setTimeout(() => {
                        window.location.href = "/login";
                    }, 2000);
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
};