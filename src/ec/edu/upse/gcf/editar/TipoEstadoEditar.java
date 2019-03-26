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

import ec.edu.upse.gcf.dao.TipoestadoDAO;
import ec.edu.upse.gcf.modelo.Tipoestado;


public class TipoEstadoEditar {
	// Enlaza a la ventana para poderla cerrar
	@Wire
	private Textbox descripcion;
	
	@Wire
	private Window winTipoEstadoEditar;

	private TipoestadoDAO tipoestadoDao = new TipoestadoDAO();
	private Tipoestado tipoestado;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {

		// Permite enlazar los componentes que se asocian con la anotacion @Wire
		Selectors.wireComponents(view, this, false);

	    // Recupera el objeto pasado como parametro. 
		tipoestado = (Tipoestado) Executions.getCurrent().getArg().get("Tipoestado");	
	}

	public boolean isValidarDatos() {
		try {
			Boolean resultado = false;	
			if(descripcion.getText() == null) {
				Clients.showNotification("Por favor ingrese una descripción.!!");
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
			if (isValidarDatos() == false) {
				Clients.showNotification("Verifique que los campos esten llenos.!!");
			}else {
				// Inicia la transaccion
				tipoestadoDao.getEntityManager().getTransaction().begin();
				
				// Almacena los datos.
				// Si es nuevo sa el metodo "persist" de lo contrario usa el metodo "merge"
				if (tipoestado.getIdTipoestado() == 0) {
					tipoestadoDao.getEntityManager().persist(tipoestado);
				}else{
					tipoestado = (Tipoestado) tipoestadoDao.getEntityManager().merge(tipoestado);
				}
				
				// Cierra la transaccion.
				tipoestadoDao.getEntityManager().getTransaction().commit();
				
				Clients.showNotification("Proceso Ejecutado con exito.");
				
				// Actualiza la lista
				BindUtils.postGlobalCommand(null, null, "TipoestadoLista.buscar", null);
				
				// Cierra la ventana
				salir();
			}
		} catch (Exception e) {
			e.printStackTrace();
			
			// Si hay error, reversa la transaccion.
			tipoestadoDao.getEntityManager().getTransaction().rollback();
		}
		
	}

	@Command
	public void salir(){
		winTipoEstadoEditar.detach();
	}

	public Tipoestado getTipoestado() {
		return tipoestado;
	}

	public void setTipoestado(Tipoestado tipoestado) {
		this.tipoestado = tipoestado;
	}
	
}
	