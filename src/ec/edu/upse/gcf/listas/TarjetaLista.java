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

import ec.edu.upse.gcf.dao.TarjetaDAO;
import ec.edu.upse.gcf.modelo.Tarjeta;

@SuppressWarnings({ "serial", "deprecation" })
public class TarjetaLista extends SelectorComposer<Component> {
	@Wire
	private Window winListaTarjetas;

	@Wire
	private Textbox txtBuscar;

	@Wire 
	private Listbox lstTarjetas;

	private TarjetaDAO tarjetaDao = new TarjetaDAO();

	private List<Tarjeta> tarjetas;
	private Tarjeta tarjetaSeleccionado;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Listen("onClick=#btnBuscar;onOK=#txtBuscar")
	public void buscar(){

		System.out.println("entra buscar");

		if (tarjetas != null) {
			tarjetas = null; 
		}

		tarjetas = tarjetaDao.getTarjetas(txtBuscar.getValue());
		lstTarjetas.setModel(new ListModelList(tarjetas));
		
		tarjetaSeleccionado = null;

		AnnotateDataBinder binder = new AnnotateDataBinder(winListaTarjetas);
		binder.loadComponent(winListaTarjetas);

	}
	/**
	 * Escucha el evento "onClick" del objeto "btnNuevo"
	 * Carga el formulario sin una persona para crear uno nuevo.
	 */
	@Listen("onClick=#btnNuevo")
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Tarjeta", null);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/tarjetas/tarjetaEditar.zul", winListaTarjetas, params);
		ventanaCargar.doModal();
	}

	@Listen("onClick=#btnEditar")
	public void editar(){
		if (tarjetaSeleccionado == null) {
			Clients.showNotification("Debe seleccionar una tarjeta.");
			return; 
		}

		// Actualiza la instancia antes de enviarla a editar.
		tarjetaDao.getEntityManager().refresh(tarjetaSeleccionado);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Tarjeta", tarjetaSeleccionado);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/tarjetas/tarjetaEditar.zul", winListaTarjetas, params);
		ventanaCargar.doModal();
	}
	
    /**
     * Escucha el evento "onClick" del objeto "btnEliminar"
     * Elimina logicamente una persona.
     */
	@Listen("onClick=#btnEliminar")
    public void eliminar(){
        
        if (tarjetaSeleccionado == null) {
            Clients.showNotification("Debe seleccionar una tarjeta.");
            return; 
        }
        
        Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener<Event>() {
        	@Override
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onYes")) {
                    try {    
                    	tarjetaSeleccionado.setEstado("1");
                    	tarjetaDao.getEntityManager().getTransaction().begin();
                    	tarjetaDao.getEntityManager().persist(tarjetaSeleccionado);
                    	tarjetaDao.getEntityManager().getTransaction().commit();;
                        Clients.showNotification("Transaccion ejecutada con exito.");
                        buscar();
                    } catch (Exception e) {
                        e.printStackTrace();
                        tarjetaDao.getEntityManager().getTransaction().rollback();
                    }
                }
            }
		});        
    }


	//Getter and Setter

	public List<Tarjeta> getTarjetas() {
		return tarjetas;
	}

	public void setTarjetas(List<Tarjeta> tarjetas) {
		this.tarjetas = tarjetas;
	}
	
	public Tarjeta getTarjetaSeleccionado() {
		return tarjetaSeleccionado;
	}

	public void setTarjetaSeleccionado(Tarjeta tarjetaSeleccionado) {
		this.tarjetaSeleccionado = tarjetaSeleccionado;
	}
}
