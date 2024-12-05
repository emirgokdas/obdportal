module org.demoapplication.demo {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.logging;



    requires org.kordamp.bootstrapfx.core;
    requires com.fazecast.jSerialComm;

    opens org.demoapplication.demo to javafx.fxml;
    exports org.demoapplication.demo;
}