package ec.edu.upse.gcf.editar;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.PosicionjuegoDAO;
import ec.edu.upse.gcf.listas.PosicionLista;
import ec.edu.upse.gcf.modelo.Posicionjuego;

@SuppressWarnings("serial")
public class PosicionEditar extends SelectorComposer<Component>{
	@Wire private Window winPosicionEditar;	
	@Wire private Textbox descripcion;
	
	private PosicionLista posicionLista; 
	private PosicionjuegoDAO posicionDao = new PosicionjuegoDAO();
	private Posicionjuego posicion;

	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);
		posicionLista = (PosicionLista)Executions.getCurrent().getArg().get("VentanaPadre");
		posicion = null; 
		if (Executions.getCurrent().getArg().get("Posicionjuego") != null) {
			posicion = (Posicionjuego) Executions.getCurrent().getArg().get("Posicionjuego");
		}else{
			posicion = new Posicionjuego(); 
		}
	}

	public boolean isValidarDatos() {
		try {
			Boolean resultado = true;			
			if(descripcion.getText() == null) {
				Clients.showNotification("Por favor ingrese el nombre de la tarjeta.!!");
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
		System.out.println("entra grabando");
		try {
			if (isValidarDatos() == true) {
				// Inicia la transaccion
				posicionDao.getEntityManager().getTransaction().begin();
				if (posicion.getIdPosicionjuego() == null) {
					posicionDao.getEntityManager().persist(posicion);
				}else{
					posicion = (Posicionjuego) posicionDao.getEntityManager().merge(posicion);
				}
				posicionDao.getEntityManager().getTransaction().commit();
				Clients.showNotification("Proceso Ejecutado con exito.");
				posicionLista.buscar();
				salir();
			}else {
				Clients.showNotification("Verifique que los campos esten llenos.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			posicionDao.getEntityManager().getTransaction().rollback();
		}
	}

	@Listen("onClick=#salir")
	public void salir(){
		winPosicionEditar.detach();
	}

	public Posicionjuego getPosicion() {
		return posicion;
	}

	public void setPosicion(Posicionjuego posicion) {
		this.posicion = posicion;
	}	

}