package mavenjavafxapp.controller;

import hibernate.HibernateUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.Students;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by HP on 30.05.2017.
 */
public class UserSettings {
    @FXML
    private Label resultRemove;
    @FXML
    private TextField newPasswordField;
    @FXML
    private TextField findUserField;
    @FXML
    private TextField oldPasswordField;
    @FXML
    private TextField changeLoginField;
    @FXML
    private Label resultChangeLogin;
    @FXML
    private Label resultChangePassword;
    @FXML
    private Label resultFindUser;
    static private Students students=MainController.students;
    @FXML
    public void ChangeLogin() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        List<Students> students = null;
        Students students1 = new Students();

        try {//початок транзакції
            session.beginTransaction();
            Query query = session.createQuery("FROM Students");
            students = query.list();
            session.getTransaction().commit();
            session.close();
            boolean b = true;
            for (Students gg : students) {
                System.out.println(gg.toString());
                if (gg.getLogin().equals(changeLoginField.getText())) {
                    resultChangeLogin.setStyle("-fx-text-fill: red");
                    resultChangeLogin.setText("Username are used");
                    System.out.println("username used");
                    b = false;
                    break;
                }
            }
            if (b) {//літерал логічної константи
                session = sessionFactory.openSession();
                session.beginTransaction();
                Students studentsToUpdate = (Students) session.get(Students.class, UserSettings.students.getId());
                studentsToUpdate.setLogin(changeLoginField.getText());
                session.persist(studentsToUpdate);
                session.getTransaction().commit();
                session.close();
                resultChangeLogin.setText("You change login");
                resultChangeLogin.setStyle("-fx-text-fill: green");
            }
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    @FXML
    public void RemoveHimself() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.delete(students);
            session.getTransaction().commit();
            session.close();
            resultRemove.setText("You remove himself");
            resultRemove.setStyle("-fx-text-fill: green");
        } catch (Exception e) {
            e.printStackTrace();
            resultRemove.setText("Sasay lalka");
            resultRemove.setStyle("-fx-text-fill: red");
        }
    }

    @FXML
    public void ChangePassword() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        /*try {
            session.beginTransaction();
            String p=students.getPassword();
            System.out.println(p);
            if(students.getPassword().equals(DigestUtils.md5Hex(oldPasswordField.getText()))) {
                Students userToUpdate = (Students) session.get(Students.class, students.getId());
                userToUpdate.setPassword(DigestUtils.md5Hex(newPasswordField.getText()));
                session.persist(userToUpdate);
                session.getTransaction().commit();
                session.close();
                resultChangePassword.setText("You change password");
                resultChangePassword.setStyle("-fx-text-fill: green");
            }else{
                resultChangePassword.setText("Unsuccessfully");
                resultChangePassword.setStyle("-fx-text-fill: red");
            }
            System.out.println(p);
        }
        catch (Exception e) {
            e.printStackTrace();
            resultChangePassword.setText("Unsuccessfully");
            resultChangePassword.setStyle("-fx-text-fill: red");
        }*/
        try {
            session.beginTransaction();
            Students studentsToUpdate = (Students) session.get(Students.class, students.getId());
            studentsToUpdate.setPassword(DigestUtils.md5Hex(newPasswordField.getText()));
            session.persist(studentsToUpdate);
            session.getTransaction().commit();
            session.close();
            resultChangePassword.setText("You change password");
            resultChangePassword.setStyle("-fx-text-fill: green");
        } catch (Exception e) {
            e.printStackTrace();
            resultChangePassword.setText("Unsuccessfull");
            resultChangePassword.setStyle("-fx-text-fill: red");
        }
    }

    @FXML
    public void FindUser(ActionEvent actionEvent) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM Students");
        List<Students> students = query.list();
        session.getTransaction().commit();
        session.close();
        for (Students gg : students) {
            System.out.println(gg.toString());
            if (gg.getLogin().equals(findUserField.getText())) {
                resultFindUser.setStyle("-fx-text-fill: green");
                resultFindUser.setText(gg.getLogin() + " " + gg.getGroups());
                break;
            } else {
                resultFindUser.setStyle("-fx-text-fill: red");
                resultFindUser.setText("Students not found");
            }
        }
    }
}
