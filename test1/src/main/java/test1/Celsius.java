package test1;

import akka.actor.*;
import test1.Aristotle.Aristotle_msg;
import test1.Darwin.Darwin_msg;
import test1.Einstein.Einstein_msg;

public class Celsius extends AbstractActor{

    public static Props props(){
        return Props.create(Celsius.class, () -> new Celsius());
    }

    public static class Celsius_msg{
        public String msg;
    
        public Celsius_msg(String msg){
            this.msg=msg;
        }
    }

    public Celsius(){}
    
    @Override
    public Receive createReceive(){
        return receiveBuilder()
            .match(Darwin_msg.class, x ->{
                sender().tell(new Celsius_msg("i love you"),getSelf());
            })
            .match(Aristotle_msg.class, x ->{
                sender().tell("ok",getSelf());
            })
            .build();
    }
}