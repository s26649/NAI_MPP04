import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Musisz podac sciezke do pliku treningowego i testowego jako argumenty.");
            return;
        }

        String trainFile = args[0];
        String testFile = args[1];

        List<Data> trainingData = CSVLoader.loadDataFromCsv(trainFile, true);
        List<Data> testData = CSVLoader.loadDataFromCsv(testFile, false);

        NaiveBayesClassifier classifier = new NaiveBayesClassifier();
        classifier.train(trainingData);

        for (Data data : testData) {
            ClassificationResult result = classifier.classify(data.getAttributes());
            String formattedAttributes = Stream.of(data.getAttributes())
                    .map(String::trim)
                    .collect(Collectors.joining(", ", "(", ")"));
            System.out.println("Atrybuty: X = " + formattedAttributes);
            result.getProbabilities().forEach((label, prob) -> System.out.println("\tP(" + label + "|X) = " + prob));
            System.out.println("\tPrzewidywana etykieta: " + result.getBestLabel());
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Wprowadz dane do klasyfikacji (atrybuty oddzielone przecinkami):");
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) break;
            String[] attributes = input.split(",");
            ClassificationResult result = classifier.classify(attributes);
            String formattedAttributes = Stream.of(attributes)
                    .collect(Collectors.joining(", ", "(", ")"));
            System.out.println("Atrybuty: X = " + formattedAttributes);
            result.getProbabilities().forEach((label, prob) -> System.out.println("\tP(" + label + "|X) = " + prob));
            System.out.println("\tPrzewidywana etykieta: " + result.getBestLabel());
        }
        scanner.close();
    }
}