module com.bibliotheque {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.base;

    opens com.bibliotheque.controller to javafx.fxml;
    opens com.bibliotheque.model to javafx.base, javafx.fxml;
    opens com.bibliotheque.util to javafx.base;

    exports com.bibliotheque.controller;
    exports com.bibliotheque.model;
    exports com.bibliotheque.service;
    exports com.bibliotheque.dao;
    exports com.bibliotheque.exception;
    exports com.bibliotheque.util;
}