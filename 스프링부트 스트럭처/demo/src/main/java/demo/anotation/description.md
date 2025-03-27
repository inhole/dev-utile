# Annotation

공통 기능을 재사용하거나 반복 코드를 줄이기 위해 **커스텀 어노테이션(Custom Annotation)** 을 정의합니다.  
AOP, 인터셉터, 인증/인가 처리 등과 함께 자주 사용되며, 개발자 선언만으로 특정 기능을 쉽게 적용할 수 있습니다.

---

## 📁 패키지 구조 예시

```plaintext
└── annotation/
    ├── LoginRequired.java             # 로그인 여부 체크용 어노테이션
    ├── AdminOnly.java                 # 관리자 권한 체크용 어노테이션
    └── Logging.java                   # 요청/응답 로깅용 어노테이션
```

---

## 📌 예제 및 설명

### 1. 기본 커스텀 어노테이션 구조

#### ✅ 예제: `@LoginRequired`

```java
package com.example.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginRequired {
}
```

- `@Target(ElementType.METHOD)` : 메서드에 적용
- `@Retention(RetentionPolicy.RUNTIME)` : 런타임 시 동작
- AOP 또는 인터셉터와 함께 사용하여 로그인 체크를 수행

---

### 2. 관리자 전용 어노테이션 + AOP

#### ✅ 예제: `@AdminOnly`

```java
package com.example.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AdminOnly {
}
```

#### ✅ 예제: `AdminCheckAspect`

```java
package com.example.aop;

import com.example.annotation.AdminOnly;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AdminCheckAspect {

    private final HttpSession session;

    @Before("@annotation(adminOnly)")
    public void checkAdmin(JoinPoint joinPoint, AdminOnly adminOnly) {
        String role = (String) session.getAttribute("role");
        if (!"ADMIN".equals(role)) {
            throw new SecurityException("관리자만 접근 가능합니다.");
        }
    }
}
```

---

### 3. 요청/응답 로깅 어노테이션 + AOP

#### ✅ 예제: `@Logging`

```java
package com.example.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Logging {
}
```

#### ✅ 예제: `LoggingAspect`

```java
package com.example.aop;

import com.example.annotation.Logging;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("@annotation(logging)")
    public void logRequest(JoinPoint joinPoint, Logging logging) {
        System.out.println("[LOG] 요청 메서드: " + joinPoint.getSignature());
    }

    @AfterReturning(pointcut = "@annotation(logging)", returning = "result")
    public void logResponse(JoinPoint joinPoint, Logging logging, Object result) {
        System.out.println("[LOG] 응답 결과: " + result);
    }
}
```

---

## 💡 참고 사항

- 커스텀 어노테이션은 `@Retention(RUNTIME)` 설정이 필수이며, AOP나 인터셉터를 통해 실질적인 동작을 구현합니다.
- AOP를 사용할 때는 `@Aspect`, `@Component`를 반드시 함께 사용해야 합니다.
- 반복되는 인증/권한 검사, 로깅, 트랜잭션 관리 등에 매우 유용하게 사용됩니다.
- 필요한 경우 속성을 가진 어노테이션도 만들 수 있습니다:

```java
public @interface Permission {
    String role() default "USER";
}
```