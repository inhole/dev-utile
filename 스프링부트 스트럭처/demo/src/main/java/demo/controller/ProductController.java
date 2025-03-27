package demo.controller;

import demo.domain.dto.ProductDto;
import demo.service.ProductService;
import demo.util.pagination.PaginationResponse;
import demo.util.pagination.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final PaginationService paginationService;
    private final ProductService productService;

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
}
