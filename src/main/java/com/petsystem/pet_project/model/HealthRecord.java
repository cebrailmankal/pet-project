package com.petsystem.pet_project.model;

public class HealthRecord {
    private String date;
    private String description;
    private String vetName;

    public HealthRecord(String date, String description, String vetName) {
        this.date = date;
        this.description = description;
        this.vetName = vetName;
    }
    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getVetName() {
        return vetName;
    }

    public void setDescription(String newDesc) {
        this.description = newDesc;
    }

    public void setDate(String newDate) {
        this.date = newDate;
    }
}