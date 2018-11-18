/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bugsjp;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import encje.*;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author bdobrosielski
 */
public class BugsJP {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("BugsJPPU");

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        addBug("b001", "Lost wifi connection");
        addBug("b002", "Turning off wifi does not work");
        addBug("b003", "Execution slow when on battery");
        for (Bug b : findBugs("%wifi%")) {
            System.out.println("Number: " + b.getNum() + " Description: " + b.getDescription());
        }
        bulkDeleteBugs();

    }

    public static List<Bug> findBugs(String pKeyword) {
        EntityManager em = emf.createEntityManager();
        List<Bug> wyn = null;
        try {
             wyn = em.createNamedQuery("findByKeyword").setParameter("keyword", "%" + pKeyword + "%").getResultList();  
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return wyn;
    }

    public static void addBug(String pNum, String pDesc) {
        Bug b = new Bug();
        b.setNum(pNum);
        b.setDescription(pDesc);
        persist(b);

    }

    public static void bulkDeleteBugs() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.createQuery("DELETE FROM Bug").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public static void persist(Object object) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("BugsJPPU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

}
