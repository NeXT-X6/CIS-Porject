package test.analysis;

import java.util.*;
import soot.jimple.*;
import soot.*;
import test.common.*;
import soot.toolkits.graph.*;
import test.analysis.ActorGraph;
import test.common.Pair;

public class dlAnalysis extends BodyTransformer {
    public ArrayList<String> transmits = new ArrayList<String>();

    public String call = "";
    public String para = "";
    
    /** Implements the analysis for a particular method body */
    @Override
    protected void internalTransform(Body body, String phaseName, Map options) {
        SootMethod method = body.getMethod();
        
        /*System.out.println("-----analyzing the body of "
                          + method.getDeclaringClass().getName()
                           + "." + method.getName()+"--------");*/
        //String call = "";
        for(Unit unit : body.getUnits()){
            Stmt stmt = (Stmt)unit;
            //System.out.println(stmt);
            if(stmt instanceof DefinitionStmt){
                DefinitionStmt def = (DefinitionStmt)stmt;
                Value right = def.getRightOp();
                String r = right.toString();
                //System.out.println(r);
                //finds the latest class name
                if (r.contains("@this") && !(r.contains("$"))){
                    call = r.substring(r.indexOf(":")+2);
                }
                //finds the msgs that can be sent
                if (r.contains("@parameter0")){
                    String temp = para;
                    para=r.substring(r.indexOf(":")+2);
                    if(!para.contains("$"))
                        para = temp;
                }
                if(r.contains("new") && !r.contains("lambda")
                && !r.contains("java") && r.contains("$")){
                    transmits.add(call+"//"+para+"//"+r.substring(r.indexOf("w")+2));
                    //System.out.println(call+"//"+para+"//");
                }
            }
            else if (stmt instanceof InvokeStmt){
                String ies = ((InvokeStmt)stmt).getInvokeExpr().toString();
                if(ies.contains("lambda$createReceive$")){
                    String ies1 = ies.substring(ies.indexOf("<"),ies.indexOf(">")+1);
                    String ies3 = ies1.substring(ies1.indexOf("(")+1,ies1.indexOf(")"));
                    String ies2 = ies1.substring(ies1.indexOf("<")+1,ies1.indexOf(":"));
                    String s = ies2+"//"+ies3+"//"+"null";
                    transmits.add(s);
                }
            }
        }
    }

    //separated from internalTransform(), making sure that: this method
    //gets only the final result, and is not going to conflict the threads
    //caused by analyzing different classes in internalTransform().
    public void findSlave(){

        //delete the duplicate stuff (null) in transmits
        ArrayList<String> temp1 = new ArrayList<String>();
        for(String s : transmits)
            temp1.add(s);
        for(String s : temp1){
            String[] sk = s.split("//");
            if (!sk[2].equals("null")){
                for(String s1 : temp1){
                    String[] sk1 = s1.split("//");
                    if(sk[0].equals(sk1[0]) && sk[1].equals(sk1[1]) && sk1[2].equals("null"))
                        transmits.remove(s1);
                }
            }
        }

        //System.out.println(transmits);
        ArrayList<ActorGraph> graphList = new ArrayList<ActorGraph>();
        ArrayList<String> actors = new ArrayList<String>();

        //find all existing actors from all classes
        Iterator<SootClass> classes = Scene.v().getApplicationClasses().iterator();
        while(classes.hasNext()){
            SootClass sc = classes.next();
            String s = sc.toString();
            if(sc.getSuperclass().toString().contains("AbstractActor") && 
              !(actors.contains(s))) {
                graphList.add(new ActorGraph(s));
                actors.add(s);
            }
        }

        for(String str : transmits){
            String[] temp = str.split("//");
            for (ActorGraph ag : graphList)
                if(temp[0].equals(ag.getName())){
                    ag.addMapping(new Pair(temp[1],temp[2]));
                }
        }

        ArrayList<Pair<String,String>> slaves = new ArrayList<Pair<String,String>>();
        
        /** find a recv msg, and look for the actor that it belongs.
         *  if that actor failed to reply anyone with this msg, add it to slaves.
         *  ------------------------------------------------------------
         *  IN THIS CASE, the list "slaves" contains pairs such that:
         ** the key actor IS DEPENDENT on the value actor's msg,
         ** but the value actor NEVER sends it back.*/

        ArrayList<Pair<String,ArrayList<Pair>>> mappings = new ArrayList<Pair<String,ArrayList<Pair>>>();
        for(ActorGraph ag : graphList){
            mappings.add(new Pair(ag.getName(),ag.getMapping()));
        }

        //System.out.println(mappings);

        for(Pair p1 : mappings){
            String currentActor1 = (String)p1.getKey();
            List<Pair> maps1 = (ArrayList<Pair>)p1.getValue();
            for(Pair ps1 : maps1){
                String a1r = (String)ps1.getKey();
                String a1rt = a1r.substring(0,a1r.indexOf("$"));
                for(Pair p2 : mappings){
                    String currentActor2 = (String)p2.getKey();
                    List<Pair> maps2 = (ArrayList<Pair>)p2.getValue();
                    if(a1rt.equals(currentActor2)){
                        Boolean flag = false;
                        for(Pair ps2 : maps2){
                            String a2r = (String)ps2.getKey();
                            String a2rt = a2r.substring(0,a2r.indexOf("$"));
                            String a2s = (String)ps2.getValue();
                            if(a1r.equals(a2s) && a2rt.equals(currentActor1)){
                                flag = true;
                                break;
                            }
                        }
                        if(!flag)
                            slaves.add(new Pair(currentActor1,currentActor2));
                    }
                }
            }
        }
        //System.out.println(slaves);

        ArrayList<Pair<Pair<String, String>, List>> slaveChain = new ArrayList<Pair<Pair<String, String>, List>>();
        //if pair(a,b), pair(b,c) are in slaves, then merge them into pair(a,c)
        ArrayList<Pair> dontCheck = new ArrayList<Pair>();
        //make sure next time pair(b,a), pair(c,b) won't be checked
        for (Pair p1 : slaves){
            if (!dontCheck.contains(p1)){
                ArrayList<String> interval = new ArrayList<String>();
                //the interval is used to store b when merging (a,b) and (b,c)
                Boolean first = true;
                String tell = "";
                for(Pair p2 : slaves) {
                    if(first && p1.getValue().equals(p2.getKey())) {
                        interval.add((String)(p1.getValue()));
                        slaveChain.add(new Pair(new Pair(p1.getKey(), p2.getValue()), interval));
                        tell = (String)p2.getValue();
                        dontCheck.add(new Pair((String)p1.getValue(),(String)p1.getKey()));
                        first=false;
                    }
                    else{
                        Pair p = new Pair((String)p1.getKey(),tell);
                        if(p.getValue().equals(p2.getKey())){
                            interval.add((String)(p.getValue()));
                            slaveChain.add(new Pair(new Pair(p.getKey(), p2.getValue()), interval));
                            dontCheck.add(new Pair((String)p.getValue(),(String)p.getKey()));
                        }
                    }
                }
            }
        }

        //after merging, we may get pair(a,a), which is a deadlock as we expected
        for(Pair<Pair<String, String>, List> chain : slaveChain) {
            Pair p = (Pair)chain.getKey();
            String sp = (String)p.getKey();
            if(p.getKey().equals(p.getValue())) {
                String s = "";
                for(String st : (ArrayList<String>)chain.getValue()){
                    s+=st+" ";
                }
                if(!s.contains(sp))
                    System.out.println("deadlock: "+sp+" "+s);
            }
        }
    }

    /** The analysis object instance */
    public static dlAnalysis instance() {
        return theInstance;
    }
    
    private static dlAnalysis theInstance = new dlAnalysis();
}