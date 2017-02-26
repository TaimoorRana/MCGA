package com.concordia.mcga.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;
import java.util.List;

public class Class {
    private String name;
    private Date startTime;
    private Date endTime;
    private List<Room> rooms;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<Room> getRooms() {

        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Class aClass = (Class) o;

        return new EqualsBuilder()
            .append(name, aClass.name)
            .append(startTime, aClass.startTime)
            .append(endTime, aClass.endTime)
            .append(rooms, aClass.rooms)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(name)
            .append(startTime)
            .append(endTime)
            .append(rooms)
            .toHashCode();
    }
}
