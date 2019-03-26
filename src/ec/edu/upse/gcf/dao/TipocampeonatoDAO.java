package ec.edu.upse.gcf.dao;

import java.util.List;

import javax.persistence.Query;

import ec.edu.upse.gcf.modelo.Tipocampeonato;

@SuppressWarnings("unchecked")
public class TipocampeonatoDAO extends ClaseDAO {
	public List<Tipocampeonato> getTipoCampeonatos(String value) {
		List<Tipocampeonato> resultado; 
		String patron = value;

		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("TipoCampeonatos.buscarPorPatron");

		query.setHint("javax.persistence.cache.storeMode", "REFRESH");

		query.setParameter("patron", patron);

		resultado = (List<Tipocampeonato>) query.getResultList();
		
		return resultado;
	}
	public Tipocampeonato getTipoCampeonato(String nombreTipoCampeonato) {
		Tipocampeonato tipoCampeonato; 
		Query consulta;
		
		consulta = getEntityManager().createNamedQuery("TipoCampeonato.buscaPerfil");
		consulta.setParameter("nombreTipoCampeonato", nombreTipoCampeonato);
		
		tipoCampeonato = (Tipocampeonato) consulta.getSingleResult();
		
		return tipoCampeonato;
	}
}
