package com.group7cpd.model;

public class Account {
    private String user;
    private String pass;
    private String date[];

    public Account(String user, String pass, String[] date) {
        this.user = user;
        this.pass = pass;
        this.date = date;
    }

    public Account(){}

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public String[] getDate() {
        return date;
    }

    private String giveList() {
        String listString = "";
        for(int x = 0; x < date.length;x++){
            if(x == 0){
                listString = listString + "\"" + date[x] + "\"";
            }else{
                listString = listString + ", \"" + date[x] + "\"";
            }

        }
        return listString;
    }

    @Override
    public String toString() {
        return "\n\t{\n\t\t" +
                "\"user\": \"" + user + "\",\n\t\t" +
                "\"pass\": \"" + pass + "\",\n\t\t" +
                "\"date\": [" + giveList() + "]\n\t" +
                '}';
        /* Sample account info
        {
            "user": "user5",
            "pass": "pass5",
            "date": [
                "2012/12/12/830/900", "2012/12/12/830/900"
            ]
        },
        {
            "user": "user1",
            "pass": "pass1",
            "date": [
                "2012/12/12/830/900"
            ]
        },
         */
    }
}
