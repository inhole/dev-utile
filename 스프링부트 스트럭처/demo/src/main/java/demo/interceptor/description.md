# Interceptor

Spring MVC에서 Interceptor는 HTTP 요청이 Controller에 도달하기 전/후에 **공통 작업을 처리**하기 위한 컴포넌트입니다.  
주로 인증, 권한 검사, 로깅, 성능 측정, 다국어 처리 등에 사용됩니다.

---

## 📁 패키지 구조 예시

```plaintext
└── interceptor/
    ├── LoginInterceptor.java              # 로그인 체크
    ├── AdminInterceptor.java              # 관리자 권한 체크
    └── LocaleInterceptor.java             # 다국어 처리
```

인터셉터는 `WebMvcConfigurer`에서 등록해야 동작합니다.

---

## 📌 인터셉터 동작 흐름

```plaintext
Client Request
      ↓
[ preHandle() 호출 ]
      ↓
   Controller
      ↓
[ postHandle() 호출 ]
      ↓
[ afterCompletion() 호출 ]
      ↓
Client Response
```

| 메서드            | 설명 |
|-------------------|------|
| `preHandle()`     | 컨트롤러 실행 전에 실행 (false 반환 시 요청 중단) |
| `postHandle()`    | 컨트롤러 실행 후, View 렌더링 전에 실행 |
| `afterCompletion()` | View 렌더링 후 실행 (리소스 정리, 로깅 등) |

---

## ✅ 예제: `LoginInterceptor.java`

```java
package com.example.interceptor;

import jakarta.servlet.http.*;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

        if (!isLoggedIn) {
            response.sendRedirect("/login");
            return false; // 컨트롤러로 요청이 가지 않음
        }

        return true; // 컨트롤러로 요청 전달
    }
}
```

---

## ✅ 예제: 인터셉터 등록 (`WebMvcConfig.java`)

```java
package com.example.config;

import com.example.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/login", "/css/**", "/js/**", "/images/**");
    }
}
```

---

## 💡 참고 사항

- 인터셉터는 DispatcherServlet을 통과한 이후, 컨트롤러 실행 직전에 개입합니다.
- 필터(Filter)는 인터셉터보다 더 앞단에서 동작하며, 서블릿 기반 전처리에 적합합니다.
- 여러 인터셉터가 등록되면 등록 순서대로 실행되며, `order` 설정이 필요한 경우 Filter 기반 처리로 이동하는 것도 고려하세요.
- 세션 기반 인증, URL 접근 제어, 공통 로그 기록 등에 매우 유용합니다.

---

## ✅ 실무 팁

- 인터셉터는 요청마다 동작하므로 반드시 **성능을 고려한 처리**만 포함하세요.
- 인증 로직은 세션이나 토큰을 직접 체크하고, 예외 처리는 컨트롤러 어드바이스로 넘기면 구조가 깔끔해집니다.
- 보안이 중요한 경우 Spring Security와 함께 사용하는 것이 더 안전합니다.
