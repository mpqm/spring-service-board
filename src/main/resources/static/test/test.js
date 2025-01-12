// showDateTime.js
function showDateTime() {
    const currentDate = new Date();
    const dateTimeElement = document.getElementById("currentDateTime");
    if (dateTimeElement) dateTimeElement.textContent = currentDate.toString();
    else alert("날짜와 시간을 표시할 요소가 없습니다.");
}

// Enter 키 입력 시 버튼 클릭 함수
function triggerButtonClick(event) {
    if (event.key === "Enter") { // Enter 키 확인
        document.getElementById("calculateButton").click(); // 버튼 클릭 실행
    }
}

// 숫자 출력
function printNumbers() {
    const numbersList = document.getElementById("numbersList");
    const sumResult = document.getElementById("sumResult");
    const numberInput = document.getElementById("numberInput");

    if (numbersList && sumResult && numberInput) {
        const inputValue = parseInt(numberInput.value, 10);

        if (isNaN(inputValue) || inputValue <= 0) {
            alert("양의 정수를 입력하세요.");
            return;
        }

        numbersList.innerHTML = "";
        let sum = 0;

        for (let i = 1; i <= inputValue; i++) {
            const numbersListItem = document.createElement("li");
            numbersListItem.textContent = i;
            numbersList.appendChild(numbersListItem);
            sum += i; // 합계를 계산
        }

        sumResult.textContent = `1부터 ${inputValue}까지의 합: ${sum}`;
    } else {
        alert("요소를 찾을 수 없습니다.");
    }
}
