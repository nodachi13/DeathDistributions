package databaseClasses;
import SQLClasses.SQLPerson;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class database
{
    private Connection conn;
    private String url;
    private static DAOInterface personDAO;


    public database()
    {
        try
        {
            Class.forName("org.postgresql.Driver");
            Properties properties = new Properties();
            properties.setProperty("user", "postgres");
            properties.setProperty("password", "");
//            properties.setProperty("ssl", "true");

            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test", properties);
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Error attempting to initialize JDBC driver");
            e.printStackTrace();
        }
        catch (SQLException SQLe)
        {
            System.out.println("Error in DriverManager.getConnection()");
            SQLe.printStackTrace();
        }

        personDAO = new PersonDAO(conn);
    }

    public static void getPersons(PreparedStatement statement, ArrayList<SQLPerson> persons)
    {
        personDAO.get(statement, persons);
    }

    public Statement createStatement() throws SQLException
    {
        return conn.createStatement();
    }

    public PreparedStatement prepareStatement(String SQL) throws SQLException
    {
        return conn.prepareStatement(SQL);
    }
}
