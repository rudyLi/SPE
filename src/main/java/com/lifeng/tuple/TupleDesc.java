package com.lifeng.tuple;

import java.util.ArrayList;
import java.util.List;

public class TupleDesc {
    private List<String> fieldNames = new ArrayList<>();
    public List getFieldNames() {
        return fieldNames;
    }
    public TupleDesc(List<String> fieldNames) {
        this.fieldNames = fieldNames;
    }

    public void addFiledNames(String ...fields) {
        for (String field : fields) {
            fieldNames.add(field);
        }

    }
}
