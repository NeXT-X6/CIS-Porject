package dltest1;

import akka.actor.*;
import dltest1.Throw.throw_msg;

public class Catch2 extends AbstractActor{
    public static Props props(){
        return Props.create(Catch2.class, () -> new Catch2());
    }

    public static class catch2_msg{
        public String msg;
    
        public catch2_msg(String msg){
            this.msg=msg;
        }
    }

    public Catch2(){}
    
    @Override
    public Receive createReceive(){
        return receiveBuilder()
            .match(throw_msg.class, x ->{
                sender().tell("ok",getSelf());
            })
            .build();
    }
}