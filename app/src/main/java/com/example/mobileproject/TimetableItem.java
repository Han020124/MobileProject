package com.example.mobileproject;

public class TimetableItem {
    private int id; // DB PK
    private String subjectName;
    private String day;
    private int startPeriod;
    private int duration;
    private String color;

    public TimetableItem(int id, String subjectName, String day, int startPeriod, int duration, String color) {
        this.id = id;
        this.subjectName = subjectName;
        this.day = day;
        this.startPeriod = startPeriod;
        this.duration = duration;
        this.color = color;
    }

    // Getter/Setter
    public int getId() { return id; }
    public String getSubjectName() { return subjectName; }
    public String getDay() { return day; }
    public int getStartPeriod() { return startPeriod; }
    public int getDuration() { return duration; }
    public String getColor() { return color; }
}
