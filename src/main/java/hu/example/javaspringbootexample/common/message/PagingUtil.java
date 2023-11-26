package hu.example.javaspringbootexample.common.message;


import org.springframework.data.domain.Page;

import java.util.function.Function;
import java.util.stream.Collectors;

public class PagingUtil {

    public static <T extends Response> PageResponse<T> getPageResponse(Page<T> page) {
        PageResponse<T> response = new PageResponse<T>();
        response.setContent(page.getContent());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setNumber(page.getNumber());
        return response;
    }

    public static <T, R extends Response> PageResponse<R> getPageResponse(Page<T> page, Function<T, R> mapper) {
        PageResponse<R> response = new PageResponse<>();

        // Assuming that 'page' is a Page<T>
        response.setContent(page.getContent().stream().map(mapper).collect(Collectors.toList()));
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setNumber(page.getNumber());

        return response;
    }
}
