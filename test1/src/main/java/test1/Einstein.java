package test1;

import akka.actor.*;
import test1.Beethoven.Beethoven_msg;
import test1.Darwin.Darwin_msg;
import test1.Celsius.Celsius_msg;

public class Einstein extends AbstractActor{

    public static Props props(){
        return Props.create(Einstein.class, () -> new Einstein());
    }

    public static class Einstein_msg{
        public String msg;
    
        public Einstein_msg(String msg){
            this.msg=msg;
        }
    }

    public Einstein(){}
    
    @Override
    public Receive createReceive(){
        return receiveBuilder()
            .match(Darwin_msg.class, x ->{
                sender().tell("ok",getSelf());
            })
            .match(Celsius_msg.class, x->{
                sender().tell(new Einstein_msg("i love you"),getSelf());
            })
            .build();
    }
}