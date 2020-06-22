package chubak.mailclient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class Contact {
    private String contactFirstName;
    private String contactLastName;
    private String contactEmail;
    private String contactId;

    public Contact(String firstName, String lastName, String email) {
        this.contactFirstName = firstName;
        this.contactLastName = lastName;
        this.contactEmail = email;
        this.contactId = UUID.randomUUID().toString();


        insertIntoDatabase(firstName, lastName, email, this.contactId);
    }

    public Contact(String contactId) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:sushianet.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery(String.format("SELECT * from contacts WHERE ContactUID=%s", contactId));

            this.contactFirstName = rs.getString("FirstName");
            this.contactLastName = rs.getString("LastName");
            this.contactEmail = rs.getString("Email");

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

    }


    private void insertIntoDatabase(String firstName, String lastName, String email, String contactId) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:sushianet.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS contacts (ContactUID varchar(255), FirstName varchar(255), LastName varchar(255), Email varchar(255)");
            statement.executeUpdate(String.format("INSERT INTO contacts VALES(%s, %s, %s, %s)", contactId, firstName, lastName, email));


        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

    }

    public String getFirstName() {
        return this.contactFirstName;
    }

    public String getLastName() {
        return this.contactLastName;
    }

    public String getEmail() {
        return this.contactEmail;
    }

    public String getContactId() {
        return this.contactId;
    }

    public static String getFirstName(String contactId) {
        Contact contact = new Contact(contactId);
        return contact.getFirstName();
    }

    public static String getLastName(String contactId) {
        Contact contact = new Contact(contactId);
        return contact.getLastName();
    }

    public static String getEmail(String contactId) {
        Contact contact = new Contact(contactId);
        return contact.getEmail();
    }
}
