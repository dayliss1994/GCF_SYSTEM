package ec.edu.upse.gcf.dao;

import java.util.List;

import javax.persistence.Query;

import ec.edu.upse.gcf.modelo.Tarjetajugador;

@SuppressWarnings("unchecked")
public class TarjetajugadorDAO extends ClaseDAO{
	public List<Tarjetajugador> getTarjetajugadores(String value) {
		List<Tarjetajugador> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Tarjetajugador.buscarPorPatron");

		query.setHint("javax.persistence.cache.storeMode", "REFRESH");

		query.setParameter("patron", patron);

		resultado = (List<Tarjetajugador>) query.getResultList();
		
		return resultado;
	}	

}
