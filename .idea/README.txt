1. C195 Performance Assessment; Title: "Global Scheduling System"; Purpose: To enable the company to schedule appointments for customers and employees, syncing data across time zones to ensure data integrity and company efficiency.
2. Author: Tanner Tolman; ttolma4@wgu.edu, 801-380-1171, Application version 1.0, Date 4/22/2023
3. IDE version: IntelliJ IDEA Community Edition 2021.1.3; JDK version: Java SE 17.0.1; JavaFX version: JavaFX-SDK-17.0.1
4. The program is set to open the Login page upon start. Once the user has logged in with a correct combination of username and password, they are transferred to the main application screen.
    The main screen is divided into three tabs; Customers, Scheduling, and Reports.
        - Customers: Here the user can add, edit, and delete customers. A list of all customers is displayed in a table view in this tab.
        - Scheduling: Here the user can add, edit, and delete appointments. A list of all appointments is displayed in a table view. The default display is for all appointments stored in the system, but on this screen the appointment data can be filtered by current Month and Week as well.
        - Reports: Here the user has access to filter data stored in the data base by running reports. Three reports are built into the system(described below). Additional information on the data is displayed above the table view. The user can select the desired filters for the report on the right hand side, then click the run button to have the information displayed.
            - Report 1: The user can run a report that filters appointments by Customer, Type, and Month.
            - Report 2: The user can pull a report for all appointments under a selected contact.
            - Report 3: The user can obtain data on how many appointments are scheduled for various lengths(less than 15 minutes, 15 or 30 minutes, 1 hour, or greater than 1 hour).
5. MySQL Connector driver version number:  mysql-connector-java:8.0.25