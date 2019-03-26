package ec.edu.upse.gcf.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ec.edu.upse.gcf.modelo.Campeonato;
import ec.edu.upse.gcf.modelo.Detallecampeonato;

@SuppressWarnings("unchecked")
public class DetalleCampeonatoDAO extends ClaseDAO{
	
	public List<Detallecampeonato> getDetalleCampeonatos(String value) {
		List<Detallecampeonato> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Detallecampeonato.buscarPorPatron");

		query.setHint("javax.persistence.cache.storeMode", "REFRESH");

		query.setParameter("patron", patron);

		resultado = (List<Detallecampeonato>) query.getResultList();
		
		return resultado;
	}	

	
	public List<Detallecampeonato> getEquiposPorCategoria(Campeonato value) {
		List<Detallecampeonato> resultado;  
		Query query = getEntityManager().createNamedQuery("Detallecampeonato.BuscaPorCategoria");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", value);
		resultado = (List<Detallecampeonato>) query.getResultList();
		
		return resultado;
	}
	
	public List<Detallecampeonato> getListaDetalleCampeonatos() {
		List<Detallecampeonato> retorno = new ArrayList<Detallecampeonato>();

		Query query = getEntityManager().createNamedQuery("Detallecampeonato.findAll");
		retorno = (List<Detallecampeonato>) query.getResultList();

		return retorno;
	}
}
