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
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.CampeonatoDAO;
import ec.edu.upse.gcf.modelo.Campeonato;
import ec.edu.upse.gcf.util.PrintReport;

@SuppressWarnings({ "unchecked" })
public class fffff{
	@Wire
	private String textoBuscar;

	private CampeonatoDAO campeonatoDao = new CampeonatoDAO();

	private List<Campeonato> campeonatos;
	private Campeonato campeonatoSeleccionado;
	
	
	@GlobalCommand("Campeonatos.buscarPorPatron")
	@Command
	@NotifyChange({"campeonatos", "campeonatoSeleccionado"})
	public void buscar(){

		if (campeonatos != null) {
			campeonatos = null; 
		}

		campeonatos = campeonatoDao.getCampeonatos(textoBuscar);

		// Limpia os objetos de trabajo
		campeonatoSeleccionado = null; 
		campeonatoSeleccionado = null; 
	}	
	
	/**
	 * Crea una persona
	*/
	@Command
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Campeonato", new Campeonato());
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/campeonatos/campeonatoEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	
	/**
	 * Edita datos
	*/
	@Command
	public void editar(){
		if (campeonatoSeleccionado == null) {
			Clients.showNotification("Debe seleccionar un campeonato de la lista.");
			return; 
		}

		// Actualiza la instancia antes de enviarla a editar.
		campeonatoDao.getEntityManager().refresh(campeonatoSeleccionado);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Campeonato", campeonatoSeleccionado);
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/campeonatos/campeonatoEditar.zul", null, params);
		ventanaCargar.doModal();
	}
	
	@Command
	public void verReporte(){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("nombreCampeonato", "Campeonato 2018");
		PrintReport pr = new PrintReport();
		pr.crearReporte("/Reportes/prueba.jasper", campeonatoDao, param);
		pr.showReport("Reporte de caja resumido");
	}
	

	@SuppressWarnings("rawtypes")
	@Command
	public void eliminar(){

		if (campeonatoSeleccionado == null) {
			Clients.showNotification("Debe seleccionar un campeonato de la lista.");
			return; 
		}

		Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						campeonatoDao.getEntityManager().getTransaction().begin();
						campeonatoSeleccionado.setEstado("1");
						campeonatoSeleccionado = campeonatoDao.getEntityManager().merge(campeonatoSeleccionado);
						campeonatoDao.getEntityManager().getTransaction().commit();;
						Clients.showNotification("Transaccion ejecutada con exito.");

						// Actualiza la lista
						BindUtils.postGlobalCommand(null, null, "CampeonatoLista.buscar", null);

					} catch (Exception e) {
						e.printStackTrace();
						campeonatoDao.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});

	}

	//Getter and Setter
	
	public String getTextoBuscar() {
		return textoBuscar;
	}

	public void setTextoBuscar(String textoBuscar) {
		this.textoBuscar = textoBuscar;
	}

	public List<Campeonato> getCampeonatos() {
		return campeonatos;
	}

	public void setCampeonatos(List<Campeonato> campeonatos) {
		this.campeonatos = campeonatos;
	}
	public Campeonato getCampeonatoSeleccionado() {
		return campeonatoSeleccionado;
	}

	public void setCampeonatoSeleccionado(Campeonato campeonatoSeleccionado) {
		this.campeonatoSeleccionado = campeonatoSeleccionado;
	}
}
