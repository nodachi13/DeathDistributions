package databaseClasses;

import SQLClasses.SQLPerson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public abstract class DAOInterface
{
    private Connection conn;

    public DAOInterface(Connection connection)
    {
        conn = connection;
    }

    abstract public void get(PreparedStatement stmt, ArrayList<SQLPerson> persons);
}
