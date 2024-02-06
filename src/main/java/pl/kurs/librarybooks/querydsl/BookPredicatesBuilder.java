package pl.kurs.librarybooks.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BookPredicatesBuilder {
    private List<SearchCriteria> params;

    public BookPredicatesBuilder() {
        params = new ArrayList<>();
    }

    public BookPredicatesBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public BooleanExpression build() {
        if (params.isEmpty()) {
            return null;
        }

        List<BooleanExpression> predicates = params.stream()
                .map(param -> {
                    BookPredicate predicate = new BookPredicate(param);
                    return predicate.getPredicate();
                })
                .filter(Objects::nonNull)
                .toList();

        BooleanExpression result = Expressions.asBoolean(true).isTrue();
        for (BooleanExpression predicate : predicates) {
            result = result.and(predicate);
        }
        return result;
    }
}
