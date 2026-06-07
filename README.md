# KBT4_gourmet_Week4

## 1. 과제 개요

이번 과제는 3주차에 작성했던 REST API 설계를 기반으로 실제 Spring Boot API를 구현하고, Postman을 사용하여 API 요청과 응답을 테스트하는 과제입니다.

과제 요구사항에서 DB가 없다는 것을 전제로 구현하도록 안내되어 있었기 때문에, 이번 프로젝트에서는 MySQL이나 JPA를 사용하지 않고 Java 코드 내부의 `MemoryStore`를 임시 저장소로 사용했습니다.

Controller에서 요청을 받고, DTO를 통해 Request / Response 데이터를 분리하여 JSON 형태로 응답하도록 구현했습니다.

또한 Spring Security를 사용하지 않는 조건에 맞춰 인증/인가 처리는 구현하지 않았고, 회원가입과 로그인은 평문 데이터 기반으로 동작하도록 구성했습니다.

---

## 2. 구현 목표

* 3주차 REST API 설계를 기반으로 API 개발
* Postman을 사용한 API 요청/응답 테스트
* User, Post, Comment 도메인의 CRUD 구현
* DB 없이 Java 코드 기반 임시 저장소 사용
* DTO를 활용한 Request / Response 분리
* Spring Security 미사용
* 회원가입, 로그인은 평문 데이터로 처리

---

## 3. 프로젝트 구조

```text
src/main/java/KTB4_gourmet_Week4/Assignment
├─ controller
│  ├─ UserController
│  ├─ PostController
│  └─ CommentController
│
├─ dto
│  ├─ UserRequestDto
│  ├─ LoginRequestDto
│  ├─ UserResponseDto
│  ├─ PostRequestDto
│  ├─ PostResponseDto
│  ├─ CommentRequestDto
│  └─ CommentResponseDto
│
├─ entity
│  ├─ User
│  ├─ Post
│  └─ Comment
│
├─ store
│  └─ MemoryStore
│
└─ AssignmentApplication
```

---

## 4. 구현 방식

이번 과제에서는 DB를 사용하지 않는 조건에 맞춰 `MemoryStore` 클래스를 만들고, `Map`을 사용하여 사용자, 게시글, 댓글 데이터를 임시로 저장했습니다.

전체 흐름은 다음과 같습니다.

```text
Postman에서 JSON 요청
→ Controller에서 RequestDto로 요청 수신
→ MemoryStore에 데이터 저장 또는 조회
→ ResponseDto로 JSON 응답 반환
```

서버를 재시작하면 `MemoryStore`에 저장된 데이터는 초기화됩니다.
따라서 Postman 테스트는 회원가입 → 게시글 생성 → 댓글 생성 → 조회/수정/삭제 순서로 진행했습니다.

---

## 5. 주요 API 목록

### User API

| 기능       | Method | URL               |
| -------- | ------ | ----------------- |
| 회원가입     | POST   | `/users/signup`   |
| 로그인      | POST   | `/users/login`    |
| 회원 전체 조회 | GET    | `/users`          |
| 회원 단건 조회 | GET    | `/users/{userId}` |
| 회원 수정    | PATCH  | `/users/{userId}` |
| 회원 삭제    | DELETE | `/users/{userId}` |

### Post API

| 기능        | Method | URL                     |
| --------- | ------ | ----------------------- |
| 게시글 생성    | POST   | `/posts/users/{userId}` |
| 게시글 목록 조회 | GET    | `/posts?page=0&size=10` |
| 게시글 단건 조회 | GET    | `/posts/{postId}`       |
| 게시글 수정    | PATCH  | `/posts/{postId}`       |
| 게시글 삭제    | DELETE | `/posts/{postId}`       |

### Comment API

| 기능       | Method | URL                                       |
| -------- | ------ | ----------------------------------------- |
| 댓글 생성    | POST   | `/posts/{postId}/comments/users/{userId}` |
| 댓글 단건 조회 | GET    | `/posts/{postId}/comments/{commentId}`    |
| 댓글 수정    | PATCH  | `/posts/{postId}/comments/{commentId}`    |
| 댓글 삭제    | DELETE | `/posts/{postId}/comments/{commentId}`    |

---

## 6. 상태 코드 기준

지난 REST API 설계 피드백을 반영하여 상태 코드와 응답 의미가 어긋나지 않도록 기준을 정했습니다.

| 상황       | Status Code                 |
| -------- | --------------------------- |
| 생성 성공    | `201 Created`               |
| 조회 성공    | `200 OK`                    |
| 수정 성공    | `200 OK`                    |
| 삭제 성공    | `204 No Content`            |
| 잘못된 요청   | `400 Bad Request`           |
| 인증 실패    | `401 Unauthorized`          |
| 권한 없음    | `403 Forbidden`             |
| 데이터 없음   | `404 Not Found`             |
| 서버 내부 오류 | `500 Internal Server Error` |

이번 구현에서는 Spring Security를 사용하지 않았기 때문에 실제 토큰 기반 인증/권한 처리는 구현하지 않았고, 요청/응답 구조와 CRUD 흐름을 확인하는 데 집중했습니다.

---

## 7. Postman 테스트 순서

DB 없이 `MemoryStore`를 사용하므로 서버 실행 후 아래 순서로 테스트해야 합니다.

```text
1.  POST   /users/signup
2.  POST   /users/login
3.  GET    /users
4.  GET    /users/1
5.  PATCH  /users/1

6.  POST   /posts/users/1
7.  GET    /posts?page=0&size=10
8.  GET    /posts/1
9.  PATCH  /posts/1

10. POST   /posts/1/comments/users/1
11. GET    /posts/1/comments/1
12. PATCH  /posts/1/comments/1
13. DELETE /posts/1/comments/1
14. DELETE /posts/1
15. DELETE /users/1
```

---

## 8. API 요청/응답 예시

### 회원가입

```http
POST /users/signup
```

Request Body

```json
{
  "email": "yhpark@naver.com",
  "password": "gourmet1234",
  "nickname": "gourmet"
}
```

Response

```json
{
  "email": "yhpark@naver.com",
  "nickname": "gourmet",
  "id": 1
}
```

---

### 로그인

```http
POST /users/login
```

Request Body

```json
{
  "email": "yhpark@naver.com",
  "password": "gourmet1234"
}
```

Response

```json
{
  "email": "yhpark@naver.com",
  "nickname": "gourmet",
  "id": 1
}
```

---

### 회원 전체 조회

```http
GET /users
```

Response

```json
[
  {
    "email": "yhpark@naver.com",
    "nickname": "gourmet",
    "id": 1
  }
]
```

---

### 회원 단건 조회

```http
GET /users/1
```

Response

```json
{
  "email": "yhpark@naver.com",
  "nickname": "gourmet",
  "id": 1
}
```

---

### 회원 수정

```http
PATCH /users/1
```

Request Body

```json
{
  "nickname": "gourmet-update"
}
```

Response

```json
{
  "email": "yhpark@naver.com",
  "nickname": "gourmet-update",
  "id": 1
}
```

---

### 게시글 생성

```http
POST /posts/users/1
```

Request Body

```json
{
  "title": "첫 번째 게시글",
  "content": "DB 없이 MemoryStore로 작성한 게시글입니다."
}
```

Response

```json
{
  "content": "DB 없이 MemoryStore로 작성한 게시글입니다.",
  "id": 1,
  "title": "첫 번째 게시글",
  "userId": 1
}
```

---

### 게시글 목록 조회

```http
GET /posts?page=0&size=10
```

Response

```json
[
  {
    "content": "DB 없이 MemoryStore로 작성한 게시글입니다.",
    "id": 1,
    "title": "첫 번째 게시글",
    "userId": 1
  }
]
```

---

### 게시글 단건 조회

```http
GET /posts/1
```

Response

```json
{
  "content": "DB 없이 MemoryStore로 작성한 게시글입니다.",
  "id": 1,
  "title": "첫 번째 게시글",
  "userId": 1
}
```

---

### 게시글 수정

```http
PATCH /posts/1
```

Request Body

```json
{
  "title": "수정된 게시글 제목",
  "content": "수정된 게시글 내용입니다."
}
```

Response

```json
{
  "content": "수정된 게시글 내용입니다.",
  "id": 1,
  "title": "수정된 게시글 제목",
  "userId": 1
}
```

---

### 댓글 생성

```http
POST /posts/1/comments/users/1
```

Request Body

```json
{
  "content": "첫 번째 댓글입니다."
}
```

Response

```json
{
  "content": "첫 번째 댓글입니다.",
  "id": 1,
  "postId": 1,
  "userId": 1
}
```

---

### 댓글 단건 조회

```http
GET /posts/1/comments/1
```

Response

```json
{
  "content": "첫 번째 댓글입니다.",
  "id": 1,
  "postId": 1,
  "userId": 1
}
```

---

### 댓글 수정

```http
PATCH /posts/1/comments/1
```

Request Body

```json
{
  "content": "수정된 댓글입니다."
}
```

Response

```json
{
  "content": "수정된 댓글입니다.",
  "id": 1,
  "postId": 1,
  "userId": 1
}
```

---

### 댓글 삭제

```http
DELETE /posts/1/comments/1
```

Response

```text
204 No Content
```

---

### 게시글 삭제

```http
DELETE /posts/1
```

Response

```text
204 No Content
```

---

### 회원 삭제

```http
DELETE /users/1
```

Response

```text
204 No Content
```

