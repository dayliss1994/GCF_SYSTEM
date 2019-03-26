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

import ec.edu.upse.gcf.dao.TipocanchaDAO;
import ec.edu.upse.gcf.modelo.Tipocancha;

@SuppressWarnings({ "deprecation", "serial" })
public class TipoCanchaLista extends SelectorComposer<Component>{
	@Wire
	private Window winListaTipoCanchas;

	@Wire
	private Textbox txtBuscar;

	@Wire 
	private Listbox lstTipoCanchas;

	public TipocanchaDAO tipoCanchaDao = new TipocanchaDAO();

	private Tipocancha tipoCanchaSeleccionada;
	private List<Tipocancha> tipoCanchas;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Listen("onClick=#btnBuscar;onOK=#txtBuscar")
	public void buscar(){
		System.out.println("entra buscar");

		if (tipoCanchas != null) {
			tipoCanchas = null; 
		}
		tipoCanchas = tipoCanchaDao.getTipoCanchas(txtBuscar.getValue());
		lstTipoCanchas.setModel(new ListModelList(tipoCanchas));

		tipoCanchaSeleccionada = null;

		AnnotateDataBinder binder = new AnnotateDataBinder(winListaTipoCanchas);
		binder.loadComponent(lstTipoCanchas);
	}	

	@Listen("onClick=#btnNuevo")
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("TipoCancha", null);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/tipocanchas/tipoCanchaEditar.zul", winListaTipoCanchas, params);
		ventanaCargar.doModal();
	}

	@Listen("onClick=#btnEditar")
	public void editar(){
		if (tipoCanchaSeleccionada == null) {
			Clients.showNotification("Debe seleccionar tipo de campeonato.");
			return; 
		}

		// Actualiza la instancia antes de enviarla a editar.
		tipoCanchaDao.getEntityManager().refresh(tipoCanchaSeleccionada);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("TipoCancha", tipoCanchaSeleccionada);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/tipocanchas/tipoCanchaEditar.zul", winListaTipoCanchas, params);
		ventanaCargar.doModal();
	}

	@Listen("onClick=#btnEliminar")
	public void eliminar(){

		if (tipoCanchaSeleccionada == null) {
			Clients.showNotification("Debe seleccionar una opcion de la lista.");
			return; 
		}

		Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {    
						tipoCanchaSeleccionada.setEstado("1");
						tipoCanchaDao.getEntityManager().getTransaction().begin();
						tipoCanchaDao.getEntityManager().persist(tipoCanchaSeleccionada);
						tipoCanchaDao.getEntityManager().getTransaction().commit();;
						Clients.showNotification("Transaccion ejecutada con exito.");
						buscar();
					} catch (Exception e) {
						e.printStackTrace();
						tipoCanchaDao.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});        
	}

	//Getter and Setter
	public Tipocancha getTipocanchaSeleccionada() {
		return tipoCanchaSeleccionada;
	}

	public void setTipocanchaSeleccionada(Tipocancha tipocanchaSeleccionada) {
		this.tipoCanchaSeleccionada = tipocanchaSeleccionada;
	}

	public List<Tipocancha> getTipoCanchas() {
		return tipoCanchas;
	}

	public void setTipoCanchas(List<Tipocancha> tipoCanchas) {
		this.tipoCanchas = tipoCanchas;
	}
}
