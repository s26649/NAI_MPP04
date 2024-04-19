import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NaiveBayesClassifier {
    private final Map<String, Integer> labelCounts;
    private final Map<String, Map<String, Integer>> featureValueCounts;
    private final Map<String, Map<Integer, Map<String, Integer>>> conditionalFeatureCounts;

    public NaiveBayesClassifier() {
        this.labelCounts = new HashMap<>();
        this.featureValueCounts = new HashMap<>();
        this.conditionalFeatureCounts = new HashMap<>();
    }

    public void train(List<Data> trainData) {
        for (Data data : trainData) {
            String label = data.getLabel();
            labelCounts.put(label, labelCounts.getOrDefault(label, 0) + 1);
            String[] attributes = data.getAttributes();

            for (int i = 0; i < attributes.length; i++) {
                String attrValue = attributes[i];
                String attrName = "Atrybut" + i;

                featureValueCounts.putIfAbsent(attrName, new HashMap<>());
                featureValueCounts.get(attrName).put(attrValue, featureValueCounts.get(attrName).getOrDefault(attrValue, 0) + 1);

                conditionalFeatureCounts.putIfAbsent(label, new HashMap<>());
                conditionalFeatureCounts.get(label).putIfAbsent(i, new HashMap<>());
                conditionalFeatureCounts.get(label).get(i).put(attrValue, conditionalFeatureCounts.get(label).get(i).getOrDefault(attrValue, 0) + 1);
            }
        }
    }

    public ClassificationResult classify(String[] attributes) {
        ClassificationResult result = new ClassificationResult();
        double bestProb = 0.0;
        String bestLabel = null;

        for (String label : labelCounts.keySet()) {
            double labelProb = (double) labelCounts.get(label) / labelCounts.values().stream().mapToInt(Integer::intValue).sum();
            double conditionalProb = 1.0;

            for (int i = 0; i < attributes.length; i++) {
                String attrValue = attributes[i];
                String attrName = "Atrybut" + i;

                Map<String, Integer> labelAttributeCounts = conditionalFeatureCounts.get(label).getOrDefault(i, new HashMap<>());
                int count = labelAttributeCounts.getOrDefault(attrValue, 0);
                int total = featureValueCounts.get(attrName).values().stream().mapToInt(Integer::intValue).sum();

                if (count == 0) {
                    conditionalProb *= (double) (1) / (total + featureValueCounts.get(attrName).size());
                } else {
                    conditionalProb *= (double) count / total;
                }
            }

            double totalProb = labelProb * conditionalProb;
            result.setProbability(label, totalProb);
            if (totalProb > bestProb) {
                bestProb = totalProb;
                bestLabel = label;
            }
        }

        result.setBestLabel(bestLabel);
        return result;
    }

}