package it.polito.tdp.newufosightings.model;

public class StatoPesi implements Comparable<StatoPesi>{

	private State state;
	private Integer pesoTot;

	public StatoPesi(State state, Integer pesoTot) {
		super();
		this.state = state;
		this.pesoTot = pesoTot;
	}

	public State getState() {
		return state;
	}

	public Integer getPesoTot() {
		return pesoTot;
	}

	@Override
	public String toString() {
		return "Stato: " + state + " - peso totale: " + pesoTot;
	}

	@Override
	public int compareTo(StatoPesi other) {
		return other.getPesoTot().compareTo(this.pesoTot);
	}

}
