package test.analysis;

import soot.*;
import akka.actor.*;
import java.util.*;
import test.common.Pair;

public class ActorGraph{
    private String actor;
    private ArrayList<Pair<String,String>> RecvSendMapping;

    public ActorGraph(String actor){
        this.actor = actor;
        this.RecvSendMapping = new ArrayList<Pair<String,String>>();
    }

    public void addMapping(Pair<String,String> p){
        RecvSendMapping.add(p);
    }

    public ArrayList<Pair<String,String>> getMapping(){
        return RecvSendMapping;
    }

    public String getName(){
        return actor;
    }

}