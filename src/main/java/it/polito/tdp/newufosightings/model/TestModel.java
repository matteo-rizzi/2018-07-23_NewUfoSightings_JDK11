package it.polito.tdp.newufosightings.model;

public class TestModel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Model m = new Model();
		
		m.creaGrafo(2000, "circle");
		
		m.simula(2000, "circle", 20, 75);
		System.out.println(m.getStates());
	}

}
