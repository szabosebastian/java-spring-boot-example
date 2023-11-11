package hu.example.javaspringbootexample.common.filter.type;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

import java.util.List;

public interface Filter {

    boolean isPresent();

    <T extends Enum<?>> T getType();

    Predicate toPredicate(List<String> fieldNames, Path<?> root, CriteriaBuilder builder);

    default void throwFilterProcessingError() {
        throw new IllegalArgumentException(String.format("Unable to process filter (%s) with type: %s",
                getClass().getName(), getType().name()));
    }
}
