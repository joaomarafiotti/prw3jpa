package br.edu.ifsp.joaomarafiotti.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("ava1PU");


    public static EntityManager getEntityManager() { return emf.createEntityManager(); }
}