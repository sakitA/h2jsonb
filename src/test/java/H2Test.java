import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by sanco on 29/09/2020.
 * h2jsontest
 */
public class H2Test {
    private static EntityManagerFactory factory;
    private static ObjectMapper mapper;

    @BeforeClass
    public static void init(){
        factory = Persistence.createEntityManagerFactory("h2");
        mapper = new ObjectMapper();
    }

    @Test
    public void jsonFieldTest(){
        EntityManager em = factory.createEntityManager();

        JsonEntity je = new JsonEntity();
        je.setId(1L);

        ObjectNode on = mapper.createObjectNode();
        on.put("key", "value");
        je.setAttributes(on);

        try {
            em.getTransaction().begin();
            em.persist(je);
            em.getTransaction().commit();

            assert (em.createQuery("select j.id from JsonEntity j", Long.class).getSingleResult())==1L;
        }catch (Exception e){
            e.printStackTrace();
            em.getTransaction().rollback();
        }
    }

    @AfterClass
    public static void cleanResource(){
        if(factory!=null)
            factory.close();
    }
}
