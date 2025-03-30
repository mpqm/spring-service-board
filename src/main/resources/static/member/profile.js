$(document).ready(() => {
    loadMemberInfo();
    $('#editProfileForm').on('submit', handleEditProfile);
    $('#editPwForm').on('submit', handleEditPassword);
    $('#inActiveForm').on('submit', handleInActive);
    $('#profileImage').on('change', handleProfileImagePreview);
});

// 회원 정보 로드
const loadMemberInfo = () => {
    $.ajax({
        url: "/find-member",
        type: "GET",
        success: (res) => {
            if (res.success) {
                $('#nickName').val(res.result.nickName);
                $('#phoneNumber').val(res.result.phoneNumber);
                if (res.result.profileImageUrl) $('#profilePreview').attr('src', res.result.profileImageUrl);
            } else {
                setInstantAlert('danger', res);
            }
        },
        error: (e) => setInstantAlert('danger', e.responseJSON)
    });
};

// 프로필 수정
const handleEditProfile = (event) => {
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
            if (res.success) setInstantAlert('success', res);
            else setInstantAlert('danger', res);
        },
        error: (e) => setInstantAlert('danger', e.responseJSON)
    });
};

// 비밀번호 변경
const handleEditPassword = (event) => {
    event.preventDefault();
    const editPwData = {
        oldPassword: $.trim($('#oldPassword').val()),
        newPassword: $.trim($('#newPassword').val())
    };
    $.ajax({
        url: "/edit-pw",
        type: "POST",
        data: JSON.stringify(editPwData),
        contentType: "application/json",
        success: (res) => {
            if (res.success) setInstantAlert('success', res);
            else setInstantAlert('danger', res);
        },
        error: (e) => setInstantAlert('danger', e.responseJSON)
    });
};

// 회원 탈퇴
const handleInActive = (event) => {
    event.preventDefault();
    if (!confirm('정말로 탈퇴하시겠습니까?')) return;
    $.ajax({
        url: "/in-active",
        type: "GET",
        success: (res) => {
            if (res.success) {
                setSessionAlert('success', "탈퇴처리가 완료되었습니다.");
                location.href = "/login";
            } else {
                setInstantAlert('danger', res);
            }
        },
        error: (e) => setInstantAlert('danger', e.responseJSON)
    });
};

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