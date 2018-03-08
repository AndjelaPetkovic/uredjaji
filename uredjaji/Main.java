package uredjaji;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {
	
	private static Stage mainStage;
	private static List<Artikal> listaUredjaja;
	private static File file = null;
	private static String deoZaPrag;
	
	private static TextArea ispisiTA = new TextArea();
	private static RadioButton telefoniBtn, tabletiBtn;
	private static Button izaberiBtn, izracunajBtn;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage mainStage) throws Exception {
		
		mainStage.setTitle("Nabavka uredjaja");
		
		VBox root = new VBox(5);
		makeGUI(root);
		
		Scene scene = new Scene(root, 600, 350);
		mainStage.setScene(scene);
		mainStage.setResizable(false);
		mainStage.show();
		
	}
	
	public void makeGUI(VBox root) {
		
/*		TextArea ispisiTA = new TextArea();
		RadioButton telefoniBtn, tabletiBtn;
		Button izaberiBtn, izracunajBtn; */
		
		HBox top = new HBox(5); //gornji deo sa TextArea i dva Radiodugmeta
		ispisiTA.setEditable(false);
		ispisiTA.setPrefSize(520, 300);
		VBox topRight = new VBox(5);
		telefoniBtn = new RadioButton("Telefoni");
		telefoniBtn.setSelected(true);
		tabletiBtn = new RadioButton("Tableti");
		
		ToggleGroup tg = new ToggleGroup();
		telefoniBtn.setToggleGroup(tg);
		tabletiBtn.setToggleGroup(tg);
		
		topRight.getChildren().addAll(telefoniBtn, tabletiBtn);
		topRight.setAlignment(Pos.CENTER_LEFT);
		
		top.getChildren().addAll(ispisiTA, topRight);
		top.setPadding(new Insets(10));
		
		HBox bottom = new HBox(5);
		izaberiBtn = new Button("Izaberi");
		izracunajBtn = new Button("Izracunaj");
		
		izaberi();
		izracunaj();
		
		bottom.getChildren().addAll(izaberiBtn, izracunajBtn);
		bottom.setPadding(new Insets(0, 0, 10, 0));
		bottom.setAlignment(Pos.CENTER);
		
		root.getChildren().addAll(top, bottom);
	}
	
	public void izaberi() {
		izaberiBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				
				listaUredjaja = new ArrayList<Artikal>();
				ispisiTA.clear();
				StringBuilder sb = new StringBuilder();
				file = otvoriFile();
					
				if (file != null) {
					try {
						List<String> linije = Files.readAllLines(Paths.get(file.getName()), StandardCharsets.UTF_8);
						if(!linije.isEmpty()) {
							for(String linija : linije) {
								sb.append(linija + "\n");
								String[] podaci = linija.split(", ");
								String tip = podaci[0].trim();
								String naziv = podaci[1].trim();
								float cena = Float.parseFloat(podaci[2].trim());
								String dodatnaKarakteristika = podaci[3].trim();
								
								if (tip.equals("TEL")) {
									boolean pretplata = Telefon.pretplata(dodatnaKarakteristika);
									Telefon telefon = new Telefon(naziv, cena, pretplata);
									listaUredjaja.add(telefon);
								}
								else {
									int popust = Integer.parseInt(dodatnaKarakteristika);
									Tablet tablet = new Tablet(naziv, cena, popust);
									listaUredjaja.add(tablet);
								}
							}
							deoZaPrag = "Svi uredjaji u ponudi: \n\n" + sb.toString();
							ispisiTA.setText(deoZaPrag);
						}
						else {
							ispisiTA.setText("Prazan spisak uredjaja.");
						}
					}				
					catch (IOException | NumberFormatException | NullPointerException e) {
						System.out.println("Greska pri radu sa datotekom.");
						System.exit(1); // prekid daljeg izvrsavanja programa
					}
				}
				else {
					ispisiTA.setText("Pogresno izabran dokument.");
				}		
			}
			
		});
	}
	
	public File otvoriFile() {

		File file = null;
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Open Resourse File");
		chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("text files", "*.txt"));
		file = chooser.showOpenDialog(mainStage);
		
		return file;
	}
	
	public void izracunaj() {
		izracunajBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				float suma = 0f;
				if (file == null || listaUredjaja.isEmpty()) {
					ispisiTA.setText("Niste izabrali dokument. Pokusajte ponovo");
				}
				else {
					List<Artikal> uredjaji = new ArrayList<>();
					ispisiTA.setText(deoZaPrag);
					if (telefoniBtn.isSelected()) {
						for (Artikal uredjaj : listaUredjaja) {
							if (uredjaj instanceof Telefon) {
								uredjaji.add(uredjaj);
								suma += uredjaj.prodajnaCena();
							}
						}
						String text = (!uredjaji.isEmpty()) ?
								"\nTelefoni u ponudi: \n" + Artikal.listajUredjaje(uredjaji) +
								"\nUKUPNO: " + suma + " rsd" :
									"\nNema telefona u ponudi.";		
						ispisiTA.appendText(text);
					}
					else if (tabletiBtn.isSelected()) {
						for (Artikal uredjaj : listaUredjaja) {
							if (uredjaj instanceof Tablet) {
								uredjaji.add(uredjaj);
								suma += uredjaj.prodajnaCena();
							}
						}
						String text = (!uredjaji.isEmpty()) ?
								"\nTableti u ponudi: \n" + Artikal.listajUredjaje(uredjaji) +
								"\nUKUPNO: " + suma + " rsd" :
									"\nNema tableta u ponudi.";		
						ispisiTA.appendText(text);
					}
				}
				
			}
			
		});
	}

}
