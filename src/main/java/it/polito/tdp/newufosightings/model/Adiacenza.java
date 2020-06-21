package it.polito.tdp.newufosightings.model;

public class Adiacenza {

	private State state1;
	private State state2;
	private Integer peso;

	public Adiacenza(State state1, State state2, Integer peso) {
		super();
		this.state1 = state1;
		this.state2 = state2;
		this.peso = peso;
	}

	public State getState1() {
		return state1;
	}

	public void setState1(State state1) {
		this.state1 = state1;
	}

	public State getState2() {
		return state2;
	}

	public void setState2(State state2) {
		this.state2 = state2;
	}

	public Integer getPeso() {
		return peso;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}

}
