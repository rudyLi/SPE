package com.lifeng.query.operator;

import com.lifeng.tuple.Tuple;
import com.lifeng.tuple.TupleDesc;

import java.util.List;

public class FieldSelect implements OperatorIterator {
    private List<String> selectFieldNames;
    private OperatorIterator parent;

    public FieldSelect(final List<String> selectField, OperatorIterator parent) {
        this.selectFieldNames = selectField;
        this.parent = parent;
    }
    @Override
    public void open() throws Exception {
        parent.open();
    }

    @Override
    public Tuple next() throws Exception {
        Tuple tmp= parent.next();
        if (tmp != null) {
            Tuple result = new Tuple(new TupleDesc(selectFieldNames));
            for(String selectField: selectFieldNames) {
                int index = tmp.getTupleDesc().getFieldNames().indexOf(selectField);
                if (index >= 0) {
                    result.addValue(tmp.getValues().get(index));
                }
                else {

                    // pad null value
                    result.addValue(null);
                }
            }
            return result;
        }
        return null;
    }

    @Override
    public void close() throws Exception {
        parent.close();

    }
}
