package ec.edu.upse.gcf.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ec.edu.upse.gcf.modelo.Calendario;

@SuppressWarnings("unchecked")
public class CalendarioDAO extends ClaseDAO{
	public List<Calendario> getCalendarios(String value) {
		List<Calendario> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Calendario.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Calendario>) query.getResultList();
		return resultado;
	}
	public List<Calendario> getListaCalendarios() {
		List<Calendario> retorno = new ArrayList<Calendario>();
		Query query = getEntityManager().createNamedQuery("Calendario.findAll");
		retorno = (List<Calendario>) query.getResultList();		
		return retorno;
	}
}
