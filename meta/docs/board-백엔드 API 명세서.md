# 백엔드 API 명세서

## 회원 기능

<details>
<summary><strong>[POST] 회원 가입</strong></summary>


| Description | 새로운 사용자가 서비스에 등록할 수 있는 기능 |
| --- | --- |
| URL | /signup |
| Auth Required |  |

| `요구 사항 ID` | `body params` | `요구 사항 상세` |
| --- | --- | --- |
| MEMBER\_001 | dto : { "id": "test1", "password": "test1", "userName": "홍길동", "nickName": "길동이", "email": " test1@test.com", "phoneNumber": "010-1234-5678" } file : profileImage | [ 기능 설명 ] 새로운 사용자가 서비스에 등록할 수 있는 기능 [ 필수 항목 ] 아이디, 비밀번호, 이름, 닉네임, 이메일, 전화번호 [ 선택 항목 ] 프로필 이미지 [ 제약 조건 ] 1. 아이디는 중복 불가 2. 이메일은 중복 불가 3. 비밀번호는 암호화하여 저장 4. 이메일 인증 과정 필요 [ 프로세스 ] 1. 사용자가 회원가입 양식 작성 2. 필수 항목 검증 3. 이메일 중복 확인 4. 아이디 중복 확인 5. 비밀번호 암호화 6. 프로필 이미지 업로드(선택) 7. 회원 정보 저장 8. 이메일 인증 링크 발송 9. 이메일 인증 완료 후 계정 활성화 |

**✅ 성공 응답**

```json
{
    "code": 1007,
    "success": true,
    "message": "이메일 인증을 완료해야 회원가입이 완료됩니다.",
    "result": null
}

{
    "code": 1008,
    "success": true,
    "message": "계정을 복구하려면 이메일을 확인해주세요.",
    "result": null
}
```

**❌ 실패 응답**

```json
{
  "code": 1046,
  "success": false,
  "message": "이미 등록된 이메일입니다.",
  "result": null
}

{
  "code": 1015,
  "success": false,
  "message": "회원 정보 업데이트에 실패했습니다.",
  "result": null
}

{
  "code": 1021,
  "success": false,
  "message": "이메일 인증을 생성할 수 없습니다.",
  "result": null
}

{
  "code": 1024,
  "success": false,
  "message": "이미 존재하는 계정입니다.",
  "result": null
}

{
  "code": 1014,
  "success": false,
  "message": "회원을 저장할 수 없습니다.",
  "result": null
}
```


</details>

<details>
<summary><strong>[GET] 이메일 인증</strong></summary>


| Description | 회원가입 이메일 및 복구 이메일 인증 |
| --- | --- |
| URL | /email-auth |
| Auth Required |  |

| `요구 사항 ID` | `body params` | `요구 사항 상세` |
| --- | --- | --- |
| MEMBER\_001 | ?id=test01&uuid=abcd1234&isInActive=false | [ 기능 설명 ] 비활성화된 계정을 복구하는 기능 [ 제약 조건 ] 이메일 인증을 통한 복구만 가능 [ 프로세스 ] 1. 사용자가 재 회원가입시 동일한 이메일 사용 2. 사용자가 이메일로 받은 복구 링크 클릭 3. 링크의 유효성 검증 4. 계정 활성화 처리 5. 로그인 페이지로 리다이렉트 |

**✅ 성공 응답**

```json
redirect:/login
```

**❌ 실패 응답**

```json
{
  "code": 1020,
  "success": false,
  "message": "이메일 인증을 찾을 수 없습니다.",
  "result": null
}

{
  "code": 1015,
  "success": false,
  "message": "회원 정보 업데이트에 실패했습니다.",
  "result": null
}

{
  "code": 1022,
  "success": false,
  "message": "이메일 인증을 지울 수 없습니다.",
  "result": null
}
```


</details>

<details>
<summary><strong>[POST] 로그인</strong></summary>


| Description | 등록된 사용자가 서비스에 접근할 수 있는 기능 |
| --- | --- |
| URL | /login |
| Auth Required |  |

| `요구 사항 ID` | `body params` | `요구 사항 상세` |
| --- | --- | --- |
| MEMBER\_002 | { "id": "test1", "password": "test1" } | [ 기능 설명 ] 등록된 사용자가 서비스에 접근할 수 있는 기능 [ 필수 항목 ] 아이디, 비밀번호 [ 제약 조건 ] 1. 이메일 인증이 완료된 계정만 로그인 가능 2. 비활성화된 계정은 로그인 불가 [ 프로세스 ] 1. 사용자가 아이디와 비밀번호 입력 2. 아이디 존재 여부 확인 3. 비밀번호 일치 여부 확인 4. 이메일 인증 여부 확인 5. 계정 활성화 여부 확인 6. 로그인 성공 시 세션에 사용자 정보 저장 7. 로그인 기록 저장 (IP 주소, 로그인 시간) |

**✅ 성공 응답**

```json
{
    "code": 1006,
    "success": true,
    "message": "로그인에 성공했습니다.",
    "result": null
}
```

**❌ 실패 응답**

```json
{
  "code": 1018,
  "success": false,
  "message": "존재하지 않는 계정 입니다.",
  "result": null
}

{
  "code": 1016,
  "success": false,
  "message": "비밀번호가 다릅니다.",
  "result": null
}

{
  "code": 1023,
  "success": false,
  "message": "탈퇴한 계정입니다.",
  "result": null
}

{
  "code": 1019,
  "success": false,
  "message": "이메일 인증을 해주세요",
  "result": null
}

{
  "code": 1017,
  "success": false,
  "message": "로그인 이력 저장에 실패했습니다.",
  "result": null
}
```


</details>

<details>
<summary><strong>[GET] 로그아웃</strong></summary>


| Description | 로그인된 사용자의 세션을 종료하는 기능 |
| --- | --- |
| URL | /logout |
| Auth Required | Yes |

| `요구 사항 ID` | `body params` | `요구 사항 상세` |
| --- | --- | --- |
| MEMBER\_003 |  | [ 기능 설명 ] 로그인된 사용자의 세션을 종료하는 기능 [ 프로세스 ] 1. 사용자가 로그아웃 요청 2. 세션에서 사용자 정보 제거 3. 로그인 페이지로 리다이렉트 |

**✅ 성공 응답**

```json
redirect:/login
```

**❌ 실패 응답**

```json
null
```


</details>

<details>
<summary><strong>[GET] 계정 비활성화</strong></summary>


| Description | 새로운 사용자가 서비스에 등록할 수 있는 기능 |
| --- | --- |
| URL | /in-active |
| Auth Required | Yes |

| `요구 사항 ID` | `body params` | `요구 사항 상세` |
| --- | --- | --- |
| MEMBER\_001 |  | [ 기능 설명 ] 로그인한 사용자가 자신의 계정을 비활성화하는 기능 [ 제약 조건 ] 로그인한 사용자만 접근 가능 [ 프로세스 ] 1. 사용자가 계정 비활성화 요청 2. 비활성화 확인 절차 3. 사용자 계정 비활성화 처리 4. 이메일로 계정 복구 링크 발송 5. 로그아웃 처리 |

**✅ 성공 응답**

```json
{
    "code": 1009,
    "success": true,
    "message": "계정 탈퇴에 성공했습니다.",
    "result": null
}
```

**❌ 실패 응답**

```json
{
  "code": 1018,
  "success": false,
  "message": "존재하지 않는 계정 입니다.",
  "result": null
}

{
  "code": 1025,
  "success": false,
  "message": "계정 소유자가 아닙니다.",
  "result": null
}

{
  "code": 1015,
  "success": false,
  "message": "회원 정보 업데이트에 실패했습니다.",
  "result": null
}
```


</details>

<details>
<summary><strong>[POST] 계정 정보 변경</strong></summary>


| Description | 로그인한 사용자가 자신의 계정 정보를 변경하는 기능 |
| --- | --- |
| URL | /edit-profile |
| Auth Required | Yes |

| `요구 사항 ID` | `body params` | `요구 사항 상세` |
| --- | --- | --- |
| MEMBER\_005 | dto: { "nickName": "변경할닉네임", "phoneNumber": "변경할전화번호" } file : profileImage | [ 기능 설명 ] 로그인한 사용자가 자신의 계정 정보를 변경하는 기능 [ 수정 가능 항목 ] 닉네임, 전화번호, 프로필 이미지 [ 제약 조건 ] 로그인한 사용자만 접근 가능 [ 프로세스 ] 1. 사용자가 프로필 수정 양식 접근 2. 현재 사용자 정보 조회 및 표시 3. 사용자가 정보 수정 4. 변경된 정보 유효성 검증 5. 프로필 이미지 업로드(선택) 6. 변경된 정보 저장 |

**✅ 성공 응답**

```json
{
    "code": 1010,
    "success": true,
    "message": "프로필 수정에 성공했습니다.",
    "result": null
}
```

**❌ 실패 응답**

```json
{
  "code": 1018,
  "success": false,
  "message": "존재하지 않는 계정 입니다.",
  "result": null
}

{
  "code": 1025,
  "success": false,
  "message": "계정 소유자가 아닙니다.",
  "result": null
}

{
  "code": 1015,
  "success": false,
  "message": "회원 정보 업데이트에 실패했습니다.",
  "result": null
}
```


</details>

<details>
<summary><strong>[POST] 비밀번호 변경</strong></summary>


| Description | 새로운 사용자가 서비스에 등록할 수 있는 기능 |
| --- | --- |
| URL | /edit-pw |
| Auth Required | Yes |

| `요구 사항 ID` | `body params` | `요구 사항 상세` |
| --- | --- | --- |
| MEMBER\_006 | { "oldPassword": "현재비밀번호", "newPassword": "새비밀번호" } | [ 기능 설명 ] 로그인한 사용자가 자신의 비밀번호를 변경하는 기능 [ 필수 항목 ] 기존 비밀번호, 새 비밀번호, 새 비밀번호 확인 [ 제약 조건 ] 1. 로그인한 사용자만 접근 가능 2. 기존 비밀번호 일치 여부 확인 3. 새 비밀번호와 새 비밀번호 확인 일치 여부 확인 [ 프로세스 ] 1. 사용자가 비밀번호 변경 양식 접근 2. 기존 비밀번호, 새 비밀번호, 새 비밀번호 확인 입력 3. 기존 비밀번호 일치 여부 확인 4. 새 비밀번호와 새 비밀번호 확인 일치 여부 확인 5. 새 비밀번호 암호화 6. 비밀번호 정보 갱신 |

**✅ 성공 응답**

```json
{
    "code": 1013,
    "success": true,
    "message": "비밀번호 변경에 성공했습니다.",
    "result": null
}
```

**❌ 실패 응답**

```json
{
  "code": 1018,
  "success": false,
  "message": "존재하지 않는 계정 입니다.",
  "result": null
}

{
  "code": 1016,
  "success": false,
  "message": "비밀번호가 다릅니다.",
  "result": null
}

{
  "code": 1015,
  "success": false,
  "message": "회원 정보 업데이트에 실패했습니다.",
  "result": null
}
```


</details>

<details>
<summary><strong>[POST] 계정 ID/PW찾기</strong></summary>


| Description | 새로운 사용자가 서비스에 등록할 수 있는 기능 |
| --- | --- |
| URL | /signup |
| Auth Required |  |

| `요구 사항 ID` | `body params` | `요구 사항 상세` |
| --- | --- | --- |
| MEMBER\_009 | { "email": "가입시등록한이메일" }  { "id": "가입시등록한아이디" } | [ 기능 설명 ] 아이디 또는 비밀번호를 잊은 사용자를 위한 기능 [ 필수 항목 ] 이메일, 이름 [ 프로세스 ] 1. 사용자가 아이디/비밀번호 찾기 양식 접근 2. 이메일과 이름 입력 3. 입력된 정보와 일치하는 계정 확인 4. 아이디 찾기: 이메일로 아이디 정보 발송 5. 비밀번호 찾기: 아이디 입력 후 이메일로 임시 비밀번호 또는 비밀번호 재설정 링크 발송 |

**✅ 성공 응답**

```json
{
  "code": 1012,
  "success": true,
  "message": "아이디를 이메일로 전송했습니다.",
  "result": null
}

{
  "code": 1011,
  "success": true,
  "message": "임시 비밀 번호를 이메일로 전송했습니다.",
  "result": null
}
```

**❌ 실패 응답**

```json
{
  "code": 1018,
  "success": false,
  "message": "존재하지 않는 계정 입니다.",
  "result": null
}

{
  "code": 1015,
  "success": false,
  "message": "회원 정보 업데이트에 실패했습니다.",
  "result": null
}

{
  "code": 1002,
  "success": false,
  "message": "요청값이 정상적이지 않습니다.",
  "result": null
}
```


</details>

## 게시글 기능

<details>
<summary><strong>[POST] 게시글 등록</strong></summary>


| Description | 로그인한 사용자가 새로운 게시물을 작성하는 기능 |
| --- | --- |
| URL | /post |
| Auth Required | Yes |

| `요구 사항 ID` | `body params` | `요구 사항 상세` |
| --- | --- | --- |
| POST\_001 | { "dto": { "title": "게시물 제목", "content": "게시물 내용", "categoryIdx": 1, "hasPassword": false, "password": null }, "file": [첨부파일1, 첨부파일2, ...] } | [ 기능 설명 ] 로그인한 사용자가 새로운 게시물을 작성하는 기능 [ 필수 항목 ] 제목, 내용, 카테고리, 공개 범위 [ 선택 항목 ] 이미지 파일 [ 제약 조건 ] 로그인한 사용자만 접근 가능 [ 프로세스 ] 1. 사용자가 게시물 작성 페이지 접근 2. 제목, 내용, 카테고리, 공개 범위 설정 3. 이미지 파일 업로드(선택) 4. 유효성 검증, 게시물 저장 6. 게시물 목록 또는 상세 페이지로 리다이렉트 |

**✅ 성공 응답**

```json
{
  "code": 1028,
  "success": true,
  "message": "게시물이 생성되었습니다.",
  "result": null
}
```

**❌ 실패 응답**

```json
{
  "code": 1029,
  "success": false,
  "message": "게시물 생성에 실패했습니다.",
  "result": null
}
```


</details>

<details>
<summary><strong>[PUT] 게시글 수정</strong></summary>


| Description | 작성자가 자신의 게시물을 수정하는 기능 |
| --- | --- |
| URL | /post?postIdx=1 |
| Auth Required | Yes |

| `요구 사항 ID` | `body params` | `요구 사항 상세` |
| --- | --- | --- |
| POST\_004 | "dto": { "title": "수정된 제목", "content": "수정된 내용", "categoryIdx": 2, "hasPassword": false, "password": null }, "file": [첨부파일1, 첨부파일2, ...] } | [ 기능 설명 ] 작성자가 자신의 게시물을 수정하는 기능 [ 수정 가능 항목 ] 제목, 내용, 카테고리, 공개 범위, 이미지 [ 제약 조건 ] 게시물 작성자만 수정 가능 [ 프로세스 ] 1. 사용자가 게시물 수정 페이지 접근 2. 게시물 작성자 확인 3. 기존 게시물 정보 조회 및 표시 4. 사용자가 정보 수정 5. 이미지 파일 업로드(선택) 6. 유효성 검증 7. 수정된 게시물 저장 8. 게시물 상세 페이지로 리다이렉트 |

**✅ 성공 응답**

```json
{
  "code": 1030,
  "success": true,
  "message": "게시물이 수정되었습니다.",
  "result": null
}
```

**❌ 실패 응답**

```json
{
  "code": 1031,
  "success": false,
  "message": "게시물 수정에 실패했습니다.",
  "result": null
}

{
  "code": 1037,
  "success": false,
  "message": "게시물 작성자가 아닙니다.",
  "result": null
}


{
  "code": 1036,
  "success": false,
  "message": "게시물을 찾을 수 없습니다.",
  "result": null
}
```


</details>

<details>
<summary><strong>[DELETE] 게시글 삭제</strong></summary>


| Description | 작성자가 자신의 게시물을 삭제하는 기능 |
| --- | --- |
| URL | /post?postIdx=1 |
| Auth Required | Yes |

| `요구 사항 ID` | `body params` | `요구 사항 상세` |
| --- | --- | --- |
| POST\_005 |  | [ 기능 설명 ] 작성자가 자신의 게시물을 삭제하는 기능 [ 제약 조건 ] 게시물 작성자만 삭제 가능 [ 프로세스 ] 1. 사용자가 게시물 삭제 요청 2. 게시물 작성자 확인 3. 삭제 확인 절차 4. 게시물과 연관된 모든 정보(이미지, 댓글, 좋아요/싫어요) 삭제 5. 게시물 목록 페이지로 리다이렉트 |

**✅ 성공 응답**

```json
{
  "code": 1032,
  "success": true,
  "message": "게시물이 삭제되었습니다.",
  "result": null
}
```

**❌ 실패 응답**

```json
{
  "code": 1033,
  "success": false,
  "message": "게시물 삭제에 실패했습니다.",
  "result": null
}

{
  "code": 1037,
  "success": false,
  "message": "게시물 작성자가 아닙니다.",
  "result": null
}

{
  "code": 1036,
  "success": false,
  "message": "게시물을 찾을 수 없습니다.",
  "result": null
}
```


</details>

<details>
<summary><strong>[GET] 게시글 조회</strong></summary>


| Description | 로그인된 사용자의 세션을 종료하는 기능 |
| --- | --- |
| URL | /post?postIdx=1 |
| Auth Required | optional |

| `요구 사항 ID` | `body params` | `요구 사항 상세` |
| --- | --- | --- |
| POST\_002 |  | [ 기능 설명 ] 게시물 상세 내용을 조회하는 기능 [ 조회 항목 ] 제목, 내용, 작성자, 작성일, 수정일, 조회수, 좋아요/싫어요, 이미지, 댓글 [ 제약 조건 ] 1. 비공개 게시물은 작성자만 조회 가능 2. 보호된 게시물은 비밀번호 입력 후 조회 가능 [ 프로세스 ] 1. 사용자가 게시물 상세 페이지 접근 2. 게시물 ID로 데이터 조회 3. 공개 범위 확인 4. 비공개/보호 게시물 권한 확인 5. 조회수 증가 6. 게시물 정보 표시, 관련 이미지 표시 7. 댓글 보기 버튼 클릭시 댓글 목록 표시 |

**✅ 성공 응답**

```json
{
  "code": 1034,
  "success": true,
  "message": "게시물 조회에 성공했습니다.",
  "result": {
    "postIdx": 1,
    "title": "게시물 제목",
    "content": "게시물 내용",
    "viewCount": 10,
    "likeCount": 5,
    "unlikeCount": 1,
    "categoryIdx": 1,
    "categoryName": "카테고리명",
    "hasPassword": false,
    "memberIdx": 1,
    "nickName": "작성자닉네임",
    "createdAt": "2023-01-01T12:00:00",
    "imageList": ["이미지1.jpg", "이미지2.jpg"],
    "isLike": false,
    "isUnlike": false
  }
}

{
  "code": 1034,
  "success": true,
  "message": "게시물 조회에 성공했습니다.",
  "result": {
    "postIdx": 1,
    "title": "게시물 제목",
    "content": "게시물 내용",
    "viewCount": 10,
    "likeCount": 5,
    "unlikeCount": 1,
    "categoryIdx": 1,
    "categoryName": "카테고리명",
    "hasPassword": false,
    "memberIdx": 2,
    "nickName": "작성자닉네임",
    "createdAt": "2023-01-01T12:00:00",
    "imageList": ["이미지1.jpg", "이미지2.jpg"],
    "isLike": true,
    "isUnlike": false
  }
}
```

**❌ 실패 응답**

```json
{
  "code": 1036,
  "success": false,
  "message": "게시물을 찾을 수 없습니다.",
  "result": null
}

{
  "code": 1059,
  "success": false,
  "message": "접근 권한이 없는 게시물입니다.",
  "result": null
}
```


</details>

<details>
<summary><strong>[GET] 게시글 목록 조회</strong></summary>


| Description | 다양한 조건으로 게시물 목록을 조회하는 기능 |
| --- | --- |
| URL | /post?categoryIdx=1&page=1&size=10&searchType=title&searchKeyword=검색어 |
| Auth Required |  |

| `요구 사항 ID` | `body params` | `요구 사항 상세` |
| --- | --- | --- |
| POST\_003 |  | [ 기능 설명 ] 다양한 조건으로 게시물 목록을 조회하는 기능 [ 조회 조건 ] 1. 카테고리 2. 검색어(제목, 작성자, 내용) 3. 정렬 기준(최신순, 오래된순, 조회수순, 좋아요순, 싫어요순, 댓글수순) 4. 페이지네이션: 페이지당 고정된 수의 게시물 표시 [ 프로세스 ] 1. 사용자가 게시물 목록 페이지 접근 2. 카테고리, 검색어, 정렬 기준 설정 3. 조건에 맞는 게시물 목록 조회 4. 페이지네이션 처리 5. 게시물 목록 표시 |

**✅ 성공 응답**

```json
{
  "code": 1035,
  "success": true,
  "message": "게시물 목록 조회에 성공했습니다.",
  "result": {
    "posts": [
      {
        "postIdx": 1,
        "title": "게시물 제목1",
        "viewCount": 10,
        "likeCount": 5,
        "unlikeCount": 1,
        "categoryName": "카테고리명",
        "hasPassword": false,
        "nickName": "작성자1",
        "createdAt": "2023-01-01T12:00:00"
      },
      {
        "postIdx": 2,
        "title": "게시물 제목2",
        "viewCount": 15,
        "likeCount": 7,
        "unlikeCount": 2,
        "categoryName": "카테고리명",
        "hasPassword": true,
        "nickName": "작성자2",
        "createdAt": "2023-01-02T12:00:00"
      }
    ],
    "totalCount": 100,
    "totalPages": 10,
    "currentPage": 1
  }
}
```

**❌ 실패 응답**

*??? ?? ??? ????.*


</details>

<details>
<summary><strong>[GET] 보호된 게시물 인증</strong></summary>


| Description | 다양한 조건으로 게시물 목록을 조회하는 기능 |
| --- | --- |
| URL | /post?postIdx=1&password=1234 |
| Auth Required |  |

| `요구 사항 ID` | `body params` | `요구 사항 상세` |
| --- | --- | --- |
| POST\_007 |  | [ 기능 설명 ] 비밀번호로 보호된 게시물에 접근하기 위한 기능 [ 필수 항목 ] 게시물 비밀번호 [ 프로세스 ] 1. 사용자가 보호된 게시물 접근 시도 2. 비밀번호 입력 요청 3. 비밀번호 일치 여부 확인 4. 비밀번호 일치 시 게시물 내용 표시 |

**✅ 성공 응답**

```json
{
  "code": 1062,
  "success": true,
  "message": "비밀번호 인증이 완료되었습니다.",
  "result": true
}
```

**❌ 실패 응답**

```json
{
  "code": 1036,
  "success": false,
  "message": "게시물을 찾을 수 없습니다.",
  "result": false
}

{
  "code": 1060,
  "success": false,
  "message": "보호된 게시물이 아닙니다.",
  "result": null
}

{
  "code": 1061,
  "success": false,
  "message": "게시물 비밀번호가 설정되지 않았습니다.",
  "result": null
}
```


</details>

<details>
<summary><strong>[GET] Summernote 이미지 업로드</strong></summary>


| Description | 다양한 조건으로 게시물 목록을 조회하는 기능 |
| --- | --- |
| URL | /post?postIdx=1&password=1234 |
| Auth Required |  |

| `요구 사항 ID` | `body params` | `요구 사항 상세` |
| --- | --- | --- |
| POST\_006 | file: [이미지파일] | [ 기능 설명 ] 게시물 작성/수정 시 이미지를 업로드하는 기능 [ 제약 조건 ] 1. 지원 파일 형식: 일반적인 이미지 포맷(JPG, PNG, GIF 등) 2. 최대 파일 크기 제한, 최대 업로드 가능 이미지 수 제한 [ 프로세스 ] 1. 사용자가 이미지 파일 선택 (Summernote) 2. 파일 형식 및 크기 검증 3. 이미지 파일 서버에 업로드 4. 업로드된 이미지의 URL 반환 5. 게시물과 이미지 연결 정보 저장 |

**✅ 성공 응답**

```json
{
  "code": 1045,
  "success": true,
  "message": "이미지가 업로드되었습니다.",
  "result": "업로드된_이미지_파일명.jpg"
}
```

**❌ 실패 응답**

*??? ?? ??? ????.*


</details>

## 댓글 기능

<details>
<summary><strong>[GET] 댓글 목록 조회</strong></summary>


| Description | 게시물에 달린 댓글을 조회하는 기능 |
| --- | --- |
| URL | /comment-list?postIdx=1&page=1&size=10 |
| Auth Required | optional |

| `요구 사항 ID` | `body params` | `요구 사항 상세` |
| --- | --- | --- |
| COMMENT\_002 |  | [ 기능 설명 ] 게시물에 달린 댓글을 조회하는 기능 [ 조회 항목 ] 내용, 작성자, 작성일, 수정일, 좋아요/싫어요 [ 조회 조건 ] 게시물 식별자 [ 정렬 기준 ] 최신순, 오래된순 [ 프로세스 ] 1. 게시물 상세 페이지 접근 2. 게시물 식별자로 댓글 목록 조회 3. 댓글 목록 표시 |

**✅ 성공 응답**

```json
{
  "code": 1055,
  "success": true,
  "message": "댓글 목록 조회에 성공했습니다.",
  "result": {
    "comments": [
      {
        "commentIdx": 1,
        "content": "댓글 내용 1",
        "memberIdx": 1,
        "nickName": "작성자1",
        "anonymousName": null,
        "createdAt": "2023-01-01T12:00:00",
        "updatedAt": "2023-01-01T12:00:00",
        "likeCount": 5,
        "unlikeCount": 1,
        "isLike": false,
        "isUnlike": false,
        "hasReplies": true,
        "isMine": false
      },
      {
        "commentIdx": 2,
        "content": "댓글 내용 2",
        "memberIdx": null,
        "nickName": null,
        "anonymousName": "익명",
        "createdAt": "2023-01-02T12:00:00",
        "updatedAt": "2023-01-02T12:00:00",
        "likeCount": 3,
        "unlikeCount": 0,
        "isLike": false,
        "isUnlike": false,
        "hasReplies": false,
        "isMine": false
      }
    ],
    "totalCount": 50,
    "totalPages": 5,
    "currentPage": 1
  }
}
```

**❌ 실패 응답**

*??? ?? ??? ????.*


</details>

<details>
<summary><strong>[GET] 대댓글 조회</strong></summary>


| Description | 특정 댓글에 대한 대댓글을 조회하는 기능 |
| --- | --- |
| URL | /reply-list?commentIdx=1&page=1&size=10 |
| Auth Required | optional |

| `요구 사항 ID` | `body params` | `요구 사항 상세` |
| --- | --- | --- |
| COMMENT\_003 |  | [ 기능 설명 ] 특정 댓글에 대한 대댓글을 조회하는 기능 [ 조회 항목 ] 내용, 작성자, 작성일, 수정일, 좋아요/싫어요 [ 조회 조건 ] 부모 댓글 식별자 [ 정렬 기준 ] 최신순, 오래된순 [ 프로세스 ] 1. 댓글 목록에서 대댓글 표시 요청 2. 부모 댓글 식별자로 대댓글 목록 조회 3. 대댓글 목록 표시 |

**✅ 성공 응답**

```json
{
  "code": 1058,
  "success": true,
  "message": "대댓글 목록 조회에 성공했습니다.",
  "result": {
    "replies": [
      {
        "commentIdx": 3,
        "content": "대댓글 내용 1",
        "memberIdx": 1,
        "nickName": "작성자1",
        "anonymousName": null,
        "createdAt": "2023-01-01T12:10:00",
        "updatedAt": "2023-01-01T12:10:00",
        "likeCount": 2,
        "unlikeCount": 0,
        "isLike": false,
        "isUnlike": false,
        "isMine": false
      },
      {
        "commentIdx": 4,
        "content": "대댓글 내용 2",
        "memberIdx": null,
        "nickName": null,
        "anonymousName": "익명",
        "createdAt": "2023-01-01T12:15:00",
        "updatedAt": "2023-01-01T12:15:00",
        "likeCount": 1,
        "unlikeCount": 0,
        "isLike": false,
        "isUnlike": false,
        "isMine": false
      }
    ],
    "totalCount": 15,
    "totalPages": 2,
    "currentPage": 1
  }
}
```

**❌ 실패 응답**

*??? ?? ??? ????.*


</details>

<details>
<summary><strong>[POST] 댓글 등록</strong></summary>


| Description | 게시물에 대한 댓글을 작성하는 기능 |
| --- | --- |
| URL | /comment |
| Auth Required | Yes |

| `요구 사항 ID` | `body params` | `요구 사항 상세` |
| --- | --- | --- |
| COMMENT\_001 | { "postIdx": 1, "parentCommentIdx": null, "content": "댓글 내용", "anonymousName": "익명이름", "anonymousPassword": "익명비밀번호" } | [ 기능 설명 ] 게시물에 대한 댓글을 작성하는 기능 [ 필수 항목 ] 댓글 내용 [ 선택 항목 ] 부모 댓글 식별자(대댓글의 경우) [ 제약 조건 ] 로그인한 사용자만 접근 가능 [ 프로세스 ] 1. 사용자가 댓글 작성 양식 접근 2. 댓글 내용 입력 3. 유효성 검증 4. 댓글 저장 5. 댓글 목록 갱신 |

**✅ 성공 응답**

```json
{
  "code": 1047,
  "success": true,
  "message": "댓글이 생성되었습니다.",
  "result": null
}
```

**❌ 실패 응답**

```json
{
  "code": 1048,
  "success": false,
  "message": "댓글 작성에 실패했습니다. 로그인 후 작성해주세요.",
  "result": null
}

{
  "code": 1049,
  "success": false,
  "message": "댓글 생성에 실패했습니다.",
  "result": null
}

{
  "code": 1057,
  "success": false,
  "message": "대댓글은 댓글에 달 수 없습니다.",
  "result": null
}
```


</details>

<details>
<summary><strong>[PUT] 댓글 수정</strong></summary>


| Description | 작성자가 자신의 댓글을 수정하는 기능 |
| --- | --- |
| URL | /comment?commentIdx=1 |
| Auth Required | Yes |

| `요구 사항 ID` | `body params` | `요구 사항 상세` |
| --- | --- | --- |
| COMMENT\_004 | { "content": "수정된 댓글 내용" } | [ 기능 설명 ] 작성자가 자신의 댓글을 수정하는 기능 [ 수정 가능 항목 ] 댓글 내용 [ 제약 조건 ] 댓글 작성자만 수정 가능 [ 프로세스 ] 1. 사용자가 댓글 수정 요청 2. 댓글 작성자 확인 3. 댓글 수정 양식 표시 4. 수정된 내용 입, 유효성 검증 5. 수정된 댓글 저장 6. 댓글 목록 갱신 |

**✅ 성공 응답**

```json
{
  "code": 1050,
  "success": true,
  "message": "댓글이 수정되었습니다.",
  "result": null
}
```

**❌ 실패 응답**

```json
{
  "code": 1051,
  "success": false,
  "message": "댓글 수정에 실패했습니다.",
  "result": null
}

{
  "code": 1054,
  "success": false,
  "message": "존재하지 않는 댓글입니다.",
  "result": null
}

{
  "code": 1056,
  "success": false,
  "message": "댓글 작성자가 아닙니다.",
  "result": null
}
```


</details>

<details>
<summary><strong>[DELETE] 댓글 삭제</strong></summary>


| Description | 작성자가 자신의 댓글을 삭제하는 기능 |
| --- | --- |
| URL | /comment?commentIdx=1 |
| Auth Required | Yes |

| `요구 사항 ID` | `body params` | `요구 사항 상세` |
| --- | --- | --- |
| COMMENT\_005 |  | [ 기능 설명 ] 작성자가 자신의 댓글을 삭제하는 기능 [ 제약 조건 ] 댓글 작성자만 삭제 가능 [ 프로세스 ] 1. 사용자가 댓글 삭제 요청 2. 댓글 작성자 확인 3. 삭제 확인 절차 4. 댓글과 연관된 정보(좋아요/싫어요) 삭제 5. 대댓글이 있는 경우 해당 대댓글도 모두 삭제 6. 댓글 목록 갱신 |

**✅ 성공 응답**

```json
{
  "code": 1052,
  "success": true,
  "message": "댓글이 삭제되었습니다.",
  "result": null
}
```

**❌ 실패 응답**

```json
{
  "code": 1053,
  "success": false,
  "message": "댓글 삭제에 실패했습니다.",
  "result": null
}

{
  "code": 1054,
  "success": false,
  "message": "존재하지 않는 댓글입니다.",
  "result": null
}

{
  "code": 1056,
  "success": false,
  "message": "댓글 작성자가 아닙니다.",
  "result": null
}
```


</details>

## 랭킹 기능

<details>
<summary><strong>[GET] 랭킹 조회</strong></summary>


| Description | 다양한 기준으로 인기 게시물 랭킹을 조회하는 기능 |
| --- | --- |
| URL | /rank-list |
| Auth Required |  |

| `요구 사항 ID` | `body params` | `요구 사항 상세` |
| --- | --- | --- |
| RANK\_001, 002 |  | [ 기능 설명 ] 다양한 기준으로 인기 게시물 랭킹을 조회하는 기능 [ 랭킹 기준 ] 조회수, 좋아요수, 싫어요수, 댓글수 기준 랭킹 [ 갱신 주기 ] 배치 작업으로 주기적 갱신 (1시간마다) [ 조회 항목 ] 순위, 제목, 작성자, 해당 기준 수치(조회수, 좋아요수 등) [ 프로세스 ] 1. 스프링 배치 랭킹 갱신 프로세스 실행 2. 각 기준별 상위 게시물 조회 3. SortedSet을 이용해 100청크마다 처리 4. 랭킹 테이블 업데이트 5. 랭킹 정보 표시 |

**✅ 성공 응답**

```json
{
  "code": 1063,
  "success": true,
  "message": "랭크 조회에 성공했습니다.",
  "result": {
    "viewRank": [
      {
        "postIdx": 1,
        "title": "조회수 1위 게시물",
        "viewCount": 1000,
        "likeCount": 50,
        "unlikeCount": 10,
        "commentCount": 30,
        "categoryName": "카테고리명",
        "nickName": "작성자1",
        "createdAt": "2023-01-01T12:00:00"
      },
      {
        "postIdx": 2,
        "title": "조회수 2위 게시물",
        "viewCount": 800,
        "likeCount": 40,
        "unlikeCount": 5,
        "commentCount": 25,
        "categoryName": "카테고리명",
        "nickName": "작성자2",
        "createdAt": "2023-01-02T12:00:00"
      }
      // 추가 데이터...
    ],
    "likeRank": [
      {
        "postIdx": 3,
        "title": "좋아요 1위 게시물",
        "viewCount": 500,
        "likeCount": 100,
        "unlikeCount": 15,
        "commentCount": 40,
        "categoryName": "카테고리명",
        "nickName": "작성자3",
        "createdAt": "2023-01-03T12:00:00"
      }
      // 추가 데이터...
    ],
    "unlikeRank": [
      {
        "postIdx": 4,
        "title": "싫어요 1위 게시물",
        "viewCount": 300,
        "likeCount": 20,
        "unlikeCount": 50,
        "commentCount": 60,
        "categoryName": "카테고리명",
        "nickName": "작성자4",
        "createdAt": "2023-01-04T12:00:00"
      }
      // 추가 데이터...
    ],
    "commentRank": [
      {
        "postIdx": 5,
        "title": "댓글 1위 게시물",
        "viewCount": 600,
        "likeCount": 70,
        "unlikeCount": 25,
        "commentCount": 120,
        "categoryName": "카테고리명",
        "nickName": "작성자5",
        "createdAt": "2023-01-05T12:00:00"
      }
      // 추가 데이터...
    ]
  }
}
```

**❌ 실패 응답**

*??? ?? ??? ????.*


</details>

## 활동 기능

<details>
<summary><strong>[GET] 내가 쓴 게시물 조회</strong></summary>


| Description | 내가 쓴 게시물 조회 |
| --- | --- |
| URL | /activity-posts |
| Auth Required | yes |

| `요구 사항 ID` | `body params` | `요구 사항 상세` |
| --- | --- | --- |
| ACTIVITY\_001 |  | [ 기능 설명 ] 사용자의 활동을 기록하고 관리하는 기능 [ 제약 조건 ] 멤버 인덱스를 통해 사용자만 접근 가능 [ 프로세스 ] 1. 내가 쓴 게시물 조회 2. 내가 쓴 댓글 조회 3. 내가 누른 좋아요 조회 4. 내가 누른 싫어요 조회 5. 로그인 기록 조회 |

**✅ 성공 응답**

```json
{
  "code": 1065,
  "success": true,
  "message": "활동 게시물 목록 조회에 성공했습니다.",
  "result": [
    {
      "postIdx": 1,
      "title": "게시물 제목1",
      "content": "게시물 내용1",
      "categoryName": "카테고리명",
      "viewCount": 100,
      "likeCount": 10,
      "unlikeCount": 2,
      "commentCount": 15,
      "createdAt": "2023-01-01T12:00:00"
    },
    {
      "postIdx": 2,
      "title": "게시물 제목2",
      "content": "게시물 내용2",
      "categoryName": "카테고리명",
      "viewCount": 50,
      "likeCount": 5,
      "unlikeCount": 1,
      "commentCount": 8,
      "createdAt": "2023-01-02T12:00:00"
    }
  ]
}
```

**❌ 실패 응답**

*??? ?? ??? ????.*


</details>

<details>
<summary><strong>[GET] 내가 쓴 댓글 조회</strong></summary>


| Description | 내가 쓴 댓글 조회 |
| --- | --- |
| URL | /activity-comments |
| Auth Required | yes |

| `요구 사항 ID` | `body params` | `요구 사항 상세` |
| --- | --- | --- |
| ACTIVITY\_001 |  | [ 기능 설명 ] 사용자의 활동을 기록하고 관리하는 기능 [ 제약 조건 ] 멤버 인덱스를 통해 사용자만 접근 가능 [ 프로세스 ] 1. 내가 쓴 게시물 조회 2. 내가 쓴 댓글 조회 3. 내가 누른 좋아요 조회 4. 내가 누른 싫어요 조회 5. 로그인 기록 조회 |

**✅ 성공 응답**

```json
{
  "code": 1066,
  "success": true,
  "message": "활동 댓글 목록 조회에 성공했습니다.",
  "result": [
    {
      "commentIdx": 1,
      "content": "댓글 내용1",
      "postIdx": 1,
      "postTitle": "게시물 제목1",
      "likeCount": 5,
      "unlikeCount": 1,
      "createdAt": "2023-01-01T12:10:00"
    },
    {
      "commentIdx": 2,
      "content": "댓글 내용2",
      "postIdx": 2,
      "postTitle": "게시물 제목2",
      "likeCount": 3,
      "unlikeCount": 0,
      "createdAt": "2023-01-02T12:15:00"
    }
  ]
}
```

**❌ 실패 응답**

*??? ?? ??? ????.*


</details>

<details>
<summary><strong>[GET] 내가 누른 좋아요 조회</strong></summary>


| Description | 내가 누른 좋아요 조회 |
| --- | --- |
| URL | /activity-likes |
| Auth Required | yes |

| `요구 사항 ID` | `body params` | `요구 사항 상세` |
| --- | --- | --- |
| ACTIVITY\_001 |  | [ 기능 설명 ] 사용자의 활동을 기록하고 관리하는 기능 [ 제약 조건 ] 멤버 인덱스를 통해 사용자만 접근 가능 [ 프로세스 ] 1. 내가 쓴 게시물 조회 2. 내가 쓴 댓글 조회 3. 내가 누른 좋아요 조회 4. 내가 누른 싫어요 조회 5. 로그인 기록 조회 |

**✅ 성공 응답**

```json
{
  "code": 1067,
  "success": true,
  "message": "활동 좋아요 목록 조회에 성공했습니다.",
  "result": [
    {
      "targetIdx": 1,
      "targetType": "POST",
      "title": "게시물 제목1",
      "content": "게시물 내용1",
      "likeCount": 15,
      "unlikeCount": 3,
      "createdAt": "2023-01-01T12:20:00"
    },
    {
      "targetIdx": 1,
      "targetType": "COMMENT",
      "title": "댓글 내용1",
      "content": "게시물 제목1에 대한 댓글",
      "likeCount": 8,
      "unlikeCount": 1,
      "createdAt": "2023-01-01T12:25:00"
    }
  ]
}
```

**❌ 실패 응답**

*??? ?? ??? ????.*


</details>

<details>
<summary><strong>[GET] 내가 누른 싫어요 조회</strong></summary>


| Description | 내가 누른 싫어요 조회 |
| --- | --- |
| URL | /activity-unlikes |
| Auth Required | yes |

| `요구 사항 ID` | `body params` | `요구 사항 상세` |
| --- | --- | --- |
| ACTIVITY\_001 |  | [ 기능 설명 ] 사용자의 활동을 기록하고 관리하는 기능 [ 제약 조건 ] 멤버 인덱스를 통해 사용자만 접근 가능 [ 프로세스 ] 1. 내가 쓴 게시물 조회 2. 내가 쓴 댓글 조회 3. 내가 누른 좋아요 조회 4. 내가 누른 싫어요 조회 5. 로그인 기록 조회 |

**✅ 성공 응답**

```json
{
  "code": 1068,
  "success": true,
  "message": "활동 싫어요 목록 조회에 성공했습니다.",
  "result": [
    {
      "targetIdx": 2,
      "targetType": "POST",
      "title": "게시물 제목2",
      "content": "게시물 내용2",
      "likeCount": 5,
      "unlikeCount": 12,
      "createdAt": "2023-01-02T12:30:00"
    },
    {
      "targetIdx": 2,
      "targetType": "COMMENT",
      "title": "댓글 내용2",
      "content": "게시물 제목2에 대한 댓글",
      "likeCount": 2,
      "unlikeCount": 6,
      "createdAt": "2023-01-02T12:35:00"
    }
  ]
}
```

**❌ 실패 응답**

*??? ?? ??? ????.*


</details>

<details>
<summary><strong>[GET] 로그인 기록 조회</strong></summary>


| Description | 로그인 기록 조회 |
| --- | --- |
| URL | /activity-history |
| Auth Required | yes |

| `요구 사항 ID` | `body params` | `요구 사항 상세` |
| --- | --- | --- |
| ACTIVITY\_001 |  | [ 기능 설명 ] 사용자의 활동을 기록하고 관리하는 기능 [ 제약 조건 ] 멤버 인덱스를 통해 사용자만 접근 가능 [ 프로세스 ] 1. 내가 쓴 게시물 조회 2. 내가 쓴 댓글 조회 3. 내가 누른 좋아요 조회 4. 내가 누른 싫어요 조회 5. 로그인 기록 조회 |

**✅ 성공 응답**

```json
{
  "code": 1069,
  "success": true,
  "message": "활동 히스토리 목록 조회에 성공했습니다.",
  "result": [
    {
      "idx": 1,
      "id": "사용자아이디",
      "ipAddress": "127.0.0.1",
      "createdAt": "2023-01-01T12:00:00"
    },
    {
      "idx": 2,
      "id": "사용자아이디",
      "ipAddress": "192.168.0.1",
      "createdAt": "2023-01-02T12:00:00"
    }
  ]
}
```

**❌ 실패 응답**

*??? ?? ??? ????.*


</details>

## 반응 기능

<details>
<summary><strong>[GET] 좋아요 증가/감소</strong></summary>


| Description | 게시물이나 댓글에 좋아요를 표시하는 기능 |
| --- | --- |
| URL | /react-like?postIdx=1 or /react-like?commentIdx=1 |
| Auth Required | Yes |

| `요구 사항 ID` | `body params` | `요구 사항 상세` |
| --- | --- | --- |
| REACT\_001 |  | [ 기능 설명 ] 게시물이나 댓글에 좋아요를 표시하는 기능 [ 제약 조건 ] 1. 로그인한 사용자만 접근 가능 2. 동일 사용자가 같은 대상에 중복 좋아요 불가 3. 이미 좋아요한 대상에 다시 좋아요 시 취소 처리 4. 이미 싫어요한 대상에 좋아요 시 싫어요 취소 후 좋아요 처리 [ 프로세스 ] 1. 사용자가 좋아요 버튼 클릭 2. 이미 좋아요한 경우 취소 처리 3. 이미 싫어요한 경우 싫어요 취소 후 좋아요 처리 4. 좋아요 정보 저장, 좋아요 카운트 갱신 |

**✅ 성공 응답**

```json
{
  "code": 1040,
  "success": true,
  "message": "좋아요가 증가되었습니다.",
  "result": null
}

{
  "code": 1041,
  "success": true,
  "message": "좋아요가 감소되었습니다.",
  "result": null
}
```

**❌ 실패 응답**

```json
{
  "code": 1042,
  "success": false,
  "message": "좋아요 증가에 실패했습니다.",
  "result": null
}

{
  "code": 1042,
  "success": false,
  "message": "좋아요 감소에 실패했습니다.",
  "result": null
}
```


</details>

<details>
<summary><strong>[GET] 좋아요 증가/감소</strong></summary>


| Description | 게시물이나 댓글에 싫어요를 표시하는 기능 |
| --- | --- |
| URL | /react-unlike?postIdx=1 or /react-like?commentIdx=1 |
| Auth Required | Yes |

| `요구 사항 ID` | `body params` | `요구 사항 상세` |
| --- | --- | --- |
| REACT\_002 |  | [ 기능 설명 ] 게시물이나 댓글에 싫어요를 표시하는 기능 [ 제약 조건 ] 1. 로그인한 사용자만 접근 가능 2. 동일 사용자가 같은 대상에 중복 싫어요 불가 3. 이미 싫어요한 대상에 다시 싫어요 시 취소 처리 4. 이미 좋아요한 대상에 싫어요 시 좋아요 취소 후 싫어요 처리 [ 프로세스 ] 1. 사용자가 싫어요 버튼 클릭 2. 이미 싫어요한 경우 취소 처리 3. 이미 좋아요한 경우 좋아요 취소 후 싫어요 처리 4. 싫어요 정보 저장, 싫어요 카운트 갱신 |

**✅ 성공 응답**

```json
{
  "code": 1043,
  "success": true,
  "message": "싫어요가 증가되었습니다.",
  "result": null
}

{
  "code": 1044,
  "success": true,
  "message": "싫어요가 감소되었습니다.",
  "result": null
}
```

**❌ 실패 응답**

```json
{
  "code": 1042,
  "success": false,
  "message": "싫어요 증가에 실패했습니다.",
  "result": null
}

{
  "code": 1042,
  "success": false,
  "message": "싫어요 감소에 실패했습니다.",
  "result": null
}
```

</details>
