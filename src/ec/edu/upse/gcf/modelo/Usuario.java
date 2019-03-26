package ec.edu.upse.gcf.modelo;

import java.io.Serializable;
import javax.persistence.*;

import org.eclipse.persistence.annotations.AdditionalCriteria;

import java.util.List;


/**
 * The persistent class for the usuario database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u"),
	@NamedQuery(name="Usuario.buscaUsuario", query="SELECT u FROM Usuario u WHERE u.usuario = :nombreUsuario"),
	@NamedQuery(name="Usuario.buscaPatron", query="SELECT u FROM Usuario u WHERE lower(u.nombres) like lower(:patron)"),
	@NamedQuery(name="Usuario.validarUsuario", query="SELECT u FROM Usuario u WHERE u.usuario  = (:usuario)"),
	@NamedQuery(name="Usuario.validaUsuarioExiste", query="SELECT u FROM Usuario u WHERE  u.cedula = (:cedulaUsuario)"),
	@NamedQuery(name="Usuario.validaCorreoExiste", query="SELECT u FROM Usuario u WHERE u.correo = lower(:correoUsuario)"),
	@NamedQuery(name="Usuario.buscaCorreo", query="SELECT u FROM Usuario u WHERE u.correo = :correo")
})
@AdditionalCriteria("this.estado IS NULL ")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_usuario")
	private Integer idUsuario;

	private String apellidos;

	private String cedula;

	private String clave;

	private String direccion;

	private String estado;

	private String nombres;

	private String telefono;

	private String correo;

	private String usuario;

	private boolean cambioclave;

	//bi-directional many-to-one association to Usuarioperfil
	@OneToMany(mappedBy="usuario")
	private List<Usuarioperfil> usuarioperfils;

	public Usuario() {
	}

	public Integer getIdUsuario() {
		return this.idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getCedula() {
		return this.cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getClave() {
		return this.clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getNombres() {
		return this.nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}	

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public List<Usuarioperfil> getUsuarioperfils() {
		return this.usuarioperfils;
	}

	public void setUsuarioperfils(List<Usuarioperfil> usuarioperfils) {
		this.usuarioperfils = usuarioperfils;
	}

	public boolean isCambioclave() {
		return cambioclave;
	}

	public void setCambioclave(boolean cambioclave) {
		this.cambioclave = cambioclave;
	}

	public Usuarioperfil addUsuarioperfil(Usuarioperfil usuarioperfil) {
		getUsuarioperfils().add(usuarioperfil);
		usuarioperfil.setUsuario(this);

		return usuarioperfil;
	}

	public Usuarioperfil removeUsuarioperfil(Usuarioperfil usuarioperfil) {
		getUsuarioperfils().remove(usuarioperfil);
		usuarioperfil.setUsuario(null);

		return usuarioperfil;
	}

	@Override
	public String toString() {
		return this.nombres;
	}
	/**@Override
	public String toString() {
		return "Usuario [idUsuario=" + idUsuario + ", apellidos=" + apellidos + ", cedula=" + cedula + ", clave="
				+ clave + ", direccion=" + direccion + ", estado=" + estado + ", nombres=" + nombres + ", telefono="
				+ telefono + ", correo=" + correo + ", usuario=" + usuario + ", cambioclave=" + cambioclave
				+ ", usuarioperfils=" + usuarioperfils + "]";
	}*/

}