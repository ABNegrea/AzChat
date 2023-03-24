package ReteaDeSocializare.Controllers;

import ReteaDeSocializare.Domain.User;
import ReteaDeSocializare.Domain.Validators.FriendshipValidator;
import ReteaDeSocializare.Domain.Validators.UserValidator;
import ReteaDeSocializare.Repository.DBRepository.FriendshipDBRepository;
import ReteaDeSocializare.Repository.DBRepository.UserDBRepository;
import ReteaDeSocializare.Service.FriendshipService;
import ReteaDeSocializare.Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateAccountController {
    private Button buton;

    private Stage stage;

    private Scene scene;

    private Parent root;

    private UserService srv = new UserService(new UserDBRepository("jdbc:postgresql://localhost:5432/ReteaDeSocializareSQL","postgres","postgres",new UserValidator()));

    private FriendshipService srv2 =  new FriendshipService(new FriendshipDBRepository("jdbc:postgresql://localhost:5432/ReteaDeSocializareSQL","postgres","postgres",new FriendshipValidator()));
    @FXML
    private TextField secondField;
    @FXML
    private TextField firstField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmField;
    @FXML
    private TextField emailField;

    public void setService(UserService srv, FriendshipService srv2){
        this.srv = srv;
        this.srv2 = srv2;
    }

    public void signUp(ActionEvent event) throws IOException {
        String email = emailField.getText();
        String password = passwordField.getText();
        String firstname = firstField.getText();
        String secondname = secondField.getText();
        String confirmpassowrd = confirmField.getText();

        if(password.equals(confirmpassowrd)) {
            srv.addUser(secondname,firstname,email,password);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/LogIn.fxml"));
            root = loader.load();
            LogInController loginController = loader.getController();
            loginController.setService(srv,srv2);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }
}
