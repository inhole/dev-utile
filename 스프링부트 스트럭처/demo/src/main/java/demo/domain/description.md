
### ✅ `domain/description.md`

# Domain

도메인 계층은 비즈니스 로직에서 사용되는 핵심 데이터 구조를 정의하는 영역입니다.  
Entity, DTO, Enum 등을 포함하며, 데이터베이스와 직접 연결되거나 계층 간 데이터 전달에 사용됩니다.

---

## 📁 패키지 구조 예시

```plaintext
└── domain/
    ├── entity/            # DB 테이블과 매핑되는 Entity 클래스
    │   ├── User.java
    │   └── Product.java
    ├── dto/               # 계층 간 데이터 전달용 객체
    │   ├── LoginRequestDto.java
    │   └── ProductResponseDto.java
    └── enums/             # 고정된 값 정의
        ├── UserRole.java
        └── ProductStatus.java
```

---

## 📌 구성 요소 설명 및 예제

### 1. Entity (`domain/entity/`)

Entity 클래스는 JPA 등 ORM을 통해 DB 테이블과 매핑되는 클래스입니다.  
비즈니스 로직 일부를 포함할 수도 있으며, `@Entity`, `@Table`, `@Id` 등의 어노테이션을 사용합니다.

#### ✅ 예제: `User.java`

```java
package com.example.domain.entity;

import com.example.domain.enums.UserRole;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    protected User() {}

    public User(String email, String password, UserRole role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public UserRole getRole() { return role; }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }
}
```

---

### 2. DTO (`domain/dto/`)

DTO(Data Transfer Object)는 Controller ↔ Service 간 데이터 전달을 위해 사용됩니다.  
엔티티와 분리하여 데이터 전달 및 유효성 검증을 담당합니다.

#### ✅ 예제: `LoginRequestDto.java`

```java
package com.example.domain.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequestDto {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    public LoginRequestDto() {}

    public LoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
}
```

#### ✅ 예제: `ProductResponseDto.java`

```java
package com.example.domain.dto;

public class ProductResponseDto {

    private Long id;
    private String name;
    private int price;

    public ProductResponseDto(Long id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getPrice() { return price; }
}
```

---

### 3. Enum (`domain/enums/`)

Enum 클래스는 고정된 값의 집합을 표현합니다.  
상태값, 사용자 권한, 타입 등의 관리에 사용되며, Entity와 함께 매핑될 수도 있습니다.

#### ✅ 예제: `UserRole.java`

```java
package com.example.domain.enums;

public enum UserRole {
    ADMIN,
    USER,
    GUEST
}
```

#### ✅ 예제: `ProductStatus.java`

```java
package com.example.domain.enums;

public enum ProductStatus {
    IN_STOCK,
    SOLD_OUT,
    DISCONTINUED
}
```

---

## 💡 참고 사항

- Entity 클래스는 DB 구조와 밀접한 관계가 있으므로 변경에 신중해야 하며, DTO를 통해 외부와의 데이터를 분리하는 것이 권장됩니다.
- Enum 사용 시 `@Enumerated(EnumType.STRING)`으로 매핑하면 숫자가 아닌 문자열로 DB에 저장되어 가독성과 유지보수성이 향상됩니다.
- 도메인 규모가 커지면 도메인 단위 패키지 구조도 고려해볼 수 있습니다.