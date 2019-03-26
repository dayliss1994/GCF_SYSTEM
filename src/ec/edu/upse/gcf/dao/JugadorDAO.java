package ec.edu.upse.gcf.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ec.edu.upse.gcf.modelo.Jugador;

@SuppressWarnings("unchecked")
public class JugadorDAO extends ClaseDAO {
	public List<Jugador> getJugadores(String value) {
		List<Jugador> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Jugadores.buscarPorPatron");

		query.setHint("javax.persistence.cache.storeMode", "REFRESH");

		query.setParameter("patron", patron);

		resultado = (List<Jugador>) query.getResultList();
		
		return resultado;
	}
	public Jugador getJugador(String nombreJugador) {
		Jugador jugador; 
		Query consulta;
		
		consulta = getEntityManager().createNamedQuery("Jugador.buscaJugador");
		consulta.setParameter("nombreJugador", nombreJugador);
		
		jugador = (Jugador) consulta.getSingleResult();
		
		return jugador;
	}
	
	public List<Jugador> getJugador() {
		List<Jugador> retorno = new ArrayList<Jugador>();  
		Query query = getEntityManager().createNamedQuery("Jugador.findAll");
		retorno = (List<Jugador>) query.getResultList();
		return retorno;
	}
}
