package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * This class represents an appointments entity and provides a constructor method,
 * as well as methods to get/set its attributes.
 *
 * @author Elley Folks
 */
public class Appointment {
    private int appointmentId = 0;
    private String appointmentTitle;
    private String appointmentDescription;
    private String appointmentLocation;
    private String appointmentType;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;
    private int customerId;
    private int userId;
    private int contactId;
    private String contactFullName;

    /**
     * Constructs an Appointment object with the specified attributes.
     *
     * @param appointmentId        The unique identifier for the appointment.
     * @param appointmentTitle     The title of the appointment.
     * @param appointmentDescription The description of the appointment.
     * @param appointmentLocation  The location where the appointment takes place.
     * @param appointmentType      The type/category of the appointment.
     * @param startTime            The start date and time of the appointment.
     * @param endTime              The end date and time of the appointment.
     * @param customerId           The unique identifier of the associated customer.
     * @param userId               The unique identifier of the user who created the appointment.
     * @param contactId            The unique identifier of the associated contact.
     * @param contactFullName      The full name of the associated contact.
     *
     * @author Elley Folks
     */
    public Appointment(
            int appointmentId,
            String appointmentTitle,
            String appointmentDescription,
            String appointmentLocation,
            String appointmentType,
            LocalDateTime startTime,
            LocalDateTime endTime,
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
        this.startDateTime = startTime;
        this.endDateTime = endTime;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
        this.contactFullName = contactFullName;
    }

    /**
     * Gets the unique identifier of the appointment.
     *
     * @return The unique identifier of the appointment.
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * Sets the unique identifier of the appointment.
     *
     * @param appointmentId The unique identifier to set for the appointment.
     */
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * Gets the title of the appointment.
     *
     * @return The title of the appointment.
     */
    public String getAppointmentTitle() {
        return appointmentTitle;
    }

    /**
     * Sets the title of the appointment.
     *
     * @param appointmentTitle The title to set for the appointment.
     */
    public void setAppointmentTitle(String appointmentTitle) {
        this.appointmentTitle = appointmentTitle;
    }

    /**
     * Gets the description of the appointment.
     *
     * @return The description of the appointment.
     */
    public String getAppointmentDescription() {
        return appointmentDescription;
    }

    /**
     * Sets the description of the appointment.
     *
     * @param appointmentDescription The description to set for the appointment.
     */
    public void setAppointmentDescription(String appointmentDescription) {
        this.appointmentDescription = appointmentDescription;
    }

    /**
     * Gets the location where the appointment takes place.
     *
     * @return The location of the appointment.
     */
    public String getAppointmentLocation() {
        return appointmentLocation;
    }

    /**
     * Sets the location where the appointment takes place.
     *
     * @param appointmentLocation The location to set for the appointment.
     */
    public void setAppointmentLocation(String appointmentLocation) {
        this.appointmentLocation = appointmentLocation;
    }

    /**
     * Gets the type/category of the appointment.
     *
     * @return The type/category of the appointment.
     */
    public String getAppointmentType() {
        return appointmentType;
    }

    /**
     * Sets the type/category of the appointment.
     *
     * @param appointmentType The type/category to set for the appointment.
     */
    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    /**
     * Gets the start date and time of the appointment.
     *
     * @return The start date and time of the appointment.
     */
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     * Sets the start date and time of the appointment.
     *
     * @param startDateTime The start date and time to set for the appointment.
     */
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * Gets the end date and time of the appointment.
     *
     * @return The end date and time of the appointment.
     */
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     * Sets the end date and time of the appointment.
     *
     * @param endDateTime The end date and time to set for the appointment.
     */
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    /**
     * Gets the unique identifier of the associated customer.
     *
     * @return The unique identifier of the associated customer.
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Sets the unique identifier of the associated customer.
     *
     * @param customerId The unique identifier to set for the associated customer.
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Gets the unique identifier of the user who created the appointment.
     *
     * @return The unique identifier of the user who created the appointment.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the unique identifier of the user who created the appointment.
     *
     * @param userId The unique identifier to set for the user who created the appointment.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets the unique identifier of the associated contact.
     *
     * @return The unique identifier of the associated contact.
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * Sets the unique identifier of the associated contact.
     *
     * @param contactId The unique identifier to set for the associated contact.
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * Gets the full name of the associated contact.
     *
     * @return The full name of the associated contact.
     */
    public String getContactFullName() {
        return contactFullName;
    }

    /**
     * Sets the full name of the associated contact.
     *
     * @param contactFullName The full name to set for the associated contact.
     */
    public void setContactFullName(String contactFullName) {
        this.contactFullName = contactFullName;
    }
}
