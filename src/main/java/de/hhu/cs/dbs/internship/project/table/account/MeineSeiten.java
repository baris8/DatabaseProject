package de.hhu.cs.dbs.internship.project.table.account;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import de.hhu.cs.dbs.internship.project.Project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MeineSeiten extends Table {
    @Override
    public String getSelectQueryForTableWithFilter(String s) throws SQLException {
        String sql = "SELECT sid as ID, EMail, Datum, Nummer, Typ FROM Seite " +
                    "WHERE EMail = '" + Project.getInstance().getData().get("email") + "' ";

        if (s != null && !s.isEmpty()) {
            sql += "AND Datum LIKE '%" + s + "%'";
        }

        return sql;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        String sql = "SELECT sid, Datum, Nummer, Typ FROM Seite " +
                "WHERE EMail = '" + Project.getInstance().getData().get("email") + "' " +
                "AND sid = '" + data.get("Seite.ID") + "'";
        return sql;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        if((Integer) Project.getInstance().getData().get("permission") != 2){
            System.out.println("Sie werden zum Autor");
            String sql = "INSERT INTO Autor VALUES(?, ?, ?, ?)";
            PreparedStatement ps = Project.getInstance().getConnection().prepareStatement(sql);
            ps.setObject(1, Project.getInstance().getData().get("email"));
            ps.setObject(2, 0.0);
            ps.setObject(3, Project.getInstance().getData().get("email"));
            ps.setObject(4, null);
            ps.execute();

            Project.getInstance().getData().remove("permission");
            Project.getInstance().getData().put("permission", 2);
        }

        String sql = "INSERT INTO Seite(EMail, Datum, Nummer, Typ) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = Project.getInstance().getConnection().prepareStatement(sql);
        ps.setObject(1, Project.getInstance().getData().get("email"));
        ps.setObject(2, data.get("Seite.Datum"));
        ps.setObject(3, data.get("Seite.Nummer"));
        ps.setObject(4, data.get("Seite.Typ"));
        ps.executeUpdate();
    }

    @Override
    public void updateRowWithData(Data data, Data data1) throws SQLException {
        throw new SQLException("Sie koennen hier nichts mehr aendern!");
    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
            if(((String)data.get("Seite.Typ")).equals("public")){
                String sql = "DELETE FROM Seite WHERE sid = '" + data.get("Seite.ID") + "'";
                PreparedStatement ps = Project.getInstance().getConnection().prepareStatement(sql);
                ps.executeUpdate();
            }else{
                String s = "SELECT * FROM Transaktion WHERE EMailA = '" + Project.getInstance().getData().get("email") + "'";
                Statement tmp = Project.getInstance().getConnection().createStatement();
                ResultSet rs = tmp.executeQuery(s);

                if(!rs.isClosed()){
                    throw new SQLException("Ihr Tagebuch wurde schon gekauft, sie koennen keine Seite loeschen!!!");
                }

                String sql = "DELETE FROM Seite WHERE sid = '" + data.get("Seite.ID") + "'";
                PreparedStatement ps = Project.getInstance().getConnection().prepareStatement(sql);
                ps.executeUpdate();
            }
    }
}
