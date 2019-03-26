package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.AdditionalCriteria;


/**
 * The persistent class for the tipoestado database table.
 * 
 */
@Entity

@NamedQueries({
	@NamedQuery(name="Tipoestado.findAll", query="SELECT t FROM Tipoestado t"),
	@NamedQuery(name="Tipoestado.buscarPorPatron", 
	            query="SELECT t FROM Tipoestado t WHERE LOWER(t.descripcion) LIKE (:patron) AND t.estado = NULL")
})

@AdditionalCriteria(" this.estado IS NULL ")
public class Tipoestado implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_tipoestado")
	private int idTipoestado;

	private String descripcion;

	private String estado;

	public Tipoestado() {
	}

	public int getIdTipoestado() {
		return this.idTipoestado;
	}

	public void setIdTipoestado(int idTipoestado) {
		this.idTipoestado = idTipoestado;
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

}