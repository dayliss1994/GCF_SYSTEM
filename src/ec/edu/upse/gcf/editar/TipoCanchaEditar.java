package ec.edu.upse.gcf.editar;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.TipocanchaDAO;
import ec.edu.upse.gcf.listas.TipoCanchaLista;
import ec.edu.upse.gcf.modelo.Tipocancha;


@SuppressWarnings("serial")
public class TipoCanchaEditar extends SelectorComposer<Component> {
	// Enlaza a la ventana para poderla cerrar
	@Wire
	private Window winTipoCanchaEditar;

	private TipoCanchaLista tipoCanchaLista; 
	private TipocanchaDAO tipoCanchaDao = new TipocanchaDAO();
	private Tipocancha tipoCancha;

	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);

		// Recupera la ventana padre.
		tipoCanchaLista = (TipoCanchaLista)Executions.getCurrent().getArg().get("VentanaPadre");

		// Recupera el objeto pasado como parametro. Si no lo recibe, crea uno
		tipoCancha = null; 
		if (Executions.getCurrent().getArg().get("TipoCancha") != null) {
			tipoCancha = (Tipocancha) Executions.getCurrent().getArg().get("TipoCancha");
		}else{
			tipoCancha = new Tipocancha(); 
		}
	}

	/**
	 * Escucha el evento "onClick" del objeto "grabar"
	 */
	@Listen("onClick=#grabar")
	public void grabar(){
		System.out.println("entra grabando");
		try {
			// Inicia la transaccion
			tipoCanchaDao.getEntityManager().getTransaction().begin();

			// Almacena los datos.
			// Si es nuevo utiliza el metodo "persist" de lo contrario usa el metodo "merge"
			if (tipoCancha.getId_tipoC() == null) {
				tipoCanchaDao.getEntityManager().persist(tipoCancha);
			}else{
				tipoCancha = (Tipocancha) tipoCanchaDao.getEntityManager().merge(tipoCancha);
			}

			// Cierra la transaccion.
			tipoCanchaDao.getEntityManager().getTransaction().commit();

			
			Clients.showNotification("Proceso Ejecutado con exito.");

			// Actualiza la lista
			tipoCanchaLista.buscar();
			// Cierra la ventana
			salir();

		} catch (Exception e) {
			e.printStackTrace();

			// Si hay error, reversa la transaccion.
			tipoCanchaDao.getEntityManager().getTransaction().rollback();
		}
	}

	/**
	 * Escucha el evento "onClick" del objeto "salir"
	 */
	@Listen("onClick=#salir")
	public void salir(){
		winTipoCanchaEditar.detach();
	}

	/**
	 * GETTER AND SETTER
	 * @return
	 */

	public Tipocancha getTipoCancha() {
		return tipoCancha;
	}


	public void setTipoCancha(Tipocancha tipoCancha) {
		this.tipoCancha = tipoCancha;
	}
}