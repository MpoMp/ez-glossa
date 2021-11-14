module mbogdanos.ezglossa {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens mbogdanos.ezglossa to javafx.fxml;
    exports mbogdanos.ezglossa;
}