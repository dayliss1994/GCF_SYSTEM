
package ec.edu.upse.gcf.editar;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.EquipoDAO;
import ec.edu.upse.gcf.dao.JugadorDAO;
import ec.edu.upse.gcf.dao.PosicionjuegoDAO;
import ec.edu.upse.gcf.modelo.Equipo;
import ec.edu.upse.gcf.modelo.Jugador;
import ec.edu.upse.gcf.modelo.Posicionjuego;
import ec.edu.upse.gcf.util.FileUtil;

public class JugadorEditar {

	@Wire
	private Window winJugadorEditar;

	@Wire
	private Textbox cedula;	

	@Wire
	private Textbox nombres;

	@Wire
	private Textbox apellidos;

	@Wire
	private Textbox edad;

	@Wire
	private Textbox numcamisa;

	@Wire
	private Datebox fechaNac;

	@Wire
	private Checkbox validar;

	// Instancia el objeto para acceso a datos.
	private JugadorDAO jugadorDao = new JugadorDAO(); 
	private PosicionjuegoDAO posicionjuegoDao = new PosicionjuegoDAO();
	private EquipoDAO equipoDao = new EquipoDAO();
	// Objeto que contiene la persona con la que se esta trabajando
	private Jugador jugador;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {

		// Permite enlazar los componentes que se asocian con la anotacion @Wire
		Selectors.wireComponents(view, this, false);
		jugador = (Jugador) Executions.getCurrent().getArg().get("Jugador");		
	}
	
	public List<String> getGenero() {		
        List<String> genero = new ArrayList<String>();	
        genero.add("FEMENINO");				
        genero.add("MASCULINO");   
        return genero;					
      }

	@SuppressWarnings("unused")
	/*@Command
	public void calcularEdad(@ContextParam(ContextType.VIEW) Component view,Calendar fechaNac) {
		Calendar hoy = Calendar.getInstance();
		int anio = hoy.get(Calendar.YEAR) -  fechaNac.get(Calendar.YEAR);
		int mes = hoy.get(Calendar.MONTH) - fechaNac.get(Calendar.MONTH);
		int dia = hoy.get(Calendar.DAY_OF_MONTH) - fechaNac.get(Calendar.DAY_OF_MONTH);

		//Si está en ese año pero todavía no los ha cumplido
		if (mes < 0 || (mes == 0 && dia < 0)) {
			anio = anio - 1; //no aparecían los dos guiones del postincremento :|			
			//edad.setText(anio);
			System.out.println("su edad es:" + anio);
		}
		//return anio;		
	}*/
	@Listen("onChange = #fechaNac")
	public void changeCreatedDate() {
        Date createdDate = fechaNac.getValue();
        
        DateFormat formatter = new SimpleDateFormat(fechaNac.getFormat());
        System.out.println("su edad es:" + createdDate);
        showNotify("Changed to: " + formatter.format(createdDate), fechaNac);
    }
	 private void showNotify(String msg, Component ref) {
	        Clients.showNotification(msg, "info", ref, "end_center", 2000);
	    }
	/** Método para validar cédula*/
	public boolean validarCedula(String cedula) {
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


	public boolean isValidarDatos() {
		try {
			Boolean resultado = true;			
			//if(validar.isChecked() == true) {
			if(validarCedula(cedula.getText()) == false) {
				Clients.showNotification("La cédula ingresada no es válida!!");
				cedula.focus();
				resultado = false;
				return resultado;
			}
			//}
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
			if(edad.getText() == null) {
				Clients.showNotification("Por favor ingrese apellidos.!!");
				edad.focus();
				return resultado;
			}
			if(numcamisa.getText() == null) {
				Clients.showNotification("Por favor ingrese apellidos.!!");
				edad.focus();
				return resultado;
			}
			return resultado;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Command
	public void grabar (@ContextParam(ContextType.VIEW) Component view){
		try {
			if (isValidarDatos() == true) {
				// Inicia la transaccion
				jugadorDao.getEntityManager().getTransaction().begin();

				// Almacena los datos.
				// Si es nuevo sa el metodo "persist" de lo contrario usa el metodo "merge"
				if (jugador.getIdJugador() == 0) {
					jugadorDao.getEntityManager().persist(jugador);
				}else{
					jugador = (Jugador) jugadorDao.getEntityManager().merge(jugador);
				}

				// Cierra la transaccion.
				jugadorDao.getEntityManager().getTransaction().commit();

				Clients.showNotification("Proceso Ejecutado con exito.");			
				// Actualiza la lista
				BindUtils.postGlobalCommand(null, null, "Jugadores.buscarPorPatron", null);

				//jugadorLista.buscar();
				// Cierra la ventana
				salir();
			}else {
				Clients.showNotification("Verifique que los campos esten llenos.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// Si hay error, reversa la transaccion.
			jugadorDao.getEntityManager().getTransaction().rollback();
		}		

	}

	@Command
	@NotifyChange("imagen")
	public void subir(@ContextParam(ContextType.TRIGGER_EVENT) UploadEvent evento){
		String imagen = FileUtil.loadFile(evento.getMedia());
		if (imagen != null) {
			jugador.setFoto(imagen);
		}

	}

	public AImage getImagen() {
		AImage retorno = null;
		if (jugador.getFoto() != null) {
			try {
				retorno = FileUtil.getImagenTamanoFijo(new AImage(jugador.getFoto()), 163, 163);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return retorno; 
	}

	@Command
	public void salir(){
		winJugadorEditar.detach();
	}

	public List<Posicionjuego> getPosiciones() {
		return posicionjuegoDao.getPosicionjuego(); 
	}

	public List<Equipo> getEquipos() {
		return equipoDao.getListaEquipos(); 
	}

	public Jugador getJugador() {
		return jugador;
	}

	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
	}	

}
