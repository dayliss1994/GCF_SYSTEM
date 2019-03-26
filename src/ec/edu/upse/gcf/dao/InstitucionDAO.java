package ec.edu.upse.gcf.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ec.edu.upse.gcf.modelo.Institucion;

@SuppressWarnings("unchecked")
public class InstitucionDAO extends ClaseDAO {
	public List<Institucion> getInstituciones(String value) {
		List<Institucion> resultado; 
		String patron = value; 

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Institucion.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Institucion>) query.getResultList();		
		return resultado;
	}
	public Institucion getInstitucion(String nombreInstitucion) {
		Institucion institucion; 
		Query consulta;
		
		consulta = getEntityManager().createNamedQuery("Institucion.buscaInstitucion");
		consulta.setParameter("nombreInstitucion", nombreInstitucion);
		
		institucion = (Institucion) consulta.getSingleResult();
		return institucion; 
	}
	
	public List<Institucion> getInstitucion() {
		List<Institucion> retorno = new ArrayList<Institucion>();  
		Query query = getEntityManager().createNamedQuery("Institucion.findAll");
		retorno = (List<Institucion>) query.getResultList();
		return retorno;
	}
}
