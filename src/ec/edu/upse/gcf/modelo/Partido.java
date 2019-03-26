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
 * The persistent class for the partido database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Partido.findAll", query="SELECT p FROM Partido p"),
	@NamedQuery(name="Partido.buscarPorPatron", query="SELECT p FROM Partido p WHERE LOWER(p.nombrepartido) LIKE LOWER(:patron)")
})
@AdditionalCriteria("this.estado IS NULL")
public class Partido implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_partido")
	private int idPartido;

	private String arbitro;


	private String estado;

	@Temporal(TemporalType.DATE)
	private Date fecha;

	private String nombrepartido;

	private String observacion;

	private String resultadoGanador;

	private String resultadoPerdedor;

	private int tiempoadicional;

	//bi-directional many-to-one association to Calendario
	@ManyToOne
	@JoinColumn(name="equipoganador")
	private Equipo equipoganador;

	//bi-directional many-to-one association to Calendario
	@ManyToOne
	@JoinColumn(name="equipoperdedor")
	private Equipo equiperdedor;
	
	@ManyToOne
	@JoinColumn(name="id_tipoestado")
	private Tipoestado tipoestado;
	
	@ManyToOne
	@JoinColumn(name="id_calendario")
	private Calendario id_calendario;
	

	public Partido() {
	}

	public int getIdPartido() {
		return this.idPartido;
	}

	public void setIdPartido(int idPartido) {
		this.idPartido = idPartido;
	}

	public String getArbitro() {
		return this.arbitro;
	}

	public void setArbitro(String arbitro) {
		this.arbitro = arbitro;
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

	public String getNombrepartido() {
		return this.nombrepartido;
	}

	public void setNombrepartido(String nombrepartido) {
		this.nombrepartido = nombrepartido;
	}

	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getResultadoGanador() {
		return this.resultadoGanador;
	}

	public void setResultadoGanador(String resultadoGanador) {
		this.resultadoGanador = resultadoGanador;
	}

	public String getResultadoPerdedor() {
		return this.resultadoPerdedor;
	}

	public void setResultadoPerdedor(String resultadoPerdedor) {
		this.resultadoPerdedor = resultadoPerdedor;
	}

	public int getTiempoadicional() {
		return this.tiempoadicional;
	}

	public void setTiempoadicional(int tiempoadicional) {
		this.tiempoadicional = tiempoadicional;
	}

	public Equipo getEquipoganador() {
		return equipoganador;
	}

	public void setEquipoganador(Equipo equipoganador) {
		this.equipoganador = equipoganador;
	}

	public Equipo getEquiperdedor() {
		return equiperdedor;
	}

	public void setEquiperdedor(Equipo equiperdedor) {
		this.equiperdedor = equiperdedor;
	}

	public Tipoestado getTipoestado() {
		return tipoestado;
	}

	public void setTipoestado(Tipoestado tipoestado) {
		this.tipoestado = tipoestado;
	}

	public Calendario getId_calendario() {
		return id_calendario;
	}

	public void setId_calendario(Calendario id_calendario) {
		this.id_calendario = id_calendario;
	}

}