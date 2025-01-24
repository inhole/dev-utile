# 각 비즈니스에 맞는 Controller를 생성하여 API를 구현한다.

## Controller 패키지 구조
```plaintext
└── controller/ # 컨트롤러 (MVC)
    ├── login/
    │   └── LoginController.java
    │   └── LoginRestController.java
    └── main/
    │   └── mainController.java
    │   └── mainRestController.java
    ...
````

## Controller 예제 코드

아래는 간단한 `@Controller`와 `@RestController` 예제 코드입니다.

```java
@Controller
public class ExampleController {

    @GetMapping("/example")
    public String example() {
        return "example";
    }
}

@RestController
public class ExampleRestController {

    @PostMapping("/api/example")
    public ResponseEntity<String> example() {
        return ResponseEntity.ok("example");
    }
}

