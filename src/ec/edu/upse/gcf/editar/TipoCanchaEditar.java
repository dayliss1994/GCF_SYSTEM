package ec.edu.upse.gcf.editar;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.TipocanchaDAO;
import ec.edu.upse.gcf.listas.TipoCanchaLista;
import ec.edu.upse.gcf.modelo.Tipocancha;


@SuppressWarnings("serial")
public class TipoCanchaEditar extends SelectorComposer<Component> {
	// Enlaza a la ventana para poderla cerrar
	@Wire
	private Window winTipoCanchaEditar;
	@Wire private Textbox descripcion;

	private TipoCanchaLista tipoCanchaLista; 
	private TipocanchaDAO tipoCanchaDao = new TipocanchaDAO();
	private Tipocancha tipoCancha;

	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);
		tipoCanchaLista = (TipoCanchaLista)Executions.getCurrent().getArg().get("VentanaPadre");
		tipoCancha = null; 
		if (Executions.getCurrent().getArg().get("TipoCancha") != null) {
			tipoCancha = (Tipocancha) Executions.getCurrent().getArg().get("TipoCancha");
		}else{
			tipoCancha = new Tipocancha(); 
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
				tipoCanchaDao.getEntityManager().getTransaction().begin();
				if (tipoCancha.getId_tipoC() == null) {
					tipoCanchaDao.getEntityManager().persist(tipoCancha);
				}else{
					tipoCancha = (Tipocancha) tipoCanchaDao.getEntityManager().merge(tipoCancha);
				}
				tipoCanchaDao.getEntityManager().getTransaction().commit();
				Clients.showNotification("Proceso Ejecutado con exito.");
				tipoCanchaLista.buscar();
				salir();
			}
		} catch (Exception e) {
			e.printStackTrace();
			tipoCanchaDao.getEntityManager().getTransaction().rollback();
		}
	}

	@Listen("onClick=#salir")
	public void salir(){
		winTipoCanchaEditar.detach();
	}
	public Tipocancha getTipoCancha() {
		return tipoCancha;
	}
	public void setTipoCancha(Tipocancha tipoCancha) {
		this.tipoCancha = tipoCancha;
	}
}