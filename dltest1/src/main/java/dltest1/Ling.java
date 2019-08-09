package dltest1;

import akka.actor.*;
import dltest1.Ping.ping_msg;

public class Ling extends AbstractActor{
    public static Props props(){
        return Props.create(Ling.class, () -> new Ling());
    }

    public static class ling_msg{
        public String msg;
    
        public ling_msg(String msg){
            this.msg=msg;
        }
    }

    public Ling(){}
    
    @Override
    public Receive createReceive(){
        return receiveBuilder()
            .match(ping_msg.class, x ->{
                sender().tell("ok",getSelf());
            })
            .build();
    }
}