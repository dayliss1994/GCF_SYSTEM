package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import org.eclipse.persistence.annotations.AdditionalCriteria;


/**
 * The persistent class for the tipocancha database table.
 * 
 */

@Entity 
@NamedQueries({
	@NamedQuery(name="Tipocancha.findAll", query="SELECT t FROM Tipocancha t"),
	@NamedQuery(name="Tipocancha.buscarPorPatron", 
	query="SELECT t FROM Tipocancha t WHERE LOWER(t.descripcion) LIKE (:patron)")
})
@AdditionalCriteria(" this.estado IS NULL ")
public class Tipocancha implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_tipoC")
	private Integer id_tipoC;

	private String descripcion;

	private String estado;

	public Tipocancha() {
	}

	public Integer getId_tipoC() {
		return this.id_tipoC;
	}

	public void setId_tipoC(Integer id_tipoC) {
		this.id_tipoC = id_tipoC;
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

	//bi-directional many-to-one association to Persona
	@OneToMany(mappedBy="tipocancha")
	private List<Lugarpartido> lugarpartidos;

}