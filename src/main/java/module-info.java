module com.exams.farmingsystem.examsproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires okhttp3;
    requires org.json;
    requires mysql.connector.j;
    requires javafx.graphics;


    opens com.exams.farmingsystem.examsproject;



    opens com.exams.farmingsystem.examsproject.models;
    //exports com.exams.farmingsystem.examsproject.models;
}