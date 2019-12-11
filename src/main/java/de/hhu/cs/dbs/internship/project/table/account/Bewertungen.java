package de.hhu.cs.dbs.internship.project.table.account;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import de.hhu.cs.dbs.internship.project.Project;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Bewertungen extends Table {
    @Override
    public String getSelectQueryForTableWithFilter(String s) throws SQLException {
        String sql = "SELECT EMailB as Benutzer, EMailA as Autor, Benotung " +
                "FROM Benutzer_bewertet_Autor ";
        if (s != null && !s.isEmpty()) {
            sql += "WHERE Benutzer_bewertet_Autor.EMailA LIKE '%" + s + "%'";
        }

        return sql;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        String sql = "SELECT EMailB as Benutzer, EMailA as Autor, Benotung, Text FROM Benutzer_bewertet_Autor " +
                "WHERE Benutzer_bewertet_Autor.EMailB = '" + data.get("Benutzer_bewertet_Autor.Benutzer") + "' " +
                "AND Benutzer_bewertet_Autor.EMailA = '" + data.get("Benutzer_bewertet_Autor.Autor") + "'";
        return sql;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        String sql = "INSERT INTO Benutzer_bewertet_Autor VALUES(?, ?, ?, ?)";
        PreparedStatement ps = Project.getInstance().getConnection().prepareStatement(sql);
        ps.setObject(1, Project.getInstance().getData().get("email"));
        ps.setObject(2, data.get("Benutzer_bewertet_Autor.Autor"));
        ps.setObject(3, data.get("Benutzer_bewertet_Autor.Benotung"));
        ps.setObject(4, data.get("Benutzer_bewertet_Autor.Text"));
        ps.executeUpdate();
    }

    @Override
    public void updateRowWithData(Data data, Data data1) throws SQLException {
        String benutzer = (String)data.get("Benutzer_bewertet_Autor.Benutzer");
        String curEmail = (String) Project.getInstance().getData().get("email");

        if(!benutzer.equals(curEmail)){
            throw new SQLException("Sie sind nicht der Verfasser der Bewertung!!!");
        }

        String sql = "UPDATE Benutzer_bewertet_Autor SET Text = ?, Benotung = ? " +
                "WHERE EMailB = '" + Project.getInstance().getData().get("email") + "' " +
                "AND EMailA = '" + data.get("Benutzer_bewertet_Autor.Autor") + "'";
        PreparedStatement ps = Project.getInstance().getConnection().prepareStatement(sql);
        ps.setObject(1, data1.get("Benutzer_bewertet_Autor.Text"));
        ps.setObject(2, data1.get("Benutzer_bewertet_Autor.Benotung"));
        ps.executeUpdate();
    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        String benutzer = (String)data.get("Benutzer_bewertet_Autor.Benutzer");
        String curEmail = (String) Project.getInstance().getData().get("email");

        if(!benutzer.equals(curEmail)){
            throw new SQLException("Sie sind nicht der Verfasser der Bewertung!!!");
        }

        String sql = "DELETE FROM Benutzer_bewertet_Autor " +
                "WHERE EMailB = '" + Project.getInstance().getData().get("email") + "' " +
                "AND EMailA = '" + data.get("Benutzer_bewertet_Autor.Autor") + "'";
        PreparedStatement ps = Project.getInstance().getConnection().prepareStatement(sql);
        ps.executeUpdate();
    }
}
