package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Time;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the calendario database table.
 * 
 */
@Entity
@NamedQuery(name="Calendario.findAll", query="SELECT c FROM Calendario c")
public class Calendario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_calendario")
	private int idCalendario;

	private String estado;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	private Time hora;

	@Column(name="id_campeonato")
	private int idCampeonato;

	//@Column(name="id_jornada")
	//private int idJornada;

	private int id_lugarP;

	@JoinColumn(name="id_modalidad")
	private Modalidad modalidad;

	//bi-directional many-to-one association to Detallecalendario
	@OneToMany(mappedBy="calendario")
	private List<Detallecalendario> detallecalendarios;

	public Calendario() {
	}

	public int getIdCalendario() {
		return this.idCalendario;
	}

	public void setIdCalendario(int idCalendario) {
		this.idCalendario = idCalendario;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Time getHora() {
		return this.hora;
	}

	public void setHora(Time hora) {
		this.hora = hora;
	}

	public int getIdCampeonato() {
		return this.idCampeonato;
	}

	public void setIdCampeonato(int idCampeonato) {
		this.idCampeonato = idCampeonato;
	}

	/**public int getIdJornada() {
		return this.idJornada;
	}

	public void setIdJornada(int idJornada) {
		this.idJornada = idJornada;
	}*/

	public int getId_lugarP() {
		return this.id_lugarP;
	}

	public void setId_lugarP(int id_lugarP) {
		this.id_lugarP = id_lugarP;
	}

	public Modalidad getModalidad() {
		return this.modalidad;
	}

	public void setModalidad(Modalidad modalidad) {
		this.modalidad = modalidad;
	}

	public List<Detallecalendario> getDetallecalendarios() {
		return this.detallecalendarios;
	}

	public void setDetallecalendarios(List<Detallecalendario> detallecalendarios) {
		this.detallecalendarios = detallecalendarios;
	}

	public Detallecalendario addDetallecalendario(Detallecalendario detallecalendario) {
		getDetallecalendarios().add(detallecalendario);
		detallecalendario.setCalendario(this);

		return detallecalendario;
	}

	public Detallecalendario removeDetallecalendario(Detallecalendario detallecalendario) {
		getDetallecalendarios().remove(detallecalendario);
		detallecalendario.setCalendario(null);

		return detallecalendario;
	}

}