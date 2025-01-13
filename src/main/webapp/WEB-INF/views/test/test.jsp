<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>test</title>
    <link rel="stylesheet" href="/test/test.css">
    <script src="/test/test.js"></script>
</head>

<body>
    <jsp:include page="/WEB-INF/views/test/testHeader.jsp" />

    <main>
        <!-- 날짜와 시간 -->
        <div>
            날짜, 시간:
            <button onclick="showDateTime()">날짜와 시간 보기</button>
            <!-- 날짜와 시간이 표시될 요소 -->
             <p id="currentDateTime"></p>
        </div>


        <!-- 1 부터 입력 숫자까지 합 -->
        <div> 1 부터 입력 숫자까지 합
            <input type="number" id="numberInput" placeholder="숫자를 입력하세요" onkeypress="triggerButtonClick(event)" />
            <button id="calculateButton" onclick="printNumbers()">합계 계산</button>
            <ul id="numbersList"></ul>
            <p id="sumResult"></p>
        </div>


    </main>

    <jsp:include page="/WEB-INF/views/test/testFooter.jsp" />
</body>
</html>
