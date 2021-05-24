package sample.Controllers;



import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import objects.Account;
import objects.Event;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class Controller {
    public Account currentAccount;
    public GridPane main;
    public Group displayedDays;
    public Label Test;
    public Button leftMonthBtn;
    public Date calendarDate;
    public Date currentDate;
    public Button logOutBtn;
    public TextField dayTField;
    public TextField monthTField;
    public TextField yearTField;
    public TextField infoTField;
    public TextArea infoArea;
    public Label dayLabel;
    public Label monthLabel;
    public Label yearLabel;
    public Label infoLabel;
    public Button saveEventBtn;
    public Button createEventBtn;
    public Button deleteEventBtn;
    private boolean openWindow = false;
    private boolean createEvent = false;
    private boolean deleteEvent = false;

    public void init(Account acc) {
        currentAccount = acc;
        Date date = new Date();
        calendarDate = date;
        currentDate = date;
        displayMonth();
        displayedDays.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if(!deleteEvent){
                String getId = event.getPickResult().getIntersectedNode().getId();
                int year = currentDate.getYear()+1900;
                int month = currentDate.getMonth()+1;
                for(int z = 0; z < currentAccount.events.length; z++) {
                    if (currentAccount.events[z].year == year && currentAccount.events[z].month == month && currentAccount.events[z].day == Integer.parseInt(getId)) {
                        infoArea.setText(currentAccount.events[z].info);
                        break;
                    }
                }
            }else{
                String getId = event.getPickResult().getIntersectedNode().getId();
                int year = currentDate.getYear()+1900;
                int month = currentDate.getMonth()+1;
                for(int z = 0; z < currentAccount.events.length; z++) {
                    if (currentAccount.events[z].year == year && currentAccount.events[z].month == month && currentAccount.events[z].day == Integer.parseInt(getId)) {
                        Event[] events = new Event[currentAccount.events.length-1];
                        for(int a = 0, b = 0; a < currentAccount.events.length-1;a++,b++){
                            if(a == z){
                                b++;
                            }
                            events[a] = currentAccount.events[b];
                        }
                        currentAccount.events = events;
                        displayMonth();
                        break;
                    }
                }
                deleteEvent = false;
                deleteEventBtn.setText("Delete Event");
            }
        });
    }

    private int findMonthDays(String name) {
        if (name.compareTo("Jan") == 0) {
            return 31;
        } else if (name.compareTo("Feb") == 0) {
            return 28;
        } else if (name.compareTo("Mar") == 0) {
            return 31;
        } else if (name.compareTo("Apr") == 0) {
            return 30;
        } else if (name.compareTo("May") == 0) {
            return 31;
        } else if (name.compareTo("Jun") == 0) {
            return 30;
        } else if (name.compareTo("Jul") == 0) {
            return 31;
        } else if (name.compareTo("Aug") == 0) {
            return 31;
        } else if (name.compareTo("Sep") == 0) {
            return 30;
        } else if (name.compareTo("Oct") == 0) {
            return 31;
        } else if (name.compareTo("Nov") == 0) {
            return 30;
        } else if (name.compareTo("Dec") == 0) {
            return 31;
        }
        return 30;
    }

    private int processStartDayMonth(int dNum, int dName) {
        int startDay = dName;
        while (dNum > 1) {
            if (startDay > 0) {
                startDay = startDay - 1;
            } else {
                startDay = 6;
            }
            dNum = dNum - 1;
        }
        return startDay;
    }

    private void generateMonth(int dayName, int maxDay, String[] dateIn) {
        int day = 1;
        int level = 0;
        while (day <= maxDay) {
            Rectangle rect = new Rectangle(+(121.4 * dayName), 0 + (level * 63.5), 120, 62.5);
            Label lab = new Label(Integer.toString(day));
            lab.setTranslateX(5 + (121.4 * dayName));
            lab.setTranslateY(0 + (level * 63.5));
            lab.setAlignment(Pos.TOP_LEFT);
            lab.setId(Integer.toString(day));
            rect.setId(Integer.toString(day));
            rect.setFill(Color.WHITESMOKE);
            displayedDays.getChildren().add(rect);
            displayedDays.getChildren().add(lab);
            if (dayName < 6) {
                dayName = dayName + 1;
            } else {
                dayName = 0;
                level++;
            }
            day++;
        }
        Test.setText(dateIn[1] + " " + dateIn[5]);
    }

    private void generateEvents(int dayName, int maxDay, String[] dateIn) {
        int day = 1;
        int level = 0;
        int year = currentDate.getYear()+1900;
        int month = currentDate.getMonth()+1;
        if(currentAccount.events != null){
            for(int z = 0; z < currentAccount.events.length; z++){
                if(currentAccount.events[z].year == year && currentAccount.events[z].month == month){
                    day = (currentAccount.events[z].day - 1 + dayName)%7;
                    level = (currentAccount.events[z].day - 1 + dayName)/7;
                    Label eventLab = new Label("!");
                    eventLab.setTranslateX(20 + (121.4 * day));
                    eventLab.setTranslateY(31.25 + (level * 63.5));
                    eventLab.setId(Integer.toString(day));
                    displayedDays.getChildren().add(eventLab);
                }
            }
        }
    }


    public void backMonth(ActionEvent actionEvent) {
        calendarDate.setMonth(calendarDate.getMonth() - 1);
        displayedDays.getChildren().clear();
        displayMonth();
    }

    public void forwMonth(ActionEvent actionEvent) {
        calendarDate.setMonth(calendarDate.getMonth() + 1);
        displayedDays.getChildren().clear();
        displayMonth();
    }

    private void displayMonth() {
        String[] dateInfo = calendarDate.toString().split(" ");
        int dayNum = Integer.parseInt(dateInfo[2]);
        int dayName = calendarDate.getDay();
        int dayStart = (processStartDayMonth(dayNum, dayName));
        generateMonth(dayStart, findMonthDays(dateInfo[1]), dateInfo);
        generateEvents(dayStart, findMonthDays(dateInfo[1]), dateInfo);
    }

    public void logOut(ActionEvent actionEvent) throws IOException {
        String test = "";
        for(int a = 0; a < currentAccount.events.length; a++){
            test += currentAccount.events[a].year + "/" + currentAccount.events[a].day + "/" + currentAccount.events[a].month + "/" + currentAccount.events[a].info.replace(" ", "-");
            if((a+1)!=currentAccount.events.length){
                test += " ";
            }
        }
        byte[] out = test.getBytes(StandardCharsets.UTF_8);
        int length = out.length;
        URL url = new URL("http://localhost:8080/account/"+currentAccount.user+"/events");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setFixedLengthStreamingMode(length);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);
        conn.connect();
        try(OutputStream os = conn.getOutputStream()) {
            os.write(out);
        }
        conn.disconnect();
        Stage stage = (Stage) logOutBtn.getScene().getWindow();
        stage.close();
    }

    public void createEventMode(ActionEvent actionEvent) {
        if(!createEvent){
            createEvent = true;
            dayTField.setVisible(true);
            monthTField.setVisible(true);
            yearTField.setVisible(true);
            infoTField.setVisible(true);
            infoArea.setVisible(false);
            dayLabel.setVisible(true);
            monthLabel.setVisible(true);
            yearLabel.setVisible(true);
            infoLabel.setVisible(true);
            saveEventBtn.setVisible(true);
            createEventBtn.setText("Leave Create Event");
        }else{
            createEvent = false;
            dayTField.setVisible(false);
            monthTField.setVisible(false);
            yearTField.setVisible(false);
            infoTField.setVisible(false);
            infoArea.setVisible(true);
            dayLabel.setVisible(false);
            monthLabel.setVisible(false);
            yearLabel.setVisible(false);
            infoLabel.setVisible(false);
            saveEventBtn.setVisible(false);
            createEventBtn.setText("Create Event");
        }
    }

    public void saveEvent(ActionEvent actionEvent) {
        if(yearTField.getText().compareTo("")==0 && dayTField.getText().compareTo("")==0 && monthTField.getText().compareTo("")==0 && infoTField.getText().compareTo("")==0){

        }else{
            Event inputEvent = new Event(Integer.parseInt(yearTField.getText()),Integer.parseInt(dayTField.getText()),Integer.parseInt(monthTField.getText()));
            inputEvent.info = infoTField.getText();
            Event[] events = new Event[currentAccount.events.length+1];
            for(int z = 0; z < currentAccount.events.length;z++){
                events[z] = currentAccount.events[z];
            }
            events[currentAccount.events.length]=inputEvent;
            currentAccount.events = events;
            displayMonth();
            System.out.println("Event saved!");
            createEvent = false;
            dayTField.setVisible(false);
            monthTField.setVisible(false);
            yearTField.setVisible(false);
            infoTField.setVisible(false);
            infoArea.setVisible(true);
            dayLabel.setVisible(false);
            monthLabel.setVisible(false);
            yearLabel.setVisible(false);
            infoLabel.setVisible(false);
            saveEventBtn.setVisible(false);
            createEventBtn.setText("Create Event");
        }
    }

    public void deleteEventMode(ActionEvent actionEvent) {
        if(!deleteEvent){
            deleteEvent = true;
            deleteEventBtn.setText("Exit Delete Event");
        }else{
            deleteEvent = false;
            deleteEventBtn.setText("Delete Event");
        }
    }
}
