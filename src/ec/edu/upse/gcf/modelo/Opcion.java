package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.eclipse.persistence.annotations.AdditionalCriteria;


/**
 * The persistent class for the opcion database table.
 * 
 */
@Entity
@NamedQueries({
@NamedQuery(name="Opcion.findAll", query="SELECT o FROM Opcion o"),
@NamedQuery(name="Opcion.opcionPorPerfilPadre", query="SELECT o  FROM Opcion o WHERE o.idOpcionPadre IS NULL "
		+ " AND o.idOpcion IN (SELECT a.opcion.idOpcion FROM Opcionperfil a WHERE a.perfil.idPerfil = :perfil) ORDER BY o.titulo"),

@NamedQuery(name="Opcion.opcionPorPerfilHijo", query="SELECT o  FROM Opcion o WHERE o.idOpcionPadre IS NOT NULL "
		+ " AND o.idOpcionPadre = :opcionPadre AND o.idOpcion IN (SELECT a.opcion.idOpcion FROM Opcionperfil a WHERE a.perfil.idPerfil = :perfil) ORDER BY o.titulo"),

//@NamedQuery(name="Opcion.opcionPadrePorHijo", query="SELECT o  FROM Opcion o WHERE o.idOpcionPadre IS NOT NULL "
	//	+ " AND o.idOpcionPadre = ?1 ORDER BY o.titulo"),

//@NamedQuery(name="Opcion.opcionPadrePorHijo", query="SELECT o  FROM Opcion o WHERE o.idOpcionPadre = (:idOpcionPadre)  "),

@NamedQuery(name="Opcion.opcionPadreHijo", query="SELECT o  FROM Opcion o WHERE o.idOpcionPadre IS NOT NULL "
		+ " AND o.idOpcionPadre = :idOpcionPadre ORDER BY o.titulo"),


@NamedQuery(name="Opcion.opcionPadre", query="SELECT o  FROM Opcion o WHERE o.idOpcionPadre IS NULL "
		+ " ORDER BY o.titulo"),

@NamedQuery(name="Opcion.opcionesDisponibles", query="SELECT o  FROM Opcion o WHERE "
		+ "  o.idOpcion NOT IN (SELECT a.opcion.idOpcion FROM Opcionperfil a WHERE a.perfil = :perfil) ORDER BY o.titulo"),

@NamedQuery(name="Opcion.opcDisp", query="SELECT o  FROM Opcion o WHERE LOWER(o.titulo) LIKE LOWER(:patron)"),

@NamedQuery(name="Opciones.buscarPorPatron", query="SELECT o FROM Opcion o WHERE LOWER(o.titulo) LIKE LOWER(:patron)")
})

@AdditionalCriteria("this.estado IS NULL ")
public class Opcion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_opcion")
	private Integer idOpcion;

	private String estado;

	@Column(name="id_opcion_padre")
	private int idOpcionPadre;

	private String imagen;

	private String titulo;

	private String url;

	//bi-directional many-to-one association to Opcionperfil
	@OneToMany(mappedBy="opcion", cascade = CascadeType.ALL)
	private List<Opcionperfil> opcionperfils;

	public Opcion() {
	}

	public Integer getIdOpcion() {
		return this.idOpcion;
	}

	public void setIdOpcion(Integer idOpcion) {
		this.idOpcion = idOpcion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public int getIdOpcionPadre() {
		return this.idOpcionPadre;
	}

	public void setIdOpcionPadre(int idOpcionPadre) {
		this.idOpcionPadre = idOpcionPadre;
	}

	public String getImagen() {
		return this.imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Opcionperfil> getOpcionperfils() {
		return this.opcionperfils;
	}

	public void setOpcionperfils(List<Opcionperfil> opcionperfils) {
		this.opcionperfils = opcionperfils;
	}

	public Opcionperfil addOpcionperfil(Opcionperfil opcionperfil) {
		getOpcionperfils().add(opcionperfil);
		opcionperfil.setOpcion(this);

		return opcionperfil;
	}

	@Override
	public String toString() {
		return "Opcion [idOpcion=" + idOpcion + ", estado=" + estado + ", idOpcionPadre=" + idOpcionPadre + ", imagen="
				+ imagen + ", titulo=" + titulo + ", url=" + url + ", opcionperfils=" + opcionperfils + "]";
	}

	public Opcionperfil removeOpcionperfil(Opcionperfil opcionperfil) {
		getOpcionperfils().remove(opcionperfil);
		opcionperfil.setOpcion(null);

		return opcionperfil;
	}

}