package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.AdditionalCriteria;

import java.util.List;


/**
 * The persistent class for the equipo database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Equipo.findAll", query="SELECT e FROM Equipo e"),
	@NamedQuery(name="Equipos.buscarPorPatron", query="SELECT e FROM Equipo e WHERE LOWER(e.nombre) LIKE LOWER(:patron)")
})
@AdditionalCriteria("this.estado IS NULL")
public class Equipo implements Serializable, Cloneable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_equipo")
	private int idEquipo;

	private String escudo;

	private String estado;

	private String nombre;

	private String representante;

	//bi-directional many-to-one association to Persona
	@OneToMany(mappedBy="equipo")
	private List<Jugador> jugadores;

	//bi-directional many-to-one association to Persona
	@OneToMany(mappedBy="equipo")
	private List<Jugador> detallecampeonatos;

	//bi-directional many-to-one association to Detallecalendario
	@OneToMany(mappedBy="equipo1")
	private List<Detallecalendario> detallecalendarios1;

	//bi-directional many-to-one association to Detallecalendario
	@OneToMany(mappedBy="equipo2")
	private List<Detallecalendario> detallecalendarios2;

	public Equipo() {
	}

	public int getIdEquipo() {
		return this.idEquipo;
	}

	public void setIdEquipo(int idEquipo) {
		this.idEquipo = idEquipo;
	}

	public String getEscudo() {
		return this.escudo;
	}

	public void setEscudo(String escudo) {
		this.escudo = escudo;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getRepresentante() {
		return representante;
	}

	public void setRepresentante(String representante) {
		this.representante = representante;
	}

	public List<Detallecalendario> getDetallecalendarios1() {
		return this.detallecalendarios1;
	}

	public void setDetallecalendarios1(List<Detallecalendario> detallecalendarios1) {
		this.detallecalendarios1 = detallecalendarios1;
	}

	public Detallecalendario addDetallecalendarios1(Detallecalendario detallecalendarios1) {
		getDetallecalendarios1().add(detallecalendarios1);
		detallecalendarios1.setEquipo1(this);

		return detallecalendarios1;
	}

	public Detallecalendario removeDetallecalendarios1(Detallecalendario detallecalendarios1) {
		getDetallecalendarios1().remove(detallecalendarios1);
		detallecalendarios1.setEquipo1(null);

		return detallecalendarios1;
	}

	public List<Detallecalendario> getDetallecalendarios2() {
		return this.detallecalendarios2;
	}

	public void setDetallecalendarios2(List<Detallecalendario> detallecalendarios2) {
		this.detallecalendarios2 = detallecalendarios2;
	}

	public Detallecalendario addDetallecalendarios2(Detallecalendario detallecalendarios2) {
		getDetallecalendarios2().add(detallecalendarios2);
		detallecalendarios2.setEquipo2(this);

		return detallecalendarios2;
	}

	public Detallecalendario removeDetallecalendarios2(Detallecalendario detallecalendarios2) {
		getDetallecalendarios2().remove(detallecalendarios2);
		detallecalendarios2.setEquipo2(null);

		return detallecalendarios2;
	}

	@Override
	public String toString() {
		return this.nombre;
	}
	public Object clone(){
        Object obj=null;
        try{
            obj=super.clone();
        }catch(CloneNotSupportedException ex){
            System.out.println(" no se puede duplicar");
        }
        return obj;
    }
}