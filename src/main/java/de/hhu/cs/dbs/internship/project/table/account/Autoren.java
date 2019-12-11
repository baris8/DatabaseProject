package de.hhu.cs.dbs.internship.project.table.account;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import de.hhu.cs.dbs.internship.project.Project;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Autoren extends Table {
    @Override
    public String getSelectQueryForTableWithFilter(String s) throws SQLException {
        String sql = "SELECT EMail, Pseudonym, Avatar FROM Autor ";

        if (s != null && !s.isEmpty()) {
            sql += "WHERE Pseudonym LIKE '%" + s + "%'";
        }

        return sql;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        String sql = "SELECT Benutzer.Vorname, Benutzer.Nachname, Benutzer.EMail, " +
                "Autor.Pseudonym, Autor.Preis, Autor.Avatar " +
                "FROM Benutzer JOIN Autor WHERE Autor.EMail = Benutzer.EMail " +
                "AND Benutzer.EMail = '" + data.get("Autor.EMail") + "'";
        return sql;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
            throw new SQLException("Sie können hier nichts hijnzufügen!!!");
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
