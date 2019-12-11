package de.hhu.cs.dbs.internship.project.table.account;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import de.hhu.cs.dbs.internship.project.Project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GekaufteEintraege extends Table {

    private String verkaueferEMail;
    public GekaufteEintraege(String s){
        verkaueferEMail = s;
    }

    @Override
    public String getSelectQueryForTableWithFilter(String s) throws SQLException {
        String sql = "SELECT eid as ID, Eintrag.sid as SeitenID, Titel, Text, Uhrzeit " +
                "FROM Eintrag " +
                "JOIN Seite ON Seite.sid = Eintrag.sid " +
                "WHERE Seite.EMail = '" + verkaueferEMail + "' ";

        if (s != null && !s.isEmpty()) {
            sql += "AND Eintrag.Uhrzeit LIKE '%" + s + "%' " +
                "OR Eintrag.Titel LIKE '%" + s + "%'";
        }

        return sql;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        String sql = "SELECT eid, Eintrag.sid, Titel, Uhrzeit, Text, Datum, Nummer, Typ " +
                "FROM Eintrag JOIN Seite ON Seite.sid = Eintrag.sid " +
                "WHERE Eintrag.eid = '" + data.get("Eintrag.ID") + "' " +
                "AND Eintrag.sid = '" + data.get("Eintrag.SeitenID") + "'";

        return sql;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        throw new SQLException("Sie koennen hier nichts einfuegen!!!");
    }

    @Override
    public void updateRowWithData(Data data, Data data1) throws SQLException {
        throw new SQLException("Sie koennen hier nichts einfuegen!!!");
    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        throw new SQLException("Sie koennen hier nichts einfuegen!!!");
    }
}
