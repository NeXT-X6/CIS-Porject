package test1;

import akka.actor.*;
import test1.Beethoven.Beethoven_msg;

public class Galileo extends AbstractActor{

    public static Props props(){
        return Props.create(Galileo.class, () -> new Galileo());
    }

    public static class Galileo_msg{
        public String msg;
    
        public Galileo_msg(String msg){
            this.msg=msg;
        }
    }

    public Galileo(){}
    
    @Override
    public Receive createReceive(){
        return receiveBuilder()
            .match(Beethoven_msg.class, x ->{
                sender().tell("ok",getSelf());
            })
            .build();
    }
}