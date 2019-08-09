package test1;

import akka.actor.*;
import test1.Celsius.Celsius_msg;
import test1.Einstein.Einstein_msg;

public class Beethoven extends AbstractActor{

    public static Props props(){
        return Props.create(Beethoven.class, () -> new Beethoven());
    }

    public static class Beethoven_msg{
        public String msg;
    
        public Beethoven_msg(String msg){
            this.msg=msg;
        }
    }

    public Beethoven(){}
    
    @Override
    public Receive createReceive(){
        return receiveBuilder()
            .match(Celsius_msg.class, x ->{
                sender().tell("ok",getSelf());
            })
            .match(Einstein_msg.class, x ->{
                sender().tell(new Beethoven_msg("i love you"),getSelf());
            })
            .build();
    }
}