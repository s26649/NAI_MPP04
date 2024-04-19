import java.util.HashMap;
import java.util.Map;

public class ClassificationResult {
    private Map<String, Double> probabilities;
    private String bestLabel;

    public ClassificationResult() {
        this.probabilities = new HashMap<>();
    }

    public void setProbability(String label, double probability) {
        this.probabilities.put(label, probability);
    }

    public void setBestLabel(String bestLabel) {
        this.bestLabel = bestLabel;
    }

    public String getBestLabel() {
        return bestLabel;
    }

    public Map<String, Double> getProbabilities() {
        return probabilities;
    }
}