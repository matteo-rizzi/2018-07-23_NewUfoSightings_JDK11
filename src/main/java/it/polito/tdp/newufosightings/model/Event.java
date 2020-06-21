package it.polito.tdp.newufosightings.model;

import java.time.LocalDateTime;

public class Event implements Comparable<Event> {

	public enum EventType {
		AVVISTAMENTO, CESSATA_ALLERTA
	}

	public enum Verifica {
		STATO, ADIACENTE
	}

	private EventType type;
	private LocalDateTime date;
	private State state; // STATO IN CUI VI E' STATO L'AVVISTAMENTO CORRENTE
	private Verifica verifica;

	public Event(EventType type, LocalDateTime date, State state, Verifica verifica) {
		super();
		this.type = type;
		this.date = date;
		this.state = state;
		this.verifica = verifica;
	}

	public Verifica getVerifica() {
		return verifica;
	}

	public void setVerifica(Verifica verifica) {
		this.verifica = verifica;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	@Override
	public int compareTo(Event other) {
		return this.date.compareTo(other.date);
	}

	@Override
	public String toString() {
		return "Event [type=" + type + ", date=" + date + ", state=" + state + ", verifica=" + verifica + "]";
	}
}
