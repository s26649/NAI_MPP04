import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVLoader {
    public static List<Data> loadDataFromCsv(String filePath, boolean hasLabel) throws IOException {
        List<Data> dataList = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists() || !file.isFile()) {
            throw new IOException("Plik nie istnieje lub podana sciezka nie jest plikiem: " + filePath);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String[] attributes = new String[hasLabel ? values.length - 1 : values.length];
                System.arraycopy(values, 0, attributes, 0, attributes.length);
                String label = hasLabel ? values[values.length - 1].trim() : null;
                dataList.add(new Data(attributes, label));
            }
        }
        return dataList;
    }
}