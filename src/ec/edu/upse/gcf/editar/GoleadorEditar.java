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

import ec.edu.upse.gcf.dao.GoleadorDAO;
import ec.edu.upse.gcf.dao.JugadorDAO;
import ec.edu.upse.gcf.dao.PartidoDAO;
import ec.edu.upse.gcf.modelo.Goleador;
import ec.edu.upse.gcf.modelo.Jugador;
import ec.edu.upse.gcf.modelo.Partido;

public class GoleadorEditar {
	@Wire
	private Window winGoleadorEditar;

	private GoleadorDAO goleadorDao = new GoleadorDAO();
	private PartidoDAO partidoDao = new PartidoDAO();
	private JugadorDAO jugadorDao = new JugadorDAO();
	private Goleador goleador;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {

		// Permite enlazar los componentes que se asocian con la anotacion @Wire
		Selectors.wireComponents(view, this, false);

		// Recupera el objeto pasado como parametro. 
		goleador = (Goleador) Executions.getCurrent().getArg().get("Goleador");	
	}

	@Command
	public void grabar(){			
		try {
			// Inicia la transaccion
			goleadorDao.getEntityManager().getTransaction().begin();
			// Almacena los datos.
			// Si es nuevo sa el metodo "persist" de lo contrario usa el metodo "merge"
			if (goleador.getIdGoleador() == 0) {
				goleadorDao.getEntityManager().persist(goleador);
			}else{
				goleador = (Goleador) goleadorDao.getEntityManager().merge(goleador);
			}

			// Cierra la transaccion.
			goleadorDao.getEntityManager().getTransaction().commit();

			Clients.showNotification("Proceso Ejecutado con exito.");

			// Actualiza la lista
			BindUtils.postGlobalCommand(null, null, "Goleador.buscarPorPatron", null);

			// Cierra la ventana
			salir();

		} catch (Exception e) {
			e.printStackTrace();

			// Si hay error, reversa la transaccion.
			goleadorDao.getEntityManager().getTransaction().rollback();
		}

	}
	
	@Command
	public void salir(){
		winGoleadorEditar.detach();
	}

	public List<Partido> getPartidos() {
		return partidoDao.getPartidos();
	}
	public List<Jugador> getJugadores() {
		return jugadorDao.getJugador();
	}

	public Goleador getGoleador() {
		return goleador;
	}

	public void setGoleador(Goleador goleador) {
		this.goleador = goleador;
	}
	
}
