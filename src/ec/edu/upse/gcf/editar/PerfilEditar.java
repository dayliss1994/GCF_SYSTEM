package ec.edu.upse.gcf.editar;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.PerfilDAO;
import ec.edu.upse.gcf.listas.PerfilLista;
import ec.edu.upse.gcf.modelo.Perfil;

@SuppressWarnings({ "serial", "rawtypes" })
public class PerfilEditar extends SelectorComposer{
	
	@Wire
	private Window winPerfilEditar;

	private PerfilLista perfilLista;
	private PerfilDAO perfilDao = new PerfilDAO();
	private Perfil perfil;

	@SuppressWarnings("unchecked")
	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);

		// Recupera la ventana padre.
		perfilLista = (PerfilLista)Executions.getCurrent().getArg().get("VentanaPadre");

		// Recupera el objeto pasado como parametro. Si no lo recibe, crea uno
		perfil = null; 
		if (Executions.getCurrent().getArg().get("Perfil") != null) {
			perfil = (Perfil) Executions.getCurrent().getArg().get("Perfil");
		}else{
			perfil = new Perfil(); 
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
			perfilDao.getEntityManager().getTransaction().begin();
			
			if (perfil.getIdPerfil() == null) {
				perfilDao.getEntityManager().persist(perfil);
			}else{
				perfil = (Perfil) perfilDao.getEntityManager().merge(perfil);
			}

			// Cierra la transaccion.
			perfilDao.getEntityManager().getTransaction().commit();
			
			Clients.showNotification("Proceso Ejecutado con exito.");
			
			perfilLista.buscar();			
			salir(); 			
		} catch (Exception e) {
			e.printStackTrace();

			// Si hay error, reversa la transaccion.
			perfilDao.getEntityManager().getTransaction().rollback();
		}
	}

	@Listen("onClick=#salir")
	public void salir(){
		winPerfilEditar.detach();
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
}