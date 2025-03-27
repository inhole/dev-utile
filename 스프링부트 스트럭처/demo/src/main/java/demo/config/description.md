## config 패키지 구조
````
└── config/ # 설정 파일
    └── SwaggerConfig.java
    └── GlobalExceptionHandler.java
````

##  Config 예제 코드
아래는 간단한 SwaggerConfig와 GlobalExceptionHandler 예제 코드입니다.
````java
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example"))
                .paths(PathSelectors.any())
                .build();
    }
}

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
````