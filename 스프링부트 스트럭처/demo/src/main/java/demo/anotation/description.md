## 어노테이션 패키지 구조
```plaintext
└── anotation/ # 어노테이션
    ├── CustomAnnotation.java
    ├── AnotherAnnotation.java
    ...
```

## 어노테이션 예제 코드

아래는 간단한 커스텀 어노테이션 예제 코드입니다.

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomAnnotation {
    String value() default "";
}
```