package ec.edu.upse.gcf.editar;

import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.CampeonatoDAO;
import ec.edu.upse.gcf.dao.TipocampeonatoDAO;
import ec.edu.upse.gcf.modelo.Campeonato;
import ec.edu.upse.gcf.modelo.Tipocampeonato;

public class CampeonatoEditar {
	
	@Wire
	private Window winCampeonatoEditar;
	
	@Wire
	private Textbox nombreC;

	private CampeonatoDAO campeonatoDao = new CampeonatoDAO();
	private TipocampeonatoDAO tipoCampeonatoDao = new TipocampeonatoDAO();
	
	private Campeonato campeonato;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {

		// Permite enlazar los componentes que se asocian con la anotacion @Wire
		Selectors.wireComponents(view, this, false);
		
		campeonato = (Campeonato) Executions.getCurrent().getArg().get("Campeonato");		
	}
	
	public boolean isValidarDatos() {
		try {
			Boolean resultado = true;	
			if(nombreC.getText() == null) {
				Clients.showNotification("Por favor verifique que los campos con * esten llenos.!!");
				nombreC.focus();
				return resultado;
			}			
			return resultado;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Command
	public void grabar (@ContextParam(ContextType.VIEW) Component view){
		try {
			if (isValidarDatos() == false) {
				Clients.showNotification("Por favor verifique que los campos con * esten llenos.!!");
			}else {
				// Inicia la transaccion				
				campeonatoDao.getEntityManager().getTransaction().begin();
				
				if (campeonato.getIdCampeonato() == null) {
					campeonatoDao.getEntityManager().persist(campeonato);
				}else{
					campeonato = (Campeonato) campeonatoDao.getEntityManager().merge(campeonato);
				}

				// Cierra la transaccion.
				campeonatoDao.getEntityManager().getTransaction().commit();

				Clients.showNotification("Proceso Ejecutado con exito.");			
				// Actualiza la lista
				BindUtils.postGlobalCommand(null, null, "CampeonatoLista.buscar", null);
				
				salir();
			}
		} catch (Exception e) {
			e.printStackTrace();

			// Si hay error, reversa la transaccion.
			campeonatoDao.getEntityManager().getTransaction().rollback();
		}		

	}	
	
	@Command
	public void salir(){
		winCampeonatoEditar.detach();
	}
	
	public List<Tipocampeonato> getTipoCampeonatos() {
		return tipoCampeonatoDao.getTipoCampeonatos(null);
	}
	
	public Campeonato getCampeonato() {
		return campeonato;
	}

	public void setCampeonato(Campeonato campeonato) {
		this.campeonato = campeonato;
	}
}