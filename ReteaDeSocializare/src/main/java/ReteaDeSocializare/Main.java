package ReteaDeSocializare;

import ReteaDeSocializare.Domain.User;
import ReteaDeSocializare.Domain.Validators.FriendshipValidator;
import ReteaDeSocializare.Domain.Validators.UserValidator;
import ReteaDeSocializare.Repository.DBRepository.FriendshipDBRepository;
import ReteaDeSocializare.Repository.DBRepository.UserDBRepository;
import ReteaDeSocializare.Repository.Repository;
import ReteaDeSocializare.Service.FriendshipService;
import ReteaDeSocializare.Service.UserService;
import ReteaDeSocializare.UI.UI;
import ReteaDeSocializare.Controllers.LogInController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;


//public class Main {
//    public static void main(String[] args) {
//        UI ui = new UI();
//        /*
//        String frfile = "src/main/java/ReteaDeSocializare/Friendships.txt";
//        String userfile = "src/main/java/ReteaDeSocializare/Users.txt";
//        UI ui = new UI(frfile,userfile);
//        */
//        String url = "jdbc:postgresql://localhost:5432/ReteaDeSocializareSQL";
//        String username = "postgres";
//        String password = "postgres";
//        UI ui = new UI(url, username, password);
//        ui.run();
//
//        // Repository<UUID, User> repoDB = new UserDBRepository("jdbc:postgresql://localhost:5432/ReteaDeSocializareSQL",
//        //         "postgres","postgres",new UserValidator());
//        // User user = new User("Bogdan","Andrei");
//        //repoDB.save(user);
//        //repoDB.findAll().forEach(System.out::println);
//        //   User newuser = repoDB.findOne(UUID.fromString("e3c9b18a-29f4-412c-91b2-1b2d5a3eddd8"));
//        //System.out.println(user);
//        //user = repoDB.delete(user.getId());
//        // System.out.println(user);
//        //repoDB.update(newuser,user);
//        /*String querry = "DELETE FROM \"Friendships\"";
//        try (Connection connection = DriverManager.getConnection(url, username, password);
//             PreparedStatement statement = connection.prepareStatement(querry)) {
//            statement.executeUpdate();
//        }
//        catch(SQLException e){
//            e.printStackTrace();
//        }*/
//    }
//}


public class Main extends Application{

    private Stage stage;

    private Scene scene;

    private Parent root;

    private UserService userSrv = new UserService(new UserDBRepository("jdbc:postgresql://localhost:5432/ReteaDeSocializareSQL","postgres","postgres",new UserValidator()));
    private FriendshipService frSrv = new FriendshipService(new FriendshipDBRepository("jdbc:postgresql://localhost:5432/ReteaDeSocializareSQL","postgres","postgres",new FriendshipValidator()));

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/LogIn.fxml"));
        root = loader.load();
        LogInController loginController = loader.getController();
        loginController.setService(userSrv,frSrv);
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}