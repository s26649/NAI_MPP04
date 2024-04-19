import java.util.Map;

public class Data {
    Map<String, String> attributes;
    String label;

    Data(Map<String, String> attributes, String label) {
        this.attributes = attributes;
        this.label = label;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public String getLabel() {
        return label;
    }
}