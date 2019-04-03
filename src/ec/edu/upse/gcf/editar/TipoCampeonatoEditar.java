package ec.edu.upse.gcf.editar;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.TipocampeonatoDAO;
import ec.edu.upse.gcf.listas.TipoCampeonatoLista;
import ec.edu.upse.gcf.modelo.Tipocampeonato;

@SuppressWarnings("serial")
public class TipoCampeonatoEditar extends SelectorComposer<Component> {
	// Enlaza a la ventana para poderla cerrar
	@Wire
	private Window winTipoCampeonatoEditar;
	
	@Wire private Textbox descripcion;

	private TipoCampeonatoLista tipoCampeonatoLista; 
	private TipocampeonatoDAO tipoCampeonatoDao = new TipocampeonatoDAO();	
	private Tipocampeonato tipoCampeonato;

	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);
		tipoCampeonatoLista = (TipoCampeonatoLista)Executions.getCurrent().getArg().get("VentanaPadre");
		tipoCampeonato = null; 
		if (Executions.getCurrent().getArg().get("TipoCampeonato") != null) {
			tipoCampeonato = (Tipocampeonato) Executions.getCurrent().getArg().get("TipoCampeonato");
		}else{
			tipoCampeonato = new Tipocampeonato(); 
		}
	}

	public boolean isValidarDatos() {
		try {
			Boolean resultado = true;			
			if(descripcion.getText() == null) {
				Clients.showNotification("Por favor ingrese la descripción.!!");
				descripcion.focus();
				return resultado;
			}	
			return resultado;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Listen("onClick=#grabar")
	public void grabar(){		
		try {
			if (isValidarDatos() == true) {
				tipoCampeonatoDao.getEntityManager().getTransaction().begin();

				if (tipoCampeonato.getIdTipocamp() == null) {
					tipoCampeonatoDao.getEntityManager().persist(tipoCampeonato);
				}else{
					tipoCampeonato = (Tipocampeonato) tipoCampeonatoDao.getEntityManager().merge(tipoCampeonato);
				}
				tipoCampeonatoDao.getEntityManager().getTransaction().commit();
				Clients.showNotification("Proceso Ejecutado con exito.");
				tipoCampeonatoLista.buscar();
				salir();
			}else {
				Clients.showNotification("Verifique que los campos esten llenos.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tipoCampeonatoDao.getEntityManager().getTransaction().rollback();
		}
	}

	@Listen("onClick=#salir")
	public void salir(){
		winTipoCampeonatoEditar.detach();
	}
	public Tipocampeonato getTipoCampeonato() {
		return tipoCampeonato;
	}
	public void setTipoCampeonato(Tipocampeonato tipoCampeonato) {
		this.tipoCampeonato = tipoCampeonato;
	}
}