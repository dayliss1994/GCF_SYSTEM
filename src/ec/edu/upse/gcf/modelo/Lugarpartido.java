package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.AdditionalCriteria;


/**
 * The persistent class for the lugarpartido database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Lugarpartido.findAll", query="SELECT l FROM Lugarpartido l"),
	@NamedQuery(name="Lugarpartido.buscarPorPatron", query="SELECT l FROM Lugarpartido l WHERE LOWER(l.nombreCancha) LIKE LOWER(:patron)")
})
@AdditionalCriteria("this.estado IS NULL")
public class Lugarpartido implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id_lugarP;

	private String estado;

	private String nombreCancha;
	
	@ManyToOne
	@JoinColumn(name="id_tipoC")
	private Tipocancha tipocancha;

	public Lugarpartido() {
	}

	public int getId_lugarP() {
		return this.id_lugarP;
	}

	public void setId_lugarP(int id_lugarP) {
		this.id_lugarP = id_lugarP;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getNombreCancha() {
		return this.nombreCancha;
	}

	public void setNombreCancha(String nombreCancha) {
		this.nombreCancha = nombreCancha;
	}

	public Tipocancha getTipocancha() {
		return tipocancha;
	}

	public void setTipocancha(Tipocancha tipocancha) {
		this.tipocancha = tipocancha;
	}
	
}