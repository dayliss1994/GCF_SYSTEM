package ec.edu.upse.gcf.editar;

import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.PerfilDAO;
import ec.edu.upse.gcf.dao.UsuarioDao;
import ec.edu.upse.gcf.dao.UsuarioPerfilDao;
import ec.edu.upse.gcf.modelo.Perfil;
import ec.edu.upse.gcf.modelo.Usuario;
import ec.edu.upse.gcf.modelo.Usuarioperfil;

public class UsuarioPerfilEditar {

		@Wire
		private Window winUsuarioPerfilEditar;
		
		// Instancia el objeto para acceso a datos.
		private UsuarioPerfilDao usuarioperfilDao = new UsuarioPerfilDao();
		private PerfilDAO perfilDao = new PerfilDAO();
		private UsuarioDao usuarioDao = new UsuarioDao();
		private Usuarioperfil usuarioperfil;
		
		@AfterCompose
		public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {

			Selectors.wireComponents(view, this, false);

		    // Recupera el objeto pasado como parametro. 
			usuarioperfil = (Usuarioperfil) Executions.getCurrent().getArg().get("Usuarioperfil");	
		}

		/** Graba la informacion.*/
		@Command
		public void grabar(){					
			try {
				// Inicia la transaccion
				usuarioperfilDao.getEntityManager().getTransaction().begin();
				if (usuarioperfil.getIdUsuPerfil() == 0) {
					usuarioperfilDao.getEntityManager().persist(usuarioperfil);
				}else{
					usuarioperfil = (Usuarioperfil) usuarioperfilDao.getEntityManager().merge(usuarioperfil);
				}				
				usuarioperfilDao.getEntityManager().getTransaction().commit();				
				Clients.showNotification("Proceso Ejecutado con exito.");			
				BindUtils.postGlobalCommand(null, null, "UsuarioPerfilLista.buscar", null);
				salir();				
			} catch (Exception e) {
				e.printStackTrace();
				usuarioperfilDao.getEntityManager().getTransaction().rollback();
			}			
		}
		
		@Command
		public void salir(){
			winUsuarioPerfilEditar.detach();
		}			
		public List<Usuario> getUsuarios() {
			return usuarioDao.getUsuario();
		}
		public List<Perfil> getPerfiles() {
			return perfilDao.getPerfil();
		}
		public Usuarioperfil getUsuarioperfil() {
			return usuarioperfil;
		}
		public void setUsuarioperfil(Usuarioperfil usuarioperfil) {
			this.usuarioperfil = usuarioperfil;
		}				
}
