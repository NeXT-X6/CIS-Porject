package dltest1;

import akka.actor.*;
import dltest1.Pong.pong_msg;

public class Ping extends AbstractActor{
    public static Props props(){
        return Props.create(Ping.class, () -> new Ping());
    }

    public static class ping_msg{
        public String msg;
    
        public ping_msg(String msg){
            this.msg=msg;
        }
    }

    public Ping(){}

    @Override
    public Receive createReceive(){
        return receiveBuilder()
            .match(pong_msg.class,x -> {
                sender().tell("ok",getSelf());
            })
            .build();
    }
}