package ec.edu.upse.gcf.editar;

import java.io.IOException;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.CategoriaDAO;
import ec.edu.upse.gcf.dao.EquipoDAO;
import ec.edu.upse.gcf.modelo.Categoria;
import ec.edu.upse.gcf.modelo.Equipo;
import ec.edu.upse.gcf.util.FileUtil;

public class EquipoEditar {
	
	@Wire
	private Window winEquipoEditar;
	
	@Wire
	private Textbox nombre;

	private EquipoDAO equipoDao = new EquipoDAO();
	private CategoriaDAO categoriaDao = new CategoriaDAO();
	private Equipo equipo;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {

		// Permite enlazar los componentes que se asocian con la anotacion @Wire
		Selectors.wireComponents(view, this, false);

	    // Recupera el objeto pasado como parametro. 
		equipo = (Equipo) Executions.getCurrent().getArg().get("Equipo");	
	}

	public boolean isValidarDatos() {
		try {
			Boolean resultado = true;	
			if(nombre.getText() == null) {
				Clients.showNotification("Por favor ingrese nombres.!!");
				nombre.focus();
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
				equipoDao.getEntityManager().getTransaction().begin();
				// Almacena los datos.
				// Si es nuevo sa el metodo "persist" de lo contrario usa el metodo "merge"
				if (equipo.getIdEquipo() == 0) {
					equipoDao.getEntityManager().persist(equipo);
				}else{
					equipo = (Equipo) equipoDao.getEntityManager().merge(equipo);
				}
				
				// Cierra la transaccion.
				equipoDao.getEntityManager().getTransaction().commit();
				
				Clients.showNotification("Proceso Ejecutado con exito.");
				
				// Actualiza la lista
				BindUtils.postGlobalCommand(null, null, "EquipoLista.buscar", null);
				
				// Cierra la ventana
				salir();
			}
		} catch (Exception e) {
			e.printStackTrace();
			
			// Si hay error, reversa la transaccion.
			equipoDao.getEntityManager().getTransaction().rollback();
		}
		
	}
		
	@Command
	public void salir(){
		winEquipoEditar.detach();
	}
	
	public List<Categoria> getCategorias() {
		return categoriaDao.getListaCategorias();
	}
			
	@Command
	@NotifyChange("imagenEscudo")
	public void subir(@ContextParam(ContextType.TRIGGER_EVENT) UploadEvent evento){
	   String imagen = FileUtil.loadFile(evento.getMedia());
	   if (imagen != null) {
		   equipo.setEscudo(imagen);
	   }	   
	}
	
	public AImage getImagenEscudo() {
		AImage retorno = null;
		if (equipo.getEscudo() != null) {
			try {
				retorno = FileUtil.getImagenTamanoFijo(new AImage(equipo.getEscudo()), 163, 163);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return retorno; 
	}
	
	public Equipo getEquipo() {
		return equipo;
	}

	public void setEquipo(Equipo equipo) {
		this.equipo = equipo;
	}
}