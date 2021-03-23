package cn.xzxy.lewy.luaj;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Objects;

public class LuaDemo {
    Globals globals = JsePlatform.standardGlobals();

    public void compile(File file) throws FileNotFoundException {
        globals.load(new FileReader(file), "script").call();
    }

    public void invoke(String func, Object... parameters) {
        if (parameters != null && parameters.length > 0) {
            LuaValue[] values = new LuaValue[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                values[i] = CoerceJavaToLua.coerce(parameters[i]);
            }
            globals.get(func).call(LuaValue.listOf(values));
        } else {
            globals.get(func).call();
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        LuaDemo luaDemo = new LuaDemo();
        File file = new File(Objects.requireNonNull(LuaDemo.class.getClassLoader().getResource("learning.lua")).getFile());
        luaDemo.compile(file);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            int sum = 0;
            for (int j = 1; j <= 10000; j++) {
                sum += j;
            }
            System.out.println(sum);
        }
        System.out.println("java cost time : " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            luaDemo.invoke("test");
        }

        System.out.println("lua cost time: " + (System.currentTimeMillis() - start));
    }
}
