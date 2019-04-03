package ec.edu.upse.gcf.editar;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.edu.upse.gcf.dao.TarjetaDAO;
import ec.edu.upse.gcf.listas.TarjetaLista;
import ec.edu.upse.gcf.modelo.Tarjeta;


@SuppressWarnings("serial")
public class TarjetaEditar extends SelectorComposer<Component> {
	// Enlaza a la ventana para poderla cerrar
	@Wire
	private Window winTarjetaEditar;
	@Wire private Textbox descripcion;

	private TarjetaLista tarjetaLista; 

	private TarjetaDAO tarjetaDao = new TarjetaDAO();
	private Tarjeta tarjeta;

	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);
		tarjetaLista = (TarjetaLista)Executions.getCurrent().getArg().get("VentanaPadre");
		tarjeta = null; 
		if (Executions.getCurrent().getArg().get("Tarjeta") != null) {
			tarjeta = (Tarjeta) Executions.getCurrent().getArg().get("Tarjeta");
		}else{
			tarjeta = new Tarjeta(); 
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
				tarjetaDao.getEntityManager().getTransaction().begin();
				if (tarjeta.getIdTarjeta() == null) {
					tarjetaDao.getEntityManager().persist(tarjeta);
				}else{
					tarjeta = (Tarjeta) tarjetaDao.getEntityManager().merge(tarjeta);
				}
				tarjetaDao.getEntityManager().getTransaction().commit();
				Clients.showNotification("Proceso Ejecutado con exito.");
				tarjetaLista.buscar();
				salir();
			}else {
				Clients.showNotification("Verifique que los campos esten llenos.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tarjetaDao.getEntityManager().getTransaction().rollback();
		}
	}

	@Listen("onClick=#salir")
	public void salir(){
		winTarjetaEditar.detach();
	}
	public Tarjeta getTarjeta() {
		return tarjeta;
	}
	public void setTarjeta(Tarjeta tarjeta) {
		this.tarjeta = tarjeta;
	}
}