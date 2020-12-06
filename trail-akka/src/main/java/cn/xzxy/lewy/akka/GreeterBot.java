package cn.xzxy.lewy.akka;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

/**
 * GreeterBot ：Greeter 回复者
 */
public class GreeterBot extends AbstractBehavior<Greeter.Greeted> {

    // 定义了 GreeterBot 行为构造器
    // Behavior后面指定的是发送的消息类型
    public static Behavior<Greeter.Greeted> create(int max) {
        // @@@ 第五步：GreeterBot Actor 初始化
        System.out.println("@@@ 5：GreeterBot Actor 完成构造");
        return Behaviors.setup(context -> new GreeterBot(context, max));
    }

    private final int max;
    private int greetingCounter;

    private GreeterBot(ActorContext<Greeter.Greeted> context, int max) {
        super(context);
        this.max = max;
    }

    @Override
    public Receive<Greeter.Greeted> createReceive() {
        // GreeterBot 消息处理类，调用 onGreeted 方法
        // @@@ 第八步：GreeterBot.createReceive 接受到消息，调用 onGreeted 方法
        System.out.println("@@@ 8: Greeter.Greeted.createReceive load 将处理Greeter的Greeted消息，并调用 onGreeted 方法");
        return newReceiveBuilder().onMessage(Greeter.Greeted.class, this::onGreeted).build();
    }

    private Behavior<Greeter.Greeted> onGreeted(Greeter.Greeted message) {
        greetingCounter++;
        getContext().getLog().info("Greeting {} for {}", greetingCounter, message.whom);
        if (greetingCounter == max) {
            // 回复数量超过三，结束
            System.out.println("@@@ 10->1: GreeterBot.onGreeted 执行，限定接受次数已到达，结束");
            return Behaviors.stopped();
        } else {
            // 发送回复消息
            System.out.println("@@@ 10->2: GreeterBot.onGreeted 执行，限定接受次数未到，通过greeter引用将消息扔给Greeter.Greet Actor去处理");
            message.from.tell(new Greeter.Greet(message.whom, getContext().getSelf()));
            return this;
        }
    }
}
