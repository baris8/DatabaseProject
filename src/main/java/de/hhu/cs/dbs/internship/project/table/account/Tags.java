package de.hhu.cs.dbs.internship.project.table.account;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import de.hhu.cs.dbs.internship.project.Project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Tags extends Table {
    @Override
    public String getSelectQueryForTableWithFilter(String s) throws SQLException {
        String sql = "SELECT Tag.*, Bild_setzt_Tag.bid FROM Tag " +
                "LEFT JOIN Bild_setzt_Tag " +
                "ON Bild_setzt_Tag.Tag = Tag.Tag ";

        if (s != null && !s.isEmpty()) {
            String tags[] = s.split(" ");
            int i = 0;
            while(i < tags.length){
                if(i == 0){
                    sql += "WHERE Tag.Tag LIKE '%" + tags[i] + "%' ";
                }else{
                    sql += "OR Tag.Tag LIKE '%" + tags[i] + "%' ";
                }
                i++;
            }
        }
        return sql;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        String sql = "SELECT Tag.*, Bild_setzt_Tag.bid FROM Tag " +
                "LEFT JOIN Bild_setzt_Tag " +
                "ON Bild_setzt_Tag.Tag = Tag.Tag " +
                "WHERE Tag.Tag = '" + data.get("Tag.Tag") + "'";
        return sql;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        if((Integer) Project.getInstance().getData().get("permission") != 2){
            throw new SQLException("Sie sind kein Autor!!!");
        }
        String s = "SELECT Tag FROM Tag WHERE Tag = '" + data.get("Tag.Tag") + "'";
        Statement statement = Project.getInstance().getConnection().createStatement();
        ResultSet rs = statement.executeQuery(s);

        if(rs.isClosed()){
            String sql = "INSERT INTO Tag VALUES(?)";
            PreparedStatement ps = Project.getInstance().getConnection().prepareStatement(sql);
            ps.setObject(1, data.get("Tag.Tag"));
            ps.executeUpdate();
        }

        String sql2 = "INSERT INTO Bild_setzt_Tag VALUES(?, ?)";
        PreparedStatement ps2 = Project.getInstance().getConnection().prepareStatement(sql2);
        ps2.setObject(1, data.get("Bild_setzt_Tag.bid"));
        ps2.setObject(2, data.get("Tag.Tag"));
        ps2.executeUpdate();

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
