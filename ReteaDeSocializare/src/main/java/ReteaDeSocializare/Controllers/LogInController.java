package ReteaDeSocializare.Controllers;
import ReteaDeSocializare.Domain.User;
import ReteaDeSocializare.Service.FriendshipService;
import ReteaDeSocializare.Service.UserService;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.w3c.dom.events.MouseEvent;

import java.io.File;
import java.io.IOException;

public class LogInController {
    @FXML
    private Button buton;
    private UserService srv;

    private FriendshipService srv2;
    private Stage stage;

    private Scene scene;

    private Parent root;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmField;
    @FXML
    private TextField emailField;

    @FXML
    private ImageView logoImage;


    public void setService(UserService srv, FriendshipService srv1)
    {
        this.srv = srv;
        this.srv2 = srv1;
    }

    public void handleLogin(ActionEvent event) throws IOException {
        String email = emailField.getText();
        String password = passwordField.getText();
        for(User u: srv.getUsers())
        {
            if( u.getEmail().equals(email))
                if(u.getPassword().equals(password)) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ConnectedUser.fxml"));
                    root = loader.load();
                    ConnectedUserController connectedUserController = loader.getController();
                    connectedUserController.setAll(srv,srv2,u);
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    break;
                }
        }
    }
    public void createAccount(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/CreateAccount.fxml"));
        root = loader.load();
        CreateAccountController createAccountController = loader.getController();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
