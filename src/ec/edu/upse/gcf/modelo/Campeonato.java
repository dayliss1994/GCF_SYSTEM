package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.AdditionalCriteria;

/**
 * The persistent class for the campeonato database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Campeonato.findAll", query="SELECT c FROM Campeonato c"),
	@NamedQuery(name="Campeonatos.buscarPorPatron", 
	            query="SELECT c FROM Campeonato c WHERE LOWER(c.nombreC) LIKE LOWER(:patron)")
})
@AdditionalCriteria("this.estado IS NULL")
public class Campeonato implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_campeonato")
	private Integer idCampeonato;

	private String estado;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_fin")
	private Date fechaFin;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_inicio")
	private Date fechaInicio;

	private String nombreC;

	//bi-directional many-to-one association to Tipocampeonato
	@ManyToOne
	@JoinColumn(name="id_tipocamp")
	private Tipocampeonato tipoCampeonato;

	/**
	//bi-directional many-to-one association to Grupo
	@OneToMany(mappedBy="campeonato")
	private List<Modalidad> grupos;*/

	public Campeonato() {
	}

	public Integer getIdCampeonato() {
		return this.idCampeonato;
	}

	public void setIdCampeonato(Integer idCampeonato) {
		this.idCampeonato = idCampeonato;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaFin() {
		return this.fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Date getFechaInicio() {
		return this.fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getNombreC() {
		return this.nombreC;
	}

	public void setNombreC(String nombreC) {
		this.nombreC = nombreC;
	}

	public Tipocampeonato getTipoCampeonato() {
		return this.tipoCampeonato;
	}

	public void setTipoCampeonato(Tipocampeonato tipoCampeonato) {
		this.tipoCampeonato = tipoCampeonato;
	}

	/**public List<Modalidad> getGrupos() {
		return this.grupos;
	}

	public void setGrupos(List<Modalidad> grupos) {
		this.grupos = grupos;
	}

	public Modalidad addGrupo(Modalidad grupo) {
		getGrupos().add(grupo);
		grupo.setCampeonato(this);

		return grupo;
	}

	public Modalidad removeGrupo(Modalidad grupo) {
		getGrupos().remove(grupo);
		grupo.setCampeonato(null);

		return grupo;
	}*/
	
	@Override
	public String toString() {
		return this.nombreC;
	}

}