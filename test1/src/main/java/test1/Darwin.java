package test1;

import akka.actor.*;
import test1.Aristotle.Aristotle_msg;
import test1.Einstein.Einstein_msg;

public class Darwin extends AbstractActor{

    public static Props props(){
        return Props.create(Darwin.class, () -> new Darwin());
    }

    public static class Darwin_msg{
        public String msg;
    
        public Darwin_msg(String msg){
            this.msg=msg;
        }
    }

    public Darwin(){}
    
    @Override
    public Receive createReceive(){
        return receiveBuilder()
            .match(Aristotle_msg.class, x ->{
                sender().tell(new Darwin_msg("i love you"),getSelf());
            })
            .match(Einstein_msg.class, x ->{
                sender().tell("ok",getSelf());
            })
            .build();
    }
}