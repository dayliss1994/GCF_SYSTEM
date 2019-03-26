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

import ec.edu.upse.gcf.dao.UsuarioDao;
import ec.edu.upse.gcf.modelo.Usuario;

@SuppressWarnings({ "unchecked", "serial", "deprecation" })
public class UsuarioLista extends SelectorComposer<Component> {
	@Wire
	private Window winListaUsuarios;
	
	@Wire
	private Textbox txtBuscar;
	
	@Wire 
	private Listbox lstUsuarios;
	
	private UsuarioDao usuarioDao = new UsuarioDao();
	
	private List<Usuario> usuarios;
	private Usuario usuarioSeleccionado;
	
	@SuppressWarnings("rawtypes")
	@Listen("onClick=#btnBuscar;onOK=#txtBuscar")
	public void buscar(){
		
		System.out.println("entra buscar");
		
		if (usuarios != null) {
			usuarios = null; 
		}
		
		usuarios = usuarioDao.getListaUsuariosPorPatron(txtBuscar.getValue());
		lstUsuarios.setModel(new ListModelList(usuarios));
		
		usuarioSeleccionado = null;
		
		AnnotateDataBinder binder = new AnnotateDataBinder(winListaUsuarios);
		binder.loadComponent(lstUsuarios);

	}
	/**
	 * Escucha el evento "onClick" del objeto "btnNuevo"
	 * Carga el formulario sin una persona para crear uno nuevo.
	 */
	@Listen("onClick=#btnNuevo")
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Usuario", null);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/usuarios/usuarioEditar.zul", winListaUsuarios, params);
		ventanaCargar.doModal();
	}
	
	@Listen("onClick=#btnEditar")
	public void editar(){
		if (usuarioSeleccionado == null) {
			Clients.showNotification("Debe seleccionar un usuario.");
			return; 
		}

		// Actualiza la instancia antes de enviarla a editar.
		usuarioDao.getEntityManager().refresh(usuarioSeleccionado);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Usuario", usuarioSeleccionado);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/usuarios/usuarioEditar.zul", winListaUsuarios, params);
		ventanaCargar.doModal();
	}
	@Listen("onClick=#btnBorrar")
    public void borrar(){
        if (usuarioSeleccionado == null) {
            Clients.showNotification("Debe seleccionar un usuario.");
            return; 
        }
        
        Messagebox.show("Desea borrar el registro seleccionado?", "Confirmación de borrado", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener<Event>() {
			
			@Override
			public void onEvent(Event event) throws Exception {
				// TODO Auto-generated method stub
				if (event.getName().equals("onYes")) {
                    try {
                    	usuarioDao.getEntityManager().getTransaction().begin();
                    	usuarioDao.getEntityManager().refresh(usuarioSeleccionado);
                    	usuarioDao.getEntityManager().remove(usuarioSeleccionado);
                    	usuarioDao.getEntityManager().getTransaction().commit();;
                        Clients.showNotification("Transaccion ejecutada con exito.");
                        buscar();
                    } catch (Exception e) {
                        e.printStackTrace();
                        usuarioDao.getEntityManager().getTransaction().rollback();
                    }
                }
				
			}
		});
    }

    /**
     * Escucha el evento "onClick" del objeto "btnEliminar"
     * Elimina logicamente una persona.
     */
	@Listen("onClick=#btnEliminar")
    public void eliminar(){
        
        if (usuarioSeleccionado == null) {
            Clients.showNotification("Debe seleccionar un usuario.");
            return; 
        }
        
        Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener<Event>() {
        	@Override
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onYes")) {
                    try {
                    	usuarioSeleccionado.setEstado("1");   
                    	usuarioDao.getEntityManager().getTransaction().begin();
                    	usuarioDao.getEntityManager().persist(usuarioSeleccionado);
                    	usuarioDao.getEntityManager().getTransaction().commit();;
                        Clients.showNotification("Transaccion ejecutada con exito.");
                        buscar();
                    } catch (Exception e) {
                        e.printStackTrace();
                        usuarioDao.getEntityManager().getTransaction().rollback();
                    }
                }
            }
		});        
    }
	//Getter and Setter
	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}	
	public Usuario getUsuarioSeleccionado() {
		return usuarioSeleccionado;
	}

	public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
		this.usuarioSeleccionado = usuarioSeleccionado;
	}
}