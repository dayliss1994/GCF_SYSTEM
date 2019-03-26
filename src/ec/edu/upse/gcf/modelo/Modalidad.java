package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.eclipse.persistence.annotations.AdditionalCriteria;


/**
 * The persistent class for the grupo database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Modalidad.findAll", query="SELECT m FROM Modalidad m"),
	@NamedQuery(name="Modalidades.buscarPorPatron", 
	            query="SELECT m FROM Modalidad m WHERE LOWER(m.descripcion) LIKE LOWER(:patron)")
})
@AdditionalCriteria(" this.estado IS NULL ")
public class Modalidad implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_modalidad")
	private Integer idModalidad;

	private String descripcion;

	private String estado;

	//bi-directional many-to-one association to Tablaposicione
	@OneToMany(mappedBy="modalidad")
	private List<Calendario> calendarios;

	public Modalidad() {
	}

	public Integer getIdModalidad() {
		return this.idModalidad;
	}

	public void setidModalidad(Integer idModalidad) {
		this.idModalidad = idModalidad;
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

	public List<Calendario> getCalendarios() {
		return this.calendarios;
	}

	public void setCalendarios(List<Calendario> calendario) {
		this.calendarios = calendario;
	}

	public Calendario addCalendario(Calendario calendario) {
		getCalendarios().add(calendario);
		calendario.setModalidad(this);
		return calendario;
	}

	public Calendario removeCalendario(Calendario calendario) {
		getCalendarios().remove(calendario);
		calendario.setModalidad(null);
		return calendario;
	}

}