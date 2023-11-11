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

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ComparableFilter<C extends Comparable> implements Filter {

    private C value;

    private ComparableFilterType type = ComparableFilterType.EQUALS;

    public enum ComparableFilterType {
        EQUALS,
        NOT_EQUALS,
        GREATER_THAN,
        GREATER_THAN_OR_EQUALS,
        LESS_THAN,
        LESS_THAN_OR_EQUALS
    }

    public ComparableFilter(@NotNull C value, @NotNull ComparableFilterType type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public boolean isPresent() {
        return value != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Predicate toPredicate(List<String> fieldNames, Path<?> root, CriteriaBuilder builder) {
        String fieldName = fieldNames.stream().findFirst().orElseThrow();
        Path<C> resolvedPath = PathResolver.resolve(fieldName, root);

        switch (type) {
            case EQUALS:
                return builder.equal(resolvedPath, value);
            case NOT_EQUALS:
                return builder.notEqual(resolvedPath, value);
            case LESS_THAN:
                return builder.lessThan(resolvedPath, value);
            case LESS_THAN_OR_EQUALS:
                return builder.lessThanOrEqualTo(resolvedPath, value);
            case GREATER_THAN:
                return builder.greaterThan(resolvedPath, value);
            case GREATER_THAN_OR_EQUALS:
                return builder.greaterThanOrEqualTo(resolvedPath, value);
            default:
                throwFilterProcessingError();
        }

        throw new IllegalStateException(); // should not get here
    }

}
