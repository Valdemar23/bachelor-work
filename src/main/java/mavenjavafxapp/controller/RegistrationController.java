package mavenjavafxapp.controller;

import hibernate.HibernateUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import models.Lecturers;
import models.Students;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.util.List;

//import nl.captcha.Captcha;

/**
 * Created by HP on 26.03.2017.
 */
public class RegistrationController {
    @FXML
    private TextField loginField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label label;
    @FXML
    private RadioButton radioStudent;
    @FXML
    public void Registration() throws IOException {
        if (loginField.getText().equals("") || passwordField.getText().equals("")) {
            System.out.println("sdf");
            label.setStyle("-fx-text-fill: red");
            label.setText("Invalid registration");
        } else {//якщо поля не пусті
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.openSession();
            boolean b = true;

            if (radioStudent.isSelected()) {
                List<Students> students;
                Students student = new Students();
                try {//початок транзакції
                    session.beginTransaction();
                    Criteria criteria=session.createCriteria(Students.class);
                    students = criteria.list();
                    session.getTransaction().commit();
                    session.close();

                    for (Students gg : students) {
                        String login = gg.getLogin();
                        System.out.println(gg.toString());
                        System.out.println("dd");
                        if (login.equals(loginField.getText())) {
                            label.setStyle("-fx-text-fill: red");
                            label.setText("Username are used");
                            System.out.println("username used");
                            b = false;
                            break;
                        }
                    }
                    if (b) {//літерал логічної константи
                        session = sessionFactory.openSession();
                        session.beginTransaction();
                        String login = loginField.getText();
                        String password = passwordField.getText();
                        String md5Hex = DigestUtils.md5Hex(password);
                        student.setLogin(login);
                        student.setPassword(md5Hex);

                        label.setStyle("-fx-text-fill: blue");
                        label.setText("You successfully registered!");
                        session.save(student);
                        session.getTransaction().commit();
                        session.close();
                    }
                } catch (Exception e) {
                    session.getTransaction().rollback();
                    e.printStackTrace();
                }
            }else{
                List<Lecturers> lecturers;
                Lecturers lecturer = new Lecturers();
                try {//початок транзакції
                    session.beginTransaction();
                    Criteria criteria=session.createCriteria(Lecturers.class);
                    lecturers = criteria.list();
                    session.getTransaction().commit();
                    session.close();

                    for (Lecturers gg : lecturers) {
                        String login = gg.getLogin();
                        System.out.println(gg.toString());
                        System.out.println("dd");
                        if (login.equals(loginField.getText())) {
                            label.setStyle("-fx-text-fill: red");
                            label.setText("Username are used");
                            System.out.println("username used");
                            b = false;
                            break;
                        }
                    }
                    if (b) {//літерал логічної константи
                        session = sessionFactory.openSession();
                        session.beginTransaction();
                        String login = loginField.getText();
                        String password = passwordField.getText();
                        String md5Hex = DigestUtils.md5Hex(password);
                        lecturer.setLogin(login);
                        lecturer.setPassword(md5Hex);

                        label.setStyle("-fx-text-fill: blue");
                        label.setText("You successfully registered!");
                        session.save(lecturer);
                        session.getTransaction().commit();
                        session.close();
                    }
                } catch (Exception e) {
                    session.getTransaction().rollback();
                    e.printStackTrace();
                }
            }
        }
    }
}