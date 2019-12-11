package de.hhu.cs.dbs.internship.project.gui;

import com.alexanderthelen.applicationkit.database.Data;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;

public class RegistrationViewController extends com.alexanderthelen.applicationkit.gui.RegistrationViewController {
    @FXML
    protected TextField firstNameTextField;
    @FXML
    protected TextField lastNameTextField;
    @FXML
    protected TextField eMailTextField;
    @FXML
    protected PasswordField password1PasswordField;
    @FXML
    protected PasswordField password2PasswordField;

    protected RegistrationViewController(String name) {
        super(name, RegistrationViewController.class.getResource("RegistrationView.fxml"));
    }

    public static RegistrationViewController createWithName(String name) throws IOException {
        RegistrationViewController viewController = new RegistrationViewController(name);
        viewController.loadView();
        return viewController;
    }

    @Override
    @FXML
    protected void initialize() {

    }

    @Override
    public ArrayList<Node> getInputNodes() {
        ArrayList<Node> inputNodes = new ArrayList<>();
        inputNodes.add(firstNameTextField);
        inputNodes.add(lastNameTextField);
        inputNodes.add(eMailTextField);
        inputNodes.add(password1PasswordField);
        inputNodes.add(password2PasswordField);
        return inputNodes;
    }

    @Override
    public Data getInputData() {
        Data data = new Data();
        data.put("firstName", firstNameTextField.getText());
        data.put("lastName", lastNameTextField.getText());
        data.put("eMail", eMailTextField.getText());
        data.put("password1", password1PasswordField.getText());
        data.put("password2", password2PasswordField.getText());
        return data;
    }
}
