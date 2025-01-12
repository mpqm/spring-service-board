function scrollToTop() {
        window.scrollTo({
            top: 0,
            behavior: "smooth",
        });
}
function initializeBackToTopButton(buttonId) {
    const backToTopButton = document.getElementById(buttonId);

    if (!backToTopButton) {
        console.error(`Button with ID "${buttonId}" not found.`);
        return;
    }

    // 스크롤 이벤트로 버튼 표시/숨김 처리
    window.addEventListener("scroll", function () {
        if (window.scrollY > 200) {
            backToTopButton.style.display = "block";
        } else {
            backToTopButton.style.display = "none";
        }
    });

    // 버튼 클릭 시 스크롤 함수 호출
    backToTopButton.addEventListener("click", scrollToTop);
}

document.addEventListener("DOMContentLoaded", function () {
    initializeBackToTopButton("backToTop");
});
