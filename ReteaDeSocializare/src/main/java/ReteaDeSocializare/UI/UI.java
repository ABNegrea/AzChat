package ReteaDeSocializare.UI;

import ReteaDeSocializare.Domain.Exceptions.FriendshipException;
import ReteaDeSocializare.Domain.Exceptions.ValidationException;
import ReteaDeSocializare.Domain.Friendship;
import ReteaDeSocializare.Domain.User;
import ReteaDeSocializare.Domain.Validators.FriendshipValidator;
import ReteaDeSocializare.Domain.Validators.UserValidator;
import ReteaDeSocializare.Domain.Validators.Validator;
import ReteaDeSocializare.Repository.DBRepository.FriendshipDBRepository;
import ReteaDeSocializare.Repository.DBRepository.UserDBRepository;
import ReteaDeSocializare.Repository.InFileRepository.FriendshipFileRepository;
import ReteaDeSocializare.Repository.InFileRepository.UserFileRepository;
import ReteaDeSocializare.Repository.InMemoryRepository;
import ReteaDeSocializare.Service.FriendshipService;
import ReteaDeSocializare.Service.UserService;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class UI {
    private static final UI instance = new UI();
    private final FriendshipService friendSrv;
    private final UserService userSrv;
    private final Scanner scanner = new Scanner(System.in);

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMMM dd YYYY hh:mm:ss");

    public UI() {
        friendSrv = new FriendshipService(new InMemoryRepository<UUID, Friendship>(new FriendshipValidator()));
        userSrv = new UserService(new InMemoryRepository<UUID, User>(new UserValidator()));
    }



    public UI(String frfile, String userfile) {
        friendSrv = new FriendshipService(new FriendshipFileRepository(frfile, new FriendshipValidator()));
        userSrv = new UserService(new UserFileRepository(userfile, new UserValidator()));
    }
    public UI(String url, String username, String password) {
        friendSrv = new FriendshipService(new FriendshipDBRepository(url, username, password, new FriendshipValidator()));
        userSrv = new UserService(new UserDBRepository(url, username, password, new UserValidator()));
    }
    private void populateList() {
        userSrv.addUser("Pop", "Nicolae");
        userSrv.addUser("Popa", "Cosmin");
        userSrv.addUser("Negrea", "Andrei");
        userSrv.addUser("Paslaru", "Gabriel");
        userSrv.addUser("Petrutiu", "Mihai");
        userSrv.addUser("Pintiliciuc", "Maria");
        userSrv.addUser("Suciu", "Sergiu");
        userSrv.addUser("Perta", "Rares");
    }

    private void menu() {
        System.out.println("");
        System.out.println("Choose command:");
        System.out.println("1. Show all users");
        System.out.println("2. Add user");
        System.out.println("3. Remove user");
        System.out.println("4. Show all friendships");
        System.out.println("5. Show user's friends");
        System.out.println("6. Add user's friend");
        System.out.println("7. Delete user's friend");
        System.out.println("8. Number of communities");
        System.out.println("0. Exit");
        System.out.print("Your command: ");
    }

    private void ShowAllUsers(List<User> list) {

        if (list.isEmpty()) {
            System.out.println();
            System.out.println("Empty list");
        }
        else {
            System.out.println();
            int i = 0;
            System.out.println( list.size() + " Users");
            for (User u : list) {
                System.out.println(i + ". " + u);
                i++;
            }
            System.out.println();
        }
    }

    private void AddUser() {
        String firstName;
        String lastName;
        System.out.println("");

        System.out.print("Last name: ");
        lastName = scanner.nextLine();

        System.out.print("First name: ");
        firstName = scanner.nextLine();
        try {
            userSrv.addUser(lastName, firstName);
            System.out.println("User added successfully\n");
        } catch (ValidationException ve) {
            System.out.println(ve.getMessage());
        }
    }

    private void RemoveUser() {
        System.out.print("Last Name: ");
        String substring = scanner.nextLine();
        ArrayList<User> matchingUsers = userSrv.getUsersWithFirstName(substring);
        ShowAllUsers(matchingUsers);
        if (!matchingUsers.isEmpty()) {
            System.out.println("Index of the user that you want to remove: ");
            String indexStr = scanner.nextLine();
            int index = Integer.parseInt(indexStr);
            try {
                userSrv.removeUser(matchingUsers.get(index));
                userSrv.removeUserfromUsers(matchingUsers.get(index));
                for(User u: userSrv.getUsers())
                    if(friendSrv.checkFriendship(matchingUsers.get(index).getId(),u.getId()))
                        friendSrv.deleteFriendship(matchingUsers.get(index).getId(),u.getId());
                System.out.println("User removed successfully!\n");
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            }
        }
        else {
            System.out.println("No users with first name: " + substring);
        }
    }

    private void ShowAllFriendships(List<Friendship> list) {

        if (list.isEmpty()) {
            System.out.println();
            System.out.println("No friendship stored");
        } else {
            System.out.println();
            int i = 0;
            for (Friendship f : list) {
                UUID id1 = f.getId1();
                UUID id2 = f.getId2();
                String user1 = userSrv.getFullName(id1);
                String user2 = userSrv.getFullName(id2);
                System.out.println(i + ". " + user1 + " is friends with " + user2 + " " + dtf.format(f.getTime()));
                i++;
            }
            System.out.println();
        }
    }

    private  void ShowFriendsForUser() {
        ShowAllUsers(userSrv.getUsers());
        System.out.println("Who's friendlist you want to show?(index)");

        String Index1 = scanner.nextLine();
        int index1 = Integer.parseInt(Index1);


        System.out.println("User's friendlist: ");
        int i = 0;
        for (Friendship friend : friendSrv.getAllFriendships()) {
            if (userSrv.getUsers().get(index1).getId().equals(friend.getId1())) {
                System.out.println(i + ". " + userSrv.getFullName(friend.getId2()));
                i++;
            }
            if( userSrv.getUsers().get(index1).getId().equals(friend.getId2())) {
                System.out.println(i + ". " + userSrv.getFullName(friend.getId1()));
                i++;
            }
        }
        if (i == 0) {
            System.out.println();
            System.out.println("User has no friends :(");
        }
    }

    private void AddFriend()
    {
        ShowAllUsers(userSrv.getUsers());
        System.out.println("Give out 2 indexes for creating friendship: ");

        System.out.print("First index: ");
        String Index1 = scanner.nextLine();
        int index1 = Integer.parseInt(Index1);

        System.out.print("Second index: ");
        String Index2 = scanner.nextLine();
        int index2 = Integer.parseInt(Index2);

        try{
            userSrv.addFriend(userSrv.getUsers().get(index1).getId(), userSrv.getUsers().get(index2).getId());
            friendSrv.addFriendship(userSrv.getUsers().get(index1).getId(),userSrv.getUsers().get(index2).getId());
            System.out.println("Friendship was saved successfully!\n");
        } catch (FriendshipException e){
            System.out.println(e.getMessage());
        }
    }

    private void DeleteFriend() {
        ArrayList<Friendship> friends = new ArrayList<>();
        ShowAllUsers(userSrv.getUsers());
        System.out.println("Who's friendlist you want to edit?(index)");

        String Index1 = scanner.nextLine();
        int index1 = Integer.parseInt(Index1);

        System.out.println("User's friendlist: ");
        int i = 0;
        for (Friendship friend : friendSrv.getAllFriendships()) {
            if (userSrv.getUsers().get(index1).getId().equals(friend.getId1())) {
                System.out.println(i + ". " + userSrv.getFullName(friend.getId2()));
                i++;
                friends.add(friend);
            }
            if (userSrv.getUsers().get(index1).getId().equals(friend.getId2())) {
                System.out.println(i + ". " + userSrv.getFullName(friend.getId1()));
                i++;
                friends.add(friend);
            }
        }
        if(i==0) {
            System.out.println();
            System.out.println("User has no friends :(");
        }
        else {
            System.out.println("");
            System.out.print("Select the friend you want to remove(index): ");
            String Index2 = scanner.nextLine();
            int index2 = Integer.parseInt(Index2);
            //friendSrv.deleteFriendship(userSrv.getUsers().get(index1).getFriends().get(index2).getId(), userSrv.getUsers().get(index1).getId());
            friendSrv.deleteFriendship(friends.get(index2).getId1(),friends.get(index2).getId2());
            userSrv.getUsers().get(index1).getFriends().remove(userSrv.getUsers().get(index2));
            userSrv.getUsers().get(index2).getFriends().remove(userSrv.getUsers().get(index1));
        }

    }

    void DFS(int k, int[] P, int[][] A, int n) {
        P[k] = 1;
        for (int i = 1; i <= n; i++)
            if (A[k][i] == 1 && P[i] == 0) DFS(i, P, A, n);
    }

    private void RelatedComponents() {
        int[] P = new int[101];
        int[][] A = new int[101][101];
        int n = 0;
        int cnt=0;
        for (User user : userSrv.getUsers()) {
            int aux1 = userSrv.getUsers().indexOf(user);
            for (User user1 : user.getFriends()) {
                int aux2 = userSrv.getUsers().indexOf(user1);
                if (A[aux1][aux2] == 0) {
                    A[aux1][aux2] = A[aux2][aux1] = 1;
                    n++;
                }
            }
        }
        for(int i=0;i<n;i++)
            if(P[i]==0)
            {
                cnt++;
                DFS(i,P,A,n);
            }
        System.out.println("There are " + cnt + " communities");
    }
    public void run() {
        boolean run = true;
        String line;
        int command;
        //populateList();
        while (run) {
            menu();
            line = scanner.nextLine();
            command = Integer.parseInt(line);
            if (command == 0) run = false;
            else if (command == 1) ShowAllUsers(userSrv.getUsers());
            else if (command == 2) AddUser();
            else if (command == 3) RemoveUser();
            else if (command == 4) ShowAllFriendships(friendSrv.getAllFriendships());
            else if (command == 5) ShowFriendsForUser();
            else if (command == 6) AddFriend();
            else if (command == 7) DeleteFriend();
            else if (command == 8) RelatedComponents();
            else System.out.println("Invalid command!");


        }
    }
}

