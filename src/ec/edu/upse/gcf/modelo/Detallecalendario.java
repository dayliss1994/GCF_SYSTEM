package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Time;
import java.util.Date;


/**
 * The persistent class for the detallecalendario database table.
 * 
 */
@Entity
@NamedQuery(name="Detallecalendario.findAll", query="SELECT d FROM Detallecalendario d")
public class Detallecalendario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id_detalleCalendario;

	private String estado;

	@Temporal(TemporalType.DATE)
	private Date fechaEncuentro;

	private Time hora;

	//bi-directional many-to-one association to Calendario
	@ManyToOne
	@JoinColumn(name="id_calendario")
	private Calendario calendario;

	//bi-directional many-to-one association to Equipo
	@ManyToOne
	@JoinColumn(name="id_equipolocal")
	private Equipo equipo1;

	//bi-directional many-to-one association to Equipo
	@ManyToOne
	@JoinColumn(name="id_equipovisitante")
	private Equipo equipo2;

	public Detallecalendario() {
	}

	public int getId_detalleCalendario() {
		return this.id_detalleCalendario;
	}

	public void setId_detalleCalendario(int id_detalleCalendario) {
		this.id_detalleCalendario = id_detalleCalendario;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaEncuentro() {
		return this.fechaEncuentro;
	}

	public void setFechaEncuentro(Date fechaEncuentro) {
		this.fechaEncuentro = fechaEncuentro;
	}

	public Time getHora() {
		return this.hora;
	}

	public void setHora(Time hora) {
		this.hora = hora;
	}

	public Calendario getCalendario() {
		return this.calendario;
	}

	public void setCalendario(Calendario calendario) {
		this.calendario = calendario;
	}

	public Equipo getEquipo1() {
		return this.equipo1;
	}

	public void setEquipo1(Equipo equipo1) {
		this.equipo1 = equipo1;
	}

	public Equipo getEquipo2() {
		return this.equipo2;
	}

	public void setEquipo2(Equipo equipo2) {
		this.equipo2 = equipo2;
	}

}