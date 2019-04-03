package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.AdditionalCriteria;


/**
 * The persistent class for the jugador database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Jugador.findAll", query="SELECT j FROM Jugador j"),
	@NamedQuery(name="Jugadores.buscarPorPatron", 
	query="SELECT j FROM Jugador j WHERE LOWER(j.nombres) LIKE LOWER(:patron)")
})
@AdditionalCriteria("this.estado IS NULL")
public class Jugador implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_jugador")
	private int idJugador;

	private String apellidos;
	private String genero;
	private int edad;
	private String estado;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_nac")
	private Date fechaNac;

	private String foto;

	//bi-directional many-to-one association to Tipocampeonato
	@ManyToOne
	@JoinColumn(name="id_equipo")
	private Equipo equipo;

	//bi-directional many-to-one association to Tipocampeonato
	@ManyToOne
	@JoinColumn(name="id_posicionjuego")
	private Posicionjuego posicionjuego;
	
	@OneToMany(mappedBy="jugador")
	private List<Tarjetajugador> tarjetajugadores;

	private String identificacion;

	private String nombres;

	private int numerocamiseta;

	public Jugador() {
	}

	public int getIdJugador() {
		return this.idJugador;
	}

	public void setIdJugador(int idJugador) {
		this.idJugador = idJugador;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public int getEdad() {
		return this.edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaNac() {
		return this.fechaNac;
	}

	public void setFechaNac(Date fechaNac) {
		this.fechaNac = fechaNac;
	}

	public String getFoto() {
		return this.foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getIdentificacion() {
		return this.identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public String getNombres() {
		return this.nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public int getNumerocamiseta() {
		return this.numerocamiseta;
	}

	public void setNumerocamiseta(int numerocamiseta) {
		this.numerocamiseta = numerocamiseta;
	}

	public Equipo getEquipo() {
		return equipo;
	}

	public void setEquipo(Equipo equipo) {
		this.equipo = equipo;
	}

	public Posicionjuego getPosicionjuego() {
		return posicionjuego;
	}

	public void setPosicionjuego(Posicionjuego posicionjuego) {
		this.posicionjuego = posicionjuego;
	}	
	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	@Override
	public String toString() {
		return this.nombres;
	}	
}