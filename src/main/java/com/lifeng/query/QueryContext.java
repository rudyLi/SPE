package com.lifeng.query;

import com.facebook.presto.sql.parser.SqlParser;
import com.facebook.presto.sql.tree.*;
import java.util.ArrayList;
import java.util.List;

public class QueryContext {
    private QuerySpecification queryBody;

    public QueryContext(String sql) {
        init(sql);
    }
    private void init(String sql) {
        SqlParser SQL_PARSER = new SqlParser();
        Query query = (Query)SQL_PARSER.createStatement(sql);
        queryBody = (QuerySpecification) query.getQueryBody();
    }

    public boolean hasLimit() {
        return queryBody.getLimit().isPresent();
    }

    public Integer getLimit() {
        return hasLimit() ? Integer.valueOf(queryBody.getLimit().get()) : null;
    }

    // here just use the simplest sql
    public List<String> getSelectFields() {
        List<String> selectField = new ArrayList<>();
       for (SelectItem item : queryBody.getSelect().getSelectItems()) {
           if (item instanceof SingleColumn) {
               Expression expression = ((SingleColumn)item).getExpression();
               if (expression instanceof Identifier) {
                   selectField.add(((Identifier)expression).getValue());
               }

           }
       }
       return selectField;
    }
}
