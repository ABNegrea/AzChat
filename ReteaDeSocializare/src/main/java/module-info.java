module ReteaDeSocializare {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;

    opens ReteaDeSocializare to javafx.fxml;
    opens ReteaDeSocializare.Controllers to javafx.fxml;

    exports ReteaDeSocializare;
    exports ReteaDeSocializare.Domain;
    exports ReteaDeSocializare.Controllers;
}
