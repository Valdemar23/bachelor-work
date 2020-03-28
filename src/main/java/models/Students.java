package models;

import javax.persistence.*;//фабрика де містяться всі ці анотації для конфігурування бази даних
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Entity//так вказуються сутності
@Transactional
@Table(name = "students")//так вказується імя таблиці в базі даних
public class Students extends Model {

    @Column(name = "login", length = 50, nullable = false)
//дана анатоція вказує імя колонки в таблиці та встановлює максимальну довжину значень в даній колонкі
    public String login;

    @Column(name = "password", length = 32, nullable = false)
    private String password;

    @Column(name = "kek", length = 16)
    private String kek;

    @Column(name = "group_flag", nullable = false)
    private boolean  group_flag;



    public Students() {
    }

    public Students(Long id) {
        super(id);
    }

    @OneToMany(mappedBy="students",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Groups> groups = new HashSet<Groups>();//

    public Set<Groups> getGroups() {
        return groups;
    }

    public void setGroups(Set<Groups> groups) {
        this.groups = groups;
    }

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

    public String getKek() {
        return kek;
    }

    public void setKek(String kek) {
        this.kek = kek;
    }

    public boolean isGroup() {
        return group_flag;
    }

    public void setGroup(boolean group_flag) {
        this.group_flag = group_flag;
    }

    @Override
    public String toString() {
        return login;
    }
}
