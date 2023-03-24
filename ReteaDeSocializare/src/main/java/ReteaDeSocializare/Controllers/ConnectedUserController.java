package ReteaDeSocializare.Controllers;

import ReteaDeSocializare.Domain.Friend;
import ReteaDeSocializare.Domain.Friendship;
import ReteaDeSocializare.Domain.User;
import ReteaDeSocializare.Domain.Validators.FriendshipValidator;
import ReteaDeSocializare.Domain.Validators.UserValidator;
import ReteaDeSocializare.Repository.DBRepository.FriendshipDBRepository;
import ReteaDeSocializare.Repository.DBRepository.UserDBRepository;
import ReteaDeSocializare.Service.FriendshipService;
import ReteaDeSocializare.Service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.ResourceBundle;

public class ConnectedUserController {
    private Stage stage;

    private Scene scene;

    private Parent root;

    private UserService userSrv = new UserService(new UserDBRepository("jdbc:postgresql://localhost:5432/ReteaDeSocializareSQL", "postgres", "postgres", new UserValidator()));

    private FriendshipService frSrv = new FriendshipService(new FriendshipDBRepository("jdbc:postgresql://localhost:5432/ReteaDeSocializareSQL", "postgres", "postgres", new FriendshipValidator()));

    private User connectedUser;
    @FXML
    private Button friendsBtn;

    @FXML
    private Button requestBtn;
    @FXML
    private Button chatBtn;

    @FXML
    private Button searchBtn;
    @FXML
    private Button homeBtn;
    @FXML
    private Button acceptBtn;
    @FXML
    private Button declineBtn;

    @FXML
    private Button sendBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button logBtn;
    @FXML
    private TableView<Friend> friendsTable;

    @FXML
    private TableColumn<Friend, String> name;

    @FXML
    private TableColumn<Friend, String> email;

    @FXML
    private TableColumn<Friend, String> since;

    @FXML
    private Label usr;

    @FXML
    private TextField searchField;

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

    public void openchat(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Chat.fxml"));
        root = loader.load();
        ChatController chatController = loader.getController();
        chatController.setAll(userSrv,frSrv,connectedUser);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void home(ActionEvent event) throws IOException {
        friendsTable.setVisible(false);
        searchField.setVisible(false);
        acceptBtn.setVisible(false);
        declineBtn.setVisible(false);
        deleteBtn.setVisible(false);
        sendBtn.setVisible(false);
    }

    public void setAll(UserService srv, FriendshipService srv2, User u) {
        name.setCellValueFactory(new PropertyValueFactory<Friend, String>("Name"));
        email.setCellValueFactory(new PropertyValueFactory<Friend, String>("Email"));
        since.setCellValueFactory(new PropertyValueFactory<Friend, String>("friendsFrom"));
        this.connectedUser = u;
        this.userSrv = srv;
        this.frSrv = srv2;
        usr.setText("Welcome " + u.getFullName());
        friendsTable.setVisible(false);
        searchField.setVisible(false);
        acceptBtn.setVisible(false);
        declineBtn.setVisible(false);
        deleteBtn.setVisible(false);
        sendBtn.setVisible(false);
    }

    public void showFriends(ActionEvent event) throws IOException {
        searchField.setVisible(false);
        friendsTable.setVisible(true);
        acceptBtn.setVisible(false);
        declineBtn.setVisible(false);
        deleteBtn.setVisible(true);
        sendBtn.setVisible(false);
        friendsTable.getItems().clear();
        since.setText("Friends Since");
        ObservableList<Friendship> friendships = FXCollections.observableArrayList();
        for (Friendship f : frSrv.getAllFriendships())
            if (f.getId1().equals(connectedUser.getId()) || f.getId2().equals(connectedUser.getId()))
                if (f.getStatus().equals("Accepted"))
                    friendships.add(f);
                else if (f.getStatus().equals("Requested") && f.getSentby().equals(connectedUser.getId()))
                    friendships.add(f);
        for (Friendship f : friendships) {
            String data = "";
            if (f.getStatus().equals("Requested"))
                data = f.getStatus();
            else
                data = f.getFriendsFrom().format(f.getDtf());
            if (f.getId1().equals(connectedUser.getId())) {
                Friend fr = new Friend(userSrv.getFullName(f.getId2()), userSrv.getEmail(f.getId2()), data);
                friendsTable.getItems().add(fr);
            } else if (f.getId2().equals(connectedUser.getId())) {
                Friend fr = new Friend(userSrv.getFullName(f.getId1()), userSrv.getEmail(f.getId1()), data);
                friendsTable.getItems().add(fr);
            }
        }
    }

    public void showRequests(ActionEvent event) throws IOException {
        sendBtn.setVisible(false);
        friendsTable.setVisible(true);
        friendsTable.getItems().clear();
        acceptBtn.setVisible(true);
        declineBtn.setVisible(true);
        deleteBtn.setVisible(false);
        searchField.setVisible(false);
        since.setText("Requested on");
        ObservableList<Friendship> friendships = FXCollections.observableArrayList();
        for (Friendship f : frSrv.getAllFriendships())
            if ((f.getId1().equals(connectedUser.getId()) || f.getId2().equals(connectedUser.getId()))
                    && f.getStatus().equals("Requested") && !f.getSentby().equals(connectedUser.getId()))
                friendships.add(f);
        for (Friendship f : friendships)
            if (f.getId1().equals(connectedUser.getId())) {
                String data = f.getFriendsFrom().format(f.getDtf());
                Friend fr = new Friend(userSrv.getFullName(f.getId2()), userSrv.getEmail(f.getId2()), data);
                friendsTable.getItems().add(fr);
            } else if (f.getId2().equals(connectedUser.getId())) {
                String data = f.getFriendsFrom().format(f.getDtf());
                Friend fr = new Friend(userSrv.getFullName(f.getId1()), userSrv.getEmail(f.getId1()), data);
                friendsTable.getItems().add(fr);
            }
    }

    public void deleteRequest(ActionEvent event) throws IOException {
        Friend fr = friendsTable.getSelectionModel().getSelectedItem();
        Friendship friendship = frSrv.getFriendshipByUserId(userSrv.getUserbyEmail(fr.getEmail()));

        if (!frSrv.deleteFriendship(friendship.getId1(), friendship.getId2()))
            frSrv.deleteFriendship(friendship.getId2(), friendship.getId1());
        requestBtn.fire();
    }

    public void acceptRequest(ActionEvent event) throws IOException {
        Friend fr = friendsTable.getSelectionModel().getSelectedItem();
        Friendship changed = frSrv.findFriendship(userSrv.getUserbyEmail(fr.getEmail()), connectedUser.getId());
        LocalDateTime date = changed.getFriendsFrom();
        changed.setStatus("Accepted");
        frSrv.deleteFriendship(userSrv.getUserbyEmail(fr.getEmail()), connectedUser.getId());
        frSrv.addFriendship(changed);
        //frSrv.changeStatusFriendship(userSrv.getUserbyEmail(fr.getEmail()), connectedUser.getId());
        //frSrv.getFriendshipByUserId(userSrv.getUserbyEmail(fr.getEmail())).setStatus("Accepted");
        requestBtn.fire();
    }

    public void deleteFriend(ActionEvent event) throws IOException {
        Friend fr = friendsTable.getSelectionModel().getSelectedItem();
        frSrv.deleteFriendship(userSrv.getUserbyEmail(fr.getEmail()), connectedUser.getId());
        //frSrv.getFriendshipByUserId(userSrv.getUserbyEmail(fr.getEmail())).setStatus("Accepted");
        friendsBtn.fire();
    }

    public void searchUser(ActionEvent event) throws IOException {
        friendsTable.getItems().clear();
        friendsTable.setVisible(true);
        searchField.setVisible(true);
        acceptBtn.setVisible(false);
        declineBtn.setVisible(false);
        deleteBtn.setVisible(false);
        sendBtn.setVisible(true);
        since.setText("Status");
        for (User u : userSrv.getUsers()) {
            String data = "";
            if (!u.getId().equals(connectedUser.getId())) {
                if (frSrv.findFriendship(u.getId(), connectedUser.getId()) != null) {
                    if (frSrv.findFriendship(u.getId(), connectedUser.getId()).getStatus().equals("Requested"))
                        data = "Requested";
                    else
                        data = "Already friends";
                } else if (u.getId() != connectedUser.getId())
                    data = "Not friends";
                Friend fr = new Friend(u.getFullName(), u.getEmail(), data);
                friendsTable.getItems().add(fr);
            }
        }
    }

    public void searchingUser(KeyEvent event) throws IOException {
        friendsTable.getItems().clear();
        since.setText("Status");
        for (User u : userSrv.getUsers()) {
            String data = "";
            if (!u.getId().equals(connectedUser.getId())) {
                if (u.getFullName().startsWith(searchField.getText())) {
                    if (frSrv.findFriendship(u.getId(), connectedUser.getId()) != null) {
                        if (frSrv.findFriendship(u.getId(), connectedUser.getId()).getStatus().equals("Requested"))
                            data = "Requested";
                        else
                            data = "Already friends";
                    } else
                        data = "Not friends";
                    Friend fr = new Friend(u.getFullName(), u.getEmail(), data);
                    friendsTable.getItems().add(fr);
                }
            }
        }
    }

    public void addFriend(ActionEvent event) throws IOException {
        Friend fr = friendsTable.getSelectionModel().getSelectedItem();
        String status = "Requested";
        Friendship newfr = new Friendship(connectedUser.getId(),userSrv.getUserbyEmail(fr.getEmail()),status,connectedUser.getId());
        frSrv.addFriendship(newfr);
        searchBtn.fire();
    }
}