package ec.edu.upse.gcf.listas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.PosicionjuegoDAO;
import ec.edu.upse.gcf.modelo.Posicionjuego;

@SuppressWarnings({ "unchecked", "rawtypes", "serial", "deprecation"})
public class PosicionLista extends SelectorComposer<Component>{

	@Wire
	private Window winListaPosiciones;
	
	@Wire
	private Textbox txtBuscar;

	@Wire 
	private Listbox lstPosicion;

	private PosicionjuegoDAO posicionDao = new PosicionjuegoDAO();

	private List<Posicionjuego> posiciones;
	private Posicionjuego posicionSeleccionado;

	@SuppressWarnings({ })
	@Listen("onClick=#btnBuscar;onOK=#txtBuscar")
	public void buscar(){

		System.out.println("entra buscar");

		if (posiciones != null) {
			posiciones = null; 
		}

		posiciones = posicionDao.getPosiciones(txtBuscar.getValue());
		lstPosicion.setModel(new ListModelList(posiciones));
		
		posicionSeleccionado = null;

		AnnotateDataBinder binder = new AnnotateDataBinder(winListaPosiciones);
		binder.loadComponent(winListaPosiciones);

	}
	
		@Listen("onClick=#btnNuevo")
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Posicionjuego", null);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/posicion/posicionEditar.zul", winListaPosiciones, params);
		ventanaCargar.doModal();
	}

	@Listen("onClick=#btnEditar")
	public void editar(){
		if (posicionSeleccionado == null) {
			Clients.showNotification("Debe seleccionar una opción.");
			return; 
		}

		// Actualiza la instancia antes de enviarla a editar.
		posicionDao.getEntityManager().refresh(posicionSeleccionado);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Posicionjuego", posicionSeleccionado);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/posicion/posicionEditar.zul", winListaPosiciones, params);
		ventanaCargar.doModal();
	}
	
   	@Listen("onClick=#btnEliminar")
    public void eliminar(){
        
        if (posicionSeleccionado == null) {
            Clients.showNotification("Debe seleccionar una opción.");
            return; 
        }
        
        Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener<Event>() {
        	@Override
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onYes")) {
                    try {    
                    	posicionSeleccionado.setEstado("1");
                    	posicionDao.getEntityManager().getTransaction().begin();
                    	posicionDao.getEntityManager().persist(posicionSeleccionado);
                    	posicionDao.getEntityManager().getTransaction().commit();;
                        Clients.showNotification("Transaccion ejecutada con exito.");
                        buscar();
                    } catch (Exception e) {
                        e.printStackTrace();
                        posicionDao.getEntityManager().getTransaction().rollback();
                    }
                }
            }
		});        
    }

	public List<Posicionjuego> getPosiciones() {
		return posiciones;
	}

	public void setPosiciones(List<Posicionjuego> posiciones) {
		this.posiciones = posiciones;
	}

	public Posicionjuego getPosicionSeleccionado() {
		return posicionSeleccionado;
	}

	public void setPosicionSeleccionado(Posicionjuego posicionSeleccionado) {
		this.posicionSeleccionado = posicionSeleccionado;
	}	
}
