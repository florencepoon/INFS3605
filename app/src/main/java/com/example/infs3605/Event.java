package com.example.infs3605;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Event implements Parcelable {
    //attributes
    private Integer eventID;
    private String eventName;
    private String eventOrganiser;
    private String eventFaculty;
    private String eventCategory;
    private String eventParticipation;
    private String eventLocation;
    private Date eventDate;
    private String eventStartTime;
    private String eventEndTime;
    private String eventAttachments;
    private String eventLinks;
    private String eventComments;

    public Event() {
    }

    // Constructor
    public Event(Integer eventID, String eventName, String eventOrganiser, String eventFaculty, String eventCategory,
                 String eventParticipation, String eventLocation, Date eventDate, String eventStartTime, String eventEndTime) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventOrganiser = eventOrganiser;
        this.eventFaculty = eventFaculty;
        this.eventCategory = eventCategory;
        this.eventParticipation = eventParticipation;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
    }

    protected Event(Parcel in) {
        if (in.readByte() == 0) {
            eventID = null;
        } else {
            eventID = in.readInt();
        }
        eventName = in.readString();
        eventOrganiser = in.readString();
        eventFaculty = in.readString();
        eventCategory = in.readString();
        eventParticipation = in.readString();
        eventLocation = in.readString();
        eventStartTime = in.readString();
        eventEndTime = in.readString();
        eventAttachments = in.readString();
        eventLinks = in.readString();
        eventComments = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

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

    public String getEventFaculty() {
        return eventFaculty;
    }

    public void setEventFaculty(String eventFaculty) {
        this.eventFaculty = eventFaculty;
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

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (eventID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(eventID);
        }
        dest.writeString(eventName);
        dest.writeString(eventOrganiser);
        dest.writeString(eventFaculty);
        dest.writeString(eventCategory);
        dest.writeString(eventParticipation);
        dest.writeString(eventLocation);
        dest.writeString(eventStartTime);
        dest.writeString(eventEndTime);
        dest.writeString(eventAttachments);
        dest.writeString(eventLinks);
        dest.writeString(eventComments);
    }
}

