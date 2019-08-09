package test.analysis;

import soot.Main;
import soot.PackManager;
import soot.Transform;
import soot.Scene;
import soot.options.*;

import java.util.Set;
import java.util.HashSet;

import org.junit.Test;
import org.junit.Assert;

import test.common.*;


/** The driver for DLAnalysis.
 * Also implements a JUnit test for the analysis
 */
public class dlAnalysisMain {
    /** The abbreviated name of the analysis */
    public static final String ANALYSIS_NAME = "jap.dlanalysis";
    
    /** Runs Soot with DLAnalysis available
     */
    public static void main(String[] args) {
        // Inject the analysis tagger into Soot
        PackManager.v().getPack("jap").add(new Transform(ANALYSIS_NAME, dlAnalysis.instance()));

        // run Soot with the arguments given
        // Utils.runSoot() keeps Soot from calling System.exit() if we are invoked from JUnit
        Utils.runSoot(args);

        dlAnalysis.instance().findSlave();

        System.out.println("total warnings: " + Utils.getErrors().size());
    }
    
    /** A JUnit test for DLAnalysis */
    @Test
    public void testdlAnalysis() {
        main(Utils.getSootArgs(ANALYSIS_NAME, "dltest1.dltest"));
    }
}