package demo.service;

import demo.domain.dto.ProductDto;
import demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<ProductDto> findPage(int offset, int size) {
        return productRepository.selectPage(offset, size);
    }

    public int countAll() {
        return productRepository.selectTotalCount();
    }
}
