# KDT 스터디 - 게시판

### `SpringBoot`, `MySQL`을 활용하여 만들어보는 게시판

- 구현하고자 하는 기능(API) 개발
- Thymeleaf를 활용한 View
- test code `필수` 활용

### 실습 예정 기능

- Security (`JWT`, `OAuth 2.0 : kakao, google`)
- DB : `MySQL` 활용
- 게시판 `CRUD`
- 채팅 기능 (`WebSocket` or `STOMP 프로토콜`)

### 브랜치 구조
- **main**: 배포 및 릴리즈 버전을 위한 브랜치로, 안정된 코드만 병합됩니다.
- **dev**: 통합 테스트 및 기능 검증을 위한 브랜치입니다. 각 작업 브랜치에서 작업 완료 후 병합됩니다.
- **작업 브랜치**: 기능 단위로 나눠 작업합니다.
    - Ex ) 
      - `login`: 회원가입 및 로그인 기능 개발
      - `board`: 게시판 기능 개발
      - `chat`: 채팅 기능 개발
