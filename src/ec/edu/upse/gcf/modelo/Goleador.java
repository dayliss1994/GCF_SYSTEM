package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.eclipse.persistence.annotations.AdditionalCriteria;


/**
 * The persistent class for the goleador database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Goleador.findAll", query="SELECT g FROM Goleador g"),
	@NamedQuery(name="Goleador.buscarPorPatron", query="SELECT g FROM Goleador g WHERE LOWER(g.jugador.nombres) LIKE LOWER(:patron)")
})
@AdditionalCriteria("this.estado IS NULL")
public class Goleador implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_goleador")
	private int idGoleador;

	private String estado;

	private int primertiempo;
	private int segundotiempo;

	//bi-directional many-to-one association to Tipocampeonato
	@ManyToOne
	@JoinColumn(name="id_jugador")
	private Jugador jugador;

	//bi-directional many-to-one association to Tipocampeonato
	@ManyToOne
	@JoinColumn(name="id_partido")
	private Partido partido;

	//bi-directional many-to-one association to Detallepartido
	@OneToMany(mappedBy="goleador")
	private List<Detallepartido> detallepartidos;

	public Goleador() {
	}

	public int getIdGoleador() {
		return this.idGoleador;
	}

	public void setIdGoleador(int idGoleador) {
		this.idGoleador = idGoleador;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Partido getPartido() {
		return partido;
	}

	public void setPartido(Partido partido) {
		this.partido = partido;
	}

	public int getPrimertiempo() {
		return primertiempo;
	}

	public void setPrimertiempo(int primertiempo) {
		this.primertiempo = primertiempo;
	}

	public int getSegundotiempo() {
		return segundotiempo;
	}

	public void setSegundotiempo(int segundotiempo) {
		this.segundotiempo = segundotiempo;
	}

	public Jugador getJugador() {
		return jugador;
	}

	public void setJugador(Jugador jugador) {
		this.jugador = jugador;
	}

	public List<Detallepartido> getDetallepartidos() {
		return this.detallepartidos;
	}

	public void setDetallepartidos(List<Detallepartido> detallepartidos) {
		this.detallepartidos = detallepartidos;
	}

	public Detallepartido addDetallepartido(Detallepartido detallepartido) {
		getDetallepartidos().add(detallepartido);
		detallepartido.setGoleador(this);

		return detallepartido;
	}

	public Detallepartido removeDetallepartido(Detallepartido detallepartido) {
		getDetallepartidos().remove(detallepartido);
		detallepartido.setGoleador(null);

		return detallepartido;
	}

}