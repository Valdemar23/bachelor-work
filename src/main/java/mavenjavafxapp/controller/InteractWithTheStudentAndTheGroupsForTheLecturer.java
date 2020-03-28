package mavenjavafxapp.controller;

import hibernate.HibernateUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Groups;
import models.Students;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by HP on 05.06.2017.
 */
public class InteractWithTheStudentAndTheGroupsForTheLecturer {
    @FXML
    private Button updateListViews;
    @FXML
    private TextField createGroupTextField;
    @FXML
    private ListView<String> studentsListView;
    @FXML
    private ListView<String> groupsListView;


    @FXML
    public void Update() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Criteria criteria = session.createCriteria(Students.class);
            List<Students> ss = criteria.list();
            for (Students jj : ss) {
                if (jj.isGroup() == false) {
                    String g = jj.getLogin();
                    studentsListView.getItems().add(g);
                }
            }
            updateListViews.setVisible(false);
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Criteria criteria = session.createCriteria(Groups.class);
            List<Groups> ss = criteria.list();
            //groupsListView.getItems().add("Empty");
            for (Groups jj : ss) {
                if (jj.getStudents() == null) {
                    String g = jj.getName();
                    groupsListView.getItems().add(g);
                }
            }
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @FXML
    public void CreateGroup(ActionEvent actionEvent) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            String group = createGroupTextField.getText().toString();
            session.beginTransaction();
            Groups groups = new Groups();
            groups.setName(group);
            groupsListView.getItems().add(group);
            session.save(groups);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @FXML
    public void AddStudentToGroup(ActionEvent actionEvent) {
        //отримати модель вибору для представлення списка
        MultipleSelectionModel<String> lvSelModelStudents = studentsListView.getSelectionModel();
        MultipleSelectionModel<String> lvSelModelGroups = groupsListView.getSelectionModel();

        //ввести приймач змін, щоб реагувати на вибір елементів представлення списка
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();

        session.beginTransaction();
        Query query = session.createQuery("FROM Students");
        List<Students> studentsList = query.list();
        query = session.createQuery("FROM Groups");
        List<Groups> groupsList = query.list();
        session.getTransaction().commit();
        session.close();
        final String[] group = new String[1];
        //Groups group=new Groups();
        lvSelModelGroups.selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValueGroup) {
                for (Groups gg : groupsList) {
                    if (newValueGroup.equals(gg.getName())) {
                        group[0] = gg.getName();
                    }
                }
                System.out.println(group[0]);
            }
        });
        lvSelModelStudents.selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValueStudent) {
                Session session = sessionFactory.openSession();
                int selectedIdx = studentsListView.getSelectionModel().getSelectedIndex();
                Students stud = new Students();
                for (Students ss : studentsList) {
                    if (newValueStudent.equals(ss.getLogin())) {
                        try {
                            stud=ss;
                            session.beginTransaction();
                            Students studentsToUpdate = (Students) session.get(Students.class, ss.getId());
                            studentsToUpdate.setGroup(true);
                            //studentsToUpdate.setGroups(group[0]);
                            session.persist(studentsToUpdate);
                            session.getTransaction().commit();
                        } catch (Exception e) {
                            session.getTransaction().rollback();
                            e.printStackTrace();
                        } finally {
                            session.close();
                        }
                        //System.out.println();
                        //studentsListView.getItems().remove
                    }
                }



                session = sessionFactory.openSession();
                for (Groups gg : groupsList) {
                    if (group[0].equals(gg.getName())) {
                        try {
                            session.beginTransaction();
                            gg.setStudents(stud);
                            session.save(gg);
                            session.getTransaction().commit();
                        } catch (Exception e) {
                            session.getTransaction().rollback();
                            e.printStackTrace();
                        }finally {
                            session.close();
                        }
                        //session = sessionFactory.openSession();

                        //studentsListView.getItems().remove(selectedIdx);

                        //studentsList.re(newValueStudent);

                        System.out.println(gg.getName() + " - " + stud.getLogin());
                    }
                }

                //System.out.println(newValueStudent);
            }
        });
    }

    @FXML
    public void OpenGroup(ActionEvent actionEvent) {
    }

    @FXML
    public void DeleteGroup(ActionEvent actionEvent) {
    }

    @FXML
    public void DeleteStudent(ActionEvent actionEvent) {
    }
}

