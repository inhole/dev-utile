# Controller

Spring MVC의 Controller 계층은 클라이언트의 요청을 처리하고, 적절한 서비스 호출 및 응답을 반환하는 역할을 합니다.  
비즈니스 도메인에 따라 컨트롤러를 분리하면 유지보수성과 가독성이 향상됩니다.

---

## 📁 패키지 구조 예시

```plaintext
└── controller/                     # 컨트롤러 계층 (MVC의 C)
    ├── login/
    │   ├── LoginController.java         # 뷰 반환용 컨트롤러
    │   └── LoginRestController.java     # 로그인 REST API
    └── main/
        ├── MainController.java
        └── MainRestController.java
```

- `@Controller`: JSP, Thymeleaf 등의 View를 반환
- `@RestController`: JSON 등 데이터를 반환 (REST API 용도)
- 역할에 따라 login, main, admin 등 하위 패키지로 분리

---

## 📌 컨트롤러 어노테이션 설명

| 어노테이션         | 설명 |
|----------------------|------|
| `@Controller`        | View 페이지를 반환하는 전통적인 Spring MVC 컨트롤러 |
| `@RestController`    | `@Controller + @ResponseBody`, 데이터를 직접 반환 |
| `@RequestMapping`    | 공통 경로 설정 (클래스 or 메서드) |
| `@GetMapping`        | GET 요청 매핑 |
| `@PostMapping`       | POST 요청 매핑 |
| `@RequestParam`      | 요청 파라미터 추출 (ex. 쿼리 스트링) |
| `@PathVariable`      | 경로에 포함된 변수 추출 |
| `@RequestBody`       | JSON 바디 데이터를 DTO로 바인딩 |

---

## ✅ 예제 1: 뷰 반환용 컨트롤러 (`@Controller`)

```java
package com.example.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginForm() {
        // resources/templates/login.jsp 또는 login.html 렌더링
        return "login";
    }
}
```

---

## ✅ 예제 2: REST API 컨트롤러 (`@RestController`)

```java
package com.example.controller.login;

import com.example.domain.dto.LoginRequestDto;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
public class LoginRestController {

    @PostMapping
    public ResponseEntity<String> login(@RequestBody LoginRequestDto dto) {
        // 로그인 검증 로직 생략
        return ResponseEntity.ok("로그인 성공: " + dto.getEmail());
    }
}
```

---

## ✅ 예제 3: PathVariable & RequestParam

```java
@RestController
@RequestMapping("/api/user")
public class UserRestController {

    @GetMapping("/{id}")
    public ResponseEntity<String> getUser(@PathVariable Long id) {
        return ResponseEntity.ok("사용자 ID: " + id);
    }

    @GetMapping("/search")
    public ResponseEntity<String> search(@RequestParam String keyword) {
        return ResponseEntity.ok("검색어: " + keyword);
    }
}
```

---

## 💡 참고 사항

- Controller는 절대로 비즈니스 로직을 포함하지 않고, Service 계층에 위임하는 구조가 권장됩니다.
- REST API는 `@RestController`, 웹 뷰는 `@Controller`로 명확히 구분해서 사용하는 것이 좋습니다.
- 공통 예외 처리는 `@RestControllerAdvice`, 인증 체크는 AOP 또는 인터셉터로 분리하여 작성하세요.
- 컨트롤러 테스트는 `@WebMvcTest`로 단위 테스트할 수 있습니다.
