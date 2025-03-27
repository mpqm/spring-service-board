$(document).ready(() => {
    $('#findIdForm').on('submit', handleFindIdForm);
    $('#findPwForm').on('submit', handleFindPwForm);
});

const handleFindIdForm = (event) => {
    event.preventDefault();
    const formData = {
        email: $.trim($('#email').val()),
    };
    $.ajax({
        type: 'POST',
        url: '/find-id-pw',
        contentType: 'application/json',
        data: JSON.stringify(formData),
        success: (res) => {
            // 서버에서 받은 응답의 상태 확인
            if (res.success) showAlert('success', getMessage(res));
            else showAlert('danger', getErrorMessage(res));
            
        },
        error: (e) => {
            const errorResponse = e.responseJSON || { message: '서버와의 통신 중 문제가 발생했습니다.', result: [] };
            showAlert('danger', getErrorMessage(errorResponse));
        }
    });
}

const handleFindPwForm = (event) => {
    event.preventDefault();
    const formData = {
        id: $.trim($('#id').val()),
    };
    $.ajax({
        type: 'POST',
        url: '/find-id-pw',
        contentType: 'application/json',
        data: JSON.stringify(formData),
        success: (res) => {
            
            // 서버에서 받은 응답의 상태 확인
            if (res.success) showAlert('success', getMessage(res));
            else showAlert('danger', getErrorMessage(res));
        
        },
        error: (e) => {
            const errorResponse = e.responseJSON || { message: '서버와의 통신 중 문제가 발생했습니다.', result: [] };
            showAlert('danger', getErrorMessage(errorResponse));
        }
    });
}