package model;

/**
 * This class represents a customer entity and provides a constructor method,
 * as well as methods to get/set its attributes.
 *
 * @author Elley Folks
 */
public class Customer {
    private int customerId;
    private String customerFullName;
    private String customerAddress;
    private String postalCode;
    private String customerPhoneNumber;
    private String division;
    private String country;
    private int divisionId;

    /**
     * Constructs a Customer object with the specified attributes.
     *
     * @param customerId          The unique identifier for the customer.
     * @param customerFullName    The full name of the customer.
     * @param customerAddress     The address of the customer.
     * @param postalCode          The postal code of the customer's location.
     * @param customerPhoneNumber The phone number of the customer.
     * @param division            The division of the customer.
     * @param country             The country of the customer.
     * @param divisionId          The unique identifier for the division of the customer.
     */
    public Customer(
            int customerId,
            String customerFullName,
            String customerAddress,
            String postalCode,
            String customerPhoneNumber,
            String division,
            String country,
            int divisionId){
        this.customerId = customerId;
        this.customerFullName = customerFullName;
        this.customerAddress = customerAddress;
        this.postalCode = postalCode;
        this.customerPhoneNumber = customerPhoneNumber;
        this.division = division;
        this.country = country;
        this.divisionId = divisionId;

    }

    /**
     * Gets the customer ID.
     *
     * @return The customer ID.
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Sets the customer ID.
     *
     * @param customerId The customer ID to set.
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Gets the full name of the customer.
     *
     * @return The customer's full name.
     */
    public String getCustomerFullName() {
        return customerFullName;
    }

    /**
     * Sets the full name of the customer.
     *
     * @param customerFullName The full name to set.
     */
    public void setCustomerFullName(String customerFullName) {
        this.customerFullName = customerFullName;
    }

    /**
     * Gets the address of the customer.
     *
     * @return The customer's address.
     */
    public String getCustomerAddress() {
        return customerAddress;
    }

    /**
     * Sets the address of the customer.
     *
     * @param customerAddress The address to set.
     */
    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    /**
     * Gets the postal code of the customer's location.
     *
     * @return The customer's postal code.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the postal code of the customer's location.
     *
     * @param postalCode The postal code to set.
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Gets the phone number of the customer.
     *
     * @return The customer's phone number.
     */
    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    /**
     * Sets the phone number of the customer.
     *
     * @param customerPhoneNumber The phone number to set.
     */
    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    /**
     * Gets the division of the customer.
     *
     * @return The customer's division.
     */
    public String getDivision() {
        return division;
    }

    /**
     * Sets the division of the customer.
     *
     * @param division The division to set.
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     * Gets the country of the customer.
     *
     * @return The customer's country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country of the customer.
     *
     * @param country The country to set.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets the division ID of the customer.
     *
     * @return The customer's division ID.
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * Sets the division ID of the customer.
     *
     * @param divisionId The division ID to set.
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

}
