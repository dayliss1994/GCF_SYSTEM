package ec.edu.upse.gcf.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.eclipse.persistence.annotations.AdditionalCriteria;

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
	private Integer idPosicionjuego;

	private String descripcion;

	private String estado;

	public Posicionjuego() {
	}

	public Integer getIdPosicionjuego() {
		return this.idPosicionjuego;
	}

	public void setIdPosicionjuego(Integer idPosicionjuego) {
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