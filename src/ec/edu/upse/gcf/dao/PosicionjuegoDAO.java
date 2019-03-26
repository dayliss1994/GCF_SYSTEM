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
}
