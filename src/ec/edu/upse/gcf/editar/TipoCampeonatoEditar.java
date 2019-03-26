package ec.edu.upse.gcf.editar;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.TipocampeonatoDAO;
import ec.edu.upse.gcf.listas.TipoCampeonatoLista;
import ec.edu.upse.gcf.modelo.Tipocampeonato;

@SuppressWarnings("serial")
public class TipoCampeonatoEditar extends SelectorComposer<Component> {
	// Enlaza a la ventana para poderla cerrar
	@Wire
	private Window winTipoCampeonatoEditar;

	private TipoCampeonatoLista tipoCampeonatoLista; 
	private TipocampeonatoDAO tipoCampeonatoDao = new TipocampeonatoDAO();	
	private Tipocampeonato tipoCampeonato;

	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);

		// Recupera la ventana padre.
		tipoCampeonatoLista = (TipoCampeonatoLista)Executions.getCurrent().getArg().get("VentanaPadre");

		// Recupera el objeto pasado como parametro. Si no lo recibe, crea uno
		tipoCampeonato = null; 
		if (Executions.getCurrent().getArg().get("TipoCampeonato") != null) {
			tipoCampeonato = (Tipocampeonato) Executions.getCurrent().getArg().get("TipoCampeonato");
		}else{
			tipoCampeonato = new Tipocampeonato(); 
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
			tipoCampeonatoDao.getEntityManager().getTransaction().begin();

			// Almacena los datos.
			// Si es nuevo utiliza el metodo "persist" de lo contrario usa el metodo "merge"
			if (tipoCampeonato.getIdTipocamp() == null) {
				tipoCampeonatoDao.getEntityManager().persist(tipoCampeonato);
			}else{
				tipoCampeonato = (Tipocampeonato) tipoCampeonatoDao.getEntityManager().merge(tipoCampeonato);
			}

			// Cierra la transaccion.
			tipoCampeonatoDao.getEntityManager().getTransaction().commit();

			
			Clients.showNotification("Proceso Ejecutado con exito.");

			// Actualiza la lista
			tipoCampeonatoLista.buscar();
			// Cierra la ventana
			salir();

		} catch (Exception e) {
			e.printStackTrace();

			// Si hay error, reversa la transaccion.
			tipoCampeonatoDao.getEntityManager().getTransaction().rollback();
		}
	}

	/**
	 * Escucha el evento "onClick" del objeto "salir"
	 */
	@Listen("onClick=#salir")
	public void salir(){
		winTipoCampeonatoEditar.detach();
	}

	/**
	 * Retorna una lista de paises, se deberia recuperar de una tabla.
	 * @return
	 */

	public Tipocampeonato getTipoCampeonato() {
		return tipoCampeonato;
	}


	public void setTipoCampeonato(Tipocampeonato tipoCampeonato) {
		this.tipoCampeonato = tipoCampeonato;
	}
}