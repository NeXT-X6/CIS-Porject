package dltest1;

import akka.actor.*;
import dltest1.Catch.catch_msg;

public class Throw extends AbstractActor{
    public static Props props(){
        return Props.create(Throw.class, () -> new Throw());
    }

    public static class throw_msg{
        public String msg;
    
        public throw_msg(String msg){
            this.msg=msg;
        }
    }

    public Throw(){}
    
    @Override
    public Receive createReceive(){
        return receiveBuilder()
            .match(catch_msg.class, x ->{
                sender().tell("ok",getSelf());
            })
            .build();
    }
}