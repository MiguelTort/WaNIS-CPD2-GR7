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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Controller {
    public Account currentAccount;
    public GridPane main;
    public Group displayedDays;
    public Label test;
    public Button leftMonthBtn;
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
    public Calendar currentCalendar = Calendar.getInstance();
    public Calendar displayCalendar = Calendar.getInstance();
    public TextField startHourTField;
    public Label timeSepLabel1;
    public TextField startMinuteTField;
    public Label timeSepLabel2;
    public TextField endHourTField;
    public Label timeSepLabel3;
    public TextField endMinuteHourTField;
    public Label timeLabel;
    public Group toDoDisplay;
    public Label toDo;
    public Event[] weatherEvents = new Event[3];
    public Event[] newsEvents = new Event[10];
    public TextArea NewsDisplay;
    public Button viewNewsBtn;
    private int chosenDay = 0;
    private boolean createEvent = false;
    private boolean deleteEvent = false;
    private boolean viewingNews = false;

    public void init(Account acc) throws IOException {
        currentAccount = acc;
        if(currentAccount.events == null){
            currentAccount.events = new Event[0];
        }
        findWeather();
        findNews();
        displayMonth();
        displayToDo();
        toDoDisplay.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->{
            String getId = event.getPickResult().getIntersectedNode().getId();
            int year = displayCalendar.get(Calendar.YEAR);
            int month = displayCalendar.get(Calendar.MONTH) + 1;
            if(deleteEvent){
                int count = 0;
                for(int z = 0; z < currentAccount.events.length; z++) {
                    if (currentAccount.events[z].year == year && currentAccount.events[z].month == month && currentAccount.events[z].day == chosenDay){
                        if(count>=Integer.parseInt(getId)){
                            Event[] events = new Event[currentAccount.events.length-1];
                            for(int a = 0, b = 0; a < currentAccount.events.length-1;a++,b++){
                                if(a == z){
                                    b++;
                                }
                                events[a] = currentAccount.events[b];
                            }
                            currentAccount.events = events;
                            deleteEvent = false;
                            toDo.setText("To Do");
                            deleteEventBtn.setText("Delete Event");
                            createEventBtn.setVisible(true);
                            viewNewsBtn.setVisible(true);
                            displayToDo();
                            displayMonth();
                            break;
                        }else{
                            count++;
                        }
                    }
                }
            }
        });
        displayedDays.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            String getId = event.getPickResult().getIntersectedNode().getId();
            chosenDay = Integer.parseInt(getId);
            toDoDisplay.getChildren().clear();
            int year = displayCalendar.get(Calendar.YEAR);
            int month = displayCalendar.get(Calendar.MONTH) + 1;
            infoArea.setText("");
            if(!createEvent){
                int count = 0;
                for(int z = 0; z < currentAccount.events.length; z++) {
                    if (currentAccount.events[z].year == year && currentAccount.events[z].month == month && currentAccount.events[z].day == Integer.parseInt(getId)) {
                        Rectangle rect = new Rectangle(0, 0 + (count * 105), 280, 100);
                        Label daylab = new Label(findMonthName(currentAccount.events[z].month-1)+" "+currentAccount.events[z].day);
                        daylab.setTranslateY(5 + (count * 105));
                        daylab.setTranslateX(5);
                        Label lab = new Label(currentAccount.events[z].info);
                        lab.setTranslateY(35 + (count * 105));
                        lab.setTranslateX(5);
                        Label timelab = new Label(currentAccount.events[z].startHour+":"+currentAccount.events[z].startMin+" - "+currentAccount.events[z].endHour+":"+currentAccount.events[z].endMin);
                        timelab.setTranslateY(20 + (count * 105));
                        timelab.setTranslateX(5);
                        timelab.setAlignment(Pos.TOP_LEFT);
                        rect.setFill(Color.WHITESMOKE);
                        rect.setId(count+"");
                        lab.setId(count+"");
                        timelab.setId(count+"");
                        daylab.setId(count+"");
                        toDoDisplay.getChildren().add(rect);
                        toDoDisplay.getChildren().add(lab);
                        toDoDisplay.getChildren().add(timelab);
                        toDoDisplay.getChildren().add(daylab);
                        count++;
                    }
                }
                for(int x = 0; x < weatherEvents.length; x++){
                    if (weatherEvents[x].year == year && weatherEvents[x].month == month && weatherEvents[x].day == Integer.parseInt(getId)) {
                        infoArea.setText(infoArea.getText()+"\n\n"+weatherEvents[x].info);
                        break;
                    }
                }
            }else if(createEvent){
                dayTField.setText(getId);
                monthTField.setText((displayCalendar.get(Calendar.MONTH)+1)+"");
                yearTField.setText((displayCalendar.get(Calendar.YEAR))+"");
            }
        });
    }

    private void displayToDo() {
        toDoDisplay.getChildren().clear();
        toDo.setText("To Do");
        NewsDisplay.setVisible(false);
        toDoDisplay.setVisible(true);
        int latestYear = 0;
        int max = 5;
        int count = 0;
        for(int z = 0; z < currentAccount.events.length; z++) {
            if(currentAccount.events[z].year>latestYear){
                latestYear = currentAccount.events[z].year;
            }
        }
        for(int w = currentCalendar.get(Calendar.YEAR); w < latestYear+1; w++) {
            for(int x = 1; x < 13; x++){
                for(int a = 1; a < 32; a++) {
                    for (int y = 0; y < 25; y++) {
                        for (int z = 0; z < currentAccount.events.length; z++) {
                            if (a == currentAccount.events[z].day && y == currentAccount.events[z].startHour && x == currentAccount.events[z].month && w == currentAccount.events[z].year) {
                                Rectangle rect = new Rectangle(0, 0 + (count * 105), 280, 100);
                                Label daylab = new Label(findMonthName(currentAccount.events[z].month - 1) + " " + currentAccount.events[z].day);
                                daylab.setTranslateY(5 + (count * 105));
                                daylab.setTranslateX(5);
                                Label lab = new Label(currentAccount.events[z].info);
                                lab.setTranslateY(35 + (count * 105));
                                lab.setTranslateX(5);
                                Label timelab = new Label(currentAccount.events[z].startHour + ":" + currentAccount.events[z].startMin + " - " + currentAccount.events[z].endHour + ":" + currentAccount.events[z].endMin);
                                timelab.setTranslateY(20 + (count * 105));
                                timelab.setTranslateX(5);
                                timelab.setAlignment(Pos.TOP_LEFT);
                                rect.setFill(Color.WHITESMOKE);
                                toDoDisplay.getChildren().add(rect);
                                toDoDisplay.getChildren().add(lab);
                                toDoDisplay.getChildren().add(timelab);
                                toDoDisplay.getChildren().add(daylab);
                                count++;
                            }
                            if (count >= max) {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private int findMonthDays(int month, int year) {
        if (month == 0) {
            return 31;
        } else if (month == 1) {
            if(year%4==0 && year%100==0 && year%400==0){
                return 29;
            }
            return 28;
        } else if (month == 2) {
            return 31;
        } else if (month == 3) {
            return 30;
        } else if (month == 4) {
            return 31;
        } else if (month == 5) {
            return 30;
        } else if (month == 6) {
            return 31;
        } else if (month == 7) {
            return 31;
        } else if (month == 8) {
            return 30;
        } else if (month == 9) {
            return 31;
        } else if (month == 10) {
            return 30;
        } else if (month == 11) {
            return 31;
        }
        return 30;
    }

    private String findMonthName(int month) {
        if (month == 0) {
            return "January";
        } else if (month == 1) {
            return "February";
        } else if (month == 2) {
            return "March";
        } else if (month == 3) {
            return "April";
        } else if (month == 4) {
            return "May";
        } else if (month == 5) {
            return "June";
        } else if (month == 6) {
            return "July";
        } else if (month == 7) {
            return "August";
        } else if (month == 8) {
            return "September";
        } else if (month == 9) {
            return "October";
        } else if (month == 10) {
            return "November";
        } else if (month == 11) {
            return "December";
        }
        return "Month";
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

    private void generateMonth(int dayName, int maxDay) {
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
            if(displayCalendar.get(Calendar.YEAR)==currentCalendar.get(Calendar.YEAR) && displayCalendar.get(Calendar.MONTH)==currentCalendar.get(Calendar.MONTH) && day == currentCalendar.get(Calendar.DATE)){
                rect.setFill(Color.CYAN);
            }else{
                rect.setFill(Color.WHITESMOKE);
            }
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
        test.setText(findMonthName(displayCalendar.get(Calendar.MONTH)) + " " + displayCalendar.get(Calendar.YEAR));
    }

    private void generateEvents(int dayName) {
        int day;
        int level;
        int year = displayCalendar.get(Calendar.YEAR);
        int month = displayCalendar.get(Calendar.MONTH) + 1;
        if(currentAccount.events != null){
            for(int z = 0; z < currentAccount.events.length; z++){
                if(currentAccount.events[z].year == year && currentAccount.events[z].month == month){
                    day = (currentAccount.events[z].day - 1 + dayName)%7;
                    level = (currentAccount.events[z].day - 1 + dayName)/7;
                    Label eventLab = new Label("!");
                    eventLab.setId(currentAccount.events[z].day+"");
                    eventLab.setTranslateX(20 + (121.4 * day));
                    eventLab.setTranslateY(31.25 + (level * 63.5));
                    eventLab.setId(Integer.toString(weatherEvents[z].day));
                    displayedDays.getChildren().add(eventLab);
                }
            }
            for(int x = 0; x < weatherEvents.length; x++){
                if(weatherEvents[x].year == year && weatherEvents[x].month == month){
                    ImageView icon = new ImageView(new Image("http:"+weatherEvents[x].extraInfo));
                    icon.prefWidth(62.5);
                    icon.prefHeight(62.5);
                    day = (weatherEvents[x].day - 1 + dayName)%7;
                    level = (weatherEvents[x].day - 1 + dayName)/7;
                    icon.setTranslateX(60 + (121.4 * day));
                    icon.setTranslateY(0 + (level * 63.5));
                    icon.setId(Integer.toString(weatherEvents[x].day));
                    displayedDays.getChildren().add(icon);
                }
            }
        }
    }


    public void backMonth(ActionEvent actionEvent) {
        displayCalendar.set(Calendar.MONTH, displayCalendar.get(Calendar.MONTH)-1);
        displayedDays.getChildren().clear();
        displayMonth();
    }

    public void forwMonth(ActionEvent actionEvent) {
        displayCalendar.set(Calendar.MONTH, displayCalendar.get(Calendar.MONTH)+1);
        displayedDays.getChildren().clear();
        displayMonth();
    }

    private void displayMonth() {
        int dayNum = displayCalendar.get(Calendar.DAY_OF_MONTH);
        int dayName = displayCalendar.get(Calendar.DAY_OF_WEEK)-1;
        int dayStart = (processStartDayMonth(dayNum, dayName));
        generateMonth(dayStart, findMonthDays(displayCalendar.get(Calendar.MONTH), displayCalendar.get(Calendar.YEAR)));
        generateEvents(dayStart);
    }

    public void logOut(ActionEvent actionEvent) throws IOException {
        System.out.println("Account saved 1");
        String test = "";
        if(currentAccount.events != null){
            System.out.println("Account saved 2");
            for(int a = 0; a < currentAccount.events.length; a++){
                test += currentAccount.events[a].year + "/" + currentAccount.events[a].day + "/" + currentAccount.events[a].month + "/" + currentAccount.events[a].info.replace(" ", "-") + "/" + currentAccount.events[a].startHour + ";" + currentAccount.events[a].startMin + ";" + currentAccount.events[a].endHour + ";" + currentAccount.events[a].endMin;
                if((a+1)!=currentAccount.events.length){
                    test += " ";
                }
            }
            if(currentAccount.events.length == 0){
                test = " ";
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
            startHourTField.setVisible(true);
            timeSepLabel1.setVisible(true);
            startMinuteTField.setVisible(true);
            timeSepLabel2.setVisible(true);
            endHourTField.setVisible(true);
            timeSepLabel3.setVisible(true);
            endMinuteHourTField.setVisible(true);
            timeLabel.setVisible(true);
            createEventBtn.setText("Leave Create Event");
            viewNewsBtn.setVisible(false);
            deleteEventBtn.setVisible(false);
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
            startHourTField.setVisible(false);
            timeSepLabel1.setVisible(false);
            startMinuteTField.setVisible(false);
            timeSepLabel2.setVisible(false);
            endHourTField.setVisible(false);
            timeSepLabel3.setVisible(false);
            endMinuteHourTField.setVisible(false);
            timeLabel.setVisible(false);
            createEventBtn.setText("Create Event");
            viewNewsBtn.setVisible(true);
            deleteEventBtn.setVisible(true);
        }
    }

    public void saveEvent(ActionEvent actionEvent) {
        if(yearTField.getText().compareTo("")==0 && dayTField.getText().compareTo("")==0 && monthTField.getText().compareTo("")==0 && infoTField.getText().compareTo("")==0 && startHourTField.getText().compareTo("")==0 && endHourTField.getText().compareTo("")==0 && startMinuteTField.getText().compareTo("")==0 && endMinuteHourTField.getText().compareTo("")==0){
            createError();
        }else if(Integer.parseInt(startHourTField.getText())>=25 || Integer.parseInt(startHourTField.getText())<=-1  || Integer.parseInt(endHourTField.getText())>=25 || Integer.parseInt(endHourTField.getText())<=-1 || Integer.parseInt(startHourTField.getText())>Integer.parseInt(endHourTField.getText()) ||
        Integer.parseInt(startMinuteTField.getText())>=60 || Integer.parseInt(startMinuteTField.getText())<=-1 || Integer.parseInt(endMinuteHourTField.getText())>=60 || Integer.parseInt(endMinuteHourTField.getText())<=-1){
            createError();
        }else{
            Event inputEvent = new Event(Integer.parseInt(yearTField.getText()),Integer.parseInt(dayTField.getText()),Integer.parseInt(monthTField.getText()), Integer.parseInt(startHourTField.getText()), Integer.parseInt(endHourTField.getText()), Integer.parseInt(startMinuteTField.getText()), Integer.parseInt(endMinuteHourTField.getText()));
            inputEvent.info = infoTField.getText();
            Event[] events = new Event[currentAccount.events.length+1];
            for(int z = 0; z < currentAccount.events.length;z++){
                events[z] = currentAccount.events[z];
            }
            events[currentAccount.events.length]=inputEvent;
            currentAccount.events = events;
            displayMonth();
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

            startHourTField.setVisible(false);
            timeSepLabel1.setVisible(false);
            startMinuteTField.setVisible(false);
            timeSepLabel2.setVisible(false);
            endHourTField.setVisible(false);
            timeSepLabel3.setVisible(false);
            endMinuteHourTField.setVisible(false);
            timeLabel.setVisible(false);

            createEventBtn.setText("Create Event");
            viewNewsBtn.setVisible(true);
            deleteEventBtn.setVisible(true);
            displayToDo();
        }
    }

    private void createError() {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("FXMLs/ErrorWindow.fxml"));
            Scene errorScene = new Scene(loader.load(), 250, 50);
            Stage errorStage = new Stage();
            errorStage.setTitle("Failed to Log In");
            errorStage.setScene(errorScene);
            errorStage.show();
        }catch(IOException e){

        }
    }

    public void deleteEventMode(ActionEvent actionEvent) {
        if(!deleteEvent){
            viewingNews = false;
            displayToDo();
            toDoDisplay.getChildren().clear();
            deleteEvent = true;
            deleteEventBtn.setText("Exit Delete Event");
            createEventBtn.setVisible(false);
            viewNewsBtn.setVisible(false);
            toDoDisplay.getChildren().clear();
            toDo.setText("Select Day to View Events");
        }else{
            deleteEvent = false;
            deleteEventBtn.setText("Delete Event");
            createEventBtn.setVisible(true);
            viewNewsBtn.setVisible(true);
            displayToDo();
            toDo.setText("To Do");
        }
    }

    public void findWeather() throws IOException {
        String test = "";
        URL url = new URL("http://api.weatherapi.com/v1/forecast.json?key=cd5358c7e2b54a289f005953213105&q=Manila&days=10&aqi=no&alerts=no");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        Scanner scanner = new Scanner(url.openStream());
        //Write all the JSON data into a string using a scanner
        while (scanner.hasNext()) {
            test += scanner.nextLine();
        }
        scanner.close();
        String test1[] = test.split("\"day\"");

        weatherEvents = new Event[test1.length-1];

        for(int x = 1; x < test1.length; x++){
            Date date = new Date();
            String test2[] = test1[x].split(",");
            weatherEvents[x-1] = new Event();
            date.setDate(date.getDate()+x-1);
            weatherEvents[x-1].day = date.getDate();
            weatherEvents[x-1].year = date.getYear()+1900;
            weatherEvents[x-1].month = date.getMonth()+1;
            weatherEvents[x-1].info = "Max Temperature = " + test2[0].split(":")[2] + " Celsius\nMin Temperature = " + test2[2].split(":")[1] + " Celsius\nChance of Rain = " + test2[14].split(":")[1].replace("\"", "") + "%";
            weatherEvents[x-1].extraInfo = test2[18].split(":")[1].replace("\"", "");
        }
        conn.disconnect();
    }

    public void findNews() throws IOException {
        String test = "";
        URL url = new URL("https://newsapi.org/v2/top-headlines?country=ph&apiKey=67f905863d2f4ef485f639492b7d6c29");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        Scanner scanner = new Scanner(url.openStream());
        //Write all the JSON data into a string using a scanner
        while (scanner.hasNext()) {
            test += scanner.nextLine();
        }
        scanner.close();
        String test1[] = test.split("\"title\":");
        newsEvents = new Event[test1.length-1];
        for(int x = 1; x < test1.length; x++){
            newsEvents[x-1] = new Event();
            String test2[] = test1[x].split("\"publishedAt\":\"")[1].split("-");
            newsEvents[x-1].year = Integer.parseInt(test2[0]);
            newsEvents[x-1].month = Integer.parseInt(test2[1]);
            newsEvents[x-1].day = Integer.parseInt(test2[2].split("T")[0]);
            newsEvents[x-1].info = test1[x].split(",\"description\":")[0] + "\n\n" + test1[x].split(",\"description\":")[1].split(",\"url\":\"")[0];
            newsEvents[x-1].extraInfo = test1[x].split(",\"url\":\"")[1].split("\",")[0];
        }
        conn.disconnect();
        NewsDisplay.setText("LATEST");
        int latestYear = 0;
        for(int z = 0; z < newsEvents.length; z++) {
            if(newsEvents[z].year>latestYear){
                latestYear = newsEvents[z].year;
            }
        }
        for(int w = currentCalendar.get(Calendar.YEAR); w < latestYear+1; w++) {
            for(int x = 1; x < 13; x++){
                for(int a = 1; a < 32; a++) {
                    for (int z = 0; z < newsEvents.length; z++) {
                        if (a == newsEvents[z].day && x == newsEvents[z].month && w == newsEvents[z].year) {
                            NewsDisplay.setText(NewsDisplay.getText()+"\n\n-------------------------------------\n\n"+newsEvents[z].info.replace("\"","")+"\n\nPublished: "+findMonthName(newsEvents[z].month-1)+" "+newsEvents[z].day+", "+newsEvents[z].year+"\n"+newsEvents[z].extraInfo);
                        }
                    }
                }
            }
        }
    }

    public void viewNews(ActionEvent actionEvent) {
        if(!viewingNews){
            viewingNews = true;
            toDoDisplay.getChildren().clear();
            toDo.setText("News");
            NewsDisplay.setVisible(true);
            toDoDisplay.setVisible(false);
            viewNewsBtn.setText("Exit News");
        }else{
            displayToDo();
            viewingNews = false;
            NewsDisplay.setVisible(false);
            toDoDisplay.setVisible(true);
            viewNewsBtn.setText("News");
        }
    }
}
