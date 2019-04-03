
package ec.edu.upse.gcf.editar;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import ec.edu.upse.gcf.dao.InstitucionDAO;
import ec.edu.upse.gcf.modelo.Institucion;
import ec.edu.upse.gcf.util.FileUtil;

public class InstitucionEditar {

	@Wire
	private Window winInstitucionEditar;

	@Wire
	private Textbox nombre;	

	@Wire
	private Textbox fundacion;

	@Wire
	private Textbox afiliada;

	@Wire
	private Textbox direccion;

	@Wire
	private Textbox telefono;

	@Wire
	private Textbox correo;
	
	@Wire
	private Textbox lugar;

	// Instancia el objeto para acceso a datos.
	private InstitucionDAO institucionDao = new InstitucionDAO(); 
	
	private Institucion institucion;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {

		// Permite enlazar los componentes que se asocian con la anotacion @Wire
		Selectors.wireComponents(view, this, false);
		institucion = (Institucion) Executions.getCurrent().getArg().get("Institucion");		
	}

	/** Método para validar email*/
	boolean ValidarEmail (String correo) {
		Pattern pat = null;
		Matcher mat = null;        
		pat = Pattern.compile("^([0-9a-zA-Z]([_.w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-w]*[0-9a-zA-Z].)+([a-zA-Z]{2,9}.)+[a-zA-Z]{2,3})$");
		mat = pat.matcher(correo);
		if (mat.find()) {
			return true;
		}else{
			return false;
		}        
	}
	public boolean isValidarDatos() {
		try {
			Boolean resultado = true;			
			if(nombre.getText() == null) {
				Clients.showNotification("Por favor ingrese el nombre de la institución.!!");
				nombre.focus();
				return resultado;
			}
			if(fundacion.getText() == null) {
				Clients.showNotification("Por favor ingrese la fecha de fundación.!!");
				fundacion.focus();
				return resultado;
			}
			if(afiliada.getText() == null) {
				Clients.showNotification("Por favor ingrese afiliación.!!");
				afiliada.focus();
				return resultado;
			}
			if(direccion.getText() == null) {
				Clients.showNotification("Por favor ingrese la dirección.!!");
				direccion.focus();
				return resultado;
			}
			if(telefono.getText() == null) {
				Clients.showNotification("Por favor ingrese un dato telefónico.!!");
				telefono.focus();
				return resultado;
			}		
			if(correo.getText() == null) {
				Clients.showNotification("Por favor ingrese el correo.!!");
				correo.focus();
				return resultado;
			}
			if(ValidarEmail(correo.getText()) == false) {
				Clients.showNotification("El correo ingresado no es válido!!");
				correo.focus();
				resultado = false;
				return resultado;
			}
			if(lugar.getText() == null) {
				Clients.showNotification("Por favor ingrese el lugar de ubicación.!!");
				lugar.focus();
				return resultado;
			}
			return resultado;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Command
	public void grabar (@ContextParam(ContextType.VIEW) Component view){
		try {
			System.out.println("entra");
			if (isValidarDatos() == true) {
				institucionDao.getEntityManager().getTransaction().begin();
				if (institucion.getIdInstitucion() == 0) {
					institucionDao.getEntityManager().persist(institucion);
				}else{
					institucion = (Institucion) institucionDao.getEntityManager().merge(institucion);
				}
				institucionDao.getEntityManager().getTransaction().commit();
				Clients.showNotification("Proceso Ejecutado con exito.");			
				BindUtils.postGlobalCommand(null, null, "Institucion.buscarPorPatron", null);
				salir();
			}else {
				Clients.showNotification("Verifique que los campos esten llenos.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			institucionDao.getEntityManager().getTransaction().rollback();
		}		

	}

	@Command
	@NotifyChange("imagen")
	public void subir(@ContextParam(ContextType.TRIGGER_EVENT) UploadEvent evento){
		String imagen = FileUtil.loadFile(evento.getMedia());
		if (imagen != null) {
			institucion.setImg(imagen);
		}

	}

	public AImage getImagen() {
		AImage retorno = null;
		if (institucion.getImg() != null) {
			try {
				retorno = FileUtil.getImagenTamanoFijo(new AImage(institucion.getImg()), 163, 163);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return retorno; 
	}

	@Command
	public void salir(){
		winInstitucionEditar.detach();
	}

	public Institucion getInstitucion() {
		return institucion;
	}

	public void setInstitucion(Institucion institucion) {
		this.institucion = institucion;
	}
	
}
