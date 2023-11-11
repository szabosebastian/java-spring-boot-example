package hu.example.javaspringbootexample.common.filter;

import hu.example.javaspringbootexample.common.filter.type.Filter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
public class FilterParameters<F extends Filter> {

    @Getter
    private final List<String> fieldNames;

    private final F filter;
    private final F defaultFilter;

    public F getFilter() {
        if (filter != null && filter.isPresent()) {
            return filter;
        }
        return defaultFilter;
    }

    public boolean isPresent() {
        return (fieldNames != null && !fieldNames.isEmpty()) &&
                (hasFilter() || hasDefaultFilter());
    }

    private boolean hasFilter() {
        return filter != null && filter.isPresent();
    }

    private boolean hasDefaultFilter() {
        return defaultFilter != null && defaultFilter.isPresent();
    }

}
