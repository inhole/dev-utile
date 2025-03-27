package demo.util.pagination;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * 페이지네이션 로직을 처리하는 서비스 클래스입니다.
 */
@Service
public class PaginationService {

    /**
     * 주어진 dataFetcher 함수를 사용하여 데이터를 페이지네이션합니다.
     *
     * @param <T>                페이지네이션할 데이터의 타입
     * @param page               현재 페이지 번호
     * @param size               페이지당 항목 수
     * @param dataFetcher        주어진 페이지와 크기에 대한 데이터를 가져오는 함수
     * @param totalCountSupplier 총 항목 수를 제공하는 공급자
     * @return 페이지네이션된 데이터를 포함하는 PaginationResponse
     */
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