package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.AdditionalCriteria;

import java.util.List;


/**
 * The persistent class for the tipocampeonato database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Tipocampeonato.findAll", query="SELECT t FROM Tipocampeonato t"),
	@NamedQuery(name="TipoCampeonatos.buscarPorPatron", 
	            query="SELECT t FROM Tipocampeonato t WHERE LOWER(t.descripcion) LIKE LOWER(:patron)")
})
@AdditionalCriteria(" this.estado IS NULL ")
public class Tipocampeonato implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_tipocamp")
	private Integer idTipocamp;

	private String descripcion;

	private String estado;

	//bi-directional many-to-one association to Campeonato
	@OneToMany(mappedBy="tipoCampeonato")
	private List<Campeonato> campeonatos;

	public Tipocampeonato() {
	}

	public Integer getIdTipocamp() {
		return this.idTipocamp;
	}

	public void setIdTipocamp(Integer idTipocamp) {
		this.idTipocamp = idTipocamp;
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

	public List<Campeonato> getCampeonatos() {
		return this.campeonatos;
	}

	public void setCampeonatos(List<Campeonato> campeonatos) {
		this.campeonatos = campeonatos;
	}

	public Campeonato addCampeonato(Campeonato campeonato) {
		getCampeonatos().add(campeonato);
		campeonato.setTipoCampeonato(this);

		return campeonato;
	}

	public Campeonato removeCampeonato(Campeonato campeonato) {
		getCampeonatos().remove(campeonato);
		campeonato.setTipoCampeonato(null);

		return campeonato;
	}

	@Override
	public String toString() {
		return this.descripcion;
	}

}