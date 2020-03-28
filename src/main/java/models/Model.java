package models;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by HP on 25.03.2017.
 */
@MappedSuperclass
public abstract class Model implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
//так змінна буде генеруватись як того захоче провайдер, краще поставити IDENTITY тоді первинний ключ матиме спеціальних тип даних для зберігання первинного ключа та автоматично інкрементуватись
    @Column(name = "id", length = 6, nullable = false)//так вказуються колонки в таблиці
    protected Long id;

    public Model() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Model(Long id) {
        this.id = id;
    }
}
