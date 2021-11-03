package cn.xzxy.lewy.luaj;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaThread;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

/**
 * Example that continually launches coroutines, and illustrates how to make
 * sure the orphaned coroutines are cleaned up properly.
 * <p>
 * Main points:
 * <ul><li>Each coroutine consumes one Java Thread while active or reference anywhere</li>
 * <li>All references to a coroutine must be dropped for the coroutine to be collected</li>
 * <li>Garbage collection must be run regularly to remove weak references to lua threads</li>
 * <li>LuaThread.thread_orphan_check_interval must be short enough to find orphaned references quickly</li>
 * </ul>
 */
public class CollectingOrphanedCoroutines {

  // 以下是在循环中一次又一次启动协程的脚本
  // 用于定期进行垃圾收集以查找和删除孤立线程，协程在完成时退出。
  static String script =
      "i,n = 0,0\n print(i)\n"
          + "f = function() n=n+1; coroutine.yield(false) end\n"
          + "while true do\n"
          + "  local cor = coroutine.wrap(f)\n"
          + "  cor()\n"
          + "  i = i + 1\n"
          + "  if i % 1000 == 0 then\n"
          + "    collectgarbage()\n"
          + "    print('threads:', i, 'executions:', n, collectgarbage('count'))\n"
          + "  end\n"
          + "end\n";

  public static void main(String[] args) throws InterruptedException {
    // 计时器控制每个Java线程的唤醒频率，并检查它是否被处理过。
    // 大量的线程会在孤立状态和收集之间产生很长的延迟，如果有很多线程，少量的线程会消耗孤立状态的资源轮询。
    LuaThread.thread_orphan_check_interval = 500;

    // Should work with standard or debug globals.
    Globals globals = JsePlatform.standardGlobals();
    // Globals globals = JsePlatform.debugGlobals();

    // Should work with plain compiler or lua-to-Java compiler.
    org.luaj.vm2.luajc.LuaJC.install(globals);;

    // Load and run the script, which launches coroutines over and over forever.
    LuaValue chunk = globals.load(script, "main");
    chunk.call();
  }

}