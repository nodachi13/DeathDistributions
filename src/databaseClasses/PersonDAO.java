package databaseClasses;


import SQLClasses.SQLPerson;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PersonDAO extends DAOInterface
{
    public PersonDAO(Connection connection)
    {
        super(connection);
    }

    public void get(PreparedStatement statement, ArrayList<SQLPerson> persons)
    {
        try {
            ResultSet rs = statement.executeQuery();
            while (rs.next())
            {
                String pid = rs.getString("ID");
                String name = rs.getString("FullName");
                String gender = rs.getString("Gender");
                String relations = rs.getString("Relations");
                String facts = rs.getString("Facts");
                String birth  = rs.getString("Bday");
                String death = rs.getString("Dday");

                Gson gson = new Gson();
                ArrayList<Integer> Relations = gson.fromJson(relations, ArrayList.class);
                ArrayList<Integer> Facts = gson.fromJson(facts, ArrayList.class);

                if (death != null && !death.equals("") && birth != null && !birth.equals(""))
                {
                    SQLPerson result = new SQLPerson(pid, name, gender, Relations, Facts, birth, death);
                    persons.add(result);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
