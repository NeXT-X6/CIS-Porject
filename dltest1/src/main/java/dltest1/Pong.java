package dltest1;

import akka.actor.*;
import dltest1.Ling.ling_msg;

public class Pong extends AbstractActor{
    public static Props props(){
        return Props.create(Pong.class, () -> new Pong());
    }

    public static class pong_msg{
        public String msg;
    
        public pong_msg(String msg){
            this.msg=msg;
        }
    }

    public Pong(){}
    
    @Override
    public Receive createReceive(){
        return receiveBuilder()
            .match(ling_msg.class, x ->{
                sender().tell("ok",getSelf());
            })
            .build();
    }
}