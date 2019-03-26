package ec.edu.upse.gcf.editar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.CampeonatoDAO;
import ec.edu.upse.gcf.dao.CategoriaDAO;
import ec.edu.upse.gcf.dao.DetalleCampeonatoDAO;
import ec.edu.upse.gcf.dao.EquipoDAO;
import ec.edu.upse.gcf.modelo.Campeonato;
import ec.edu.upse.gcf.modelo.Categoria;
import ec.edu.upse.gcf.modelo.Detallecampeonato;
import ec.edu.upse.gcf.modelo.Equipo;

@SuppressWarnings("unused")
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class DetalleCampeonatoEditar {
	
	@Wire
	private Window winDetalleCampeonatoEditar;
	
	@Wire Listbox listBox;
	
	@Wire
	private Combobox cboNombreC;
	
	@Wire
	private Combobox cboCategoria;
	
	@Wire
	private Combobox cboEquipo;

	private DetalleCampeonatoDAO detallecampeonatoDao = new DetalleCampeonatoDAO();
	private CampeonatoDAO campeonatoDao = new CampeonatoDAO();
	private CategoriaDAO categoriaDao = new CategoriaDAO();
	private EquipoDAO equipoDao = new EquipoDAO();
		
	private Detallecampeonato detallecampeonato;

	private Detallecampeonato detallecampeonatoSeleccionado;
	private Equipo equipoSeleccionado;
	private ArrayList<Equipo> listaEquipos = new ArrayList<Equipo>();
	

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {

		// Permite enlazar los componentes que se asocian con la anotacion @Wire
		Selectors.wireComponents(view, this, false);
		
		detallecampeonato = (Detallecampeonato) Executions.getCurrent().getArg().get("Detallecampeonato");
		//campeonato = (Campeonato) Executions.getCurrent().getArg().get("Campeonato");
		//categoria = (Categoria) Executions.getCurrent().getArg().get("Categoria");
		//equipo = (Equipo) Executions.getCurrent().getArg().get("Equipo");}
		
		
	}
	
	public boolean isValidarDatos() {
		try {
			Boolean resultado = true;	
			if(cboNombreC.getModel() == null) {
				Clients.showNotification("Por favor verifique que los campos con * esten llenos.!!");
				cboNombreC.focus();
				return resultado;
			}
			if(cboCategoria.getModel() == null) {
				Clients.showNotification("Por favor verifique que los campos con * esten llenos.!!");
				cboCategoria.focus();
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
			collectSelectedItems();
			/*if (isValidarDatos() == false) {
				Clients.showNotification("Por favor verifique que los campos con * esten llenos.!!");
			}else {*/
				// Inicia la transaccion				
				detallecampeonatoDao.getEntityManager().getTransaction().begin();
				for(Equipo equip : listaEquipos) {
					Detallecampeonato detClon = (Detallecampeonato) detallecampeonato.clone();
					if (detClon.getId_detalleC() == null) {						
						detClon.setEquipo(equip);
						detallecampeonatoDao.getEntityManager().persist(detClon);
					}else{
						detallecampeonato = (Detallecampeonato) detallecampeonatoDao.getEntityManager().merge(detClon);
					}
				}
				// Cierra la transaccion.
				detallecampeonatoDao.getEntityManager().getTransaction().commit();

				Clients.showNotification("Proceso Ejecutado con exito.");			
				// Actualiza la lista
				BindUtils.postGlobalCommand(null, null, "Detallecampeonato.buscarPorPatron", null);
				
				salir();
			//}
		} catch (Exception e) {
			e.printStackTrace();

			// Si hay error, reversa la transaccion.
			detallecampeonatoDao.getEntityManager().getTransaction().rollback();
		}		

	}	
	
	public void collectSelectedItems() {
		listaEquipos.clear();
		Iterator<Listitem> iterator = listBox.getSelectedItems().iterator();
		while (iterator.hasNext()) {
			Listitem listitem = (Listitem) iterator.next();
			listaEquipos.add(listitem.getValue());
		}
	}


	@Command
	public void salir(){
		winDetalleCampeonatoEditar.detach();
	}
	
	public List<Campeonato> getCampeonatos() {
		return campeonatoDao.getListaCampeonato();
	}
	
	public List<Categoria> getCategoria() {
		return categoriaDao.getCategorias(null);
	}
	
	public List<Equipo> getEquipos() {
		System.out.println(detallecampeonato.getCampeonato());
		return equipoDao.getEquipos(null);
	}

	public Detallecampeonato getDetallecampeonato() {
		return detallecampeonato;
	}

	public void setDetallecampeonato(Detallecampeonato detallecampeonato) {
		this.detallecampeonato = detallecampeonato;
	}

	public Detallecampeonato getDetallecampeonatoSeleccionado() {
		return detallecampeonatoSeleccionado;
	}

	public void setDetallecampeonatoSeleccionado(Detallecampeonato detallecampeonatoSeleccionado) {
		this.detallecampeonatoSeleccionado = detallecampeonatoSeleccionado;
	}

	public Equipo getEquipoSeleccionado() {
		return equipoSeleccionado;
	}

	public void setEquipoSeleccionado(Equipo equipoSeleccionado) {
		this.equipoSeleccionado = equipoSeleccionado;
	}

}