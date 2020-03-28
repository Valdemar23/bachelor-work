package dto;

import models.Groups;
import models.Students;

/**
 * Created by HP on 30.03.2017.
 */
public class UserRegistrationDto {
    private String login;
    private String password;
    private Groups groups;

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGroups(Groups groups) {
        this.groups = groups;
    }

    /*public Students toUser() {
        Students students = new Students();
        students.setLogin(login);
        students.setPassword(password);
        students.setRole(groups);
        return students;
    }*/
}
