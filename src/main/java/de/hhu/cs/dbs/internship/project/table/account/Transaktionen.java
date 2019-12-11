package de.hhu.cs.dbs.internship.project.table.account;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import com.sun.javafx.text.PrismTextLayoutFactory;
import de.hhu.cs.dbs.internship.project.Project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Transaktionen extends Table {
    @Override
    public String getSelectQueryForTableWithFilter(String s) throws SQLException {
        String sql = "SELECT tid as ID, EMailA as Verkaeufer, Datum, Zahlungsmittelwahl " +
                "FROM Transaktion " +
                "WHERE EMailB = '" + Project.getInstance().getData().get("email") + "' ";
        if (s != null && !s.isEmpty()) {
            sql += "AND EMailA LIKE '%" + s + "%'";
        }

        return sql;
    }

    @Override
    public String getSelectQueryForRowWithData(Data data) throws SQLException {
        String sql = "SELECT EMailA as Verkaeufer, Zweck, Betrag, Zahlungsmittelwahl, Gutscheincode " +
                "FROM Transaktion " +
                "WHERE tid = '" + data.get("Transaktion.ID") + "'";
        return sql;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        double gutschein;
        double betrag;

        if(data.get("Transaktion.Betrag") == null){
            betrag = 0.0;
        }else{
            betrag = (double)data.get("Transaktion.Betrag");
        }
        if(data.get("Transaktion.Gutscheincode") == null){
            gutschein = 0.0;
        }else{
            gutschein = (double)data.get("Transaktion.Gutscheincode");
        }


        //Preis vom Autor herausfinden
        String s = "SELECT EMail, Preis FROM Autor WHERE EMail = '" + data.get("Transaktion.Verkaeufer") + "'";
        Statement state = Project.getInstance().getConnection().createStatement();
        ResultSet rs = state.executeQuery(s);

        double preis = rs.getDouble("Preis");

        if(preis > (gutschein + betrag)){
            throw new SQLException("Sie haben nicht genügend Geld!");
        }

        if(preis <= betrag || preis <= gutschein || preis <= (gutschein + betrag)){
            String sql = "INSERT INTO Transaktion(EMailA, EMailB, Zweck, Betrag, Zahlungsmittelwahl, Gutscheincode) " +
                    "VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = Project.getInstance().getConnection().prepareStatement(sql);
            ps.setObject(1, data.get("Transaktion.Verkaeufer"));
            ps.setObject(2, Project.getInstance().getData().get("email"));
            ps.setObject(3, data.get("Transaktion.Zweck"));
            ps.setObject(4, data.get("Transaktion.Betrag"));
            ps.setObject(5, data.get("Transaktion.Zahlungsmittelwahl"));
            ps.setObject(6, data.get("Transaktion.Gutscheincode"));
            ps.executeUpdate();
        }
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
