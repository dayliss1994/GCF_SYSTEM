package ec.edu.upse.gcf.editar;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.CategoriaDAO;
import ec.edu.upse.gcf.listas.CategoriaLista;
import ec.edu.upse.gcf.modelo.Categoria;

@SuppressWarnings("serial")
public class CategoriaEditar extends SelectorComposer<Component> {
	// Enlaza a la ventana para poderla cerrar
	@Wire
	private Window winCategoriaEditar;

	// Contiene la ventana padre para invocar a la actualizacion de la misma cuando 
	// se cierra esta ventana.
	private CategoriaLista categoriaLista; 

	// Instancia el objeto para acceso a datos.
	private CategoriaDAO categoriaDao = new CategoriaDAO();
		// Objeto que contiene la persona con la que se esta trabajando
	private Categoria categoria;

	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);

		// Recupera la ventana padre.
		categoriaLista = (CategoriaLista)Executions.getCurrent().getArg().get("VentanaPadre");

		// Recupera el objeto pasado como parametro. Si no lo recibe, crea uno
		categoria = null; 
		if (Executions.getCurrent().getArg().get("Categoria") != null) {
			categoria = (Categoria) Executions.getCurrent().getArg().get("Categoria");
		}else{
			categoria = new Categoria(); 
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
			categoriaDao.getEntityManager().getTransaction().begin();
			
			if (categoria.getIdCategoria() == null) {
				categoriaDao.getEntityManager().persist(categoria);
			}else{
				categoria = (Categoria) categoriaDao.getEntityManager().merge(categoria);
			}
			
			// Cierra la transaccion.
			categoriaDao.getEntityManager().getTransaction().commit();

			
			Clients.showNotification("Proceso Ejecutado con exito.");

			categoriaLista.buscar();
			salir();
		} catch (Exception e) {
			e.printStackTrace();

			// Si hay error, reversa la transaccion.
			categoriaDao.getEntityManager().getTransaction().rollback();
		}
	}

	
	@Listen("onClick=#salir")
	public void salir(){
		winCategoriaEditar.detach();
	}
	
	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
}