package ec.edu.upse.gcf.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ec.edu.upse.gcf.modelo.Posicionjuego;

@SuppressWarnings("unchecked")
public class PosicionjuegoDAO extends ClaseDAO {
	
	public List<Posicionjuego> getPosicionjuego() {
		List<Posicionjuego> retorno = new ArrayList<Posicionjuego>();  
		Query query = getEntityManager().createNamedQuery("Posicionjuego.findAll");
		retorno = (List<Posicionjuego>) query.getResultList();
		return retorno;
	}
	public List<Posicionjuego> getPosiciones(String value) {
		List<Posicionjuego> resultado; 
		String patron = value;
		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Posicionjuego.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Posicionjuego>) query.getResultList();		
		return resultado;
	}
}
