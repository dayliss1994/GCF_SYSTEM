package ec.edu.upse.gcf.util;

import ec.edu.upse.gcf.editar.ModalidadEditar;
import ec.edu.upse.gcf.modelo.Campeonato;

public class Context {
	private final static Context instance = new Context();
	
	private Campeonato campeonatoSeleccionado;
	private Campeonato campeonatoC�digo; //para codigo
	private Integer idUsuarioLogeado;
	private ModalidadEditar grupoEditar;
	public static Context getInstance() {
		return instance;
	}

	public Campeonato getCampeonatoSeleccionado() {
		return campeonatoSeleccionado;
	}

	public void setCampeonatoSeleccionado(Campeonato campeonatoSeleccionado) {
		this.campeonatoSeleccionado = campeonatoSeleccionado;
	}

	public ModalidadEditar getGrupoEditar() {
		return grupoEditar;
	}

	public void setGrupoEditar(ModalidadEditar grupoEditar) {
		this.grupoEditar = grupoEditar;
	}

	public Campeonato getCampeonatoC�digo() {
		return campeonatoC�digo;
	}

	public void setCampeonatoC�digo(Campeonato campeonatoC�digo) {
		this.campeonatoC�digo = campeonatoC�digo;
	}

	public Integer getIdUsuarioLogeado() {
		return idUsuarioLogeado;
	}

	public void setIdUsuarioLogeado(Integer idUsuarioLogeado) {
		this.idUsuarioLogeado = idUsuarioLogeado;
	}
	
}
