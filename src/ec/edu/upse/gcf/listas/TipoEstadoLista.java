package ec.edu.upse.gcf.listas;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.TipoestadoDAO;
import ec.edu.upse.gcf.modelo.Tipoestado;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TipoEstadoLista{

	public String textoBuscar;
	public TipoestadoDAO tipoestadoDao = new TipoestadoDAO();
	private List<Tipoestado> tipoestados;
	private Tipoestado tipoEstadoSeleccionado;

	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		textoBuscar="";
	}

	@GlobalCommand("TipoestadoLista.buscar")
	@Command
	@NotifyChange({"tipoestados", "tipoEstadoSeleccionado."})
	public void buscar(){

		if (tipoestados != null) {
			tipoestados = null; 
		}

		tipoestados = tipoestadoDao.getTipoEstados(textoBuscar);

		// Limpia os objetos de trabajo
		tipoEstadoSeleccionado = null; 
	}

	/**
	 * Crea una persona
	 */
	@Command
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Tipoestado", new Tipoestado());
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/tipoestado/tipoEstadoEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	
	/**
	 * Edita una persona
	 */
	@Command
	public void editar(){
		if (tipoEstadoSeleccionado == null) {
			Clients.showNotification("Debe seleccionar una estado de la lista.");
			return; 
		}

		// Actualiza la instancia antes de enviarla a editar.
		tipoestadoDao.getEntityManager().refresh(tipoEstadoSeleccionado);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Tipoestado", tipoEstadoSeleccionado);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/tipoestado/tipoEstadoEditar.zul", null, params);
		ventanaCargar.doModal();
		
	}

	/**
	 * Borra "logicamente" un registro
	 */
	@Command
	public void eliminar(){
		
		if (tipoEstadoSeleccionado == null) {
			Clients.showNotification("Debe seleccionar una estado de la lista.");
			return; 
		}
		
		Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						tipoestadoDao.getEntityManager().getTransaction().begin();
						tipoEstadoSeleccionado.setEstado("1");
						tipoEstadoSeleccionado = tipoestadoDao.getEntityManager().merge(tipoEstadoSeleccionado);
						tipoestadoDao.getEntityManager().getTransaction().commit();;
						Clients.showNotification("Transaccion ejecutada con exito.");
						
						// Actualiza la lista
						BindUtils.postGlobalCommand(null, null, "TipoestadoLista.buscar", null);
						
					} catch (Exception e) {
						e.printStackTrace();
						tipoestadoDao.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
		
	}

	//Getter and Setter
	public List<Tipoestado> getTipoestados() {
		return tipoestados;
	}

	public void setTipoestados(List<Tipoestado> tipoestados) {
		this.tipoestados = tipoestados;
	}

	public String getTextoBuscar() {
		return textoBuscar;
	}

	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}

	public Tipoestado getTipoEstadoSeleccionado() {
		return tipoEstadoSeleccionado;
	}

	public void setTipoEstadoSeleccionado(Tipoestado tipoEstadoSeleccionado) {
		this.tipoEstadoSeleccionado = tipoEstadoSeleccionado;
	}
	
}
