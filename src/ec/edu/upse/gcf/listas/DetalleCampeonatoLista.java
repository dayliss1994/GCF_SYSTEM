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
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.DetalleCampeonatoDAO;
import ec.edu.upse.gcf.modelo.Detallecampeonato;

@SuppressWarnings({ "unchecked" })
public class DetalleCampeonatoLista{
	@Wire
	private String textoBuscar;

	private DetalleCampeonatoDAO detallecampeonatoDao = new DetalleCampeonatoDAO();

	private List<Detallecampeonato> detallecampeonatos;
	private Detallecampeonato detallecampeonatoSeleccionado;
	
	
	@GlobalCommand("Detallecampeonato.buscarPorPatron")
	@Command
	@NotifyChange({"detallecampeonatos", "detallecampeonatoSeleccionado"})
	public void buscar(){

		if (detallecampeonatos != null) {
			detallecampeonatos = null; 
		}

		detallecampeonatos = detallecampeonatoDao.getDetalleCampeonatos(textoBuscar);

		// Limpia os objetos de trabajo
		detallecampeonatoSeleccionado = null; 
		detallecampeonatoSeleccionado = null; 

	}	
	
	/**
	 * Crea una persona
	*/
	@Command
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Detallecampeonato", new Detallecampeonato());
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/detallecampeonato/detallecampeonatoEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	
	/**
	 * Edita datos
	*/
	@Command
	public void editar(){
		if (detallecampeonatoSeleccionado == null) {
			Clients.showNotification("Debe seleccionar una opción de la lista.");
			return; 
		}

		// Actualiza la instancia antes de enviarla a editar.
		detallecampeonatoDao.getEntityManager().refresh(detallecampeonatoSeleccionado);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Detallecampeonato", detallecampeonatoSeleccionado);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/detallecampeonato/detallecampeonatoEditar.zul", null, params);
		ventanaCargar.doModal();

	}
	

	@SuppressWarnings("rawtypes")
	@Command
	public void eliminar(){

		if (detallecampeonatoSeleccionado == null) {
			Clients.showNotification("Debe seleccionar una opción de la lista.");
			return; 
		}

		Messagebox.show("Desea eliminar el registro seleccionado?", "ConfirmaciÃ³n de EliminaciÃ³n", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						detallecampeonatoDao.getEntityManager().getTransaction().begin();
						detallecampeonatoSeleccionado.setEstado("1");
						detallecampeonatoSeleccionado = detallecampeonatoDao.getEntityManager().merge(detallecampeonatoSeleccionado);
						detallecampeonatoDao.getEntityManager().getTransaction().commit();;
						Clients.showNotification("Transaccion ejecutada con exito.");

						// Actualiza la lista
						BindUtils.postGlobalCommand(null, null, "DetallecampeonatoLista.buscar", null);

					} catch (Exception e) {
						e.printStackTrace();
						detallecampeonatoDao.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});

	}

	//Getter and Setter
	
	public String getTextoBuscar() {
		return textoBuscar;
	}

	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}

	public List<Detallecampeonato> getDetallecampeonatos() {
		return detallecampeonatos;
	}

	public void setDetallecampeonatos(List<Detallecampeonato> detallecampeonatos) {
		this.detallecampeonatos = detallecampeonatos;
	}

	public Detallecampeonato getDetallecampeonatoSeleccionado() {
		return detallecampeonatoSeleccionado;
	}

	public void setDetallecampeonatoSeleccionado(Detallecampeonato detallecampeonatoSeleccionado) {
		this.detallecampeonatoSeleccionado = detallecampeonatoSeleccionado;
	}


}
