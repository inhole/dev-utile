# Repository

`repository` 디렉토리는 데이터베이스와 직접적으로 통신하는 계층으로, Entity 객체와 SQL 또는 ORM을 통해 CRUD 작업을 수행합니다.
Spring에서는 주로 MyBatis의 Mapper 인터페이스나 Spring Data JPA의 Repository 인터페이스를 사용합니다.

---

## 📁 패키지 구조 예시

```plaintext
└── repository/
    ├── UserRepository.java                 # JPA Repository
    ├── ProductRepository.java              # MyBatis Mapper
    ├── mapper/                             # MyBatis XML 매퍼 파일
    │   └── ProductMapper.xml
    └── custom/
        └── CustomUserRepository.java       # 사용자 커스텀 조회용
```

---

## 🔷 JPA 기반 Repository

### ✅ UserRepository
```java
package com.example.repository;

import com.example.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
```

### ✅ Custom Repository (JPA + Native Query)

#### 인터페이스
```java
package com.example.repository.custom;

public interface CustomUserRepository {
    boolean isOverseasUser(Long userId);
}
```

#### 구현체
```java
package com.example.repository.custom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class CustomUserRepositoryImpl implements CustomUserRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public boolean isOverseasUser(Long userId) {
        // Native Query로 해외 사용자 여부 확인
        String sql = "SELECT COUNT(*) FROM users WHERE id = :userId AND country NOT IN ('KR', 'KOR')";
        Number count = (Number) em.createNativeQuery(sql)
                .setParameter("userId", userId)
                .getSingleResult();
        return count.intValue() > 0;
    }
}
```

---

## 🔷 MyBatis 기반 Repository

### ✅ ProductRepository (Interface)
```java
package com.example.repository;

import com.example.domain.entity.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductRepository {
    List<Product> findExpensiveProducts(int price);
}
```

### ✅ ProductMapper.xml (resources/mapper/ProductMapper.xml)
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.example.repository.ProductRepository">

    <select id="findExpensiveProducts" resultType="com.example.domain.entity.Product">
        SELECT *
        FROM product
        WHERE price &gt; #{price}
    </select>

</mapper>
```

### ✅ 설정 파일 예시

#### 📄 application.yml
```yaml
mybatis:
  config-location: classpath:configuration/mybatis.xml
  mapper-locations: classpath:mapper/**/*.xml
  type-aliases-package: com.example.domain.entity
```

#### 📄 application.properties
```properties
mybatis.config-location=classpath:configuration/mybatis.xml
mybatis.mapper-locations=classpath:mapper/**/*.xml
mybatis.type-aliases-package=com.example.domain.entity
```

- `mapper-locations`: MyBatis XML 매퍼 파일 경로
- `type-aliases-package`: resultType, parameterType에서 사용될 엔티티 경로 설정

---

## 💡 참고 사항

- JPA 사용 시, `JpaRepository`, `CrudRepository`, `PagingAndSortingRepository` 등을 활용할 수 있습니다.
- MyBatis 사용 시, Mapper 인터페이스와 XML 매퍼 파일을 조합하여 사용하거나, 어노테이션 기반 쿼리도 가능합니다.
- 복잡한 쿼리는 QueryDSL, Native Query, 또는 XML Mapper를 통해 분리하는 것이 유지보수에 유리합니다.
- Repository 인터페이스는 Service 계층에서만 호출되도록 구조를 설계하는 것이 일반적입니다.

---