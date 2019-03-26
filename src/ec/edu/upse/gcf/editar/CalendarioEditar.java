package ec.edu.upse.gcf.editar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Calendar;

import ec.edu.upse.gcf.dao.CampeonatoDAO;
import ec.edu.upse.gcf.dao.DetalleCampeonatoDAO;
//import ec.edu.upse.gcf.dao.JornadaDAO;
import ec.edu.upse.gcf.dao.LugarPartidoDAO;
import ec.edu.upse.gcf.dao.ModalidadDAO;
import ec.edu.upse.gcf.modelo.Calendario;
import ec.edu.upse.gcf.modelo.Campeonato;
import ec.edu.upse.gcf.modelo.Detallecampeonato;
import ec.edu.upse.gcf.modelo.Lugarpartido;
import ec.edu.upse.gcf.modelo.Modalidad;

public class CalendarioEditar {

	//private JornadaDAO jornadaDAO = new JornadaDAO(); 
	private LugarPartidoDAO lugarEquipoDAO = new LugarPartidoDAO();
	private CampeonatoDAO campeonatoDAO = new CampeonatoDAO();
	private DetalleCampeonatoDAO detalleCampeonatoDAO = new DetalleCampeonatoDAO();
	@SuppressWarnings("unused")
	private ModalidadDAO modalidadDAO = new ModalidadDAO();

	private Calendario calendario = new Calendario(); 
	private List<Detallecampeonato> detalleCampeonato = new ArrayList<Detallecampeonato>();
	private List<Integer> numeroGenerado = new ArrayList<>();
	private List<Integer> auxNumeroGenerado = new ArrayList<>();
	private Campeonato campeonatoSeleccionado;
	//private Jornada jornadaSeleccionada;
	private Lugarpartido lugarSeleccionado;
	private Modalidad modalidadSeleccionada;
	private List<String> listaEquipoLocal = new ArrayList<>();
	private List<String> listaEquipoVisitante = new ArrayList<>();


	@Wire
	private Calendar calendars;

	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException{
		Selectors.wireComponents(view, this, false);
	}

	@Command
	@NotifyChange({"detalleCampeonato"})
	public void listarCategoriaAsignada() {
		detalleCampeonato = detalleCampeonatoDAO.getEquiposPorCategoria(campeonatoSeleccionado);
	}

	public List<Modalidad> getListaModalidad(){
		List<Modalidad> lm = new ArrayList<Modalidad>();
		Modalidad modalidad = new Modalidad();
		modalidad.setDescripcion("Por grupo");
		lm.add(modalidad);

		Modalidad mod = new Modalidad();
		mod.setDescripcion("Todo contra todo");
		lm.add(mod);
		return lm;
	}

	/**@Command
	//@NotifyChange({"detalleCampeonato","numeroGenerado"})
	public void generar() {
		if (campeonatoSeleccionado == null) {
			Clients.showNotification("Seleccione el campeonato.");
			return;

		}

		if (lugarSeleccionado == null) {
			Clients.showNotification("Seleccione el lugar.");
			return;
		}

		if (modalidadSeleccionada == null) {
			Clients.showNotification("Seleccione la modalidad.");
			return;
		}

		if(detalleCampeonato.isEmpty()) {
			Clients.showNotification("No se puede generar porque no hay equipos");
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Calendario", new Calendario());
		Window ventanaCargar = (Window) Executions.createComponents("/Mantenimiento/calendarios/calendario.zul", null, params);
		ventanaCargar.doModal();		
	}*/

	@Command
	@NotifyChange({"detalleCampeonato","numeroGenerado"})
	public void Listagrupos() {
		@SuppressWarnings("unused")
		List<Calendario> ca = new ArrayList<Calendario>();
		for (@SuppressWarnings("unused") Detallecampeonato d : detalleCampeonato) {
			Integer numero = generarNumeroAleatorio();
			if (numero != 0) {
				numeroGenerado.add(numero); 
			}									
		} 
		for (Integer valor : numeroGenerado) {
			for (int i = 0; i < detalleCampeonato.size(); i=i+2) {
				if(valor == i+1) { 
					System.out.println(" equipo local : "+detalleCampeonato.get(i).getEquipo().getNombre());
					listaEquipoLocal.add(detalleCampeonato.get(i).getEquipo().getNombre());					
					break;
				}					
			}				
		}

		for (Integer valor : numeroGenerado) {
			for (int i = 1; i < detalleCampeonato.size(); i=i+2) {
				if(valor == i+1) { 
					System.out.println(" equipo visitante : "+detalleCampeonato.get(i).getEquipo().getNombre());
					listaEquipoVisitante.add(detalleCampeonato.get(i).getEquipo().getNombre());
					break;
				}					
			}

		}	

	}

	@Command
	@NotifyChange({"detalleCampeonato","numeroGenerado"})
	public void generar() {
		if (campeonatoSeleccionado == null) {
			Clients.showNotification("Seleccione el campeonato.");
			return;

		}

		if (lugarSeleccionado == null) {
			Clients.showNotification("Seleccione el lugar.");
			return;
		}

		if (modalidadSeleccionada == null) {
			Clients.showNotification("Seleccione la modalidad.");
			return;
		}

		if(detalleCampeonato.isEmpty()) {
			Clients.showNotification("No se puede generar porque no hay equipos");
			return;
		} else {			
			for (@SuppressWarnings("unused") Detallecampeonato d : detalleCampeonato) {
				Integer numero = generarNumeroAleatorio();
				if (numero != 0) {
					numeroGenerado.add(numero); 
				}									
			} 
			for (Integer valor : numeroGenerado) {
				for (int i = 0; i < detalleCampeonato.size(); i=i+2) {
					if(valor == i+1) { 
						System.out.println(" equipo local : "+detalleCampeonato.get(i).getEquipo().getNombre());
						listaEquipoLocal.add(detalleCampeonato.get(i).getEquipo().getNombre());
						break;
					}					
				}				
			}

			for (Integer valor : numeroGenerado) {
				for (int i = 1; i < detalleCampeonato.size(); i=i+2) {
					if(valor == i+1) { 
						System.out.println(" equipo visitante : "+detalleCampeonato.get(i).getEquipo().getNombre());
						listaEquipoVisitante.add(detalleCampeonato.get(i).getEquipo().getNombre());
						break;
					}					
				}

			} 	 		

		}

	}

	public Integer generarNumeroAleatorio() {
		Integer resultado;
		int retorna = 1; 
		try {
			do {
				retorna = 1;
				Random r = new Random();
				resultado = r.nextInt(detalleCampeonato.size())+1;
				for (Integer valor : numeroGenerado) {
					if(resultado == valor) {
						retorna = 2;
						break; 
					}
				}			

			} while (retorna == 2);


		} catch (Exception e) {
			resultado = 0;
			// TODO: handle exception
		}
		return resultado;
	}

	public List<Campeonato> getListaCampeonato(){
		return campeonatoDAO.getListaCampeonato();
	}
	//public List<Jornada> getListaJornada(){
		//return jornadaDAO.getEquipos();
	//}

	public List<Lugarpartido> getLugarEquipo() {
		return lugarEquipoDAO.getLugarEquipo();
	}

	public Calendario getCalendario() {
		return calendario;
	}

	public void setCalendario(Calendario calendario) {
		this.calendario = calendario;
	}

	public Calendar getCalendars() {
		return calendars;
	}

	public void setCalendars(Calendar calendars) {
		this.calendars = calendars;
	}

	public List<Detallecampeonato> getDetalleCampeonato() {
		return detalleCampeonato;
	}

	public void setDetalleCampeonato(List<Detallecampeonato> detalleCampeonato) {
		this.detalleCampeonato = detalleCampeonato;
	}

	public Campeonato getCampeonatoSeleccionado() {
		return campeonatoSeleccionado;
	}

	public void setCampeonatoSeleccionado(Campeonato campeonatoSeleccionado) {
		this.campeonatoSeleccionado = campeonatoSeleccionado;
	}

	/**public Jornada getJornadaSeleccionada() {
		return jornadaSeleccionada;
	}

	public void setJornadaSeleccionada(Jornada jornadaSeleccionada) {
		this.jornadaSeleccionada = jornadaSeleccionada;
	}*/

	public Lugarpartido getLugarSeleccionado() {
		return lugarSeleccionado;
	}

	public void setLugarSeleccionado(Lugarpartido lugarSeleccionado) {
		this.lugarSeleccionado = lugarSeleccionado;
	}

	public Modalidad getModalidadSeleccionada() {
		return modalidadSeleccionada;
	}

	public void setModalidadSeleccionada(Modalidad modalidadSeleccionada) {
		this.modalidadSeleccionada = modalidadSeleccionada;
	}

	public List<Integer> getNumeroGenerado() {
		return numeroGenerado;
	}

	public void setNumeroGenerado(List<Integer> numeroGenerado) {
		this.numeroGenerado = numeroGenerado;
	}

	public List<Integer> getAuxNumeroGenerado() {
		return auxNumeroGenerado;
	}

	public void setAuxNumeroGenerado(List<Integer> auxNumeroGenerado) {
		this.auxNumeroGenerado = auxNumeroGenerado;
	}


}
