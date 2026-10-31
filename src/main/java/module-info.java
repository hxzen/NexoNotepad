module br.nexo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; // usado só na fase SQL (database / repository.sqlite)

    exports br.nexo;

    // O FXMLLoader precisa de acesso por reflexão ao controller (@FXML).
    opens br.nexo.controller to javafx.fxml;
}
