package hu.example.javaspringbootexample.common.filter;

import hu.example.javaspringbootexample.common.filter.annotation.FilterField;
import hu.example.javaspringbootexample.common.filter.exception.InvalidFilterException;
import hu.example.javaspringbootexample.common.filter.type.Filter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FilterPredicateBuilder {

    private final Path<?> root;
    private final CriteriaQuery<?> query;
    private final CriteriaBuilder builder;

    private final List<Predicate> predicateList = new ArrayList<>();

    private FilterPredicateBuilder(Path<?> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        this.root = root;
        this.query = query;
        this.builder = builder;
    }

    public static FilterPredicateBuilder with(Path<?> path, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return new FilterPredicateBuilder(path, query, builder);
    }

    public <F extends Filter> FilterPredicateBuilder filter(String entityFieldName, F filter) {
        return filter(entityFieldName, filter, null);
    }

    public <F extends Filter> FilterPredicateBuilder filter(String entityFieldName, F filter, F defaultFilter) {
        return filter(new FilterParameters<>(List.of(entityFieldName), filter, defaultFilter));
    }

    public FilterPredicateBuilder filter(FilterParameters<?> filterParameters) {
        for (String fieldName : filterParameters.getFieldNames()) {
            PathResolver.validate(fieldName, root.getJavaType()); // this checks the entityFieldName validity even when the filter or the filter value is null
        }

        if (filterParameters.isPresent()) {
            predicateList.add(filterParameters.getFilter().toPredicate(filterParameters.getFieldNames(), root, builder));
        }
        return this;
    }

    public FilterPredicateBuilder filter(List<FilterParameters<?>> filterParametersList) {
        for (FilterParameters<?> filterParameters : filterParametersList) {
            filter(filterParameters);
        }
        return this;
    }

    @SneakyThrows
    public FilterPredicateBuilder filter(Object filterObject) {
        if (filterObject == null) {
            return this;
        }

        for (Field filterField : filterObject.getClass().getDeclaredFields()) {
            if (!filterField.isSynthetic()) {
                addFilter(filterObject, filterField);
            }
        }

        return this;
    }

    public FilterPredicateBuilder distinct() {
        query.distinct(true);
        return this;
    }

    public Predicate build() {
        return builder.and(predicateList.toArray(new Predicate[0]));
    }

    private void addFilter(Object filterObject, Field filterField) throws IllegalAccessException {
        filterField.setAccessible(true);
        Object filterValue = filterField.get(filterObject);
        if (filterValue == null) {
            return;
        }

        if (!isFilter(filterValue)) {
            throw new InvalidFilterException(String.format("Unable to filter by field: '%s %s', in class '%s'",
                    filterField.getType().getName(), filterField.getName(), filterObject.getClass().getName()));
        }

        this.filter(getFieldName(filterField), (Filter) filterValue);
    }

    private boolean isFilter(Object filterObject) {
        return Filter.class.isAssignableFrom(filterObject.getClass());
    }

    private String getFieldName(Field filterField) {
        FilterField annotation = filterField.getAnnotation(FilterField.class);
        if (annotation != null) {
            return annotation.value();
        }

        return filterField.getName();
    }

}
