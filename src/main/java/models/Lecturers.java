package models;

import javax.persistence.*;

/**
 * Created by HP on 26.05.2017.
 */
@Entity
@Table(name="lecturers")
public class Lecturers extends Model{
    @Column(name="login",length = 50, nullable = true)
    private String login;

    @Column(name="password",length = 32,nullable = true)
    private String password;

    @Column(name="subject",length = 50,nullable = true)
    private String subject;

    @ManyToOne
//пишемо тип зєднання (багато ролів будуть приходить на дану таблицю), до того ж тута буде множина тому пишемо ManyToOne
    @JoinColumn(name="id_groups")//таким чином зєднуємо між собою таблички беручи дане поле із role_id
    private Groups groups;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Groups getGroups() {
        return groups;
    }

    public void setGroups(Groups groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "Lecturers{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", subject='" + subject + '\'';
    }
}
