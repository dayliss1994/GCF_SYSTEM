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

import ec.edu.upse.gcf.dao.JugadorDAO;
import ec.edu.upse.gcf.dao.PartidoDAO;
import ec.edu.upse.gcf.dao.TarjetaDAO;
import ec.edu.upse.gcf.dao.TarjetajugadorDAO;
import ec.edu.upse.gcf.modelo.Jugador;
import ec.edu.upse.gcf.modelo.Partido;
import ec.edu.upse.gcf.modelo.Tarjeta;
import ec.edu.upse.gcf.modelo.Tarjetajugador;

public class TarjetajugadorEditar {
	
	@Wire
	private Window winTarjetajugadorEditar;
	private TarjetajugadorDAO tarjetajugadorDao = new TarjetajugadorDAO();
	private PartidoDAO partidoDao = new PartidoDAO();
	private JugadorDAO jugadorDao = new JugadorDAO();
	private TarjetaDAO tarjetaDao = new TarjetaDAO();
	
	private Tarjetajugador tarjetajugador;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {

		// Permite enlazar los componentes que se asocian con la anotacion @Wire
		Selectors.wireComponents(view, this, false);
		tarjetajugador = (Tarjetajugador) Executions.getCurrent().getArg().get("Tarjetajugador");		
	}
	
	@Command
	public void grabar (@ContextParam(ContextType.VIEW) Component view){
		try {
			// Inicia la transaccion
			tarjetajugadorDao.getEntityManager().getTransaction().begin();

			// Almacena los datos.
			// Si es nuevo sa el metodo "persist" de lo contrario usa el metodo "merge"
			if (tarjetajugador.getIdTarjetaJugador() == 0) {
				tarjetajugadorDao.getEntityManager().persist(tarjetajugador);
			}else{
				tarjetajugador = (Tarjetajugador) tarjetajugadorDao.getEntityManager().merge(tarjetajugador);
			}

			// Cierra la transaccion.
			tarjetajugadorDao.getEntityManager().getTransaction().commit();

			Clients.showNotification("Proceso Ejecutado con exito.");			
			// Actualiza la lista
			BindUtils.postGlobalCommand(null, null, "Tarjetajugador.buscarPorPatron", null);
			
			//jugadorLista.buscar();
			// Cierra la ventana
			salir();

		} catch (Exception e) {
			e.printStackTrace();

			// Si hay error, reversa la transaccion.
			tarjetajugadorDao.getEntityManager().getTransaction().rollback();
		}	
	}
	
	@Command
	public void salir(){
		winTarjetajugadorEditar.detach();
	}	

	public Tarjetajugador getTarjetajugador() {
		return tarjetajugador;
	}

	public void setTarjetajugador(Tarjetajugador tarjetajugador) {
		this.tarjetajugador = tarjetajugador;
	}
	
	public List<Jugador> getJugadores() {
		return jugadorDao.getJugador(); 
	}
	public List<Partido> getPartidos() {
		return partidoDao.getPartidos();
	}
	public List<Tarjeta> getTarjetas() {
		return tarjetaDao.getTarjetas();
	}
}
