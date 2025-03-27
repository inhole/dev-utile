package demo.repository;

import demo.domain.dto.ProductDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepository {

    public List<ProductDto> selectPage(int offset, int size) {
        return null;
    }

    public int selectTotalCount() {
        return 0;
    }
}
