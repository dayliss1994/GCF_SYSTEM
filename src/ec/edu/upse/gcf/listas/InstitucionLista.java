package ec.edu.upse.gcf.listas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.InstitucionDAO;
import ec.edu.upse.gcf.modelo.Institucion;

@SuppressWarnings({ "unchecked", "rawtypes"})
public class InstitucionLista {

	// Atributos consumidos en el formulario
	public String textoBuscar;

	// Instancia el objeto para acceso a datos.
	private InstitucionDAO institucionDao = new InstitucionDAO();

	// Objeto que contiene el jugador
	private Institucion institucionSeleccionada;
	private List<Institucion> instituciones;

	@GlobalCommand("Institucion.buscarPorPatron")
	@Command
	@NotifyChange({"instituciones", "institucionSeleccionada"})
	public void buscar(){

		if (instituciones != null) {
			instituciones = null; 
		}

		instituciones = institucionDao.getInstituciones(textoBuscar);

		// Limpia os objetos de trabajo
		institucionSeleccionada = null; 
		institucionSeleccionada = null; 

	}

	/**
	 * Crea una persona
	 */
	@Command
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Institucion", new Institucion());
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/institucion/institucionEditar.zul", null, params);
		ventanaCargar.doModal();
	}

	/**
	 * Edita una persona
	 */
	@Command
	public void editar(){
		if (institucionSeleccionada == null) {
			Clients.showNotification("Debe seleccionar una opción de la lista.");
			return; 
		}

		// Actualiza la instancia antes de enviarla a editar.
		institucionDao.getEntityManager().refresh(institucionSeleccionada);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Institucion", institucionSeleccionada);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/institucion/institucionEditar.zul", null, params);
		ventanaCargar.doModal();

	}

	/**
	 * Borra "logicamente" un registro
	 */
	@Command
	public void eliminar(){

		if (institucionSeleccionada == null) {
			Clients.showNotification("Debe seleccionar una opción de la lista.");
			return; 
		}

		Messagebox.show("Desea eliminar el registro seleccionado?", "ConfirmaciÃ³n de EliminaciÃ³n", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						institucionDao.getEntityManager().getTransaction().begin();
						institucionSeleccionada.setEstado("1");
						institucionSeleccionada = institucionDao.getEntityManager().merge(institucionSeleccionada);
						institucionDao.getEntityManager().getTransaction().commit();;
						Clients.showNotification("Transaccion ejecutada con exito.");

						// Actualiza la lista
						BindUtils.postGlobalCommand(null, null, "Institucion.buscarPorPatron", null);

					} catch (Exception e) {
						e.printStackTrace();
						institucionDao.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});

	}

	public String getTextoBuscar() {
		return textoBuscar;
	}

	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}

	public List<Institucion> getInstituciones() {
		return instituciones;
	}

	public Institucion getInstitucionSeleccionada() {
		return institucionSeleccionada;
	}

	public void setInstitucionSeleccionada(Institucion institucionSeleccionada) {
		this.institucionSeleccionada = institucionSeleccionada;
	}


}
