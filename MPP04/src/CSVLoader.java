import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                String[] values = line.split(";");
                Map<String, String> attributes = new HashMap<>();
                String label = null;
                for (int i = 0; i < values.length - (hasLabel ? 1 : 0); i++) {
                    attributes.put("Atrybut" + i, values[i].trim());
                }
                if (hasLabel) {
                    label = values[values.length - 1].trim();
                }
                dataList.add(new Data(attributes, label));
            }
        }
        return dataList;
    }
}


