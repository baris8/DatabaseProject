package de.hhu.cs.dbs.internship.project.gui;

import com.alexanderthelen.applicationkit.database.Table;
import com.alexanderthelen.applicationkit.gui.TableViewController;
import com.alexanderthelen.applicationkit.gui.ViewController;
import de.hhu.cs.dbs.internship.project.Project;
import de.hhu.cs.dbs.internship.project.table.account.*;
import javafx.scene.control.TreeItem;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MasterViewController extends com.alexanderthelen.applicationkit.gui.MasterViewController {
    protected MasterViewController(String name) {
        super(name);
    }

    public static MasterViewController createWithName(String name) throws IOException {
        MasterViewController controller = new MasterViewController(name);
        controller.loadView();
        return controller;
    }

    @Override
    protected ArrayList<TreeItem<ViewController>> getTreeItems() {
        ArrayList<TreeItem<ViewController>> treeItems = new ArrayList<>();
        TreeItem<ViewController> treeItem;
        TreeItem<ViewController> subTreeItem;
        TableViewController tableViewController;
        Table table;

        table = new Account();
        table.setTitle("Account");
        try {
            tableViewController = TableViewController.createWithNameAndTable("account", table);
            tableViewController.setTitle("Account");
        } catch (IOException e) {
            tableViewController = null;
        }
        treeItem = new TreeItem<>(tableViewController);
        treeItem.setExpanded(true);
        treeItems.add(treeItem);

        table = new Transaktionen();
        table.setTitle("Transaktionen");
        try {
            tableViewController = TableViewController.createWithNameAndTable("transaktionen", table);
            tableViewController.setTitle("Transaktionen");
        } catch (IOException e) {
            tableViewController = null;
        }
        subTreeItem = new TreeItem<>(tableViewController);
        treeItem.getChildren().add(subTreeItem);

        table = new MeineSeiten();
        table.setTitle("Meine Seiten");
        try {
            tableViewController = TableViewController.createWithNameAndTable("meineseiten", table);
            tableViewController.setTitle("Meine Seiten");
        } catch (IOException e) {
            tableViewController = null;
        }
        subTreeItem = new TreeItem<>(tableViewController);
        treeItem.getChildren().add(subTreeItem);

        table = new MeineEintraege();
        table.setTitle("Meine Eintraege");
        try {
            tableViewController = TableViewController.createWithNameAndTable("meineeintraege", table);
            tableViewController.setTitle("Meine Eintraege");
        } catch (IOException e) {
            tableViewController = null;
        }
        subTreeItem = new TreeItem<>(tableViewController);
        treeItem.getChildren().add(subTreeItem);


        table = new Tagebuecher();
        table.setTitle("Gekaufte Tagebücher");
        try {
            tableViewController = TableViewController.createWithNameAndTable("gekauftetagebücher", table);
            tableViewController.setTitle("Gekaufte Tagebücher");
        } catch (IOException e) {
            tableViewController = null;
        }
        subTreeItem = new TreeItem<>(tableViewController);
        treeItem.getChildren().add(subTreeItem);
        //Gekaufte Tagebücher
        try {
            String sql = "SELECT EMailA FROM Transaktion WHERE EMailB = '" +  Project.getInstance().getData().get("email") + "'";
            Statement statement = Project.getInstance().getConnection().createStatement();
            ResultSet rs = statement.executeQuery(sql);
            rs.next();

            while(!rs.isClosed()){
                table = new GekaufteTB(rs.getString("EMailA"));
                table.setTitle(rs.getString("EMailA") +" Tagebuch");
                try {
                    tableViewController = TableViewController.createWithNameAndTable("meineeintraege", table);
                    tableViewController.setTitle(rs.getString("EMailA") +" Tagebuch");
                } catch (IOException e) {
                    tableViewController = null;
                }
                TreeItem<ViewController> subTreeItem2 = new TreeItem<>(tableViewController);
                subTreeItem.getChildren().add(subTreeItem2);

                table = new GekaufteEintraege(rs.getString("EMailA"));
                table.setTitle("Eintraege");
                try {
                    tableViewController = TableViewController.createWithNameAndTable("gekaufteeintraege", table);
                    tableViewController.setTitle("Eintraege");
                } catch (IOException e) {
                    tableViewController = null;
                }
                TreeItem<ViewController> subTreeItem3 = new TreeItem<>(tableViewController);
                subTreeItem2.getChildren().add(subTreeItem3);

                rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        table = new Autoren();
        table.setTitle("Autoren");
        try {
            tableViewController = TableViewController.createWithNameAndTable("autoren", table);
            tableViewController.setTitle("Autoren");
        } catch (IOException e) {
            tableViewController = null;
        }
        treeItem = new TreeItem<>(tableViewController);
        treeItem.setExpanded(true);
        treeItems.add(treeItem);

        table = new Bewertungen();
        table.setTitle("Bewertungen");
        try {
            tableViewController = TableViewController.createWithNameAndTable("bewertungen", table);
            tableViewController.setTitle("Bewertungen");
        } catch (IOException e) {
            tableViewController = null;
        }
        treeItem = new TreeItem<>(tableViewController);
        treeItem.setExpanded(true);
        treeItems.add(treeItem);

        table = new Tags();
        table.setTitle("Tags");
        try {
            tableViewController = TableViewController.createWithNameAndTable("tags", table);
            tableViewController.setTitle("Tags");
        } catch (IOException e) {
            tableViewController = null;
        }
        treeItem = new TreeItem<>(tableViewController);
        treeItem.setExpanded(true);
        treeItems.add(treeItem);

        table = new Bilder();
        table.setTitle("Bilder");
        try {
            tableViewController = TableViewController.createWithNameAndTable("bilder", table);
            tableViewController.setTitle("Bilder");
        } catch (IOException e) {
            tableViewController = null;
        }
        treeItem = new TreeItem<>(tableViewController);
        treeItem.setExpanded(true);
        treeItems.add(treeItem);

        table = new AlleEintraege();
        table.setTitle("Alle Eintraege");
        try {
            tableViewController = TableViewController.createWithNameAndTable("alleeintraege", table);
            tableViewController.setTitle("Alle Eintraege");
        } catch (IOException e) {
            tableViewController = null;
        }
        treeItem = new TreeItem<>(tableViewController);
        treeItem.setExpanded(true);
        treeItems.add(treeItem);

        return treeItems;
    }
}
