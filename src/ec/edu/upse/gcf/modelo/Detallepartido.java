package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the detallepartido database table.
 * 
 */
@Entity
@NamedQuery(name="Detallepartido.findAll", query="SELECT d FROM Detallepartido d")
public class Detallepartido implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id_detallePartido;

	private String estado;

	private String jugadorentra;

	private String jugadorsale;

	private int tiempoadic;

	//bi-directional many-to-one association to Goleador
	@ManyToOne
	@JoinColumn(name="id_goleador")
	private Goleador goleador;

	public Detallepartido() {
	}

	public int getId_detallePartido() {
		return this.id_detallePartido;
	}

	public void setId_detallePartido(int id_detallePartido) {
		this.id_detallePartido = id_detallePartido;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getJugadorentra() {
		return this.jugadorentra;
	}

	public void setJugadorentra(String jugadorentra) {
		this.jugadorentra = jugadorentra;
	}

	public String getJugadorsale() {
		return this.jugadorsale;
	}

	public void setJugadorsale(String jugadorsale) {
		this.jugadorsale = jugadorsale;
	}

	public int getTiempoadic() {
		return this.tiempoadic;
	}

	public void setTiempoadic(int tiempoadic) {
		this.tiempoadic = tiempoadic;
	}

	public Goleador getGoleador() {
		return this.goleador;
	}

	public void setGoleador(Goleador goleador) {
		this.goleador = goleador;
	}

}