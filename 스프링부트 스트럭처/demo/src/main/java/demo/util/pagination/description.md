# Pagination

Pagination(페이지네이션)은 대량의 데이터를 페이지 단위로 나누어 보여주는 기술로, 사용자에게 효율적인 UI/UX를 제공하고, 서버 리소스를 절약하는 데 매우 중요합니다.

Spring에서는 `Page`, `Pageable`, `PageRequest`를 제공하지만, 커스텀 구조가 필요할 경우 직접 유틸 클래스를 만들기도 합니다.

---

## 📁 구성 파일 예시

```plaintext
└── dto/
    └── PaginationResponse.java     # 페이징 응답 DTO
└── util/
    └── PaginationUtil.java         # 페이지 계산 유틸리티
└── service/
    └── PaginationService.java      # 공통 페이징 처리 서비스
```

---

## 📌 PaginationUtil 기능 설명

| 메서드 | 설명 |
|--------|------|
| `safePage(int page)` | 페이지 번호가 0 이하일 경우 1로 보정 |
| `safeSize(int size, int defaultSize)` | 페이지 크기가 유효하지 않으면 기본값 사용 |
| `getStartIndex(int page, int size)` | DB 조회 시작 인덱스 계산 |
| `getTotalPages(int totalElements, int size)` | 전체 페이지 수 계산 |
| `buildResponse(List<T> list, int page, int size, int totalElements)` | PaginationResponse<T> 전체 생성 |

---

## 📌 PaginationResponse<T> 구조

```java
@Data
@AllArgsConstructor
public class PaginationResponse<T> {
  private List<T> content;         // 결과 데이터 목록
  private int page;                // 현재 페이지
  private int size;                // 페이지당 항목 수
  private int totalElements;       // 전체 데이터 수
  private int totalPages;          // 전체 페이지 수
}
```

---

## 📌 PaginationService 예시

```java
package com.example.service;

import demo.dto.PaginationResponse;
import demo.util.PaginationUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@Service
public class PaginationService {

  public <T> PaginationResponse<T> paginate(
          int page,
          int size,
          BiFunction<Integer, Integer, List<T>> dataFetcher,
          Supplier<Integer> totalCountSupplier
  ) {
    page = PaginationUtil.safePage(page);
    size = PaginationUtil.safeSize(size, 10);
    int startIndex = PaginationUtil.getStartIndex(page, size);

    List<T> list = dataFetcher.apply(startIndex, size);
    int total = totalCountSupplier.get();

    return PaginationUtil.buildResponse(list, page, size, total);
  }
}
```

---

## ✅ 예제 코드

### 📦 Controller (기본 방식)
```java
@GetMapping("/api/products")
public ResponseEntity<PaginationResponse<ProductDto>> getProducts(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size
) {
  page = PaginationUtil.safePage(page);
  size = PaginationUtil.safeSize(size, 10);
  int startIndex = PaginationUtil.getStartIndex(page, size);

  List<ProductDto> list = productService.findPage(startIndex, size);
  int total = productService.countAll();

  PaginationResponse<ProductDto> response = new PaginationResponse<>(
          list, page, size, total, PaginationUtil.getTotalPages(total, size)
  );

  return ResponseEntity.ok(response);
}
```

### 📦 Controller (간소화된 유틸 메서드 활용)
```java
@GetMapping("/api/products")
public ResponseEntity<PaginationResponse<ProductDto>> getProducts(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size
) {
  int startIndex = PaginationUtil.getStartIndex(page, size);
  List<ProductDto> list = productService.findPage(startIndex, size);
  int total = productService.countAll();

  return ResponseEntity.ok(PaginationUtil.buildResponse(list, page, size, total));
}
```

### 📦 Controller (PaginationService 활용)
```java
@GetMapping("/api/products")
public ResponseEntity<PaginationResponse<ProductDto>> getProducts(
    @RequestParam(defaultValue = "1") int page,
    @RequestParam(defaultValue = "10") int size
) {
    return ResponseEntity.ok(paginationService.paginate(
        page,
        size,
        productService::findPage,   // BiFunction<Integer offset, Integer size, List<T>> 형태여야 함
        productService::countAll    // Supplier<Integer> 형태여야 함
    ));
}
```

---

## 🔍 productService 메서드 구현 예시

### ✅ MyBatis 기반 예시
```java
public List<ProductDto> findPage(int offset, int size) {
    return productMapper.selectPage(offset, size);
}

public int countAll() {
    return productMapper.selectTotalCount();
}
```

### ✅ JPA 기반 예시
```java
public List<ProductDto> findPage(int offset, int size) {
    Pageable pageable = PageRequest.of(offset / size, size);
    return productRepository.findAll(pageable)
                            .stream()
                            .map(ProductDto::new)
                            .toList();
}

public int countAll() {
    return (int) productRepository.count();
}
```

---

## 💡 참고 사항

- 페이지 번호는 일반적으로 1부터 시작하지만, DB에서는 0-based 인덱스를 사용하므로 `startIndex = (page - 1) * size` 계산이 필요합니다.
- Spring Data JPA를 사용하는 경우 `PageRequest.of(page, size)` 등 Pageable 기반 구조도 고려해 볼 수 있습니다.
- 커스텀 구조는 MyBatis, JDBC 등 직접 쿼리에서 더 유연하게 활용됩니다.
- `PaginationService`를 사용하면 컨트롤러 단에서 페이징 로직을 더 간결하게 유지할 수 있습니다.

---

