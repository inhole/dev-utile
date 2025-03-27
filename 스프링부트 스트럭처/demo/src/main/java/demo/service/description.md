# Service

`service` 디렉토리는 비즈니스 로직을 처리하는 계층으로, Controller와 Repository 사이에서 데이터를 가공하거나 트랜잭션을 관리하는 역할을 합니다.
요청을 받아 필요한 데이터를 조회하고 가공하여 Controller에 전달하거나, 복잡한 처리 로직을 수행합니다.

---

## 📁 패키지 구조 예시

```plaintext
└── service/
    ├── UserService.java             # 사용자 관련 서비스
    └── ProductService.java          # 상품 관련 서비스
```

---

## 💼 서비스 클래스 예제 (인터페이스 분리 없이)

```java
package com.example.service;

import com.example.domain.dto.UserDto;
import com.example.domain.entity.User;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return new UserDto(user);
    }

    @Transactional
    public void registerUser(UserDto userDto) {
        User user = new User(userDto.getEmail(), userDto.getPassword(), userDto.getRole());
        userRepository.save(user);
    }
}
```

---

## 🔄 트랜잭션 처리

- `@Transactional(readOnly = true)` : 조회 전용 메서드에 사용하여 성능 최적화
- `@Transactional` : 데이터 변경(등록, 수정, 삭제) 작업에 사용

---

## 💡 참고 사항

- 최근에는 서비스 인터페이스와 구현 클래스를 굳이 분리하지 않고, 하나의 `@Service` 클래스에 비즈니스 로직을 작성하는 경우가 많습니다.
- 인터페이스 분리는 다음과 같은 경우에만 고려해도 충분합니다:
    - 구현체가 여러 개 생길 가능성이 있는 경우
    - 테스트나 유연한 아키텍처 분리가 필요한 경우
- 서비스 계층은 비즈니스 정책을 중심으로 구성되어야 하며, Controller나 Repository에 로직이 섞이지 않도록 주의합니다.
- 공통 로직은 추상 클래스나 별도 Util/Helper 클래스로 분리하여 재사용성을 높일 수 있습니다.

---

