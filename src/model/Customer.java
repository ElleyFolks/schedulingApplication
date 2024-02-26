package model;

public class Customer {
    private int customerId;
    private String customerFullName;
    private String customerAddress;
    private String postalCode;
    private String customerPhoneNumber;
    private String division;
    private String country;
    private int divisionId;

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

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerFullName() {
        return customerFullName;
    }

    public void setCustomerFullName(String customerFullName) {
        this.customerFullName = customerFullName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

}
