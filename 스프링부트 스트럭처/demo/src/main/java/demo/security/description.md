# Security

`security` 디렉토리는 Spring Security 기반의 인증, 인가, 필터 설정 등을 담당합니다. 로그인, 로그아웃 처리부터 사용자 권한 제어, 세션 관리 등 보안 관련 전반적인 설정과 구현을 여기에 구성합니다.

---

## 📁 패키지 구조 예시

```plaintext
└── security/
    ├── SecurityConfig.java            # Spring Security 설정 클래스
    ├── CustomUserDetails.java         # 사용자 정보 구현체
    ├── CustomUserDetailsService.java  # 사용자 정보 조회 서비스
    ├── handler/
    │   ├── CustomAuthSuccessHandler.java  # 로그인 성공 핸들러
    │   └── CustomAuthFailureHandler.java  # 로그인 실패 핸들러
    └── filter/
        └── JwtAuthenticationFilter.java   # JWT 인증 필터 (선택)
```

---

## 🔐 SecurityConfig 설정 예제

```java
package com.example.security;

import com.example.security.handler.CustomAuthFailureHandler;
import com.example.security.handler.CustomAuthSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthSuccessHandler successHandler;
    private final CustomAuthFailureHandler failureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .formLogin()
                .loginPage("/login")
                .successHandler(successHandler)
                .failureHandler(failureHandler)
            .and()
            .authorizeHttpRequests()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll();

        return http.build();
    }
}
```

---

## 👤 CustomUserDetails & Service

```java
package com.example.security;

import com.example.domain.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> "ROLE_" + user.getRole().name());
    }

    @Override public String getPassword() { return user.getPassword(); }
    @Override public String getUsername() { return user.getEmail(); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
```

```java
package com.example.security;

import com.example.domain.entity.User;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }
        return new CustomUserDetails(user);
    }
}
```

---

## 💡 참고 사항

- `@EnableWebSecurity`는 Spring Security를 활성화하는 애노테이션입니다.
- `SecurityFilterChain` 빈을 사용하면 최신 방식(Spring Boot 3 기준)으로 설정할 수 있습니다.
- JWT, OAuth2 등과의 통합도 이 디렉토리 내에서 확장 가능합니다.
- 실제 비밀번호 비교는 Spring Security 내부에서 `PasswordEncoder`를 통해 처리됩니다.

---

