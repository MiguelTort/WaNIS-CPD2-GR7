package sample.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import objects.Account;
import objects.Event;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


public class LogInScreenController {

    public Button LogInBtnAccept;
    public TextField UsernameTField;
    public PasswordField PasswordTField;
    public Label LogInLabel;
    public Button CreateAccountBtnAccept;
    public Button DeleteAccountBtnAccept;
    public Label InfoLabel;
    private boolean openWindow;
    private boolean createAccountMode;
    private boolean deleteAccountMode;
    public Account receivedAccount;
    String inline = "";

    public void checkAccountInfo(ActionEvent actionEvent) throws IOException {
        if(!openWindow){
            if(UsernameTField.getText().compareTo("")==0 || PasswordTField.getText().compareTo("")==0){
                InfoLabel.setText("Please do not leave any information blank.");
            }else if(!createAccountMode && !deleteAccountMode){
                URL url = new URL("http://localhost:8080/account/login/"+ UsernameTField.getText() + "/" + PasswordTField.getText());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                try{
                    conn.connect();
                    int responsecode = conn.getResponseCode();

                    if(responsecode==200){
                        Scanner scanner = new Scanner(url.openStream());
                        //Write all the JSON data into a string using a scanner
                        while (scanner.hasNext()) {
                            inline += scanner.nextLine();
                        }
                        scanner.close();
                        if(inline.compareTo("")==0){
                            openWindow = true;
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("FXMLs/LogInFail.fxml"));
                            Scene errorScene = new Scene(loader.load(), 250, 50);
                            Stage errorStage = new Stage();
                            errorStage.setTitle("Failed to Log In");
                            errorStage.setScene(errorScene);
                            errorStage.show();
                            errorStage.setOnCloseRequest(event ->{
                                openWindow = false;
                            });
                        }else{
                            receivedAccount = new Account();
                            receivedAccount.user = takeUsername();
                            receivedAccount.events = takeEvents();

                            changeWindow();
                        }
                    }
                    conn.disconnect();
                }catch(IOException connectError){
                    InfoLabel.setText("Could not connect to server.");
                }
            }else if(createAccountMode){
                String test = "{\"name\": \"" + UsernameTField.getText() + "\",\n\"password\": \"" + PasswordTField.getText() + "\",\n\"events\": \"\"\n}";
                byte[] out = test.getBytes(StandardCharsets.UTF_8);
                int length = out.length;
                URL url = new URL("http://localhost:8080/account/register");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setFixedLengthStreamingMode(length);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);

                try(OutputStream os = conn.getOutputStream()) {
                    conn.connect();
                    os.write(out);
                    receivedAccount = new Account();
                    receivedAccount.user = UsernameTField.getText();
                    receivedAccount.events = null;
                    changeWindow();
                    conn.disconnect();
                }catch(IOException connectError){
                    InfoLabel.setText("Could not connect to server.");
                }
            }else{
                URL url = new URL("http://localhost:8080/account/deletion/"+ UsernameTField.getText() + "/" + PasswordTField.getText());
                String ping = "ping";
                byte[] out = ping.getBytes(StandardCharsets.UTF_8);
                int length = out.length;
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setFixedLengthStreamingMode(length);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);

                try(OutputStream os = conn.getOutputStream()) {
                    conn.connect();
                    os.write(out);
                    InfoLabel.setText("Account with chosen username and password has been deleted. Please enter other Log In information.");
                    LogInLabel.setText("Please enter other Log In information.");
                    conn.disconnect();
                }catch(IOException connectError){
                    LogInLabel.setText("Could not connect to server.");
                }
                deleteAccountMode = false;
                LogInBtnAccept.setText("Log In");
                DeleteAccountBtnAccept.setText("Delete Account");
                CreateAccountBtnAccept.setVisible(true);
            }
        }
    }

    private String takeUsername(){
        String info[] = inline.split(",");
        String info2[] = info[1].split(":");
        String user = info2[1].replace("\"", "");
        return user;
    }

    private Event[] takeEvents(){
        String info[] = inline.split(",");
        String info2[] = info[3].split(":");
        System.out.println("-"+info2[1].replace("\"", "").replace("}", "")+"-");
        if(info2[1].replace("\"", "").replace("}", "").compareTo("")!=0 || info2[1].replace("\"", "").replace("}", "").compareTo(" ")!=0 || info2[1].compareTo("")!=0 || info2[1].compareTo(" ")!=0){

            String info3[] = info2[1].split(" ");
            info3[0] = info3[0].replace("\"", "");
            info3[info3.length-1] = info3[info3.length-1].replace("\"", "");
            info3[info3.length-1] = info3[info3.length-1].replace("}", "");
            Event events[] = new Event[info3.length];
            for(int z = 0; z < info3.length; z++){
                String info4[] = info3[z].split("/");
                if(info4[z].compareTo("")==0 || info4[z].compareTo(" ")==0){
                    return new Event[0];
                }
                events[z] = new Event();
                events[z].year = Integer.parseInt(info4[0]);
                events[z].day = Integer.parseInt(info4[1]);
                events[z].month = Integer.parseInt(info4[2]);
                events[z].info = info4[3].replace("-", " ");
                String info5[] = info4[4].split(";");
                events[z].startHour = Integer.parseInt(info5[0]);
                events[z].startMin = Integer.parseInt(info5[1]);
                events[z].endHour = Integer.parseInt(info5[2]);
                events[z].endMin = Integer.parseInt(info5[3]);
            }
            return events;
        }
        return new Event[0];
    }

    public void createAccount(ActionEvent actionEvent) {
        if(!createAccountMode){
            createAccountMode = true;
            LogInLabel.setText("Please enter your account information.");
            LogInBtnAccept.setText("Create Account");
            CreateAccountBtnAccept.setText("Exit");
            DeleteAccountBtnAccept.setVisible(false);
            InfoLabel.setText("");
            PasswordTField.setText("");
            UsernameTField.setText("");
        }else{
            createAccountMode = false;
            LogInLabel.setText("Please enter your Log In information.");
            LogInBtnAccept.setText("Log In");
            CreateAccountBtnAccept.setText("Create Account");
            DeleteAccountBtnAccept.setVisible(true);
            InfoLabel.setText("");
            PasswordTField.setText("");
            UsernameTField.setText("");
        }
    }

    public void changeWindow() throws IOException {
        Stage stage = (Stage) LogInBtnAccept.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("FXMLs/sample.fxml"));
        Parent root = loader.load();

        Controller cont = loader.getController();
        cont.init(receivedAccount);
        stage.setTitle("Calendar App!");
        stage.setScene(new Scene(root, 1200, 750));
    }

    public void deleteAccount(ActionEvent actionEvent) {
        if(!deleteAccountMode){
            deleteAccountMode = true;
            LogInLabel.setText("Please enter your account information for DELETION.");
            LogInBtnAccept.setText("Delete Account");
            DeleteAccountBtnAccept.setText("Exit");
            CreateAccountBtnAccept.setVisible(false);
            InfoLabel.setText("");
            PasswordTField.setText("");
            UsernameTField.setText("");
        }else{
            deleteAccountMode = false;
            LogInLabel.setText("Please enter your Log In information.");
            LogInBtnAccept.setText("Log In");
            DeleteAccountBtnAccept.setText("Delete Account");
            CreateAccountBtnAccept.setVisible(true);
            InfoLabel.setText("");
            PasswordTField.setText("");
            UsernameTField.setText("");
        }
    }
}
