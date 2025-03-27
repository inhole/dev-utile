package demo.util.pagination;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class PaginationUtil {

    public int safePage(int page) {
        return Math.max(page, 1);
    }

    public int safeSize(int size, int defaultSize) {
        return (size > 0) ? size : defaultSize;
    }

    public int getStartIndex(int page, int size) {
        return (safePage(page) - 1) * size;
    }

    public int getTotalPages(int totalElements, int size) {
        return (int) Math.ceil((double) totalElements / size);
    }

    /**
     * PaginationResponse<T> 생성 유틸 메서드
     *
     * @param list 결과 데이터 리스트
     * @param page 현재 페이지
     * @param size 페이지당 개수
     * @param totalElements 전체 데이터 수
     * @return PaginationResponse<T> 객체
     */
    public <T> PaginationResponse<T> buildResponse(List<T> list, int page, int size, int totalElements) {
        return new PaginationResponse<>(
            list,
            safePage(page),
            safeSize(size, 10),
            totalElements,
            getTotalPages(totalElements, size)
        );
    }
}
