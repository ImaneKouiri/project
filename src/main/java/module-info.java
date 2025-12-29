module com.bibliotheque {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.bibliotheque to javafx.fxml;
    opens com.bibliotheque.util to javafx.base;

    exports com.bibliotheque;
}
