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

import ec.edu.upse.gcf.dao.TarjetajugadorDAO;
import ec.edu.upse.gcf.modelo.Tarjetajugador;

@SuppressWarnings({ "unchecked", "rawtypes"})
public class SancionLista {

	public String textoBuscar;
	private TarjetajugadorDAO tarjetajugadorDao = new TarjetajugadorDAO();
	private Tarjetajugador tarjetajugadorSeleccionada;
	private List<Tarjetajugador> tarjetajugadores;

	@GlobalCommand("Tarjetajugador.buscarPorPatron")
	@Command
	@NotifyChange({"tarjetajugadores", "tarjetajugadorSeleccionada"})
	public void buscar(){

		if (tarjetajugadores != null) {
			tarjetajugadores = null; 
		}

		tarjetajugadores = tarjetajugadorDao.getTarjetajugadores(textoBuscar);

		// Limpia os objetos de trabajo
		tarjetajugadorSeleccionada = null; 
		tarjetajugadorSeleccionada = null; 
	}

	@Command
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Tarjetajugador", new Tarjetajugador());
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/sanciones/sancionEditar.zul", null, params);
		ventanaCargar.doModal();
	}

	@Command
	public void editar(){
		if (tarjetajugadorSeleccionada == null) {
			Clients.showNotification("Debe seleccionar una opción de la lista.");
			return; 
		}

		// Actualiza la instancia antes de enviarla a editar.
		tarjetajugadorDao.getEntityManager().refresh(tarjetajugadorSeleccionada);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Tarjetajugador", tarjetajugadorSeleccionada);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/sanciones/sancionEditar.zul", null, params);
		ventanaCargar.doModal();
	}

	@Command
	public void eliminar(){

		if (tarjetajugadorSeleccionada == null) {
			Clients.showNotification("Debe seleccionar una opción de la lista.");
			return; 
		}

		Messagebox.show("Desea eliminar el registro seleccionado?", "ConfirmaciÃ³n de EliminaciÃ³n", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						tarjetajugadorDao.getEntityManager().getTransaction().begin();
						tarjetajugadorSeleccionada.setEstado("1");
						tarjetajugadorSeleccionada = tarjetajugadorDao.getEntityManager().merge(tarjetajugadorSeleccionada);
						tarjetajugadorDao.getEntityManager().getTransaction().commit();;
						Clients.showNotification("Transaccion ejecutada con exito.");

						// Actualiza la lista
						BindUtils.postGlobalCommand(null, null, "Tarjetajugador.buscarPorPatron", null);

					} catch (Exception e) {
						e.printStackTrace();
						tarjetajugadorDao.getEntityManager().getTransaction().rollback();
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

	public Tarjetajugador getTarjetajugadorSeleccionada() {
		return tarjetajugadorSeleccionada;
	}

	public void setTarjetajugadorSeleccionada(Tarjetajugador tarjetajugadorSeleccionada) {
		this.tarjetajugadorSeleccionada = tarjetajugadorSeleccionada;
	}

	public List<Tarjetajugador> getTarjetajugadores() {
		return tarjetajugadores;
	}

	public void setTarjetajugadores(List<Tarjetajugador> tarjetajugadores) {
		this.tarjetajugadores = tarjetajugadores;
	}	
}
