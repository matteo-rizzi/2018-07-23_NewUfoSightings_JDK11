package it.polito.tdp.newufosightings.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;

public class Model {
	
	public NewUfoSightingsDAO dao;
	private Map<String, State> idMap;
	private Graph<State, DefaultWeightedEdge> grafo;
	private Simulator simulator;
	
	public Model() {
		this.dao = new NewUfoSightingsDAO();
		this.idMap = new HashMap<>();
		this.dao.loadAllStates(this.idMap);
	}
	
	public List<String> getShapesByYear(int anno) {
		return this.dao.getShapesByYear(anno);
	}
	
	public void creaGrafo(int anno, String forma) {
		this.grafo = new SimpleWeightedGraph<State, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		// aggiungo i vertici
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		// aggiungo gli archi
		for(Adiacenza a : this.dao.getAdiacenze(this.idMap, anno, forma)) {
			Graphs.addEdge(this.grafo, a.getState1(), a.getState2(), a.getPeso());
		}
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<StatoPesi> getStatesWithTotalWeight() {
		List<StatoPesi> result = new ArrayList<>();
		for(State state : this.grafo.vertexSet()) {
			int pesoTot = 0;
			for(State vicino : Graphs.neighborListOf(this.grafo, state)) {
				pesoTot += this.grafo.getEdgeWeight(this.grafo.getEdge(state, vicino));
			}
			
			result.add(new StatoPesi(state, pesoTot));
		}
		Collections.sort(result);
		return result;
	}
	
	public List<Sighting> loadSightingsByYearAndShape(int anno, String forma) {
		return this.dao.loadSightingsByYearAndShape(anno, forma, idMap);
	}
	
	public void simula(int anno, String forma, int alfa, int T1) {
		this.simulator = new Simulator(this.grafo, this);
		this.simulator.setAlfa(alfa);
		this.simulator.setT1(T1);
		this.simulator.init(anno, forma);
		this.simulator.run();
	}
	
	public Map<State, Double> getStates() {
		return this.simulator.getStates();
	}

}
