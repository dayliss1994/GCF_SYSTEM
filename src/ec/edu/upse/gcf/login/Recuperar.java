package ec.edu.upse.gcf.login;

import java.util.Random;

import org.zkforge.bwcaptcha.Captcha;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;

import ec.edu.upse.gcf.dao.UsuarioDao;
import ec.edu.upse.gcf.editar.UsuarioEditar;
import ec.edu.upse.gcf.modelo.Usuario;


public class Recuperar{
	
	@Wire Button enviar;
	@WireVariable
	private UsuarioDao correousuario;

	private String captcha;
	private String correo;
	private String textoCaptcha;
	private boolean textoCaptchaControl;
	private boolean buttonIngresar;
	
	private UsuarioEditar usuarioo = new UsuarioEditar();
	private UsuarioDao usuarioDao = new UsuarioDao();

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		setCaptcha(getRandomString());
		setButtonIngresar(true);
		setTextoCaptchaControl(false);
	}

	public String getRandomString() {
		int lenght = 5;
		String alfabeto = "abcdefghijklmn�opqrstuvwxyz";
		Random rn = new Random();
		StringBuilder sb = new StringBuilder(lenght);

		for(int i=0; i<lenght; i++) {
			sb.append(alfabeto.charAt(rn.nextInt(alfabeto.length())));
		}		
		return sb.toString();		
	}

	@Command
	@NotifyChange("*")
	public void actualizarCaptcha(@BindingParam("valor") Captcha valor){
		setCaptcha(getRandomString());
		textoCaptcha = "";
		setButtonIngresar(true);
		setTextoCaptchaControl(false);
	}

	@Command
	public void enviarMail() throws Exception{
		UsuarioDao usu = new UsuarioDao();
		EmailSenderService ess = new EmailSenderService();
		String correoUsuario = correo.trim();
		Usuario usu2 = usu.getUsuarioCorreo(correoUsuario);
		if (usu2 == null) {
			Clients.showNotification("El correo electr�nico ingresado no est� registrado en nuestro sistema. Por favor ingrese otro");
			return;
		}	
		if(ess.enviarCorreo(usu2.getCorreo().trim(),usu2.getNombres() + usu2.getApellidos())){
			String resultado = usuarioo.encriptar(VariableGeneral.getNumeroAleatorio().toString());
			usuarioDao.getEntityManager().getTransaction().begin();
			usu2.setClave(resultado);
			usuarioDao.getEntityManager().merge(usu2);
			usuarioDao.getEntityManager().getTransaction().commit();
			Clients.showNotification("Mail enviado con Exito");
		}else{
			Clients.showNotification("Ha ocurrido un error al momento de restablecer la contrase�a . Por favor intente m�s tarde");
		}
		
	}
	
	//GETTER AND SETTER
	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getTextoCaptcha() {
		return textoCaptcha;
	}

	public void setTextoCaptcha(String textoCaptcha) {
		this.textoCaptcha = textoCaptcha;
	}

	public boolean isTextoCaptchaControl() {
		return textoCaptchaControl;
	}

	public void setTextoCaptchaControl(boolean textoCaptchaControl) {
		this.textoCaptchaControl = textoCaptchaControl;
	}

	public boolean isButtonIngresar() {
		return buttonIngresar;
	}

	public void setButtonIngresar(boolean buttonIngresar) {
		this.buttonIngresar = buttonIngresar;
	}



}
