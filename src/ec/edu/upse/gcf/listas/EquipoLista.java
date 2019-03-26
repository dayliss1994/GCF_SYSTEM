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

import ec.edu.upse.gcf.dao.EquipoDAO;
import ec.edu.upse.gcf.modelo.Equipo;


@SuppressWarnings({ "unchecked", "rawtypes"})
public class EquipoLista {

	public String textoBuscar;

	private EquipoDAO equipoDao = new EquipoDAO();

	private List<Equipo> equipos ;
	private Equipo equipoSeleccionado;
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		textoBuscar="";
	}
		
	@GlobalCommand("Equipo.buscarPorPatron")
	@Command
	@NotifyChange({"equipos", "equipoSeleccionado."})
	public void buscar(){
		
		if (equipos != null) {
			equipos = null; 
		}
		
		equipos = equipoDao.getEquipos(textoBuscar);
	
		// Limpia os objetos de trabajo
		equipoSeleccionado = null; 
	}
	
	/**
	 * Crea una persona
	 */
	@Command
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Equipo", new Equipo());
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/equipos/equipoEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	/**
	 * Edita una persona
	 */
	@Command
	public void editar(){
		if (equipoSeleccionado == null) {
			Clients.showNotification("Debe seleccionar una equipo.");
			return; 
		}

		// Actualiza la instancia antes de enviarla a editar.
		equipoDao.getEntityManager().refresh(equipoSeleccionado);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Equipo", equipoSeleccionado);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/equipos/equipoEditar.zul", null, params);
		ventanaCargar.doModal();
		
	}
	
	/**
	 * Borra "logicamente" un registro
	 */
	@Command
	public void eliminar(){
		
		if (equipoSeleccionado == null) {
			Clients.showNotification("Debe seleccionar una equipo.");
			return; 
		}
		
		Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						equipoDao.getEntityManager().getTransaction().begin();
						equipoSeleccionado.setEstado("1");
						equipoSeleccionado = equipoDao.getEntityManager().merge(equipoSeleccionado);
						equipoDao.getEntityManager().getTransaction().commit();;
						Clients.showNotification("Transaccion ejecutada con exito.");
						
						// Actualiza la lista
						BindUtils.postGlobalCommand(null, null, "Equipo.buscarPorPatron", null);
						
					} catch (Exception e) {
						e.printStackTrace();
						equipoDao.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});
		
	}

	
	//Getter and Setter

	
	public List<Equipo> getEquipos() {
		return equipos;
	}

	public String getTextoBuscar() {
		return textoBuscar;
	}

	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}

	public void setEquipos(List<Equipo> equipos) {
		this.equipos = equipos;
	}
	
	public Equipo getEquipoSeleccionado() {
		return equipoSeleccionado;
	}

	public void setEquipoSeleccionado(Equipo equipoSeleccionado) {
		this.equipoSeleccionado = equipoSeleccionado;
	}
}
