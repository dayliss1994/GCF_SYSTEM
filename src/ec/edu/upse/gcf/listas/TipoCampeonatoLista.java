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

import ec.edu.upse.gcf.dao.TipocampeonatoDAO;
import ec.edu.upse.gcf.modelo.Tipocampeonato;

@SuppressWarnings({ "deprecation", "serial" })
public class TipoCampeonatoLista extends SelectorComposer<Component> {
	@Wire
	private Window winListaTipoCampeonatos;
	
	@Wire
	private Textbox txtBuscar;
	
	@Wire 
	private Listbox lstTipoCampeonatos;
	
	private TipocampeonatoDAO tipoCampeonatoDao = new TipocampeonatoDAO();
	
	private List<Tipocampeonato> tipoCampeonatos;
	private Tipocampeonato tipoCampeonatoSeleccionado;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Listen("onClick=#btnBuscar;onOK=#txtBuscar")
	public void buscar(){
			System.out.println("entra buscar");
			
			if (tipoCampeonatos != null) {
				tipoCampeonatos = null; 
			}
			tipoCampeonatos = tipoCampeonatoDao.getTipoCampeonatos(txtBuscar.getValue());
			lstTipoCampeonatos.setModel(new ListModelList(tipoCampeonatos));
			
			tipoCampeonatoSeleccionado = null;

			AnnotateDataBinder binder = new AnnotateDataBinder(winListaTipoCampeonatos);
			binder.loadComponent(lstTipoCampeonatos);

	}	
	
	/**
	 * Escucha el evento "onClick" del objeto "btnNuevo"
	 * Carga el formulario sin una persona para crear uno nuevo.
	 */
	@Listen("onClick=#btnNuevo")
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("TipoCampeonato", null);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/tipocampeonatos/tipoCampeonatoEditar.zul", winListaTipoCampeonatos, params);
		ventanaCargar.doModal();
	}
	
	@Listen("onClick=#btnEditar")
	public void editar(){
		if (tipoCampeonatoSeleccionado == null) {
			Clients.showNotification("Debe seleccionar tipo de campeonato.");
			return; 
		}

		// Actualiza la instancia antes de enviarla a editar.
		tipoCampeonatoDao.getEntityManager().refresh(tipoCampeonatoSeleccionado);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("TipoCampeonato", tipoCampeonatoSeleccionado);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/tipocampeonatos/tipoCampeonatoEditar.zul", winListaTipoCampeonatos, params);
		ventanaCargar.doModal();
	}
	
	/**
     * Escucha el evento "onClick" del objeto "btnEliminar"
     * Elimina logicamente una persona.
     */
	@Listen("onClick=#btnEliminar")
    public void eliminar(){
        
        if (tipoCampeonatoSeleccionado == null) {
            Clients.showNotification("Debe seleccionar una opcion de la lista.");
            return; 
        }
        
        Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener<Event>() {
        	@Override
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onYes")) {
                    try {    
                    	tipoCampeonatoSeleccionado.setEstado("1");
                    	tipoCampeonatoDao.getEntityManager().getTransaction().begin();
                    	tipoCampeonatoDao.getEntityManager().persist(tipoCampeonatoSeleccionado);
                    	tipoCampeonatoDao.getEntityManager().getTransaction().commit();;
                        Clients.showNotification("Transaccion ejecutada con exito.");
                        buscar();
                    } catch (Exception e) {
                        e.printStackTrace();
                        tipoCampeonatoDao.getEntityManager().getTransaction().rollback();
                    }
                }
            }
		});        
    }


	//Getter and Setter

	public List<Tipocampeonato> getTipoCampeonatos() {
		return tipoCampeonatos;
	}

	public void setTipoCampeonatos(List<Tipocampeonato> tipoCampeonatos) {
		this.tipoCampeonatos = tipoCampeonatos;
	}

	public Tipocampeonato getTipoCampeonatoSeleccionado() {
		return tipoCampeonatoSeleccionado;
	}

	public void setTipoCampeonatoSeleccionado(Tipocampeonato tipoCampeonatoSeleccionado) {
		this.tipoCampeonatoSeleccionado = tipoCampeonatoSeleccionado;
	}
}
