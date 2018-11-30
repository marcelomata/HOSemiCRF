package Applications.PuncFeatures;

import java.util.*;
import HOSemiCRF.*;

/**
 * Edge and two consecutive words features within a segment
 * @author Nguyen Viet Cuong
 */
public class EdgeTwoWordBag extends FeatureType {
	
    public ArrayList<String> generateObsAt(DataSequence seq, int segStart, int segEnd) {
        ArrayList<String> obs = new ArrayList<String>();
        for (int i = segStart + 1; i <= segEnd; i++) {
            obs.add("ETWB." + seq.x(i - 1) + "." + seq.x(i));
        }
        return obs;
    }
	
    public int order() {
        return 0;
    }
}
