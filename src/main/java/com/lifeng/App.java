package com.lifeng;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.lifeng.network.NettyMessage;
import com.lifeng.tuple.Tuple;
import io.netty.buffer.*;

import java.util.ArrayList;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
//        try {
//            Terminal terminal = TerminalBuilder.terminal();
//            LineReader lineReader = LineReaderBuilder.builder().terminal(terminal).build();
//            String prompt = ">";
//            PrintWriter out = new PrintWriter(System.out);
//            while (true){
//                out.flush();
//                String line = lineReader.readLine(prompt);
//
//                ExprLexer lexer = new ExprLexer(CharStreams.fromString(line+System.lineSeparator()));
//                CommonTokenStream tokens = new CommonTokenStream(lexer);
//                ExprParser parser = new ExprParser(tokens);
//                ParseTree tree = parser.prog(); // parse
//
//                ExprBaseVisitorImpl visitor = new ExprBaseVisitorImpl();
//                visitor.visit(tree);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Kryo kryo = new Kryo();
        NettyMessage.StreamMessage streamMessage = new NettyMessage.StreamMessage();
        streamMessage.streams = new ArrayList<>();
        streamMessage.streams.add(new Tuple());

        ByteBuf byteBuf = Unpooled.buffer();
        Output outputStream = new Output(new ByteBufOutputStream(byteBuf));

        kryo.writeObject(outputStream, streamMessage);
        outputStream.close();
        System.out.println(byteBuf);


        Input input = new Input(new ByteBufInputStream(byteBuf));
        NettyMessage.StreamMessage ls = kryo.readObject(input, NettyMessage.StreamMessage.class);
        input.close();
        System.out.println(ls.streams.get(0));
    }

}
