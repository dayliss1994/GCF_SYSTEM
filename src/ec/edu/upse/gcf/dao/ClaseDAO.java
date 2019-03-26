package ec.edu.upse.gcf.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ClaseDAO {

	// Crea una sola instancia de EntityManagerFactory para toda la applicacion.
	private static final String PERSISTENCE_UNIT_NAME = "APP-GCF";
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	
	// Objeto Entity Manager para cada instancia de un objeto que 
	// herede de esta clase.
	private EntityManager em;
	
	/**
	 * Retorna el Entity Mananager, si no existe lo crea.
	 * @return
	 */
	public EntityManager getEntityManager() {
		if (em == null){
	        em = emf.createEntityManager();
	    }
	    return em; 
	}  
}
