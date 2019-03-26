package ec.edu.upse.gcf.editar;

import java.util.List;

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

import ec.edu.upse.gcf.dao.LugarPartidoDAO;
import ec.edu.upse.gcf.dao.TipocanchaDAO;
import ec.edu.upse.gcf.modelo.Lugarpartido;
import ec.edu.upse.gcf.modelo.Tipocancha;

public class LugarPartidoEditar {
	@Wire
	private Textbox descripcion;

	@Wire
	private Window winLugarPartidoEditar;

	private LugarPartidoDAO lugarpartidoDao = new LugarPartidoDAO();
	private Lugarpartido lugarpartido;	
	private TipocanchaDAO tipocanchaDao = new TipocanchaDAO();

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {

		// Permite enlazar los componentes que se asocian con la anotacion @Wire
		Selectors.wireComponents(view, this, false);

		// Recupera el objeto pasado como parametro. 
		lugarpartido = (Lugarpartido) Executions.getCurrent().getArg().get("Lugarpartido");	
	}

	public boolean isValidarDatos() {
		try {
			Boolean resultado = true;	
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
				lugarpartidoDao.getEntityManager().getTransaction().begin();

				// Almacena los datos.
				// Si es nuevo sa el metodo "persist" de lo contrario usa el metodo "merge"
				if (lugarpartido.getId_lugarP() == 0) {
					lugarpartidoDao.getEntityManager().persist(lugarpartido);
				}else{
					lugarpartido = (Lugarpartido) lugarpartidoDao.getEntityManager().merge(lugarpartido);
				}

				// Cierra la transaccion.
				lugarpartidoDao.getEntityManager().getTransaction().commit();

				Clients.showNotification("Proceso Ejecutado con exito.");

				// Actualiza la lista
				BindUtils.postGlobalCommand(null, null, "Lugarpartido.buscarPorPatron", null);

				// Cierra la ventana
				salir();
			}
		} catch (Exception e) {
			e.printStackTrace();

			// Si hay error, reversa la transaccion.
			lugarpartidoDao.getEntityManager().getTransaction().rollback();
		}		
	}

	@Command
	public void salir(){
		winLugarPartidoEditar.detach();
	}

	public Lugarpartido getLugarpartido() {
		return lugarpartido;
	}

	public void setLugarpartido(Lugarpartido lugarpartido) {
		this.lugarpartido = lugarpartido;
	}

	public List<Tipocancha> getTipocanchas() {
		return tipocanchaDao.getListaTipocanchas(); 
	}
}
