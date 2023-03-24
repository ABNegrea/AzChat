package ReteaDeSocializare.Controllers;

import ReteaDeSocializare.Domain.Friend;
import ReteaDeSocializare.Domain.Friendship;
import ReteaDeSocializare.Domain.Message;
import ReteaDeSocializare.Domain.User;
import ReteaDeSocializare.Domain.Validators.FriendshipValidator;
import ReteaDeSocializare.Domain.Validators.MessageValidator;
import ReteaDeSocializare.Domain.Validators.UserValidator;
import ReteaDeSocializare.Repository.DBRepository.FriendshipDBRepository;
import ReteaDeSocializare.Repository.DBRepository.MessageDBRepository;
import ReteaDeSocializare.Repository.DBRepository.UserDBRepository;
import ReteaDeSocializare.Service.FriendshipService;
import ReteaDeSocializare.Service.MessageService;
import ReteaDeSocializare.Service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

public class ChatController {
    private Stage stage;

    private Scene scene;

    private Parent root;

    private UserService userSrv = new UserService(new UserDBRepository("jdbc:postgresql://localhost:5432/ReteaDeSocializareSQL", "postgres", "postgres", new UserValidator()));

    private FriendshipService frSrv = new FriendshipService(new FriendshipDBRepository("jdbc:postgresql://localhost:5432/ReteaDeSocializareSQL", "postgres", "postgres", new FriendshipValidator()));

    private MessageService msgSrv = new MessageService(new MessageDBRepository("jdbc:postgresql://localhost:5432/ReteaDeSocializareSQL", "postgres", "postgres", new MessageValidator()));
    private User connectedUser;

    private User receiverUser;

    @FXML
    private Button homeBtn;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Friend> friendList;

    @FXML
    private TableColumn<Friend, String> name;

    @FXML
    private ListView<Message> messageList;
    @FXML
    private Button logBtn;

    @FXML
    private TextArea message;

    public void setAll(UserService srv, FriendshipService srv2, User u) {
        this.connectedUser = u;
        this.userSrv = srv;
        this.frSrv = srv2;
        name.setCellValueFactory(new PropertyValueFactory<Friend, String>("name"));
        name.setResizable(true);
        loadFriends();

        // create a filter to limit the number of characters to 125
        UnaryOperator<Change> filter = change -> {
            if (change.getControlNewText().length() > 125) {
                return null;
            }
            return change;
        };
        TextFormatter<String> formatter = new TextFormatter<>(filter);
        message.setTextFormatter(formatter);

        messageList.setCellFactory(lv -> new ListCell<Message>() {
            @Override
            public void updateItem(Message item, boolean empty) {
                super.updateItem(item, empty);
                this.setTextFill(Color.RED);
                if (item != null) {
                    setGraphic(item.getMessage());
                    setStyle("-fx-background-color: #A3ACC8;");
                    if(item.getSender().equals(connectedUser.getId()))
                        setAlignment(Pos.CENTER_RIGHT);
                    else
                        setAlignment(Pos.CENTER_LEFT);
                } else {
                    setGraphic(null);
                    setStyle("-fx-background-color: #A3ACC8;");
                }
            }

        });
    }

    public void selectUser(MouseEvent event) throws IOException {
        receiverUser = userSrv.getUserFromId(userSrv.getUserbyEmail(friendList.getSelectionModel().getSelectedItem().getEmail()));
        searchField.setText(receiverUser.getFullName());
        ObservableList<Message> messages = FXCollections.observableArrayList();
        for( Message msg : msgSrv.getAllMessages())
            if(msg.getReceiver().equals(connectedUser.getId()) && msg.getSender().equals(receiverUser.getId()))
                messages.add(msg);
            else
            if (msg.getReceiver().equals(receiverUser.getId()) && msg.getSender().equals(connectedUser.getId()))
                    messages.add(msg);
        messages.sort(Comparator.comparing(Message::getSentat));
        messageList.setItems(messages);
        messageList.scrollTo(messageList.getItems().size());
    }

    public void sendMsg(ActionEvent event) throws IOException {
        Text text = new Text();
        text.setWrappingWidth(100);
        text.setText(message.getText());
        Message msg = new Message(connectedUser.getId(), receiverUser.getId(),text);
        messageList.getItems().add(msg);
        msgSrv.addMessage(msg);
        messageList.scrollTo(messageList.getItems().size());
    }

    public void loadFriends() {
        for (Friendship f : frSrv.getAllFriendships())
            if ((f.getId1().equals(connectedUser.getId()) || f.getId2().equals(connectedUser.getId()))
                    && f.getStatus().equals("Accepted")) {
                if (f.getId1().equals(connectedUser.getId())) {
                    String data = f.getFriendsFrom().format(f.getDtf());
                    Friend fr = new Friend(userSrv.getUserFromId(f.getId2()).getId(),userSrv.getUserFromId(f.getId2()).getFirstName(), userSrv.getEmail(f.getId2()), data);
                    friendList.getItems().add(fr);
                } else if (f.getId2().equals(connectedUser.getId())) {
                    String data = f.getFriendsFrom().format(f.getDtf());
                    Friend fr = new Friend(userSrv.getUserFromId(f.getId1()).getId(),userSrv.getUserFromId(f.getId1()).getFirstName(), userSrv.getEmail(f.getId1()), data);
                    friendList.getItems().add(fr);
                }
            }
    }

    public void searching(KeyEvent event) throws IOException {
        friendList.getItems().clear();
        for (User u : userSrv.getUsers()) {
            String data = "";
            if (!u.getId().equals(connectedUser.getId())) {
                if (u.getFullName().startsWith(searchField.getText())) {
                    if (frSrv.findFriendship(u.getId(), connectedUser.getId()) != null && frSrv.findFriendship(u.getId(), connectedUser.getId()).getStatus().equals("Accepted"))
                    {
                        Friend fr = new Friend(u.getFullName(), u.getEmail(), data);
                        friendList.getItems().add(fr);
                    }
                }
            }
        }
    }

    public void searchFunction(ActionEvent event) throws IOException {
        friendList.getItems().clear();
        for (User u : userSrv.getUsers()) {
            String data = "";
            if (!u.getId().equals(connectedUser.getId()))
                if (u.getFullName().startsWith(searchField.getText())) {
                    if (frSrv.findFriendship(u.getId(), connectedUser.getId()) != null && frSrv.findFriendship(u.getId(), connectedUser.getId()).getStatus().equals("Accepted")) {
                        Friend fr = new Friend(u.getFullName(), u.getEmail(), data);
                        friendList.getItems().add(fr);
                    }
                }
        }
    }
    public void home(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ConnectedUser.fxml"));
        root = loader.load();
        ConnectedUserController connectedUserController = loader.getController();
        connectedUserController.setAll(userSrv,frSrv,connectedUser);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void logoff(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/LogIn.fxml"));
        root = loader.load();
        LogInController loginController = loader.getController();
        loginController.setService(userSrv,frSrv);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}