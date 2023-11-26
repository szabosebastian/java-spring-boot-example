package hu.example.javaspringbootexample.common.message;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageResponse<T> {

    private List<T> content;

    long totalElements;

    int totalPages;

    int number;

    public PageResponse() {
    }

    public PageResponse(List<T> content, long totalElements, int totalPages, int number) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.number = number;
    }
}
