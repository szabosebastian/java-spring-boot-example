package hu.example.javaspringbootexample.common.filter;

import hu.example.javaspringbootexample.common.filter.type.Filter;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterBuilder {

    private final List<FilterParameters<?>> filterParameterList = new ArrayList<>();

    public <F extends Filter> FilterBuilder filter(String name, F filter) {
        return filter(name, filter, null);
    }

    public <F extends Filter> FilterBuilder filter(String name, F filter, F defaultFilter) {
        return filter(new FilterParameters<>(List.of(name), filter, defaultFilter));
    }

    public <F extends Filter> FilterBuilder filter(FilterParameters<F> filterParameters) {
        filterParameterList.add(filterParameters);
        return this;
    }

    public <T> Specification<T> build() {
        return (root, criteriaQuery, criteriaBuilder) ->
                FilterPredicateBuilder.with(root, criteriaQuery, criteriaBuilder)
                        .filter(filterParameterList)
                        .distinct()
                        .build();
    }

    public static <T> Specification<T> fromObject(Object filterObject) {
        return (root, criteriaQuery, criteriaBuilder) ->
                FilterPredicateBuilder.with(root, criteriaQuery, criteriaBuilder)
                        .filter(filterObject)
                        .distinct()
                        .build();
    }

    // todo: kell ez?
    public static <T> Specification<T> of(String entityFieldName, Filter filter) {
        return new FilterBuilder().filter(entityFieldName, filter).build();
    }

    @SafeVarargs
    public static <T> Specification<T> or(Specification<T>... specifications) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            criteriaQuery.distinct(true);
            return criteriaBuilder.or(
                    Arrays.stream(specifications)
                            .map(s -> s.toPredicate(root, criteriaQuery, criteriaBuilder))
                            .toArray(Predicate[]::new)
            );
        };
    }

    @SafeVarargs
    public static <T> Specification<T> and(Specification<T>... specifications) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            criteriaQuery.distinct(true);
            return criteriaBuilder.and(
                    Arrays.stream(specifications)
                            .map(s -> s.toPredicate(root, criteriaQuery, criteriaBuilder))
                            .toArray(Predicate[]::new)
            );
        };
    }

    public static <T> Specification<T> not(Specification<T> specification) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            criteriaQuery.distinct(true);
            return criteriaBuilder.not(
                    specification.toPredicate(root, criteriaQuery, criteriaBuilder)

            );
        };
    }

    @SuppressWarnings("unchecked")
    public static <T> Specification<T> complement(Specification<T> specification) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            criteriaQuery.distinct(true);

            Subquery<? extends T> subquery = criteriaQuery.subquery(root.getJavaType());
            Root<T> sqRoot = (Root<T>) subquery.from(root.getJavaType());

            String idField = sqRoot.getModel().getId(Object.class).getName();
            subquery.select(sqRoot.get(idField))
                    .where(specification.toPredicate(sqRoot, criteriaQuery, criteriaBuilder));

            return criteriaBuilder.in(root).value(subquery).not();
        };
    }

}
