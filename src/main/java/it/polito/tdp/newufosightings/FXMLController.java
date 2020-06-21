package it.polito.tdp.newufosightings;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.newufosightings.model.Model;
import it.polito.tdp.newufosightings.model.State;
import it.polito.tdp.newufosightings.model.StatoPesi;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

//controller turno A --> switchare al branch master_turnoB per turno B

public class FXMLController {

	private Model model;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextArea txtResult;

	@FXML
	private TextField txtAnno;

	@FXML
	private Button btnSelezionaAnno;

	@FXML
	private ComboBox<String> cmbBoxForma;

	@FXML
	private Button btnCreaGrafo;

	@FXML
	private TextField txtT1;

	@FXML
	private TextField txtAlfa;

	@FXML
	private Button btnSimula;

	@FXML
	void doCreaGrafo(ActionEvent event) {
		this.txtResult.clear();

		try {
			Integer anno;
			try {
				anno = Integer.parseInt(this.txtAnno.getText());
				if (anno < 1910 || anno > 2014) {
					this.txtResult.appendText(
							"Errore! L'anno inserito deve essere compreso tra 1910 e 2014 (estremi inclusi).\n");
					return;
				}
			} catch (NumberFormatException e) {
				this.txtResult.appendText("Errore! Devi inserire un valore intero rappresentante un anno.\n");
				return;
			}

			if (this.cmbBoxForma.getValue() == null) {
				this.txtResult.appendText("Errore! Devi selezionare una forma dall'apposito menu a tendina.\n");
				return;
			}

			String forma = this.cmbBoxForma.getValue();

			this.model.creaGrafo(anno, forma);
			this.txtResult.appendText("Grafo creato!\n");
			this.txtResult.appendText("# VERTICI: " + this.model.nVertici() + "\n");
			this.txtResult.appendText("# ARCHI: " + this.model.nArchi() + "\n\n");
			
			this.txtResult.appendText("Elenco stati con il rispettivo peso totale degli archi adiacenti:\n");
			for(StatoPesi sp : this.model.getStatesWithTotalWeight()) {
				this.txtResult.appendText(sp + "\n");
			}
		} catch (RuntimeException e) {
			this.txtResult.appendText("Errore nella creazione del grafo!\n");
			return;
		}

	}

	@FXML
	void doSelezionaAnno(ActionEvent event) {
		this.txtResult.clear();
		this.cmbBoxForma.getItems().clear();

		Integer anno;
		try {
			anno = Integer.parseInt(this.txtAnno.getText());
			if (anno < 1910 || anno > 2014) {
				this.txtResult.appendText(
						"Errore! L'anno inserito deve essere compreso tra 1910 e 2014 (estremi inclusi).\n");
				return;
			}
		} catch (NumberFormatException e) {
			this.txtResult.appendText("Errore! Devi inserire un valore intero rappresentante un anno.\n");
			return;
		}

		this.cmbBoxForma.getItems().addAll(this.model.getShapesByYear(anno));

	}

	@FXML
	void doSimula(ActionEvent event) {
		this.txtResult.clear();

		try {
			Integer anno;
			try {
				anno = Integer.parseInt(this.txtAnno.getText());
				if (anno < 1910 || anno > 2014) {
					this.txtResult.appendText(
							"Errore! L'anno inserito deve essere compreso tra 1910 e 2014 (estremi inclusi).\n");
					return;
				}
			} catch (NumberFormatException e) {
				this.txtResult.appendText("Errore! Devi inserire un valore intero rappresentante un anno.\n");
				return;
			}
			
			Integer T1;
			try {
				T1 = Integer.parseInt(this.txtT1.getText());
				if (T1 > 365) {
					this.txtResult.appendText(
							"Errore! Il valore di T1 deve essere minore di 365 (estremo escluso).\n");
					return;
				}
			} catch (NumberFormatException e) {
				this.txtResult.appendText("Errore! Devi inserire un valore intero per T1.\n");
				return;
			}
			
			Integer alfa;
			try {
				alfa = Integer.parseInt(this.txtAlfa.getText());
				if (alfa < 0 || alfa > 100) {
					this.txtResult.appendText(
							"Errore! Il valore di alfa deve essere compreso tra 0 e 100 (estremi inclusi).\n");
					return;
				}
			} catch (NumberFormatException e) {
				this.txtResult.appendText("Errore! Devi inserire un valore intero per alfa.\n");
				return;
			}

			if (this.cmbBoxForma.getValue() == null) {
				this.txtResult.appendText("Errore! Devi selezionare una forma dall'apposito menu a tendina.\n");
				return;
			}

			String forma = this.cmbBoxForma.getValue();
			
			this.model.simula(anno, forma, alfa, T1);
			
			this.txtResult.appendText("Elenco degli stati con il rispettivo DEFCON finale:\n\n");
			
			Map<State, Double> states = this.model.getStates();
			for(State s : states.keySet()) {
				this.txtResult.appendText(String.format("Stato: %s - DEFCON: %.1f\n", s, states.get(s)));
			}

			
		} catch (RuntimeException e) {
			this.txtResult.appendText("Errore! Per poter avviare la simulazione devi prima creare il grafo!\n");
			return;
		}
	}

	@FXML
	void initialize() {
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert btnSelezionaAnno != null : "fx:id=\"btnSelezionaAnno\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert cmbBoxForma != null : "fx:id=\"cmbBoxForma\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert txtT1 != null : "fx:id=\"txtT1\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert txtAlfa != null : "fx:id=\"txtAlfa\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
		assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";

	}

	public void setModel(Model model) {
		this.model = model;
	}
}
