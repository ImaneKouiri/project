module com.bibliotheque {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.base;

    opens com.bibliotheque.controller to javafx.fxml;
    opens com.bibliotheque to javafx.graphics;
    opens com.bibliotheque.model to javafx.base, javafx.fxml;
    opens com.bibliotheque.util to javafx.base;

    exports com.bibliotheque;
}