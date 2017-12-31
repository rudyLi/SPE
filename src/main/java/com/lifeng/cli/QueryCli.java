package com.lifeng.cli;

import com.lifeng.parser.analysis.ExprBaseVisitorImpl;
import com.lifeng.parser.antlr.ExprLexer;
import com.lifeng.parser.antlr.ExprParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.io.PrintWriter;

public class QueryCli {
    public static void main( String[] args )
    {
        try {
            Terminal terminal = TerminalBuilder.terminal();
            LineReader lineReader = LineReaderBuilder.builder().terminal(terminal).build();
            String prompt = ">";
            PrintWriter out = new PrintWriter(System.out);
            while (true){
                out.flush();
                String line = lineReader.readLine(prompt);

                ExprLexer lexer = new ExprLexer(CharStreams.fromString(line+System.lineSeparator()));
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                ExprParser parser = new ExprParser(tokens);
                ParseTree tree = parser.prog(); // parse

                ExprBaseVisitorImpl visitor = new ExprBaseVisitorImpl();
                visitor.visit(tree);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
