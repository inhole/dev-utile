# Util

`util` 디렉토리는 애플리케이션 전반에서 재사용 가능한 **공통 유틸리티 클래스**를 모아두는 공간입니다.
문자열 처리, 날짜 변환, 암호화, 파일 경로 처리 등과 같이 도메인과 직접적으로 관계없는 순수 기능성 메서드들을 포함합니다.

---

## 🔎 언제 Util 클래스를 직접 작성할까?

최근에는 Java 17의 표준 라이브러리와 Spring, Apache Commons 등의 유틸 클래스가 매우 잘 갖춰져 있어
기본적인 기능은 굳이 `util` 클래스를 만들지 않고도 해결할 수 있습니다.

| 기능 | Java/Spring 대체 API 예시 |
|------|---------------------------|
| 문자열 공백 체크 | `str == null || str.isBlank()` (Java 11+) |
| 날짜 포맷 | `DateTimeFormatter.ofPattern(...)` |
| 리스트 null 체크 | `CollectionUtils.isEmpty(list)` (Spring) |
| 객체 비교 | `Objects.equals(a, b)` |

그러나 다음과 같은 경우에는 여전히 `util` 클래스를 직접 만드는 것이 유용합니다:

- 도메인 특화 유틸리티가 필요한 경우 (ex. 사업자번호 하이픈 자동 삽입)
- 복잡한 문자열/날짜/숫자 조작을 반복적으로 처리해야 할 경우
- 외부 라이브러리 없이 경량화된 기능만 사용하고 싶은 경우

---

## 📁 패키지 구조 예시

```plaintext
└── util/
    ├── DateUtil.java           # 날짜 관련 유틸
    ├── StringUtil.java         # 문자열 관련 유틸
    └── FileUtil.java           # 파일 처리 유틸
```

---

## 🧱 예제 클래스

### ✅ StringUtil.java
```java
package com.example.util;

public class StringUtil {

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static String capitalize(String str) {
        if (isEmpty(str)) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
```

### ✅ DateUtil.java
```java
package com.example.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static String format(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static LocalDate parse(String dateStr) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
```

---

## 💡 참고 사항

- 모든 메서드는 `static` 으로 작성하여 인스턴스를 생성하지 않고 사용할 수 있도록 구성합니다.
- 유틸 클래스는 상태를 가지지 않으므로 반드시 `@Component`로 등록할 필요는 없습니다.
- 일반적으로 생성자는 `private`으로 선언하여 외부에서 인스턴스화할 수 없도록 만듭니다.

```java
private StringUtil() {}
```

- Lombok의 `@UtilityClass`를 활용하면 자동으로 static + private 생성자 구성이 가능합니다:

```java
import lombok.experimental.UtilityClass;

@UtilityClass
public class FileUtil {
    public String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
```

---

