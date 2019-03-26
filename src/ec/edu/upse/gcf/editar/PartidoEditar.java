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
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.CalendarioDAO;
import ec.edu.upse.gcf.dao.EquipoDAO;
import ec.edu.upse.gcf.dao.PartidoDAO;
import ec.edu.upse.gcf.dao.TipoestadoDAO;
import ec.edu.upse.gcf.modelo.Calendario;
import ec.edu.upse.gcf.modelo.Equipo;
import ec.edu.upse.gcf.modelo.Partido;
import ec.edu.upse.gcf.modelo.Tipoestado;

public class PartidoEditar {
	@Wire
	private Window winPartidoEditar;
	private PartidoDAO partidoDao = new PartidoDAO();
	private CalendarioDAO calendarioDao = new CalendarioDAO();
	private TipoestadoDAO tipoestadoDao = new TipoestadoDAO();
	private EquipoDAO equiporDao = new EquipoDAO();
	private Partido partido;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {

		// Permite enlazar los componentes que se asocian con la anotacion @Wire
		Selectors.wireComponents(view, this, false);

		// Recupera el objeto pasado como parametro. 
		partido = (Partido) Executions.getCurrent().getArg().get("Partido");	
	}

	@Command
	public void grabar(){			
		try {			
			// Inicia la transaccion
			partidoDao.getEntityManager().getTransaction().begin();
			// Almacena los datos.
			// Si es nuevo sa el metodo "persist" de lo contrario usa el metodo "merge"
			if (partido.getIdPartido() == 0) {
				partidoDao.getEntityManager().persist(partido);
			}else{
				partido = (Partido) partidoDao.getEntityManager().merge(partido);
			}

			// Cierra la transaccion.
			partidoDao.getEntityManager().getTransaction().commit();

			Clients.showNotification("Proceso Ejecutado con exito.");

			// Actualiza la lista
			BindUtils.postGlobalCommand(null, null, "Partido.buscarPorPatron", null);

			// Cierra la ventana
			salir();

		} catch (Exception e) {
			e.printStackTrace();

			// Si hay error, reversa la transaccion.
			partidoDao.getEntityManager().getTransaction().rollback();
		}

	}

	@Command
	public void salir(){
		winPartidoEditar.detach();
	}

	public List<Calendario> getCalendarios() {
		return calendarioDao.getListaCalendarios();
	}

	public List<Equipo> getEquipos() {
		return equiporDao.getListaEquipos();
	}

	public List<Tipoestado> getTipoestados() {
		return tipoestadoDao.getListaTipoestados();
	}

	public Partido getPartido() {
		return partido;
	}

	public void setPartido(Partido partido) {
		this.partido = partido;
	}	
}
