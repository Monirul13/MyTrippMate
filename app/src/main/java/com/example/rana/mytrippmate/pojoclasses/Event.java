package com.example.rana.mytrippmate.pojoclasses;

import java.io.Serializable;

/**
 * Created by Rana on 12/7/2018.
 */

public class Event implements Serializable {

    private String id;
    private String eventName;
    private String startingLocation;
    private String destination;
    private String departureTime;
    private String budget;

    public Event() {
    }

    public Event(String id, String eventName, String startingLocation, String destination, String departureTime, String budget) {
        this.id = id;
        this.eventName = eventName;
        this.startingLocation = startingLocation;
        this.destination = destination;
        this.departureTime = departureTime;
        this.budget = budget;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getStartingLocation() {
        return startingLocation;
    }

    public void setStartingLocation(String startingLocation) {
        this.startingLocation = startingLocation;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }
}
