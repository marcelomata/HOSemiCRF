package HOCRF;

import java.util.*;
import Parallel.*;

/**
 * Generator class for the features in each sequence
 * @author Nguyen Viet Cuong
 * @author Sumit Bhagwani
 */
public class SentenceFeatGenerator implements Schedulable {

    int curID; // Current task ID (for parallelization)
    ArrayList trainData; // List of training sequences
    FeatureGenerator featGen; // Feature generator

    /**
     * Construct a generator for the features.
     * @param data Training data
     * @param fgen Feature generator
     */
    public SentenceFeatGenerator(ArrayList data, FeatureGenerator fgen) {
        curID = -1;
        trainData = data;
        featGen = fgen;
    }

    /**
     * Compute the features for all the positions in a given sequence.
     * @param taskID Index of the training sequence
     * @return The updated sequence
     */
    public Object compute(int taskID) {
        DataSequence seq = (DataSequence) trainData.get(taskID);
        seq.features = new ArrayList[seq.length()][featGen.patternMap.size()];
        
        for (int pos = 0; pos < seq.length(); pos++) {
            for (int patID = 0; patID < featGen.patternMap.size(); patID++) {
                seq.features[pos][patID] = new ArrayList<Integer>();
                ArrayList<String> obs = featGen.generateObs(seq, pos);
                for (String o : obs) {
                    Integer oID = featGen.getObsIndex(o);
                    if (oID != null) {
                        Integer feat = (Integer) featGen.featureMap.get(new FeatureIndex(oID, patID));
                        if (feat != null) {
                            seq.features[pos][patID].add(feat);
                        }
                    }
                }
            }
        }
		
        return seq;
    }

    /**
     * Return the number of tasks (for parallelization).
     * @return Training data size
     */
    public int getNumTasks() {
        return trainData.size();
    }

    /**
     * Return the next task ID (for parallelization).
     * @return Index of the next sequence
     */
    public synchronized int fetchCurrTaskID() {
        if (curID < getNumTasks()) {
            curID++;
        }
        return curID;
    }

    /**
     * Update partial result (for parallelization).
     * Note that this method does nothing in this case.
     * @param partialResult Partial result
     */
    public synchronized void update(Object partialResult) {
        // Do nothing
    }
}
