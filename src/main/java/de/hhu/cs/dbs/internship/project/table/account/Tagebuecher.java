package de.hhu.cs.dbs.internship.project.table.account;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import de.hhu.cs.dbs.internship.project.Project;

import java.sql.SQLException;

public class Tagebuecher extends Table {

    @Override
    public String getSelectQueryForTableWithFilter(String s) throws SQLException {
        String sql = "SELECT Autor.* FROM Transaktion " +
                "JOIN Autor ON EMailA = Autor.EMail WHERE " +
                "EMailB = '" + Project.getInstance().getData().get("email") + "'";

        return sql;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        String sql = "";
        return sql;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        throw new SQLException("Sie koennen hier nichts einfuegen!!!");
    }

    @Override
    public void updateRowWithData(Data data, Data data1) throws SQLException {
        throw new SQLException("Sie koennen hier nichts aendern!!!");
    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        throw new SQLException("Sie koennen hier nichts loeschen!!!");
    }
}
