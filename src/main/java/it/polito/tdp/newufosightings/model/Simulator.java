package it.polito.tdp.newufosightings.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.newufosightings.model.Event.EventType;
import it.polito.tdp.newufosightings.model.Event.Verifica;

public class Simulator {

	// CODA DEGLI EVENTI
	private PriorityQueue<Event> queue;

	// PARAMETRI DI SIMULAZIONE
	private int T1 = 50;
	private double alfa = 20.0 / 100.0;

	// MODELLO DEL MONDO
	private Graph<State, DefaultWeightedEdge> grafo;
	private Model model;

	// VALORI DA CALCOLARE
	private Map<State, Double> states;

	public Map<State, Double> getStates() {
		return states;
	}

	public void setT1(int t1) {
		T1 = t1;
	}

	public void setAlfa(int alfa) {
		this.alfa = alfa / 100.0;
	}

	public Simulator(Graph<State, DefaultWeightedEdge> grafo, Model model) {
		this.grafo = grafo;
		this.model = model;
	}

	public void init(int anno, String forma) {
		System.out.println(alfa);
		this.queue = new PriorityQueue<>();
		this.states = new HashMap<>();
		for (State state : this.grafo.vertexSet()) {
			states.put(state, 5.0);
		}

		for (Sighting s : this.model.loadSightingsByYearAndShape(anno, forma)) {
			Event e = new Event(EventType.AVVISTAMENTO, s.getDatetime(), s.getState(), null);
			this.queue.add(e);
		}
	}

	public void run() {
		while (!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			System.out.println(e);
			System.out.println(states);
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		switch (e.getType()) {
		case AVVISTAMENTO:
			Double nuovo = this.states.get(e.getState()) - 1; // SE nuovo < 1 NON POSSO AGGIUNGERLO!
			if (nuovo >= 1) {
				this.states.replace(e.getState(), nuovo);
				LocalDateTime cessato = e.getDate().plusDays(T1);
				this.queue.add(new Event(EventType.CESSATA_ALLERTA, cessato, e.getState(), Verifica.STATO));
			}
			double probabilita = Math.random();
			System.out.println(probabilita);
			if (probabilita < alfa) {
				// DEVO DECREMENTARE DI 0.5 IL DEFCON DI TUTTI GLI STATI ADIACENTI
				for (State vicino : Graphs.neighborListOf(this.grafo, e.getState())) {
					Double daAggiungere = this.states.get(vicino) - 0.5; // SE daAggiungere < 1 NON POSSO
																				// AGGIUNGERLO!
					if (daAggiungere >= 1) {
						this.states.replace(vicino, daAggiungere);
						System.out.println(vicino + " " + daAggiungere);
						LocalDateTime cessato = e.getDate().plusDays(T1);
						this.queue.add(new Event(EventType.CESSATA_ALLERTA, cessato, vicino, Verifica.ADIACENTE));
					}
				}
			}
			break;
		case CESSATA_ALLERTA:
			Double DEFCON;
			if (e.getVerifica() == Verifica.STATO) {
				DEFCON = this.states.get(e.getState()) + 1;
				if (DEFCON <= 5) {
					this.states.replace(e.getState(), DEFCON);
				}
			} else {
				DEFCON = this.states.get(e.getState()) + 0.5;
				if (DEFCON <= 5) {
					this.states.replace(e.getState(), DEFCON);
				}
			}
			break;
		}

	}

}
