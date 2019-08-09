package dltest1;

import akka.actor.*;
import dltest1.Throw.throw_msg;
import dltest1.Throw2.throw2_msg;
import dltest1.Throw;

public class Catch extends AbstractActor{
    public static ActorRef t = ActorSystem.create().actorOf(Throw.props());

    public static Props props(){
        return Props.create(Catch.class, () -> new Catch());
    }

    public static class catch_msg{
        public String msg;
    
        public catch_msg(String msg){
            this.msg=msg;
        }
    }

    public Catch(){}
    
    @Override
    public Receive createReceive(){
        return receiveBuilder()
            .match(throw_msg.class, x ->{
                sender().tell(new catch_msg("fuck"),getSelf());
            })
            .match(throw2_msg.class, x ->{
                sender().tell("ok",getSelf());
            })
            .build();
    }
}