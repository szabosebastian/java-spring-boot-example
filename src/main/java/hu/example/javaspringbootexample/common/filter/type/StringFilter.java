package hu.example.javaspringbootexample.common.filter.type;

import hu.example.javaspringbootexample.common.filter.PathResolver;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
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
@ToString
@NoArgsConstructor
public class StringFilter implements Filter {

    private String value;

    private StringFilterType type = StringFilterType.CONTAINS;

    private StringFilterMode mode = StringFilterMode.CASE_INSENSITIVE;

    public enum StringFilterType {
        CONTAINS,
        STARTS_WITH,
        EXACT_MATCH
    }

    public enum StringFilterMode {
        CASE_SENSITIVE,
        CASE_INSENSITIVE
    }

    public StringFilter(@NotNull String value) {
        this(value, StringFilterType.CONTAINS);
    }

    public StringFilter(@NotNull String value, @NotNull StringFilterType type) {
        this(value, type, StringFilterMode.CASE_INSENSITIVE);
    }

    public StringFilter(@NotNull String value, @NotNull StringFilterType type, @NotNull StringFilterMode mode) {
        this.value = value;
        this.type = type;
        this.mode = mode;
    }

    @Override
    public boolean isPresent() {
        return value != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Predicate toPredicate(List<String> fieldNames, Path<?> root, CriteriaBuilder builder) {
        String fieldName = fieldNames.stream().findFirst().orElseThrow();
        Expression<String> resolvedPath = PathResolver.resolve(fieldName, root);

        String filterValue = value;

        if (mode == StringFilterMode.CASE_INSENSITIVE) {
            filterValue = filterValue.toLowerCase();
            resolvedPath = builder.lower(resolvedPath);
        }

        switch (type) {
            case CONTAINS -> {
                return builder.like(resolvedPath, "%" + filterValue + "%");
            }
            case STARTS_WITH -> {
                return builder.like(resolvedPath, filterValue + "%");
            }
            case EXACT_MATCH -> {
                return builder.equal(resolvedPath, filterValue);
            }
            default -> throwFilterProcessingError();
        }
        throw new IllegalStateException(); // should not get here
    }

}
