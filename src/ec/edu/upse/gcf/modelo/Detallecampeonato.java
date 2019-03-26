package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.AdditionalCriteria;


/**
 * The persistent class for the detallecampeonato database table.
 * 
 */
@Entity
@NamedQueries({
@NamedQuery(name="Detallecampeonato.findAll", query="SELECT d FROM Detallecampeonato d where d.campeonato.tipoCampeonato.descripcion = (:apertura)"),
@NamedQuery(name="Detallecampeonato.BuscaPorCategoria", query="SELECT d FROM Detallecampeonato d WHERE d.campeonato = (:patron) " ),
@NamedQuery(name="Detallecampeonato.buscarPorPatron", 
query="SELECT d FROM Detallecampeonato d WHERE LOWER(d.campeonato.nombreC) LIKE LOWER(:patron)")
})
@AdditionalCriteria("this.estado IS NULL")
public class Detallecampeonato implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id_detalleC;

	private String estado;

	@JoinColumn(name="id_campeonato")
	private Campeonato campeonato;

	@JoinColumn(name="id_categoria")
	private Categoria categoria;

	@JoinColumn(name="id_equipo")
	private Equipo equipo;

	private String observacion;

	public Detallecampeonato() {
	}

	public Integer getId_detalleC() {
		return this.id_detalleC;
	}

	public void setId_detalleC(Integer id_detalleC) {
		this.id_detalleC = id_detalleC;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
 
	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Campeonato getCampeonato() {
		return campeonato;
	}

	public void setCampeonato(Campeonato campeonato) {
		this.campeonato = campeonato;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Equipo getEquipo() {
		return equipo;
	}

	public void setEquipo(Equipo equipo) {
		this.equipo = equipo;
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