package de.hhu.cs.dbs.internship.project.table.account;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import de.hhu.cs.dbs.internship.project.Project;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Account extends Table {
    @Override
    public String getSelectQueryForTableWithFilter(String s) throws SQLException {
        String sql = "";
        if((Integer) Project.getInstance().getData().get("permission") == 1) {
            sql = "SELECT EMail, Vorname, Nachname FROM Benutzer " +
                    "WHERE EMail = '" + Project.getInstance().getData().get("email") + "'";
        }else{
            sql = "SELECT Benutzer.EMail, Benutzer.Vorname, Benutzer.Nachname, sum(Transaktion.Betrag + Transaktion.Gutscheincode) as Gesamtverdienst " +
                    "FROM Autor " +
                    "JOIN Benutzer ON Benutzer.EMail = Autor.EMail " +
                    "LEFT JOIN Transaktion ON Transaktion.EMailA = Autor.EMail " +
                    "WHERE Benutzer.EMail = '" + Project.getInstance().getData().get("email") + "'";
        }
        return sql;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        String sql = "";
        if((Integer) Project.getInstance().getData().get("permission") == 1){
            sql = "SELECT * FROM Benutzer " +
                    "WHERE EMail = '" + Project.getInstance().getData().get("email") + "'";
        }else if((Integer) Project.getInstance().getData().get("permission") == 2){
            sql = "SELECT * FROM Benutzer " +
                    "JOIN Autor ON Autor.EMail = Benutzer.EMail " +
                    "AND Benutzer.EMail = '" + Project.getInstance().getData().get("email") + "'";
        }
        return sql;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        throw new SQLException("Sie koennen hier nichts hinzufuegen!!!");
    }

    @Override
    public void updateRowWithData(Data data, Data data1) throws SQLException {
        //Autor und Benutzer Updaten
        if((Integer) Project.getInstance().getData().get("permission") == 2){
            String bSQL = "UPDATE Benutzer SET Passwort = ?, Vorname = ?, Nachname = ? " +
                    "WHERE Benutzer.EMail LIKE '" + Project.getInstance().getData().get("email") + "'";
            PreparedStatement ps = Project.getInstance().getConnection().prepareStatement(bSQL);
            ps.setObject(1, data1.get("Benutzer.Passwort"));
            ps.setObject(2, data1.get("Benutzer.Vorname"));
            ps.setObject(3, data1.get("Benutzer.Nachname"));
            ps.executeUpdate();

            String aSQL = "UPDATE Autor SET Preis = ?, Pseudonym = ?, Avatar = ?" +
                    " WHERE Autor.EMail = '" + Project.getInstance().getData().get("email") + "'";
            PreparedStatement ps2 = Project.getInstance().getConnection().prepareStatement(aSQL);
            ps2.setObject(1, data1.get("Autor.Preis"));
            ps2.setObject(2, data1.get("Autor.Pseudonym"));
            ps2.setObject(3, data1.get("Autor.Avatar"));
            ps2.executeUpdate();
        }
        //Benutzer Updaten
        if((Integer) Project.getInstance().getData().get("permission") == 1){
            String bSQL = "UPDATE Benutzer SET Passwort = ?, Vorname = ?, Nachname = ?" +
                    " WHERE Benutzer.EMail = '" + Project.getInstance().getData().get("email") + "'";
            PreparedStatement ps = Project.getInstance().getConnection().prepareStatement(bSQL);
            ps.setObject(1, data1.get("Benutzer.Passwort"));
            ps.setObject(2, data1.get("Benutzer.Vorname"));
            ps.setObject(3, data1.get("Benutzer.Nachname"));
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        throw new SQLException("Sie koennen hier nichts loeschen!!!");
    }
}
