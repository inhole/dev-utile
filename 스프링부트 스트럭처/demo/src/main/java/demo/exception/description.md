
# Exception

Spring Boot에서 예외 처리는 클린 코드와 사용자 친화적인 응답을 위해 중요한 역할을 합니다.  
`exception` 패키지에는 **커스텀 예외 클래스**, **전역 예외 처리기**, **응답 객체** 등을 정의합니다.

---

## 📁 패키지 구조 예시

```plaintext
└── exception/
    ├── handler/                  # 전역 예외 처리 클래스
    │   └── GlobalExceptionHandler.java
    ├── custom/                   # 사용자 정의 예외 클래스
    │   ├── UserNotFoundException.java
    │   └── InvalidRequestException.java
    └── model/                    # 예외 응답 객체
        └── ErrorResponse.java
```

---

## 📌 구성 요소 설명 및 예제

### 1. Custom Exception (`exception/custom/`)

비즈니스 상황에 맞는 **사용자 정의 예외 클래스**를 생성하여 의미 있는 예외 처리를 할 수 있습니다.  
RuntimeException을 상속받는 것이 일반적이며, 필요에 따라 상태 코드, 메시지 등을 포함할 수 있습니다.

#### ✅ 예제: `UserNotFoundException.java`

```java
package com.example.exception.custom;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String email) {
        super("사용자를 찾을 수 없습니다: " + email);
    }
}
```

---

### 2. Error Response 모델 (`exception/model/`)

예외 발생 시 클라이언트에 반환할 응답 형태를 정의한 클래스입니다.  
상태 코드, 메시지, 타임스탬프 등을 포함할 수 있습니다.

#### ✅ 예제: `ErrorResponse.java`

```java
package com.example.exception.model;

import java.time.LocalDateTime;

public class ErrorResponse {

    private int status;
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
```

---

### 3. Global Exception Handler (`exception/handler/`)

`@RestControllerAdvice`를 사용해 모든 예외를 한 곳에서 처리할 수 있습니다.  
예외 유형에 따라 다른 응답을 설정할 수 있어 유지보수가 용이합니다.

#### ✅ 예제: `GlobalExceptionHandler.java`

```java
package com.example.exception.handler;

import com.example.exception.custom.UserNotFoundException;
import com.example.exception.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다.");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

---

## 💡 참고 사항

- `@RestControllerAdvice`는 모든 `@RestController`에서 발생하는 예외를 처리합니다.  
  뷰 반환용 컨트롤러에서는 `@ControllerAdvice` + `@ExceptionHandler` 조합을 사용할 수 있습니다.
- 필요 시 예외에 따라 상태 코드와 메시지를 커스터마이징해서 일관성 있는 API 응답을 만들 수 있습니다.
- 공통적인 `ErrorCode Enum`, `BaseException`, `ResponseEntityFactory` 등의 도입도 확장 시 유용합니다.