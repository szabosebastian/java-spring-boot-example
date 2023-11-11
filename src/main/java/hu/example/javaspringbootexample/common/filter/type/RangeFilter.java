package hu.example.javaspringbootexample.common.filter.type;

import hu.example.javaspringbootexample.common.filter.PathResolver;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RangeFilter<C extends Comparable> implements Filter {

    private C minValue;

    private C maxValue;

    private RangeFilterType type = RangeFilterType.RANGE;

    public enum RangeFilterType {
        RANGE
    }

    public RangeFilter(C minValue, C maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public boolean isPresent() {
        return minValue != null || maxValue != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Predicate toPredicate(List<String> fieldNames, Path<?> root, CriteriaBuilder builder) {
        String fieldName = fieldNames.stream().findFirst().orElseThrow();
        Path<C> resolvedPath = PathResolver.resolve(fieldName, root);

        if (minValue != null && maxValue != null) {
            return builder.between(resolvedPath, minValue, maxValue);
        }

        if (minValue != null && maxValue == null) {
            return builder.greaterThanOrEqualTo(resolvedPath, minValue);
        }

        if (minValue == null && maxValue != null) {
            return builder.lessThanOrEqualTo(resolvedPath, maxValue);
        }

        throwFilterProcessingError();

        throw new IllegalStateException(); // should not get here
    }

}
