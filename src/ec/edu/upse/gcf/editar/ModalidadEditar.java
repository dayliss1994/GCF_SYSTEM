package ec.edu.upse.gcf.editar;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.ModalidadDAO;
import ec.edu.upse.gcf.modelo.Modalidad;


public class ModalidadEditar {
	
	@Wire
	private Window winModalidadEditar;

	@Wire 
	public Textbox descripcion;

	private ModalidadDAO modalidadDao = new ModalidadDAO();		
	private Modalidad modalidad;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {

		// Permite enlazar los componentes que se asocian con la anotacion @Wire
		Selectors.wireComponents(view, this, false);

		// Recupera el objeto pasado como parametro. 
		modalidad = (Modalidad) Executions.getCurrent().getArg().get("Modalidad");	
	}

	public boolean isValidarDatos() {
		try {
			Boolean resultado = true;	
			if(descripcion.getText() == null) {
				Clients.showNotification("Por favor ingrese la modalidad.!!");
				descripcion.focus();
				return resultado;
			}			
			return resultado;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Command
	public void grabar(){			
		try {
				
			if (isValidarDatos() == true) {
				// Inicia la transaccion
				modalidadDao.getEntityManager().getTransaction().begin();
		
				if (modalidad.getIdModalidad() == null) {
					
					modalidadDao.getEntityManager().persist(modalidad);
				}else{
					modalidad = (Modalidad) modalidadDao.getEntityManager().merge(modalidad);
				}
				
				// Cierra la transaccion.
				modalidadDao.getEntityManager().getTransaction().commit();
				
				Clients.showNotification("Proceso Ejecutado con exito.");
				
				// Actualiza la lista
				BindUtils.postGlobalCommand(null, null, "ModalidadLista.buscar", null);
				
				// Cierra la ventana
				salir();
			}else {
				Clients.showNotification("Verifique que los campos esten llenos.");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
			// Si hay error, reversa la transaccion.
			modalidadDao.getEntityManager().getTransaction().rollback();
		}
		
	}
		
	@Command
	public void salir(){
		winModalidadEditar.detach();
	}
	public Modalidad getModalidad() {
		return modalidad;
	}

	public void setModalidad(Modalidad modalidad) {
		this.modalidad = modalidad;
	}
}