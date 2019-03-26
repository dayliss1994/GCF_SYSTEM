package ec.edu.upse.gcf.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ec.edu.upse.gcf.modelo.Goleador;

@SuppressWarnings("unchecked")
public class GoleadorDAO extends ClaseDAO {

	public List<Goleador> getGoleadores(String value) {
		List<Goleador> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Goleador.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Goleador>) query.getResultList();
		return resultado;
	}
	public List<Goleador> getListaGoleadores() {
		List<Goleador> retorno = new ArrayList<Goleador>();
		Query query = getEntityManager().createNamedQuery("Goleador.findAll");
		retorno = (List<Goleador>) query.getResultList();		
		return retorno;
	}
}
