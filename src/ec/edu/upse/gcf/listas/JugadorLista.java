package ec.edu.upse.gcf.listas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.JugadorDAO;
import ec.edu.upse.gcf.modelo.Jugador;

@SuppressWarnings({ "unchecked", "rawtypes"})
public class JugadorLista {

	// Atributos consumidos en el formulario
	public String textoBuscar;

	// Instancia el objeto para acceso a datos.
	private JugadorDAO jugadorDao = new JugadorDAO();

	// Objeto que contiene el jugador
	private Jugador jugadorSeleccionada;
	private List<Jugador> jugadores;

	@GlobalCommand("Jugadores.buscarPorPatron")
	@Command
	@NotifyChange({"jugadores", "jugadorSeleccionada"})
	public void buscar(){

		if (jugadores != null) {
			jugadores = null; 
		}

		jugadores = jugadorDao.getJugadores(textoBuscar);

		// Limpia os objetos de trabajo
		jugadorSeleccionada = null; 
		jugadorSeleccionada = null; 

	}

	/**
	 * Crea una persona
	 */
	@Command
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Jugador", new Jugador());
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/jugadores/jugadorEditar.zul", null, params);
		ventanaCargar.doModal();
	}

	/**
	 * Edita una persona
	 */
	@Command
	public void editar(){
		if (jugadorSeleccionada == null) {
			Clients.showNotification("Debe seleccionar una jugador de la lista.");
			return; 
		}

		// Actualiza la instancia antes de enviarla a editar.
		jugadorDao.getEntityManager().refresh(jugadorSeleccionada);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Jugador", jugadorSeleccionada);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/jugadores/jugadorEditar.zul", null, params);
		ventanaCargar.doModal();

	}

	/**
	 * Borra "logicamente" un registro
	 */
	@Command
	public void eliminar(){

		if (jugadorSeleccionada == null) {
			Clients.showNotification("Debe seleccionar un jugador de la lista.");
			return; 
		}

		Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						jugadorDao.getEntityManager().getTransaction().begin();
						jugadorSeleccionada.setEstado("1");
						jugadorSeleccionada = jugadorDao.getEntityManager().merge(jugadorSeleccionada);
						jugadorDao.getEntityManager().getTransaction().commit();;
						Clients.showNotification("Transaccion ejecutada con exito.");

						// Actualiza la lista
						BindUtils.postGlobalCommand(null, null, "Jugadores.buscarPorPatron", null);

					} catch (Exception e) {
						e.printStackTrace();
						jugadorDao.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});

	}

	public String getTextoBuscar() {
		return textoBuscar;
	}

	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}

	public Jugador getJugadorSeleccionada() {
		return jugadorSeleccionada;
	}

	public void setJugadorSeleccionada(Jugador jugadorSeleccionada) {
		this.jugadorSeleccionada = jugadorSeleccionada;
	}
	
	public List<Jugador> getJugadores() {
		return jugadores;
	}

	public void setPersonas(List<Jugador> jugadores) {
		this.jugadores = jugadores;
	}

}
