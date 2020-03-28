package models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "groups")
public class Groups extends Model {

    @Column(name = "name", length = 50, nullable = false)
    private String name;

	/*@Column(name="students")
    private Set<Students> students = new HashSet<Students>();*/

    public Groups() {
    }

    public Groups(Long id) {
        super(id);
    }

    @ManyToOne//пишемо тип зєднання (багато ролів будуть приходить на дану таблицю), до того ж тута буде множина тому пишемо ManyToOne
    @JoinColumn(name="id_student")//таким чином зєднуємо між собою таблички беручи дане поле із role_id
    private Students students;


    public String getName() {
        return name;
    }
    @OneToMany(mappedBy="groups")
    private Set<Lecturers> lecturers= new HashSet<Lecturers>();
    public Set<Lecturers> getLecturers() {
        return lecturers;
    }

    public void setLecturers(Set<Lecturers> lecturers) {
        this.lecturers = lecturers;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Students getStudents() {
        return students;
    }

    public void setStudents(Students students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "Groups{" +
                "name='" + name + '\'' +
                ", students=" + students +
                ", lecturers=" + lecturers +
                '}';
    }
}
