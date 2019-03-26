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

import ec.edu.upse.gcf.dao.CategoriaDAO;
import ec.edu.upse.gcf.modelo.Categoria;

@SuppressWarnings({"unchecked", "serial", "deprecation"})
public class CategoriaLista extends SelectorComposer<Component> {
	@Wire
	private Window winListaCategorias;

	@Wire
	private Textbox txtBuscar;

	@Wire 
	private Listbox lstCategorias;

	private CategoriaDAO categoriaDao = new CategoriaDAO();

	private List<Categoria> categorias;
	private Categoria categoriaSeleccionado;

	@SuppressWarnings({ "rawtypes" })
	@Listen("onClick=#btnBuscar;onOK=#txtBuscar")
	public void buscar(){

		System.out.println("entra buscar");

		if (categorias != null) {
			categorias = null; 
		}

		categorias = categoriaDao.getCategorias(txtBuscar.getValue());
		lstCategorias.setModel(new ListModelList(categorias));

		categoriaSeleccionado = null;

		AnnotateDataBinder binder = new AnnotateDataBinder(winListaCategorias);
		binder.loadComponent(lstCategorias);

	}
	/**
	 * Escucha el evento "onClick" del objeto "btnNuevo"
	 * Carga el formulario sin una persona para crear uno nuevo.
	 */
	@Listen("onClick=#btnNuevo")
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Categoria", null);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/categorias/categoriaEditar.zul", winListaCategorias, params);
		ventanaCargar.doModal();
	}

	@Listen("onClick=#btnEditar")
	public void editar(){
		if (categoriaSeleccionado == null) {
			Clients.showNotification("Debe seleccionar una categoría.");
			return; 
		}

		// Actualiza la instancia antes de enviarla a editar.
		categoriaDao.getEntityManager().refresh(categoriaSeleccionado);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Categoria", categoriaSeleccionado);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/categorias/categoriaEditar.zul", winListaCategorias, params);
		ventanaCargar.doModal();
	}
	
	/**
     * Escucha el evento "onClick" del objeto "btnEliminar"
     * Elimina logicamente una persona.
     */
	@Listen("onClick=#btnEliminar")
    public void eliminar(){
        
        if (categoriaSeleccionado == null) {
            Clients.showNotification("Debe seleccionar una categoria.");
            return; 
        }
        
        Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener<Event>() {
        	@Override
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onYes")) {
                    try {
                    	categoriaSeleccionado.setEstado("1");
                    	categoriaDao.getEntityManager().getTransaction().begin();
                    	categoriaDao.getEntityManager().persist(categoriaSeleccionado);
                    	categoriaDao.getEntityManager().getTransaction().commit();;
                        Clients.showNotification("Transaccion ejecutada con exito.");
                        buscar();
                    } catch (Exception e) {
                        e.printStackTrace();
                        categoriaDao.getEntityManager().getTransaction().rollback();
                    }
                }
            }
		});        
    }


	//Getter and Setter

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}
	public Categoria getCategoriaSeleccionado() {
		return categoriaSeleccionado;
	}

	public void setCategoriaSeleccionado(Categoria categoriaSeleccionado) {
		this.categoriaSeleccionado = categoriaSeleccionado;
	}



}

