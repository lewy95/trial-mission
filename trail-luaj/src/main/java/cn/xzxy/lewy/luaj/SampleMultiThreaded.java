package cn.xzxy.lewy.luaj;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.luaj.vm2.luajc.LuaJC;

import java.io.IOException;

/**
 * Simple toy program illustrating how to run Luaj in multiple threads.
 *
 * <p>By creating separate Globals in each thread, scripts can be run in each thread.
 *
 * <p>However note the following:
 *  <ul><li>type-related metatables such as LuaNumber.s_metatable are shared by all threads, so are not thread-safe.
 *  </li><li>creating additional threads within a Java element called by lua could result in shared access to globals.
 *  </li></ul>
 * <p>
 * This can be used when all the scripts running can be trusted.
 * For examples of how to sandbox scripts in protect against rogue scripts,
 * see examples/jse/SampLeSandboxed.java.
 */
public class SampleMultiThreaded {

    static class Runner implements Runnable {
        final String script;
        LuaValue luaValue;

        Runner(String script) {
            // Each thread must have its own Globals.
            Globals g = JsePlatform.standardGlobals();
            // LuaJC.install(g); // use LuaJC
            luaValue = g.loadfile(script);

            this.script = script;
        }

        public void run() {
            try {
                luaValue.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(final String[] args) throws IOException {
        final String script = args.length > 0 ? args[0] : "lua/hello.lua";
        try {
            Thread[] thread = new Thread[10];
            for (int i = 0; i < thread.length; ++i)
                thread[i] = new Thread(new Runner(script), "Runner-" + i);
            for (Thread value : thread) value.start();
            for (Thread value : thread) value.join();
            System.out.println("done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

