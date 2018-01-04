package com.lifeng.query;

import com.lifeng.query.operator.FieldSelect;
import com.lifeng.query.operator.Limit;
import com.lifeng.query.operator.Scan;
import com.lifeng.storage.MemoryStorage;
import com.lifeng.storage.Storage;
import com.lifeng.tuple.Tuple;

public class Optimizer {
    public static Cursor query(QueryContext query, Storage storage){
        return new Cursor(_buildQueryPlan(query, storage));
    }

    private static QueryPlan _buildQueryPlan(QueryContext query, Storage storage) {

        // resolve the query body
        QueryPlan queryPlan = new QueryPlan();
        if (query.hasLimit()) {
            queryPlan.setHeadOperator(new FieldSelect(query.getSelectFields(),
                                           new Limit(
                                                   new Scan(storage), query.getLimit())));
        }
        else {
            queryPlan.setHeadOperator(new FieldSelect(query.getSelectFields(),
                                           new Scan(storage)));
        }
        return queryPlan;
    }

    public static void main(String[] args) throws Exception {
        Cursor cursor = Optimizer.query(new QueryContext("select a from test limit 1"),new MemoryStorage());
        cursor.open();
        while (true) {
            Tuple tuple = cursor.next();
            if (tuple == null) {
                break;
            }
        }
        cursor.close();
    }
}
