package dltest1;

import akka.actor.*;
import dltest1.Catch.catch_msg;

public class Throw2 extends AbstractActor{
    public static Props props(){
        return Props.create(Throw2.class, () -> new Throw2());
    }

    public static class throw2_msg{
        public String msg;
    
        public throw2_msg(String msg){
            this.msg=msg;
        }
    }

    public Throw2(){}
    
    @Override
    public Receive createReceive(){
        return receiveBuilder()
            .match(catch_msg.class, x ->{
                sender().tell("ok",getSelf());
            })
            .build();
    }
}