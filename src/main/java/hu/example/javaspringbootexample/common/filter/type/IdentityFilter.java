package hu.example.javaspringbootexample.common.filter.type;

import hu.example.javaspringbootexample.common.filter.PathResolver;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class IdentityFilter<T> implements Filter {

    private T value;

    private IdentityFilterType type = IdentityFilterType.EQUALS;

    public enum IdentityFilterType {
        EQUALS,
        NOT_EQUALS,
        IS_NULL,
        IS_NOT_NULL
    }

    public IdentityFilter(@NotNull T value) {
        this(value, IdentityFilterType.EQUALS);
    }

    public IdentityFilter(@NotNull T value, @NotNull IdentityFilterType type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public boolean isPresent() {
        return value != null;
    }

    @Override
    public Predicate toPredicate(List<String> fieldNames, Path<?> root, CriteriaBuilder builder) {
        String fieldName = fieldNames.stream().findFirst().orElseThrow();
        Path<?> resolvedPath = PathResolver.resolve(fieldName, root);

        switch (type) {
            case EQUALS:
                return builder.equal(resolvedPath, value);
            case NOT_EQUALS:
                return builder.notEqual(resolvedPath, value);
            case IS_NULL:
                return builder.isNull(resolvedPath);
            case IS_NOT_NULL:
                return builder.isNotNull(resolvedPath);
            default:
                throwFilterProcessingError();
        }

        throw new IllegalStateException(); // should not get here
    }

    @ToString
    public static class UUIDFilter extends IdentityFilter<UUID> {
        public UUIDFilter() {
        }

        public UUIDFilter(@NotNull UUID value) {
            this(value, IdentityFilterType.EQUALS);
        }

        public UUIDFilter(@NotNull UUID value, @NotNull IdentityFilterType type) {
            super(value, type);
        }
    }

    @ToString
    public static class StringFilter extends IdentityFilter<String> {
        public StringFilter() {
        }

        public StringFilter(@NotNull String value) {
            this(value, IdentityFilterType.EQUALS);
        }

        public StringFilter(@NotNull String value, @NotNull IdentityFilterType type) {
            super(value, type);
        }
    }

    @ToString
    public static class BooleanFilter extends IdentityFilter<Boolean> {
        public BooleanFilter() {
        }

        public BooleanFilter(@NotNull Boolean value) {
            this(value, IdentityFilterType.EQUALS);
        }

        public BooleanFilter(@NotNull Boolean value, @NotNull IdentityFilterType type) {
            super(value, type);
        }
    }

    @ToString
    public static class LocalDateFilter extends IdentityFilter<LocalDate> {
        public LocalDateFilter() {
        }

        public LocalDateFilter(@NotNull LocalDate value) {
            super(value);
        }

        public LocalDateFilter(@NotNull LocalDate value, @NotNull IdentityFilterType type) {
            super(value, type);
        }
    }

    @ToString
    public static class LocalDateTimeFilter extends IdentityFilter<LocalDateTime> {
        public LocalDateTimeFilter() {
        }

        public LocalDateTimeFilter(@NotNull LocalDateTime value) {
            super(value);
        }

        public LocalDateTimeFilter(@NotNull LocalDateTime value, @NotNull IdentityFilterType type) {
            super(value, type);
        }
    }

}
