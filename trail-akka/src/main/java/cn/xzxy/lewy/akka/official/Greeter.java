package cn.xzxy.lewy.akka.official;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

import java.util.Objects;

/**
 * Greeter
 */
public class Greeter extends AbstractBehavior<Greeter.Greet> {

    // Greet 消息
    public static final class Greet {
        public final String whom;
        public final ActorRef<Greeted> replyTo;

        // whom: 消息接受方名称
        // ActorRef<Greeted> replyTo 消息接受方实例引用
        public Greet(String whom, ActorRef<Greeted> replyTo) {
            this.whom = whom; // 可以认为是消息内容
            this.replyTo = replyTo;
        }
    }

    // Greeted 消息 用于确认其他 Actor 的 Greet 消息
    public static final class Greeted {
        public final String whom;
        public final ActorRef<Greet> from;

        public Greeted(String whom, ActorRef<Greet> from) {
            this.whom = whom;
            this.from = from;
        }

        // #greeter
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Greeted greeted = (Greeted) o;
            return Objects.equals(whom, greeted.whom) &&
                    Objects.equals(from, greeted.from);
        }

        @Override
        public int hashCode() {
            return Objects.hash(whom, from);
        }

        @Override
        public String toString() {
            return "Greeted{" +
                    "whom='" + whom + '\'' +
                    ", from=" + from +
                    '}';
        }
        // #greeter
    }

    public static Behavior<Greet> create() {
        return Behaviors.setup(Greeter::new);
    }

    private Greeter(ActorContext<Greet> context) {
        super(context);
    }

    public Receive<Greet> createReceive() {
        // @@@ 第七步： Greeter.Greet.createReceive 接受到消息，调用 onGreet 方法
        System.out.println("@@@ 7: Greeter.createReceive load 将处理Greeter的Greet类型消息，并调用 onGreet 方法");
        return newReceiveBuilder().onMessage(Greet.class, this::onGreet).build();
    }

    private Behavior<Greet> onGreet(Greet command) {
        getContext().getLog().info("Hello {}!", command.whom);
        // @@@ 第九步：Greeter.onGreet 执行，发送消息给 GreetBot Actor
        System.out.println("@@@ 9: Greeter.onGreet 执行，replyTo 发送消息给 Greeter.Greeted Actor 去处理");
        // replyTo 引用将消息扔给 Greeter.Greeted Actor 去处理反馈
        command.replyTo.tell(new Greeted(command.whom, getContext().getSelf()));
        return this;
    }
}
// #greeter

