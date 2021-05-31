package objects;

public class Event {
    public int year;
    public int day;
    public int month;
    public int startHour;
    public int endHour;
    public int startMin;
    public int endMin;
    public String extraInfo;
    public String info;

    public Event(){

    }

    public Event(int yr, int dy, int mnth, int strtHr, int endHr, int strtMn, int endMn){
        year = yr;
        day = dy;
        month = mnth;
        startHour = strtHr;
        endHour = endHr;
        startMin = strtMn;
        endMin = endMn;
    }

}
