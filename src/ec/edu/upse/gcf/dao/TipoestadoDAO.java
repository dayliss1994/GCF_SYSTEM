package ec.edu.upse.gcf.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import ec.edu.upse.gcf.modelo.Tipoestado;


@SuppressWarnings("unchecked")
public class TipoestadoDAO extends ClaseDAO {
	
	public List<Tipoestado> getTipoEstados(String value) {
		List<Tipoestado> resultado; 
		String patron = value;
		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Tipoestado.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Tipoestado>) query.getResultList();
		System.out.println(resultado.size());
		return resultado;
	}
	public Tipoestado getTipoEstado(String nombreTipoestado) {
		Tipoestado tipoestado; 
		Query consulta;
		
		consulta = getEntityManager().createNamedQuery("Tipoestado.buscaTipoestado");
		consulta.setParameter("nombreTipoestado", nombreTipoestado);
		
		tipoestado = (Tipoestado) consulta.getSingleResult();
		
		return tipoestado;
	}
	
	public List<Tipoestado> getListaTipoestados() {
		List<Tipoestado> retorno = new ArrayList<Tipoestado>();
		Query query = getEntityManager().createNamedQuery("Tipoestado.findAll");
		retorno = (List<Tipoestado>) query.getResultList();		
		return retorno;
	}
}
