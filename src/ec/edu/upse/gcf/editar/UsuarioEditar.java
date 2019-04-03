package ec.edu.upse.gcf.editar;

import java.security.MessageDigest;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.UsuarioDao;
import ec.edu.upse.gcf.dao.UsuarioPerfilDao;
import ec.edu.upse.gcf.listas.UsuarioLista;
import ec.edu.upse.gcf.modelo.Usuario;
import ec.edu.upse.gcf.security.SecurityUtil;

@SuppressWarnings({ "serial", "rawtypes" })
public class UsuarioEditar extends SelectorComposer{
	@Wire
	private Window winUsuarioEditar;

	@Wire
	private Textbox cedula;	

	@Wire
	private Textbox clave;	

	@Wire
	private Textbox claveNueva;	

	@Wire
	private Textbox confirmaClave;	

	@Wire
	private Textbox correo;

	@Wire
	private Textbox nombUsuario;

	@Wire
	private Textbox nombres;

	@Wire
	private Textbox apellidos;

	private UsuarioLista usuarioLista; 
	private UsuarioDao usuarioDao = new UsuarioDao();
	private Usuario usuario;
	//private Context context;
	//private Integer usu; 

	@SuppressWarnings("unchecked")
	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);

		//usu = Context.getInstance().getIdUsuarioLogeado(); //**

		//Recupera la ventana padre.
		usuarioLista = (UsuarioLista)Executions.getCurrent().getArg().get("VentanaPadre");

		// Recupera el objeto pasado como parametro. Si no lo recibe, crea uno
		usuario = null; 
		if (Executions.getCurrent().getArg().get("Usuario") != null) {
			usuario = (Usuario)Executions.getCurrent().getArg().get("Usuario");
		}else{
			usuario = new Usuario(); 
		}
	}

	/**Validar que claves sean iguales */
	@Listen("onClick=#validar")
	public void validarClavesIguales () {
		try {
			String a = claveNueva.getText();
			String b = confirmaClave.getText();
			if (a.equals(b)==false) {
				Clients.showNotification("Los datos ingresados no coinciden, por favor confirme nuevamente la clave");
			}else {				
				Clients.showNotification("Los datos ingresados coinciden");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/** Método para validar cédula*/
	public boolean validarDeCedula(String cedula) {
		boolean cedulaCorrecta = false;
		try {
			if (cedula.length() == 10) // ConstantesApp.LongitudCedula
			{
				int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
				if (tercerDigito < 6) {
					// Coeficientes de validación cédula
					// El decimo digito se lo considera dígito verificador
					int[] coefValCedula = { 2, 1, 2, 1, 2, 1, 2, 1, 2 };
					int verificador = Integer.parseInt(cedula.substring(9,10));
					int suma = 0;
					int digito = 0;
					for (int i = 0; i < (cedula.length() - 1); i++) {
						digito = Integer.parseInt(cedula.substring(i, i + 1))* coefValCedula[i];
						suma += ((digito % 10) + (digito / 10));
					}

					if ((suma % 10 == 0) && (suma % 10 == verificador)) {
						cedulaCorrecta = true;
					}
					else if ((10 - (suma % 10)) == verificador) {
						cedulaCorrecta = true;
					} else {
						cedulaCorrecta = false;
					}
				} else {
					cedulaCorrecta = false;
				}
			} else {
				cedulaCorrecta = false;
			}
		} catch (NumberFormatException nfe) {
			cedulaCorrecta = false;
		} catch (Exception err) {
			System.out.println("Una excepcion ocurrio en el proceso de validacion");
			cedulaCorrecta = false;
		}

		if (!cedulaCorrecta) {
			System.out.println("La Cédula ingresada es Incorrecta");
		}
		return cedulaCorrecta;
	}

	/**Funcion encriptar*/
	public String encriptar(String clave) throws Exception {

		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] b = md.digest(clave.getBytes());

		int size = b.length;
		StringBuilder h = new StringBuilder(size);
		for (int i = 0; i < size; i++) {

			int u = b[i] & 255;

			if (u < 16)
			{
				h.append("0").append(Integer.toHexString(u));
			}
			else
			{
				h.append(Integer.toHexString(u));
			}
		}
		return h.toString();
	}

	/** Método para validar email*/
	boolean ValidarEmail (String correo) {
		Pattern pat = null;
		Matcher mat = null;        
		pat = Pattern.compile("^([0-9a-zA-Z]([_.w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-w]*[0-9a-zA-Z].)+([a-zA-Z]{2,9}.)+[a-zA-Z]{2,3})$");
		mat = pat.matcher(correo);
		if (mat.find()) {
			return true;
		}else{
			return false;
		}        
	}
	/** Validar usuario */
	boolean validarUsuario() {
		try {
			List<Usuario> listaUsuario;
			listaUsuario = usuarioDao.getValidarNombreUsuario(nombUsuario.getText());
			if(listaUsuario.size() != 0)
				return true;
			else
				return false;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}

	/** Validar si el usuario existe a través de la cedula */
	boolean validarUsuarioExistente() {
		try {
			List<Usuario> listaUsuario;
			listaUsuario = usuarioDao.getValidarUsuarioExistente(cedula.getText());
			if(listaUsuario.size() != 0)
				return true;
			else
				return false;
		}catch(Exception ex) {
			return false;
		}
	}

	/** Validar si el correo del usuario ya se encuentra registrado */
	public boolean isValidarCorreoExistente() {
		try {
			Usuario objeto = usuarioDao.getValidarCorreoExistente(correo.getText());  
			if(objeto != null)
				return true;
			else
				return false;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}

	/** Validar Datos */
	public boolean isValidarDatos() {
		try {
			Boolean resultado = true;
			if(validarDeCedula(cedula.getText())== false) {
				Clients.showNotification("La cédula ingresada no es válida!!");
				cedula.focus();
				resultado = false;
				return resultado;
			}
			if(ValidarEmail(correo.getText()) == false) {
				Clients.showNotification("El correo ingresado no es válido!!");
				correo.focus();
				resultado = false;
				return resultado;
			}
			if(validarUsuario() == true) {
				Clients.showNotification("El nombre de usuario ingresado ya existe!!");
				nombUsuario.focus();
				resultado = false;
				return resultado;
			}
			if(validarUsuarioExistente() == true) {
				Clients.showNotification("El usuario ingresado ya existe!!");
				cedula.focus();
				resultado = false;
				return resultado;
			}
			if(isValidarCorreoExistente()) {
				Clients.showNotification("El correo de usuario ingresado ya exite, ingrese su correo personal.!!");
				correo.focus();
				resultado = false;
				return resultado;
			}
			if(correo.getText() == null) {
				Clients.showNotification("Por favor ingrese un correo electrónico.!!");
				correo.focus();
				return resultado;
			}
			if(nombUsuario.getText() == null) {
				Clients.showNotification("Por favor ingrese nombre de usuario.!!");
				nombUsuario.focus();
				return resultado;
			}
			if(clave.getText() == null) {
				Clients.showNotification("Por favor ingrese una clave de usuario.!!");
				clave.focus();
				return resultado;
			}
			if(nombres.getText() == null) {
				Clients.showNotification("Por favor ingrese nombres.!!");
				nombres.focus();
				return resultado;
			}
			if(apellidos.getText() == null) {
				Clients.showNotification("Por favor ingrese apellidos.!!");
				apellidos.focus();
				return resultado;
			}
			return resultado;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Escucha el evento "onClick" del objeto "grabar"
	 */
	@Listen("onClick=#grabar")
	public void grabar(){
		System.out.println("entra grabando");
		try {
			if (isValidarDatos() == true) {
				// Inicia la transaccion
				usuarioDao.getEntityManager().getTransaction().begin();	
				if (usuario.getIdUsuario() == null) {
					usuario.setClave(encriptar(clave.getText()));
					usuarioDao.getEntityManager().persist(usuario);
				}else{ 
					usuario = (Usuario) usuarioDao.getEntityManager().merge(usuario);
					usuario.setClave(encriptar(clave.getText()));				
				}
				usuarioDao.getEntityManager().getTransaction().commit();
				Clients.showNotification("Proceso Ejecutado con exito.");
				usuarioLista.buscar();
				salir();
			}else {
				Clients.showNotification("Verifique que los campos esten llenos.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			// Si hay error, reversa la transaccion.
			usuarioDao.getEntityManager().getTransaction().rollback();
		}		
	}

	@Listen("onClick=#grabando")
	public void grabando(){
		try {
			UsuarioPerfilDao usuarioPerfilDao = new UsuarioPerfilDao();
			Usuario obj = usuarioPerfilDao.getUsuario(SecurityUtil.getUser().getUsername().trim());
			usuarioDao.getEntityManager().getTransaction().begin();	
			if(obj.isCambioclave() == false) {
				obj.setClave(encriptar(confirmaClave.getText()));
				obj.setCambioclave(true);
				obj = (Usuario) usuarioDao.getEntityManager().merge(obj);

			}
			usuarioDao.getEntityManager().getTransaction().commit();
			Clients.showNotification("Proceso Ejecutado con exito.");
			Executions.getCurrent().sendRedirect("/menuPrincipal.zul");
			System.out.println(obj.isCambioclave());

		} catch (Exception e) {
			e.printStackTrace();
			// Si hay error, reversa la transaccion.
			usuarioDao.getEntityManager().getTransaction().rollback();
		}
	}
	
	@Listen("onClick=#graba")
	public void graba(){
		try {
			UsuarioPerfilDao usuarioPerfilDao = new UsuarioPerfilDao();
			Usuario obje = usuarioPerfilDao.getUsuario(SecurityUtil.getUser().getUsername().trim());
			usuarioDao.getEntityManager().getTransaction().begin();	
			if(obje.isCambioclave() == true) {
				obje.setClave(encriptar(confirmaClave.getText()));				
				obje = (Usuario) usuarioDao.getEntityManager().merge(obje);
			}
			usuarioDao.getEntityManager().getTransaction().commit();
			Clients.showNotification("Proceso Ejecutado con exito.");
			Executions.getCurrent().sendRedirect("/menuPrincipal.zul");
		} catch (Exception e) {
			e.printStackTrace();
			// Si hay error, reversa la transaccion.
			usuarioDao.getEntityManager().getTransaction().rollback();
		}	
	}


	@Listen("onClick=#salir")
	public void salir(){
		winUsuarioEditar.detach();
	}
	
	@Listen("onClick=#cancelar")
	public void sale(){
		Executions.getCurrent().sendRedirect("/menuPrincipal.zul");
	}


	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}