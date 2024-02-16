package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * This class represents an Appointment object and provides methods to get/set its properties.
 */
public class Appointment {
    private int appointmentId;
    private String appointmentTitle;
    private String appointmentDescription;
    private String appointmentLocation;
    private String appointmentType;
    private LocalDate appointmentStartDate;
    private LocalDateTime appointmentStartTime;
    private LocalDate appointmentEndDate;
    private LocalDateTime appointmentEndTime;
    private int customerId;
    private int userId;
    private int contactId;
    private String contactFullName;

    public Appointment(int appointmentId,
                       String appointmentTitle,
                       String appointmentDescription,
                       String appointmentLocation,
                       String appointmentType,
                       LocalDate appointmentStartDate,
                       LocalDateTime appointmentStartTime,
                       LocalDate appointmentEndDate,
                       LocalDateTime appointmentEndTime,
                       int customerId,
                       int userId,
                       int contactId,
                       String contactFullName
    ) {
        this.appointmentId = appointmentId;
        this.appointmentTitle = appointmentTitle;
        this.appointmentDescription = appointmentDescription;
        this.appointmentLocation = appointmentLocation;
        this.appointmentType = appointmentType;
        this.appointmentStartDate = appointmentStartDate;
        this.appointmentStartTime = appointmentStartTime;
        this.appointmentEndDate = appointmentEndDate;
        this.appointmentEndTime = appointmentEndTime;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
        this.contactFullName = contactFullName;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getAppointmentTitle() {
        return appointmentTitle;
    }

    public void setAppointmentTitle(String appointmentTitle) {
        this.appointmentTitle = appointmentTitle;
    }

    public String getAppointmentDescription() {
        return appointmentDescription;
    }

    public void setAppointmentDescription(String appointmentDescription) {
        this.appointmentDescription = appointmentDescription;
    }

    public String getAppointmentLocation() {
        return appointmentLocation;
    }

    public void setAppointmentLocation(String appointmentLocation) {
        this.appointmentLocation = appointmentLocation;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public LocalDate getAppointmentStartDate() {
        return appointmentStartDate;
    }

    public void setAppointmentStartDate(LocalDate appointmentStartDate) {
        this.appointmentStartDate = appointmentStartDate;
    }

    public LocalDateTime getAppointmentStartTime() {
        return appointmentStartTime;
    }

    public void setAppointmentStartTime(LocalDateTime appointmentStartTime) {
        this.appointmentStartTime = appointmentStartTime;
    }

    public LocalDate getAppointmentEndDate() {
        return appointmentEndDate;
    }

    public void setAppointmentEndDate(LocalDate appointmentEndDate) {
        this.appointmentEndDate = appointmentEndDate;
    }

    public LocalDateTime getAppointmentEndTime() {
        return appointmentEndTime;
    }

    public void setAppointmentEndTime(LocalDateTime appointmentEndTime) {
        this.appointmentEndTime = appointmentEndTime;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getContactFullName() {
        return contactFullName;
    }

    public void setContactFullName(String contactFullName) {
        this.contactFullName = contactFullName;
    }
}
