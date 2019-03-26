package ec.edu.upse.gcf.editar;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.TarjetaDAO;
import ec.edu.upse.gcf.listas.TarjetaLista;
import ec.edu.upse.gcf.modelo.Tarjeta;


@SuppressWarnings("serial")
public class TarjetaEditar extends SelectorComposer<Component> {
	// Enlaza a la ventana para poderla cerrar
	@Wire
	private Window winTarjetaEditar;

	// Contiene la ventana padre para invocar a la actualizacion de la misma cuando 
	// se cierra esta ventana.
	private TarjetaLista tarjetaLista; 

	// Instancia el objeto para acceso a datos.
	private TarjetaDAO tarjetaDao = new TarjetaDAO();
		// Objeto que contiene la persona con la que se esta trabajando
	private Tarjeta tarjeta;

	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);

		// Recupera la ventana padre.
		tarjetaLista = (TarjetaLista)Executions.getCurrent().getArg().get("VentanaPadre");

		// Recupera el objeto pasado como parametro. Si no lo recibe, crea uno
		tarjeta = null; 
		if (Executions.getCurrent().getArg().get("Tarjeta") != null) {
			tarjeta = (Tarjeta) Executions.getCurrent().getArg().get("Tarjeta");
		}else{
			tarjeta = new Tarjeta(); 
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
			tarjetaDao.getEntityManager().getTransaction().begin();

			// Almacena los datos.
			// Si es nuevo utiliza el metodo "persist" de lo contrario usa el metodo "merge"
			if (tarjeta.getIdTarjeta() == null) {
				tarjetaDao.getEntityManager().persist(tarjeta);
			}else{
				tarjeta = (Tarjeta) tarjetaDao.getEntityManager().merge(tarjeta);
			}

			// Cierra la transaccion.
			tarjetaDao.getEntityManager().getTransaction().commit();

			
			Clients.showNotification("Proceso Ejecutado con exito.");

			// Actualiza la lista
			tarjetaLista.buscar();
			// Cierra la ventana
			salir();

		} catch (Exception e) {
			e.printStackTrace();

			// Si hay error, reversa la transaccion.
			tarjetaDao.getEntityManager().getTransaction().rollback();
		}
	}

	/**
	 * Escucha el evento "onClick" del objeto "salir"
	 */
	@Listen("onClick=#salir")
	public void salir(){
		winTarjetaEditar.detach();
	}

	/**
	 * Retorna una lista de paises, se deberia recuperar de una tabla.
	 * @return
	 */

	public Tarjeta getTarjeta() {
		return tarjeta;
	}


	public void setTarjeta(Tarjeta tarjeta) {
		this.tarjeta = tarjeta;
	}
}