# validation 라이브러리를 활용한 커스텀 어노테이션
- 사용자가 입력한 텍스트에 대한 검증을 위한 커스텀 어노테이션
- Jsoup 라이브러리를 활용하여 사용자가 입력한 텍스트에 대한 HTML 태그를 제거하고, 텍스트만을 반환

````build.gradle
implementation 'org.springframework.boot:spring-boot-starter-validation'
````    

## 사용 예제:
```java 
package com.cescomicropage.dto;

import com.cescomicropage.annotation.SafeContent;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MessageDTO {

    @NotBlank(message = "텍스트를 입력해주세요.")
    @SafeContent
    private String content;
    private String codeType;
    private LocalDateTime createAt;

    @Builder
    public MessageDTO(String content, String codeType, LocalDateTime createAt) {
        this.content = content;
        this.codeType = codeType;
        this.createAt = createAt;
    }
}
```

```java
 /**
 * 댓글 등록
 */
@PostMapping("/message")
public ResponseEntity<Void> addMessage(@Valid @RequestBody MessageDTO request) {
    mainService.addMessage(request); // 댓글 등록
    mainService.addParticipation("댓글등록"); // 누적 인원,금액 추가
    return ResponseEntity.noContent().build();
}
```