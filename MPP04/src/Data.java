public class Data {
    String[] attributes;
    String label;

    Data(String[] attributes, String label) {
        this.attributes = attributes;
        this.label = label;
    }

    public String[] getAttributes() {
        return attributes;
    }

    public String getLabel() {
        return label;
    }
}