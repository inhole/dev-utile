package demo.util.pagination;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PaginationResponse<T> {
    private List<T> content;
    private int page;
    private int size;
    private int totalElements;
    private int totalPages;
}
