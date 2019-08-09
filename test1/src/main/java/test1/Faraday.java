package test1;

import akka.actor.*;
import test1.Beethoven.Beethoven_msg;

public class Faraday extends AbstractActor{

    public static Props props(){
        return Props.create(Faraday.class, () -> new Faraday());
    }

    public static class Faraday_msg{
        public String msg;
    
        public Faraday_msg(String msg){
            this.msg=msg;
        }
    }

    public Faraday(){}
    
    @Override
    public Receive createReceive(){
        return receiveBuilder()
            .match(Beethoven_msg.class, x ->{
                sender().tell("ok",getSelf());
            })
            .build();
    }
}