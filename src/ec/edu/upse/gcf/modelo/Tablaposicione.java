package ec.edu.upse.gcf.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the tablaposiciones database table.
 * 
 */
@Entity
@Table(name="tablaposiciones")
@NamedQuery(name="Tablaposicione.findAll", query="SELECT t FROM Tablaposicione t")
public class Tablaposicione implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_taplapo")
	private int idTaplapo;

	private int gc;

	private int gd;

	private int gf;

	private int pe;

	private int pg;

	private int pj;

	private int pp;

	private int ptos;

	//bi-directional many-to-one association to Grupo
	@ManyToOne
	@JoinColumn(name="id_modalidad")
	private Modalidad modalidad;

	public Tablaposicione() {
	}

	public int getIdTaplapo() {
		return this.idTaplapo;
	}

	public void setIdTaplapo(int idTaplapo) {
		this.idTaplapo = idTaplapo;
	}

	public int getGc() {
		return this.gc;
	}

	public void setGc(int gc) {
		this.gc = gc;
	}

	public int getGd() {
		return this.gd;
	}

	public void setGd(int gd) {
		this.gd = gd;
	}

	public int getGf() {
		return this.gf;
	}

	public void setGf(int gf) {
		this.gf = gf;
	}

	public int getPe() {
		return this.pe;
	}

	public void setPe(int pe) {
		this.pe = pe;
	}

	public int getPg() {
		return this.pg;
	}

	public void setPg(int pg) {
		this.pg = pg;
	}

	public int getPj() {
		return this.pj;
	}

	public void setPj(int pj) {
		this.pj = pj;
	}

	public int getPp() {
		return this.pp;
	}

	public void setPp(int pp) {
		this.pp = pp;
	}

	public int getPtos() {
		return this.ptos;
	}

	public void setPtos(int ptos) {
		this.ptos = ptos;
	}

	public Modalidad getModalidad() {
		return this.modalidad;
	}

	public void setModalidad(Modalidad modalidad) {
		this.modalidad = modalidad;
	}

}