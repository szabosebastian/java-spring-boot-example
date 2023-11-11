package hu.example.javaspringbootexample.common.filter;

import hu.example.javaspringbootexample.common.filter.exception.UnknownFieldException;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Path;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class PathResolver {

    private PathResolver() {
        // utility class
    }

    public static Path resolve(String rawPath, Path<?> root) {
        try {
            List<String> pathElements = Arrays.stream(rawPath.split("\\.")).collect(Collectors.toList());
            Path<?> resolvedPath = root;
            for (String element : pathElements) {
                if (isLastElement(element, pathElements)) {
                    resolvedPath = resolvedPath.get(element);
                } else {
                    resolvedPath = ((From<?, ?>) resolvedPath).join(element);
                }
            }
            return resolvedPath;

        } catch (Exception e) {
            throw new UnknownFieldException(
                    String.format("Entity (%s) cannot be filtered by field: %s", root.getJavaType().getName(), rawPath), e);
        }
    }

    public static <T> void validate(String rawPath, Class<T> clazz) {
        try {
            String[] pathElements = rawPath.split("\\."); // split the path into field names
            Class<?> tClass = clazz;
            for (String element : pathElements) {
                if (shouldJoin(tClass, element)) { // if the field name is Collection<T> we have to get the actual type argument otherwise the next class would be Collection instead of T
                    tClass =
                            (Class<?>) (
                                    (ParameterizedType) tClass.getDeclaredField(element).getGenericType()
                            ).getActualTypeArguments()[0];
                } else { // if the field name is not a collection we can get the element type
                    tClass = tClass.getDeclaredField(element).getType();
                }
            }

        } catch (Exception e) { // we should get here only if the field being checked does not exist in the current class
            throw new UnknownFieldException(
                    String.format("Entity (%s) cannot be filtered by field: %s Field is not valid.", clazz.getName(), rawPath), e);
        }
    }

    private static boolean isLastElement(String element, List<String> pathElements) {
        return pathElements.indexOf(element) == pathElements.size() - 1;
    }

    private static boolean shouldJoin(Class<?> clazz, String field) throws NoSuchFieldException {
        return Collection.class.isAssignableFrom(clazz.getDeclaredField(field).getType());
    }

}
