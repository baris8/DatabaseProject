package de.hhu.cs.dbs.internship.project.table.account;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import de.hhu.cs.dbs.internship.project.Project;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MeineEintraege extends Table {
    @Override
    public String getSelectQueryForTableWithFilter(String s) throws SQLException {
        String sql = "SELECT eid as ID, Eintrag.sid as SeitenID, Titel, Text, Uhrzeit " +
                "FROM Eintrag " +
                "JOIN Seite ON Seite.sid = Eintrag.sid " +
                "WHERE Seite.EMail = '" + Project.getInstance().getData().get("email") + "' ";

        if (s != null && !s.isEmpty()) {
            sql += "AND Titel LIKE '%" + s + "%'";
        }

        return sql;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        String sql = "SELECT eid, Eintrag.sid, Titel, Uhrzeit, Text " +
                "FROM Eintrag JOIN Seite ON Seite.sid = Eintrag.sid " +
                "WHERE Eintrag.eid = '" + data.get("Eintrag.ID") + "' " +
                "AND Eintrag.sid = '" + data.get("Eintrag.SeitenID") + "'";

        return sql;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        if((Integer) Project.getInstance().getData().get("permission") != 2){
            throw new SQLException("Sie sind kein Autor!!!");
        }

        String sql = "INSERT INTO Eintrag(sid, Titel, Uhrzeit, Text) VALUES(?, ?, ?, ?)";
        PreparedStatement ps = Project.getInstance().getConnection().prepareStatement(sql);
        ps.setObject(1, data.get("Eintrag.sid"));
        ps.setObject(2, data.get("Eintrag.Titel"));
        ps.setObject(3, data.get("Eintrag.Uhrzeit"));
        ps.setObject(4, data.get("Eintrag.Text"));
        ps.executeUpdate();
    }

    @Override
    public void updateRowWithData(Data data, Data data1) throws SQLException {
        if((Integer) Project.getInstance().getData().get("permission") != 2){
            throw new SQLException("Sie sind kein Autor!!!");
        }

        String sql = "UPDATE Eintrag SET sid = ?, Titel = ?, Text = ? " +
                "WHERE eid = '" + data.get("Eintrag.eid") + "'";
        PreparedStatement ps = Project.getInstance().getConnection().prepareStatement(sql);
        ps.setObject(1, data1.get("Eintrag.sid"));
        ps.setObject(2, data1.get("Eintrag.Titel"));
        ps.setObject(3, data1.get("Eintrag.Text"));
        ps.executeUpdate();
    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        throw new SQLException("Sie koennen keine Eintraege löschen!!!");
    }
}
