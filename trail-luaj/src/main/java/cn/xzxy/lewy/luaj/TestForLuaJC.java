package cn.xzxy.lewy.luaj;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.luaj.vm2.luajc.LuaJC;

public class TestForLuaJC {

    /**
     * Globals继承LuaValue对象，LuaValue对象用来表示在Lua语言的基本数据类型，
     * 比如:Nil,Number,String,Table,userdata,Function等。尤其要注意LuaValue也表示了Lua语言中的函数。
     * 所以,对于Lua语言中的函数操作都是通过LuaValue来实现的。
     */

    public static void main(String[] args) {
        Globals globals = JsePlatform.standardGlobals();
        LuaJC.install(globals);
        LuaValue chunk = globals.loadfile("lua/first.lua");
        // function 本质还是一个 LuaValue
        // System.out.println(chunk); // function: lua_first
        // call() 可以认为是编译（运行）
        // System.out.println(chunk.call()); // table: 61832929
         System.out.println(chunk.call().get("test"));
        // LuaValue chunk = globals.load("print('hello, world')", "main.lua");
        System.out.println(chunk.isclosure());  // Will be false when LuaJC is working.
        LuaValue methodName = chunk.get(LuaValue.valueOf("test"));
        System.out.println(methodName);
    }
}
