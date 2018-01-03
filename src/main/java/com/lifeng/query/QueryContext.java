package com.lifeng.query;

import com.facebook.presto.sql.parser.SqlParser;
import com.facebook.presto.sql.tree.Statement;

import java.util.ArrayList;
import java.util.List;

public class QueryContext {
    private Statement queryStatement;

    public QueryContext(String sql) {
        init(sql);
    }
    private void init(String sql) {
        SqlParser SQL_PARSER = new SqlParser();
        this.queryStatement = SQL_PARSER.createStatement(sql);
    }

    public boolean hasLimit() {
        return true;
    }

    public Integer getLimit() {
        return 10;
    }

    public List<String> getSelectFields() {
        return new ArrayList<>();
    }
}
