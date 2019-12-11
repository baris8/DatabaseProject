package de.hhu.cs.dbs.internship.project.table.account;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import de.hhu.cs.dbs.internship.project.Project;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GekaufteTB extends Table {

    private String verkaueferEMail;

    public GekaufteTB(String s){
        verkaueferEMail = s;
    }

    @Override
    public String getSelectQueryForTableWithFilter(String s) throws SQLException {
        String sql = "SELECT sid as ID, EMail, Datum, Nummer, Typ FROM Seite " +
                "WHERE EMail = '" + verkaueferEMail + "' ";

        if (s != null && !s.isEmpty()) {
            sql += "AND Datum LIKE '%" + s + "%'";
        }

        return sql;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        String sql = "SELECT sid, Datum, Nummer, Typ FROM Seite " +
                "WHERE EMail = '" + verkaueferEMail + "' " +
                "AND sid = '" + data.get("Seite.ID") + "'";
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
