package com.lifeng.parser.analysis;

import com.lifeng.parser.antlr.ExprBaseVisitor;
import com.lifeng.parser.antlr.ExprParser;

import java.util.HashMap;
import java.util.Map;

public class ExprBaseVisitorImpl extends ExprBaseVisitor<Integer> {
    private static final Map<String, Integer> TMP_RESULTS = new HashMap<>();
    @Override
    public Integer visitAddSub(ExprParser.AddSubContext ctx) {
        int left = visit(ctx.expr(0));
        int right  = visit(ctx.expr(1));
        if (ctx.op.getType() == ExprParser.ADD){
            return left + right;
        }
        else {
            return left - right;
        }
    }

    @Override
    public Integer visitAssign(ExprParser.AssignContext ctx) {
        String id = ctx.ID().getText();
        int result = visit(ctx.expr());
        TMP_RESULTS.put(id, result);
        return result;
    }

    @Override
    public Integer visitId(ExprParser.IdContext ctx) {
        if (TMP_RESULTS.containsKey(ctx.ID().getText())){
            return TMP_RESULTS.get(ctx.ID().getText());
        }
        return 0;
    }

    @Override
    public Integer visitInt(ExprParser.IntContext ctx) {
        return Integer.valueOf(ctx.INT().getText());
    }

    @Override
    public Integer visitMultiDiv(ExprParser.MultiDivContext ctx) {
        int left = visit(ctx.expr(0));
        int right = visit(ctx.expr(1));
        if (ctx.op.getType() == ExprParser.MUL) {
            return left * right;
        } else {
            return left / right;
        }
    }

    @Override
    public Integer visitPrintExpr(ExprParser.PrintExprContext ctx) {
        int result = visit(ctx.expr());
        System.out.println(result);
        return 0;
    }

    @Override
    public Integer visitParents(ExprParser.ParentsContext ctx) {
        return visit(ctx.expr());
    }
}
