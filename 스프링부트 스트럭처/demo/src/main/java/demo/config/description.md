# Config

`config` 디렉토리는 애플리케이션 전반에 걸쳐 공통적으로 적용되는 설정을 담당합니다.  
예: WebMvc 설정, CORS 설정, Swagger 설정, 인터셉터 등록, Bean 설정 등.

---

## 📁 패키지 구조 예시

```plaintext
└── config/
    ├── WebMvcConfig.java             # MVC 관련 설정 (인터셉터, 리소스 핸들러 등)
    ├── SwaggerConfig.java            # Swagger API 문서 설정
    ├── CorsConfig.java               # CORS 정책 설정
    └── AppConfig.java                # 공통 Bean 등록, 글로벌 설정
```

---

## 📌 구성 요소 설명 및 예제

### 1. WebMvc 설정 (`WebMvcConfigurer`)

- `addInterceptors`, `addResourceHandlers` 등을 오버라이드하여 Web 관련 설정을 조정합니다.
- 인터셉터, 정적 자원 경로 등을 설정할 수 있습니다.

#### ✅ 예제: `WebMvcConfig.java`

```java
package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 예: 로그인 체크 인터셉터 등록
        // registry.addInterceptor(new LoginInterceptor())
        //         .addPathPatterns("/**")
        //         .excludePathPatterns("/login", "/static/**");
    }
}
```

---

### 2. Swagger 설정 (`SwaggerConfig`)

- Swagger를 통해 API 문서를 자동으로 생성합니다.
- 개발 환경에서만 활성화하는 방식으로 설정 가능.

#### ✅ 예제: `SwaggerConfig.java` (Springfox 버전 예시)

```java
package com.example.config;

import org.springframework.context.annotation.*;
import springfox.documentation.builders.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.example.controller"))
            .paths(PathSelectors.any())
            .build();
    }
}
```

---

### 3. 공통 Bean 설정 (`AppConfig`)

- 프로젝트 전역에서 사용할 수 있는 공통 Bean을 정의합니다.
- 예: ModelMapper, ObjectMapper, PasswordEncoder 등

#### ✅ 예제: `AppConfig.java`

```java
package com.example.config;

import org.springframework.context.annotation.*;
import org.modelmapper.ModelMapper;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
```

---

### 4. CORS 설정 분리형 (`CorsConfig`)

- CORS 정책만 따로 설정하고, 필요한 경우 Filter로 분리할 수도 있습니다.

#### ✅ 예제: `CorsConfig.java`

```java
package com.example.config;

import org.springframework.context.annotation.*;
import org.springframework.web.cors.*;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
```

---

## 💡 참고 사항

- 설정 클래스에는 반드시 `@Configuration`을 붙여 Spring이 Bean으로 인식하게 해야 합니다.
- `@Configuration`은 내부에 정의된 `@Bean` 메서드들을 **싱글톤 보장**하며, `@Component`의 확장된 개념입니다.
- `@Component`를 사용해도 빈 등록은 되지만, 내부의 `@Bean` 메서드는 매번 새로운 인스턴스를 생성할 수 있습니다.  
  따라서 **설정 클래스로 사용할 경우에는 반드시 `@Configuration`을 사용**하는 것이 바람직합니다.

| 구분              | 설명 |
|-------------------|------|
| `@Component`      | 일반적인 빈 등록용 클래스 (Service, Repository, Utils 등) |
| `@Configuration`  | 설정 클래스 전용. 내부의 `@Bean` 메서드까지 싱글톤으로 관리 |

- 설정 클래스 분리는 유지보수성과 가독성을 높이는 데 매우 유리합니다.
- 환경별 설정(`application-dev.yml`, `application-prod.yml`)과 연동하여 조건부 설정도 가능합니다.
- 보안 설정(Spring Security)은 `SecurityConfig` 클래스로 별도 관리하는 것이 일반적입니다.
