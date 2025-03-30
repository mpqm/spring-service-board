$(document).ready(() => {
    $('#findIdForm').on('submit', handleFindId);
    $('#findPwForm').on('submit', handleFindPw);
});

// 아이디 찾기
const handleFindId = (event) => {
    event.preventDefault();
    const findIdData = {
        email: $.trim($('#email').val()),
    };
    $.ajax({
        type: 'POST',
        url: '/find-id-pw',
        contentType: 'application/json',
        data: JSON.stringify(findIdData),
        success: (res) => {
            if (res.success) setInstantAlert('success', res);
            else setInstantAlert('danger', res);
        },
        error: (e) => setInstantAlert('danger', e.responseJSON )
    });
}

// 비밀번호 찾기
const handleFindPw = (event) => {
    event.preventDefault();
    const findPwData = {
        id: $.trim($('#id').val()),
    };
    $.ajax({
        type: 'POST',
        url: '/find-id-pw',
        contentType: 'application/json',
        data: JSON.stringify(findPwData),
        success: (res) => {
            if (res.success) setInstantAlert('success', res);
            else setInstantAlert('danger', res);
        },
        error: (e) => setInstantAlert('danger', e.responseJSON)
    });
}