package cn.xzxy.lewy.akka;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

/**
 * GreeterMain ：Greeter 监护者
 */
public class GreeterMain extends AbstractBehavior<GreeterMain.SayHello> {

    // 定义了一种消息类型（GreeterMain.SayHello）
    public static class SayHello {
        public final String name;

        public SayHello(String name) {
            this.name = name;
        }
    }

    // ActorRef 为 Actor 实例引用
    // 由于actor是通过消息驱动的，无法直接获取actor的实例，需要通过ActorRef来向actor投递消息
    private final ActorRef<Greeter.Greet> greeter;

    public static Behavior<SayHello> create() {
        return Behaviors.setup(GreeterMain::new);
    }

    private GreeterMain(ActorContext<SayHello> context) {
        super(context);
        // GreeterMain 创建了一个 Greeter.Greet 类型的 Actor
        System.out.println("@@@ 1.5: GreeterMain 运行时创建了一个 Greeter.Greet 类型的 Actor");
        greeter = context.spawn(Greeter.create(), "greeter");
    }

    /**
     * greetMain 消息处理类
     */
    @Override
    public Receive<SayHello> createReceive() {
        // @@@ 第二步：GreeterMain 监听消息，有消息时调用 onSayHello 方法
        System.out.println("@@@ 2：GreeterMain 监听消息，有消息时调用 onSayHello 方法");
        return newReceiveBuilder().onMessage(SayHello.class, this::onSayHello).build();
    }

    private Behavior<SayHello> onSayHello(SayHello command) {
        // @@@ 第三步：GreeterMain onSayHello 被调用，消息内容为: Muller
        System.out.println("@@@ 3：GreeterMain onSayHello 被调用，消息内容为: " + command.name);
        // @@@ 第四步：GreeterMain onSayHello 中通过Behavior的getContext()方法来生产 ChildActor
        System.out.println("@@@ 4：GreeterMain onSayHello 中通过Behavior的getContext()方法来生产 ChildActor ");
        ActorRef<Greeter.Greeted> replyTo = getContext().spawn(GreeterBot.create(3), command.name);
        // @@@ 第六步: greeter ActorRef 投递消息给 Greeter.Greet Actor
        System.out.println("@@@ 6: greeter ActorRef 投递消息给 Greeter.Greet Actor");
        // Greeter.Greet(参数一：消息内容，参数二：接收者实例引用 replyTo，即 Greeter.Greeted Actor)
        greeter.tell(new Greeter.Greet(command.name, replyTo));
        return this;
    }
}
