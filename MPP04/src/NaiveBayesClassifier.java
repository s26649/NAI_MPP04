import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NaiveBayesClassifier {
    // liczba wystąpień każdej etykiety klasy w danych treningowych
    private final Map<String, Integer> labelCounts;

    // klucz zewnętrzny reprezentuje nazwę atrybutu (Atrybut+i), a mapa wewnętrzna przechowuje nazwę atrybutu (konkretną)
    // wraz z liczbą wystąpień każdej możliwej wartości tego atrybutu w zbiorze treningowym
    private final Map<String, Map<String, Integer>> featureValueCounts;

    // klucz zewnętrzny reprezentuje etykietę, drugi klucz to indeks atrybutu, a najbardziej
    // wewnętrzna mapa przechowuje liczbę wystąpień każdej wartości tego atrybutu dla danej klasy.
    private final Map<String, Map<Integer, Map<String, Integer>>> conditionalFeatureCounts;

    public NaiveBayesClassifier() {
        this.labelCounts = new HashMap<>();
        this.featureValueCounts = new HashMap<>();
        this.conditionalFeatureCounts = new HashMap<>();
    }

    public void train(List<Data> trainData) {
        for (Data data : trainData) {
            String label = data.getLabel();
            // iterując po obiektach data z pliku treningowego aktualizuje licznik wystąpień dla danej etykiety klasy
            labelCounts.put(label, labelCounts.getOrDefault(label, 0) + 1);
            String[] attributes = data.getAttributes();

            for (int i = 0; i < attributes.length; i++) {
                String attrValue = attributes[i];
                String attrName = "Atrybut" + i;

                featureValueCounts.putIfAbsent(attrName, new HashMap<>());
                // iterując po tablicy atrybutów dodaje atrybut do map, jeśli jeszcze nie istnieje i aktualizuje licznik attrValue dla wartości atrybutu
                featureValueCounts.get(attrName).put(attrValue, featureValueCounts.get(attrName).getOrDefault(attrValue, 0) + 1);

                conditionalFeatureCounts.putIfAbsent(label, new HashMap<>());
                // dla danej etykiety sprawdza czy istnieje już mapa dla indeksu atrybutu (i)
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
            // oblicza prawdopodobieństwo dla danej etykiety
            double labelProb = (double) labelCounts.get(label) / labelCounts.values().stream().mapToInt(Integer::intValue).sum();
            double conditionalProb = 1.0;

            for (int i = 0; i < attributes.length; i++) {
                String attrValue = attributes[i];
                String attrName = "Atrybut" + i;

                // pobiera liczbę wystąpień danej wartości atrybutu dla konkretnej etykiety
                Map<String, Integer> labelAttributeCounts = conditionalFeatureCounts.get(label).getOrDefault(i, new HashMap<>());
                int count = labelAttributeCounts.getOrDefault(attrValue, 0);
                // używa liczby wystąpień etykiety jako mianownika
                int total = labelCounts.get(label);

                // oblicza prawdopodobieństwo warunkowe
                if (count == 0) {
                    conditionalProb *= (double) (1) / (total + featureValueCounts.get(attrName).size()); // wygładzanie Laplace'a
                } else {
                    conditionalProb *= (double) count / total;
                }
            }

            // oblicza całkowite prawdopodobieństwo dla etykiety
            double totalProb = labelProb * conditionalProb;
            result.setProbability(label, totalProb);
            // wybiera etykietę z najwyższym prawdopodobieństwem
            if (totalProb > bestProb) {
                bestProb = totalProb;
                bestLabel = label;
            }
        }

        result.setBestLabel(bestLabel);
        return result;
    }

}