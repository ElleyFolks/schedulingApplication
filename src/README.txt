# WGU C195 - Scheduling Application

## Information

Author: Elley Folks
Contact: efolks@wgu.edu
Application Version: 1.0
Date: 3/1/2024

IDE Version: IntelliJ IDEA Community Edition 2023.3.2
JDK: java version 21.0.1
JavaFX version: javafx-sdk-21.0.1
Database Driver: mysql-connector-java-8.0.25

## Purpose

This is an application that is used to schedule appointments for various customers. It has a user-friendly GUI to create, delete, and modify customers and appointments from various tabs and table views.

This program connects to a database that is used for long-term storage of the applications. User interactions with the GUI are then transformed into the appropriate queries to modify the entry in the database.

## How to use this program

Upon app launch, users are presented with the **login** page. Useful information such as the user's **time zone** and geographical **location** is displayed. From here, users authenticate and attempt to log in. Login attempts are logged in "**login_activity.txt**" in the project's root.  Default language is English, switching to French in France according to the user's system settings.

A successful login triggers alerts for appointments within 15 minutes, if the user has any. Otherwise they are notified that they do not have any immediate appointments. Users access the home screen, which has 3 tabs - **Customers**, **Appointments**, or **Reports.** Each tab displays insightful data.

The "**Customers**" tab lists customers. "**Add**" creates a new customer, "**Edit**" modifies the information for an existing customer, and "**Delete**" removes a customer from the table. Relevant information such as the customer's division, country and address are displayed for the purposes of scheduling an appointment that is agreeable with their time zone and geographical location.

In the "**appointments**" tab, users view a table with radio buttons to filter which appointments are displayed. The options include appointments for the next week, next month, or all appointments.  "**Add**" creates a new appointment, "**Edit**" modifies the information for an existing appointment, and "**Delete**" removes a appointment from the table.

The "**Reports**" tab lets users generate reports, including radio buttons to select the following
- total **appointments by type**
- total **appointments by month**
- the total **appointments for each contact**
- total **appointments by country**
After a radio button is selected, a combo box is populated with options to refine and narrow down the search. The last report of **appointments by country** gives the business insights into what times to offer for scheduling, adjusting business hours, and *providing custom services for each region* that better suits the needs of their customers.