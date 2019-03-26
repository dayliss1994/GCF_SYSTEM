package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import org.eclipse.persistence.annotations.AdditionalCriteria;

/**
 * The persistent class for the posicionjuego database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Posicionjuego.findAll", query="SELECT p FROM Posicionjuego p"),
	@NamedQuery(name="Posicionjuego.buscarPorPatron", 
	query="SELECT p FROM Posicionjuego p WHERE LOWER(p.descripcion) LIKE LOWER(:patron)")
})
@AdditionalCriteria("this.estado IS NULL ")
public class Posicionjuego implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_posicionjuego")
	private int idPosicionjuego;

	private String descripcion;

	private String estado;

	//bi-directional many-to-one association to Persona
	@OneToMany(mappedBy="posicionjuego")
	private List<Jugador> jugadores;

	public Posicionjuego() {
	}

	public int getIdPosicionjuego() {
		return this.idPosicionjuego;
	}

	public void setIdPosicionjuego(int idPosicionjuego) {
		this.idPosicionjuego = idPosicionjuego;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Override
	public String toString() {
		return this.descripcion;
	}	

}