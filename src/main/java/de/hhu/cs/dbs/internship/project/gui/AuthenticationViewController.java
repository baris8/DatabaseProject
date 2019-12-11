package de.hhu.cs.dbs.internship.project.gui;

import com.alexanderthelen.applicationkit.database.Data;
import de.hhu.cs.dbs.internship.project.Project;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AuthenticationViewController extends com.alexanderthelen.applicationkit.gui.AuthenticationViewController {
    protected AuthenticationViewController(String name) {
        super(name);
    }

    public static AuthenticationViewController createWithName(String name) throws IOException {
        AuthenticationViewController viewController = new AuthenticationViewController(name);
        viewController.loadView();
        return viewController;
    }

    @Override
    public void loginUser(Data data) throws SQLException {
        String email = (String) data.get("email");
        String pw = (String) data.get("password");
        
        String sql = "SELECT * FROM Benutzer WHERE EMail = '" + email + "'AND Passwort = '" + pw + "'";
        Statement statement = Project.getInstance().getConnection().createStatement();
        ResultSet rs = statement.executeQuery(sql);

        //Fall: Benutzer existiert nicht
        if(rs.isClosed()){
            throw new SQLException("Der Benutzer existiert nicht, bitte registrieren sie sich!");
        }

        //Daten stimmen überein
        if(rs.getString("EMail").equals(email) && rs.getString("Passwort").equals(pw)){
            String sql2 = "SELECT * FROM Autor WHERE EMail = '" + email + "'";
            Statement statement2 = Project.getInstance().getConnection().createStatement();
            ResultSet rs2 = statement.executeQuery(sql2);
            if(!rs2.isClosed()){
                //Benutzer ist Autor
                Project.getInstance().getData().put("permission", 2);
            }else{
                //Benutzer ist kein Autors
                Project.getInstance().getData().put("permission", 1);
            }
            Project.getInstance().getData().put("email", email);
        }
    }

    @Override
    public void registerUser(Data data) throws SQLException {
        String e = (String) data.get("eMail");
        String p = (String) data.get("password1");
        String p2 = (String) data.get("password2");
        String v = (String) data.get("firstName");
        String n = (String) data.get("lastName");

        if(!p.equals(p2)){
            throw new SQLException("Die eingegebenen Passwoerter sind nicht gleich!");
        }

        if(p.equals(p2)){
            String sql = "INSERT INTO Benutzer VALUES(?, ?, ?, ?)";
            PreparedStatement ps = Project.getInstance().getConnection().prepareStatement(sql);
            ps.setObject(1, e);
            ps.setObject(2, p);
            ps.setObject(3, v);
            ps.setObject(4, n);
            ps.executeUpdate();
        }
    }
}
