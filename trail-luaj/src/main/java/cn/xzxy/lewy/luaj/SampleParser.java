package cn.xzxy.lewy.luaj;

import org.luaj.vm2.ast.Chunk;
import org.luaj.vm2.ast.Exp;
import org.luaj.vm2.ast.Stat;
import org.luaj.vm2.ast.Visitor;
import org.luaj.vm2.parser.LuaParser;
import org.luaj.vm2.parser.ParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class SampleParser {

    static public void main(String[] args) {
        try {
            File file = new File(Objects.requireNonNull(SampleParser.class.getClassLoader()
                    .getResource("lua/reference.lua")).getFile());

            // Create a LuaParser. This will fill in line and column number
            // information for most exceptions.
            LuaParser parser = new LuaParser(new FileInputStream(file));

            // Perform the parsing.
            Chunk chunk = parser.Chunk();

            // Print out line info for all function definitions.
            chunk.accept(new Visitor() {
                public void visit(Exp.AnonFuncDef exp) {
                    System.out.println("Anonymous function definition at "
                            + exp.beginLine + "." + exp.beginColumn + ","
                            + exp.endLine + "." + exp.endColumn);
                }

                public void visit(Stat.FuncDef stat) {
                    System.out.println("Function definition '" + stat.name.name.name + "' at "
                            + stat.beginLine + "." + stat.beginColumn + ","
                            + stat.endLine + "." + stat.endColumn);

                    System.out.println("\tName location "
                            + stat.name.beginLine + "." + stat.name.beginColumn + ","
                            + stat.name.endLine + "." + stat.name.endColumn);
                }

                public void visit(Stat.LocalFuncDef stat) {
                    System.out.println("Local function definition '" + stat.name.name + "' at "
                            + stat.beginLine + "." + stat.beginColumn + ","
                            + stat.endLine + "." + stat.endColumn);
                }
            });

        } catch (ParseException e) {
            System.out.println("parse failed: " + e.getMessage() + "\n"
                    + "Token Image: '" + e.currentToken.image + "'\n"
                    + "Location: " + e.currentToken.beginLine + ":" + e.currentToken.beginColumn
                    + "-" + e.currentToken.endLine + "," + e.currentToken.endColumn);

        } catch (IOException e) {
            System.out.println("IOException occurred: " + e);
            e.printStackTrace();
        }
    }
}
