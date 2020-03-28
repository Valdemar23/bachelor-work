package hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

	public static SessionFactory sessionFactory = null;
	
	static {
		Configuration cfg = new Configuration().configure();
		StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties());//даний метод реєстрації реєструє builder тільки для версій hibernate4.3, а також отримуємо конфігурації і конфігуруємо їх в змінній builder
		
		sessionFactory = cfg.buildSessionFactory(builder.build());//на основі sessionFactory можна створювати транзакції, відкривати сесії, закривати їх, комітити і так далі, а також він підхвачує файл hibernate.cfg.xml

		/*AnnotationConfiguration aconf = new AnnotationConfiguration().addAnnotatedClass(Students.class);
		Configuration conf = aconf.configure();*/


	}
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
