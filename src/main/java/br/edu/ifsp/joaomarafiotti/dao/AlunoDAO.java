package br.edu.ifsp.joaomarafiotti.dao;

import br.edu.ifsp.joaomarafiotti.modelo.Aluno;
import jakarta.persistence.EntityManager;
import java.util.List;

public class AlunoDAO {
    private final EntityManager em;
    public AlunoDAO(EntityManager em) { this.em = em; }

    public Aluno buscarPorNomeExato(String nome) {
        List<Aluno> r = em.createQuery(
                        "SELECT a FROM Aluno a WHERE LOWER(a.nome)=:n", Aluno.class)
                .setParameter("n", nome.toLowerCase())
                .setMaxResults(1)
                .getResultList();
        return r.isEmpty() ? null : r.get(0);
    }

    public boolean existeRa(String ra) {
        Long qt = em.createQuery(
                        "SELECT COUNT(a) FROM Aluno a WHERE a.ra = :ra", Long.class)
                .setParameter("ra", ra)
                .getSingleResult();
        return qt != 0;
    }

    public List<Aluno> listarTodos() {
        return em.createQuery("SELECT a FROM Aluno a ORDER BY a.nome", Aluno.class)
                .getResultList();
    }

    public void salvar(Aluno a) {
        try {
            em.getTransaction().begin();
            em.persist(a);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    public void atualizar(Aluno a) {
        try {
            em.getTransaction().begin();
            em.merge(a);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    public boolean excluirPorNome(String nome) {
        try {
            em.getTransaction().begin();
            Aluno a = buscarPorNomeExato(nome);
            if (a != null) em.remove(em.contains(a) ? a : em.merge(a));
            em.getTransaction().commit();
            return a != null;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }
}
