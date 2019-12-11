package de.hhu.cs.dbs.internship.project.table.account;

import com.alexanderthelen.applicationkit.database.Data;
import com.alexanderthelen.applicationkit.database.Table;
import de.hhu.cs.dbs.internship.project.Project;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AlleEintraege extends Table {
    @Override
    public String getSelectQueryForTableWithFilter(String s) throws SQLException {
        String sql = "SELECT EMail, eid as EintragsID, Eintrag.sid as SeitenID, Titel, Text, Seite.Datum, Uhrzeit " +
                "FROM Eintrag " +
                "JOIN Seite ON Seite.sid = Eintrag.sid " +
                "WHERE Seite.Typ = 'public'";

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
                "WHERE Eintrag.eid = '" + data.get("Eintrag.EintragsID") + "' " +
                "AND Eintrag.sid = '" + data.get("Eintrag.SeitenID") + "'";
        return sql;
    }

    @Override
    public void insertRowWithData(Data data) throws SQLException {
        throw new SQLException("Sie koennen hier nichts einfuegen!!!");
    }

    @Override
    public void updateRowWithData(Data data, Data data1) throws SQLException {
        throw new SQLException("Sie koennen hier nichts veraendern!!!");
    }

    @Override
    public void deleteRowWithData(Data data) throws SQLException {
        throw new SQLException("Sie koennen hier nichts loeschen!!!");
    }
}
