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
public class ReverseRangeFilter<C extends Comparable> implements Filter {

    private C value;

    private ReverseRangeFilterType type = ReverseRangeFilterType.REVERSE_RANGE;

    public enum ReverseRangeFilterType {
        REVERSE_RANGE
    }

    public ReverseRangeFilter(C value) {
        this.value = value;
    }

    @Override
    public boolean isPresent() {
        return value != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Predicate toPredicate(List<String> fieldNames, Path<?> root, CriteriaBuilder builder) {
        if (fieldNames.size() != 2) {
            throwFilterProcessingError();
        }

        Path<C> fromPath = PathResolver.resolve(fieldNames.get(0), root);
        Path<C> toPath = PathResolver.resolve(fieldNames.get(1), root);

        return builder.between(builder.literal(value), fromPath, toPath);
    }
}
