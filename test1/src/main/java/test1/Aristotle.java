package test1;

import akka.actor.*;
import test1.Beethoven.Beethoven_msg;

public class Aristotle extends AbstractActor{

    public static Props props(){
        return Props.create(Aristotle.class, () -> new Aristotle());
    }

    public static class Aristotle_msg{
        public String msg;
    
        public Aristotle_msg(String msg){
            this.msg=msg;
        }
    }

    public Aristotle(){}
    
    @Override
    public Receive createReceive(){
        return receiveBuilder()
            .match(Beethoven_msg.class, x ->{
                sender().tell("ok",getSelf());
            })
            .build();
    }
}