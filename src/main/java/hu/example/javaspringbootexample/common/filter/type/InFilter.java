package hu.example.javaspringbootexample.common.filter.type;

import hu.example.javaspringbootexample.common.filter.PathResolver;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.constraints.NotEmpty;
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
public class InFilter<T> implements Filter {

    protected List<T> valueList;

    protected InFilterType type = InFilterType.IN;

    public enum InFilterType {
        IN
    }

    public InFilter(@NotEmpty List<T> valueList) {
        this.valueList = valueList;
    }

    @Override
    public boolean isPresent() {
        return valueList != null && !valueList.isEmpty();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Predicate toPredicate(List<String> fieldNames, Path<?> root, CriteriaBuilder builder) {
        String fieldName = fieldNames.stream().findFirst().orElseThrow();
        Path<T> resolvedPath = PathResolver.resolve(fieldName, root);

        CriteriaBuilder.In<T> inPredicate = builder.in(resolvedPath);
        valueList.forEach(inPredicate::value);
        return inPredicate;
    }

    @ToString
    public static class UUIDFilter extends InFilter<UUID> {
        public UUIDFilter() {
        }

        public UUIDFilter(@NotEmpty List<UUID> valueList) {
            super(valueList);
        }
    }

    @ToString
    public static class StringFilter extends InFilter<String> {
        public StringFilter() {
        }

        public StringFilter(@NotEmpty List<String> valueList) {
            super(valueList);
        }
    }

    @ToString
    public static class LocalDateFilter extends InFilter<LocalDate> {
        public LocalDateFilter() {
        }

        public LocalDateFilter(@NotEmpty List<LocalDate> valueList) {
            super(valueList);
        }
    }

    @ToString
    public static class LocalDateTimeFilter extends InFilter<LocalDateTime> {
        public LocalDateTimeFilter() {
        }

        public LocalDateTimeFilter(@NotEmpty List<LocalDateTime> valueList) {
            super(valueList);
        }
    }
}
