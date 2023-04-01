package com.example.infs3605;

public class Event {
    //attributes
    private Integer eventID;
    private String eventName;
    private String eventOrganiser;
    private String eventCategory;
    private String eventParticipation;
    private String eventLocation;
    private String eventDate;
    private String eventStartTime;
    private String eventEndTime;
    private String eventAttachments;
    private String	eventLinks;
    private String eventComments;

    // Constructor
    public Event(Integer eventID, String eventName, String eventOrganiser, String eventCategory,
                 String eventParticipation, String eventDate, String eventStartTime, String eventEndTime) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventOrganiser = eventOrganiser;
        this.eventCategory = eventCategory;
        this.eventParticipation = eventParticipation;
        this.eventDate = eventDate;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
    }

    // Getters and setters
    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventOrganiser() {
        return eventOrganiser;
    }

    public void setEventOrganiser(String eventOrganiser) {
        this.eventOrganiser = eventOrganiser;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    public String getEventParticipation() {
        return eventParticipation;
    }

    public void setEventParticipation(String eventParticipation) {
        this.eventParticipation = eventParticipation;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public String getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(String eventEndTime) {
        this.eventEndTime = eventEndTime;
    }

    public String getEventAttachments() {
        return eventAttachments;
    }

    public void setEventAttachments(String eventAttachments) {
        this.eventAttachments = eventAttachments;
    }

    public String getEventLinks() {
        return eventLinks;
    }

    public void setEventLinks(String eventLinks) {
        this.eventLinks = eventLinks;
    }

    public String getEventComments() {
        return eventComments;
    }

    public void setEventComments(String EventComments) {
        this.eventComments = eventComments;
    }
}
