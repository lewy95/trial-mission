package cn.xzxy.lewy.akka.official;

import akka.actor.typed.ActorSystem;

/**
 * Akka 官方入门案例 JAVA 版
 * <p>
 * 本例中涉及三种消息类型，可以理解为三种Actor
 * 1. GreeterMain.sayHello Actor
 * 2. Greeter.greet Actor
 * 3. Greeter.greeted Actor
 * <p>
 * 这三种Actor分别由三个类完成
 * 三个类都继承了AbstractBehavior<R>，R是消息类型，都需要实现createReceive方法，该方法中定义消息处理逻辑
 */
public class AkkaQuickstart {
    public static void main(String[] args) {
        // actorSystem，akka的入口，有且只有一个
        final ActorSystem<GreeterMain.SayHello> greeterMain = ActorSystem.create(GreeterMain.create(), "helloAkka");

        // @@@ 第一步：GreeterMain actor 发送 SayHello 消息类型
        System.out.println("@@@ 1：greeterMain 发送 SayHello 消息类型，消息内容为 Muller");
        // tell的作用只是通过 actorRef 将消息丢给 Actor 去处理
        greeterMain.tell(new GreeterMain.SayHello("Muller"));
        // greeterMain: class akka.actor.typed.internal.adapter.ActorSystemAdapter
        // greeterMain: akka://helloAkka

        // --------------------------------------------------
        greeterMain.terminate();
    }
}
