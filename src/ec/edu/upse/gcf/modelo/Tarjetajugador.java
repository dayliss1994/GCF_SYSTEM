package ec.edu.upse.gcf.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;


/**
 * The persistent class for the tarjetajugador database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Tarjetajugador.findAll", query="SELECT t FROM Tarjetajugador t"),
	@NamedQuery(name="Tarjetajugador.buscarPorPatron", 
	query="SELECT t FROM Tarjetajugador t WHERE LOWER(t.jugador.nombres) LIKE LOWER(:patron)")
})
public class Tarjetajugador implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_tarjeta_jugador")
	private int idTarjetaJugador;
	private String tiempo;
	private String motivo;
	private String estado;

	//bi-directional many-to-one association to Tarjeta
	@ManyToOne
	@JoinColumn(name="id_tarjeta")
	private Tarjeta tarjeta;

	//bi-directional many-to-one association to Partido
	@ManyToOne
	@JoinColumn(name="id_partido")
	private Partido partido;

	//bi-directional many-to-one association to Jugador
	@ManyToOne
	@JoinColumn(name="id_jugador")
	private Jugador jugador;

	public Tarjetajugador() {
	}

	public int getIdTarjetaJugador() {
		return this.idTarjetaJugador;
	}

	public void setIdTarjetaJugador(int idTarjetaJugador) {
		this.idTarjetaJugador = idTarjetaJugador;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Tarjeta getTarjeta() {
		return this.tarjeta;
	}

	public void setTarjeta(Tarjeta tarjeta) {
		this.tarjeta = tarjeta;
	}

	public Partido getPartido() {
		return this.partido;
	}

	public void setPartido(Partido partido) {
		this.partido = partido;
	}

	public Jugador getJugador() {
		return this.jugador;
	}

	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
	}

		public String getTiempo() {
		return tiempo;
	}

	public void setTiempo(String tiempo) {
		this.tiempo = tiempo;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
}