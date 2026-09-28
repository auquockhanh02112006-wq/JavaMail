<%@ page import="murach.data.JPAUtil" %>
<%@ page import="javax.persistence.EntityManager" %>
<%
    EntityManager em = JPAUtil.getEntityManager();
    try {
        em.getTransaction().begin();
        int deleted = em.createQuery("DELETE FROM User u").executeUpdate();
        em.getTransaction().commit();
        out.println("Deleted " + deleted + " rows.");
    } catch (Exception e) {
        em.getTransaction().rollback();
        out.println("Error: " + e.getMessage());
    } finally {
        em.close();
    }
%>
