package de.hhu.cs.dbs.internship.project.table.account;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import de.hhu.cs.dbs.internship.project.Project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Bilder extends Table {
    @Override
    public String getSelectQueryForTableWithFilter(String s) throws SQLException {
        String sql = "SELECT Bild.bid as ID, Bild.eid as EintragID, GPS.laengengrad, GPS.breitengrad " +
                    "FROM Bild " +
                    "LEFT JOIN Bild_hat_GPS ON Bild.bid = Bild_hat_GPS.bid " +
                    "LEFT JOIN GPS ON Bild_hat_GPS.gid = GPS.gid ";

        if (s != null && !s.isEmpty()) {
            String gps[] = s.split(" ");
            if(gps.length != 2){
                throw new SQLException("Bitte geben sie 2 GPS Koordinaten ein 'von bis'");
            }
            sql += "WHERE GPS.laengengrad >= '" + gps[0] + "' AND GPS.laengengrad <= '" + gps[1] + "' " +
                    "AND GPS.breitengrad >= '" + gps[0] + "' AND GPS.breitengrad <= '" + gps[1] + "'";
        }
        return sql;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        String sql = "";

        //Einfügen
        if(data.get("Bild.ID") != null) {
            String typSQL = "SELECT Transaktion.EMailA, Seite.EMail, Seite.Typ " +
                    "FROM Bild " +
                    "LEFT JOIN Eintrag ON Bild.eid = Eintrag.eid " +
                    "LEFT JOIN Seite ON Eintrag.sid = Seite.sid " +
                    "LEFT JOIN Transaktion ON Transaktion.EMailA = Seite.EMail " +
                    "WHERE Bild.bid = '" + data.get("Bild.ID") + "' " +
                    "AND Transaktion.EMailB = '" + Project.getInstance().getData().get("email") + "'";

            Statement st = Project.getInstance().getConnection().createStatement();
            ResultSet rs = st.executeQuery(typSQL);

            //nicht gekauft
            if (rs.isClosed()) {
                String tmp = "SELECT Seite.EMail " +
                        "FROM Bild " +
                        "LEFT JOIN Eintrag ON Bild.eid = Eintrag.eid " +
                        "LEFT JOIN Seite ON Eintrag.sid = Seite.sid " +
                        "WHERE Bild.bid = '" + data.get("Bild.ID") + "'";
                Statement st2 = Project.getInstance().getConnection().createStatement();
                ResultSet rs2 = st2.executeQuery(tmp);

                //wenns deine bilder sind
                if (rs2.getString("EMail").equals((String) Project.getInstance().getData().get("email"))) {
                    sql = "SELECT Bild.bid as ID, Bild.eid as EintragID, Bild.Bild, GPS.laengengrad, GPS.breitengrad " +
                            "FROM Bild " +
                            "LEFT JOIN Bild_hat_GPS ON Bild.bid = Bild_hat_GPS.bid " +
                            "LEFT JOIN GPS ON Bild_hat_GPS.gid = GPS.gid " +
                            "WHERE Bild.bid = '" + data.get("Bild.ID") + "'";
                    return sql;
                }

                sql = "SELECT Bild.bid as ID, Bild.eid as EintragID, Bild.Bild = NULL, GPS.laengengrad, GPS.breitengrad " +
                        "FROM Bild " +
                        "LEFT JOIN Bild_hat_GPS ON Bild.bid = Bild_hat_GPS.bid " +
                        "LEFT JOIN GPS ON Bild_hat_GPS.gid = GPS.gid " +
                        "WHERE Bild.bid = '" + data.get("Bild.ID") + "'";
                return sql;
            }

            //Public oder rs nicht leer --> gekauft
            if (rs.getString("Typ").equals("public") || rs.getString("EMailA") != null) {

                sql = "SELECT Bild.bid as ID, Bild.eid as EintragID, Bild.Bild, GPS.laengengrad, GPS.breitengrad " +
                        "FROM Bild " +
                        "LEFT JOIN Bild_hat_GPS ON Bild.bid = Bild_hat_GPS.bid " +
                        "LEFT JOIN GPS ON Bild_hat_GPS.gid = GPS.gid " +
                        "WHERE Bild.bid = '" + data.get("Bild.ID") + "'";
                return sql;
            }

            return sql;
        }
        String sql2 = "SELECT Bild.bid as ID, Bild.eid as EintragID, Bild.Bild, GPS.laengengrad, GPS.breitengrad " +
                "FROM Bild " +
                "LEFT JOIN Bild_hat_GPS ON Bild.bid = Bild_hat_GPS.bid " +
                "LEFT JOIN GPS ON Bild_hat_GPS.gid = GPS.gid " +
                "WHERE Bild.bid = '" + data.get("Bild.ID") + "'";
        return sql2;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        if((Integer) Project.getInstance().getData().get("permission") != 2){
            throw new SQLException("Sie sind kein Autor!!!");
        }

        String sql = "INSERT INTO Bild(Bild, eid) VALUES(?, ?)";
        PreparedStatement ps = Project.getInstance().getConnection().prepareStatement(sql);
        ps.setObject(1, data.get("Bild.Bild"));
        ps.setObject(2, data.get("Bild.EintragID"));
        ps.executeUpdate();

        int i = ps.getGeneratedKeys().getInt(1);

        if(data.get("GPS.Laengengrad") != null && data.get("GPS.Breitengrad") != null){
            String sql2 = "INSERT INTO GPS(Laengengrad, Breitengrad) VALUES(?, ?)";
            PreparedStatement ps2 = Project.getInstance().getConnection().prepareStatement(sql2);
            ps2.setObject(1, data.get("GPS.Laengengrad"));
            ps2.setObject(2, data.get("GPS.Breitengrad"));
            ps2.executeUpdate();

            int j = ps2.getGeneratedKeys().getInt(1);

            String sql3 = "INSERT INTO Bild_hat_GPS VALUES(?, ?)";
            PreparedStatement ps3 = Project.getInstance().getConnection().prepareStatement(sql3);
            ps3.setObject(1, i);
            ps3.setObject(2, j);
            ps3.executeUpdate();
        }
    }

    @Override
    public void updateRowWithData(Data data, Data data1) throws SQLException {
        String sql = "UPDATE Bild SET Bild = ?, eid = ? WHERE bid = '" + data.get("Bild.bid") + "'";
        PreparedStatement ps = Project.getInstance().getConnection().prepareStatement(sql);
        ps.setObject(1, data1.get("Bild.Bild"));
        ps.setObject(2, data1.get("Bild.EintragID"));

        ps.executeUpdate();
    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        String sql = "DELETE FROM Bild WHERE bid = '" + data.get("Bild.ID") + "'";
        PreparedStatement ps = Project.getInstance().getConnection().prepareStatement(sql);
        ps.executeUpdate();
    }
}
