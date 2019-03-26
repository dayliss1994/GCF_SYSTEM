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

import ec.edu.upse.gcf.dao.GoleadorDAO;
import ec.edu.upse.gcf.modelo.Goleador;

@SuppressWarnings({ "unchecked", "rawtypes"})
public class GoleadorLista {
	
	public String textoBuscar;
	private GoleadorDAO goleadorDao = new GoleadorDAO();
	private List<Goleador> goleadores;
	private Goleador goleadorSeleccionado;
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
		textoBuscar="";
	}
	
	@GlobalCommand("Goleador.buscarPorPatron")
	@Command
	@NotifyChange({"goleadores", "goleadorSeleccionado."})
	public void buscar(){
		
		if (goleadores != null) {
			goleadores = null; 
		}
		
		goleadores = goleadorDao.getGoleadores(textoBuscar);
	
		// Limpia os objetos de trabajo
		goleadorSeleccionado = null; 
	}
	
	@Command
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Goleador", new Goleador());
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/goleadores/goleadorEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	
	@Command
	public void editar(){
		if (goleadorSeleccionado == null) {
			Clients.showNotification("Debe seleccionar una opci�n.");
			return; 
		}

		// Actualiza la instancia antes de enviarla a editar.
		goleadorDao.getEntityManager().refresh(goleadorSeleccionado);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Goleador", goleadorSeleccionado);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/goleadores/goleadorEditar.zul", null, params);
		ventanaCargar.doModal();
		
	}
	@Command
	public void eliminar(){
		
		if (goleadorSeleccionado == null) {
			Clients.showNotification("Debe seleccionar una equipo.");
			return; 
		}
		
		Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						goleadorDao.getEntityManager().getTransaction().begin();
						goleadorSeleccionado.setEstado("1");
						goleadorSeleccionado = goleadorDao.getEntityManager().merge(goleadorSeleccionado);
						goleadorDao.getEntityManager().getTransaction().commit();;
						Clients.showNotification("Transaccion ejecutada con exito.");
						
						// Actualiza la lista
						BindUtils.postGlobalCommand(null, null, "Goleador.buscarPorPatron", null);
						
					} catch (Exception e) {
						e.printStackTrace();
						goleadorDao.getEntityManager().getTransaction().rollback();
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

	public Goleador getGoleadorSeleccionado() {
		return goleadorSeleccionado;
	}

	public void setGoleadorSeleccionado(Goleador goleadorSeleccionado) {
		this.goleadorSeleccionado = goleadorSeleccionado;
	}

	public List<Goleador> getGoleadores() {
		return goleadores;
	}

	public void setGoleadores(List<Goleador> goleadores) {
		this.goleadores = goleadores;
	}
	
}
