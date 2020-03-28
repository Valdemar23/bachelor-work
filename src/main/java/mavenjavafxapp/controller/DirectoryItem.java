package mavenjavafxapp.controller;

/**
 * Created by HP on 12.04.2017.
 */
import javafx.beans.property.SimpleStringProperty;

public class DirectoryItem {
    private SimpleStringProperty name;
    private SimpleStringProperty size;

    DirectoryItem() {
    }

    DirectoryItem(String argName, String argSize) {
        this.name = new SimpleStringProperty(argName);
        this.size = new SimpleStringProperty(argSize);
    }

    public String getName() {
        return this.name.get();
    }

    public String getSize() {
        return this.size.get();
    }

    public void setName(String argName) {
        this.name.set(argName);
    }

    public void setSize(String argSize) {
        this.size.set(argSize);
    }
}
