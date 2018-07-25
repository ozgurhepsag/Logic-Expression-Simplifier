package application;
	
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.bind.ParseConversionEvent;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class Main extends Application {

	BooleanExpression boolexp;
	TruthTable inTable;
	FileReader fileReader;
	BufferedReader br;
    Stage window;
    TableView<TruthTableRow> inputTable = new TableView<>();
    TableView<TruthTableRow> outputTable = new TableView<>();
    KMap kmap;
	String path;
	Label label1, label2, label3; //for (in order of) input truthtable, output truthtable, karnaughmap
	Label input;
	Label output;
	Button read, select;
	Button editTt, editBe;
	Button submitBe, submitTt;
	GridPane karnaugh;
	TextField textField1 ,textField2, textField3, textField4; //for (in order of) path, boolean exp, simple boolean exp1, simple boolean exp2
	ChoiceBox cb;
	File file;
	FileChooser fileChooser; 
	String crossingColor = "-fx-control-inner-background: orange";
	String[] colors = {"-fx-control-inner-background: white", "-fx-control-inner-background: red", "-fx-control-inner-background: green", "-fx-control-inner-background: blue", "-fx-control-inner-background: yellow", "-fx-control-inner-background: #00FFFF", "-fx-control-inner-background: #D2691E", "-fx-control-inner-background: #FF7F50", "-fx-control-inner-background: purple", "-fx-control-inner-background: gray"};
	String[] colorinfo = {"white", "red", "green", "blue", "yellow", "aqua", "chocolate ", "coral", "purple", "gray"};	
	TextFlow tf;
	
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
    	
    	tf = new TextFlow();
    	
    	karnaugh = new GridPane();
		//karnaugh.setGridLinesVisible(true);
		karnaugh.setPadding(new Insets(10, 10, 10, 10));
		karnaugh.setVgap(15);
		karnaugh.setHgap(15);
		
    	window = primaryStage;
        window.setTitle("Logic Expression Simplication Tool");
        
        label1 = new Label("Input Truth Table");
        label1.setFont(new Font("Cambria", 15));
        
        label2 = new Label("Output Truth Table");
        label2.setFont(new Font("Cambria", 15));
        
        label3 = new Label("Karnaugh Map");
        label3.setFont(new Font("Cambria", 15));
        
        input = new Label("INPUT");
        input.setFont(new Font("Arial", 30));
        input.setPadding(new Insets(10,10,10,20));
        
        output = new Label("OUTPUT");
        output.setFont(new Font("Arial", 30));
        output.setPadding(new Insets(10,10,10,20));
		
		textField1 = new TextField();
		textField1.setPromptText("Path of your file");
		textField1.setPrefSize(300, 20);
		textField1.setEditable(false);
		
		textField2 = new TextField();
		textField2.setPromptText("Boolean expression");
		textField2.setPrefSize(300, 20);
		textField2.setEditable(false);
		
		textField3 = new TextField();
		textField3.setPromptText("Simplifier expression 1");
		textField3.setPrefSize(300, 20);
		textField3.setEditable(false);
		
		textField4 = new TextField();
		textField4.setPromptText("Simplifier expression 2");
		textField4.setPrefSize(300, 20);
		textField4.setEditable(false);
		
		read = new Button("Read File");
		select = new Button("Select File");
		editTt = new Button("Edit Truth Table");
		editBe = new Button("Edit Boolean Expression");
		submitBe = new Button("Submit");
		submitTt = new Button("Submit");
		
		cb = new ChoiceBox(FXCollections.observableArrayList(
				"Select variable number", 
				new Separator(),
			    "2", "3", "4"));
		
		cb.getSelectionModel().selectFirst();
		
		cb.setTooltip(new Tooltip("Select variable number."));
		
		fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll( new ExtensionFilter("Text File", "*txt"), 
				new ExtensionFilter("All files","*.*"));
		
		select.setOnAction(e-> { 
			
			file = fileChooser.showOpenDialog(primaryStage);
			
			if (file != null){
				path = file.getAbsolutePath().toString();
				textField1.setText(path);
			}			
			
		});
				
		read.setOnAction(e-> { 
			
			try {
				fileReader = new FileReader(path);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			br = new BufferedReader(fileReader);
			String line = "";
			
			try {
				line = br.readLine();
			} catch (Exception e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			
			inputTable.getColumns().clear();
			outputTable.getColumns().clear();
			
			if(path.substring(path.length() - 2).equalsIgnoreCase("be")){
				
				String[] variables2 = {"A", "B", "C", "D"};
				textField2.setText(line);	
				boolexp = new BooleanExpression(line);
				String variables[] = boolexp.getVariables();
				
				for (int i = 0; i < variables.length; i++) {

					TableColumn<TruthTableRow, String> column = new TableColumn<>(variables[i]);
					column.setMinWidth(100);
					
					if(i == variables.length - 1)
						column.setCellValueFactory(new PropertyValueFactory<TruthTableRow,String>("F"));
					else
						column.setCellValueFactory(new PropertyValueFactory<TruthTableRow,String>(variables2[i]));
						

	                inputTable.getColumns().add(column);
	            }
				
				try {
					inputTable.setItems(boolexp.getTableView());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				kmap = new KMap(boolexp.getStandardSOP());
				
				textField3.setText(boolexp.getSimpleexp());
							
				if (!(boolexp.getSimpleexp().equals("F = 1"))) {
					TruthTable temp = boolexp.ttOfSimpleBE();
					String[] tempvars = boolexp.getSimpleVars();

					for (int i = 0; i < tempvars.length; i++) {

						TableColumn<TruthTableRow, String> column = new TableColumn<>(tempvars[i]);
						column.setMinWidth(100);

						if (i == tempvars.length - 1)
							column.setCellValueFactory(new PropertyValueFactory<TruthTableRow, String>("F"));
						else
							column.setCellValueFactory(new PropertyValueFactory<TruthTableRow, String>(variables2[i]));

						outputTable.getColumns().add(column);
					}

					outputTable.setItems(temp.getTableView());
				}
				
				//textField4.setText(kmap.getSimpleStatement());
				makeColorful();			
				createKMap();
			
			}
			else if(path.substring(path.length() - 2).equalsIgnoreCase("tt")){
				
				String[] variables2 = {"A", "B", "C", "D"};
				String columns[] = line.split(",|;");
				String variables[] = columns;
				int k = 0;
				inTable = new TruthTable(columns);
				int[][] table = new int[(int) Math.pow(2, columns.length - 1)][columns.length];
				
				try {
					
					while ((line = br.readLine()) != null){
						
						columns = line.split(",|;");
						
						for (int j = 0; j < columns.length; j++) {
							
							if(columns[j].equalsIgnoreCase("X")){
							table[k][j] = -1;
							}
							else
								table[k][j] = Integer.parseInt(columns[j]);
						}

						k++;
						
					}
					
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				inTable.setTable(table);

				
				for (int i = 0; i < variables.length; i++) {

					TableColumn<TruthTableRow, String> column = new TableColumn<>(variables[i]);
					column.setMinWidth(100);
					
					if(i == variables.length - 1)
						column.setCellValueFactory(new PropertyValueFactory<TruthTableRow,String>("F"));
					else
						column.setCellValueFactory(new PropertyValueFactory<TruthTableRow,String>(variables2[i]));
						

	                inputTable.getColumns().add(column);
	            }
				
				try {
					inputTable.setItems(inTable.getTableView());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	
				inTable.createDontcares();
				kmap = new KMap(inTable.booleanExp(), inTable.getDontcares());
							
				textField2.setText(inTable.booleanExp());
				boolexp = new BooleanExpression(inTable.booleanExp());
				textField3.setText(boolexp.getSimpleexp());
				
				if (!(boolexp.getSimpleexp().equals("F = 1"))) {
					TruthTable temp = boolexp.ttOfSimpleBE();
					String[] tempvars = boolexp.getSimpleVars();

					for (int i = 0; i < tempvars.length; i++) {

						TableColumn<TruthTableRow, String> column = new TableColumn<>(tempvars[i]);
						column.setMinWidth(100);

						if (i == tempvars.length - 1)
							column.setCellValueFactory(new PropertyValueFactory<TruthTableRow, String>("F"));
						else
							column.setCellValueFactory(new PropertyValueFactory<TruthTableRow, String>(variables2[i]));

						outputTable.getColumns().add(column);
					}

					outputTable.setItems(temp.getTableView());

				}
				
				//textField4.setText(kmap.getSimpleStatement());
				makeColorful();			
				createKMap();
				
			}
			
			try {
				br.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		});
		
		editBe.setOnAction(e-> {
			
			textField2.setEditable(true);			
			
		});
		
		submitBe.setOnAction(e-> {			
			
			if (textField2.getText().length() > 4) {

				inputTable.getColumns().clear();
				outputTable.getColumns().clear();
				String[] variables2 = { "A", "B", "C", "D" };

				String expression = textField2.getText();
				textField2.setEditable(false);

				boolexp = new BooleanExpression(expression);
				String variables[] = boolexp.getVariables();
				
				kmap = new KMap(boolexp.getStandardSOP());
				
				for (int i = 0; i < variables.length; i++) {

					TableColumn<TruthTableRow, String> column = new TableColumn<>(variables[i]);
					column.setMinWidth(100);

					if (i == variables.length - 1)
						column.setCellValueFactory(new PropertyValueFactory<TruthTableRow, String>("F"));
					else
						column.setCellValueFactory(new PropertyValueFactory<TruthTableRow, String>(variables2[i]));

					inputTable.getColumns().add(column);
				}

				try {
					inputTable.setItems(boolexp.getTableView());
				} 
				catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				textField3.setText(boolexp.getSimpleexp());
								
				if (!(boolexp.getSimpleexp().equals("F = 1"))) {
					TruthTable temp = boolexp.ttOfSimpleBE();
					String[] tempvars = boolexp.getSimpleVars();

					for (int i = 0; i < tempvars.length; i++) {

						TableColumn<TruthTableRow, String> column = new TableColumn<>(tempvars[i]);
						column.setMinWidth(100);

						if (i == tempvars.length - 1)
							column.setCellValueFactory(new PropertyValueFactory<TruthTableRow, String>("F"));
						else
							column.setCellValueFactory(new PropertyValueFactory<TruthTableRow, String>(variables2[i]));

						outputTable.getColumns().add(column);
					}

					outputTable.setItems(temp.getTableView());
				}
			}
			
			//textField4.setText(kmap.getSimpleStatement());
			makeColorful();			
			createKMap();
			
			
		});
		
		editTt.setOnAction(e->{	
			
			inputTable.getColumns().clear();
			outputTable.getColumns().clear();
			
			if (!((String) cb.getSelectionModel().getSelectedItem()).equalsIgnoreCase("Select variable number")) {

				int choice = Integer.parseInt((String) cb.getSelectionModel().getSelectedItem());
				String[] variables = null;

				inputTable.setEditable(true);

				switch (choice) {
				case 2:
					variables = new String[] { "A", "B", "F" };
					break;
				case 3:
					variables = new String[] { "A", "B", "C", "F" };
					break;
				case 4:
					variables = new String[] { "A", "B", "C", "D", "F" };
					break;
				}

				inTable = new TruthTable(variables);

				for (int i = 0; i < variables.length - 1; i++) {

					TableColumn<TruthTableRow, String> column = new TableColumn<>(variables[i]);
					column.setMinWidth(100);
					column.setCellValueFactory(new PropertyValueFactory<TruthTableRow, String>(variables[i]));

					inputTable.getColumns().add(column);
				}

				TableColumn<TruthTableRow, String> columnf = new TableColumn<>(variables[variables.length - 1]);
				columnf.setMinWidth(100);
				columnf.setCellValueFactory(
						new PropertyValueFactory<TruthTableRow, String>(variables[variables.length - 1]));

				inputTable.getColumns().add(columnf);

				columnf.setCellFactory(TextFieldTableCell.forTableColumn());

				columnf.setOnEditCommit((TableColumn.CellEditEvent<TruthTableRow, String> t) -> {(t.getTableView()
						.getItems().get(t.getTablePosition().getRow())).setF(t.getNewValue());
						
					if(t.getNewValue().equalsIgnoreCase("X")){
						inTable.getTable()[t.getTablePosition().getRow()][inputTable.getColumns().size() - 1] = -1;
					}
					else if(t.getNewValue().equalsIgnoreCase("1") || t.getNewValue().equalsIgnoreCase("0")){
						inTable.getTable()[t.getTablePosition().getRow()][inputTable.getColumns().size() - 1] = Integer.parseInt(t.getNewValue());

					}								
					
				});
				
				inputTable.setItems(inTable.getTableView());
			}
		
		});
		
		submitTt.setOnAction(e->{	
			
			String[] variables2 = { "A", "B", "C", "D" };
			inputTable.setEditable(false);
			boolexp = new BooleanExpression(inTable.booleanExp());
			textField2.setText(inTable.booleanExp());
			textField3.setText(boolexp.getSimpleexp());
			inTable.createDontcares();
			kmap = new KMap(inTable.booleanExp() ,inTable.getDontcares());
			
			
			if (!(boolexp.getSimpleexp().equals("F = 1"))) {
				TruthTable temp = boolexp.ttOfSimpleBE();
				String[] tempvars = boolexp.getSimpleVars();

				for (int i = 0; i < tempvars.length; i++) {

					TableColumn<TruthTableRow, String> column = new TableColumn<>(tempvars[i]);
					column.setMinWidth(100);

					if (i == tempvars.length - 1)
						column.setCellValueFactory(new PropertyValueFactory<TruthTableRow, String>("F"));
					else
						column.setCellValueFactory(new PropertyValueFactory<TruthTableRow, String>(variables2[i]));

					outputTable.getColumns().add(column);
				}

				outputTable.setItems(temp.getTableView());
			}
			
			//textField4.setText(kmap.getSimpleStatement());
			makeColorful();
			createKMap();			
	
		});

		VBox main = new VBox();
				
		// input side
		HBox hb1 = new HBox(); 
		VBox vb1 = new VBox();
		VBox vb2 = new VBox();
		VBox vb3 = new VBox();	
		VBox vb4 = new VBox();
		
		// output side
		HBox hb2 = new HBox();
		VBox vb5 = new VBox();
		VBox vb6 = new VBox();
		VBox vb7 = new VBox();
		
		vb1.setSpacing(10);
		vb2.setSpacing(10);
		vb3.setSpacing(10);
		vb4.setSpacing(10);
		vb5.setSpacing(10);
		vb6.setSpacing(10);
		vb7.setSpacing(10);	

		vb1.getChildren().addAll(select, read, editTt, editBe);		
		vb2.getChildren().addAll(textField1, textField2, cb);
		vb3.getChildren().addAll(submitBe , submitTt);
		vb3.setPadding(new Insets(35,0,0,0));
		vb4.getChildren().addAll(label1, inputTable);

		vb5.getChildren().addAll(label2, outputTable);
		vb6.getChildren().addAll(new Label("Boolean Simplification"), textField3, new Label("Karnaugh Map Simplification"), tf);
		vb7.getChildren().addAll(label3, karnaugh);
		
		hb1.setPadding(new Insets(10,20,20,20)); 
		hb1.setSpacing(30);
		
		hb2.setPadding(new Insets(10,20,20,20)); 
		hb2.setSpacing(30);

		hb1.getChildren().addAll(vb1, vb2, vb3, vb4);
		hb2.getChildren().addAll(vb5, vb6, vb7);
		main.getChildren().addAll(input, hb1, output, hb2);

		Scene scene = new Scene(main, 1050, 900);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		window.setScene(scene);
		window.show();
		
		
    }
    
    public void makeColorful(){
    	
		tf.getChildren().add(new Text("F = "));
		
		for (int i = 0; i < kmap.getResult().size(); i++) {
			
			Text t = new Text();
			t.setStyle("-fx-fill: " + colorinfo[kmap.getResult().get(i).getColorid()] + ";");
			t.setText(kmap.getResult().get(i).getSubState());
			tf.getChildren().add(t);
			
			if(i != kmap.getResult().size() - 1){
				Text t1 = new Text(" + ");
				tf.getChildren().add(t1);
			}
			
		}  	
    	
    }
    
    public void createKMap(){
    	
		for (int i = 0; i < kmap.getMap().length; i++) {
			for (int j = 0; j < kmap.getMap()[i].length; j++) {
				
				TextField txt;
				String ttstr = "";
				Tooltip tt;
				
				if (kmap.getMap()[i][j].getValue() == 1) {
					txt = new TextField("1");
					
					if(kmap.getMap()[i][j].getColorid().size() > 1){
						txt.setStyle(crossingColor);
					}
					else if(kmap.getMap()[i][j].getColorid().size() == 1){
						txt.setStyle(colors[(int) kmap.getMap()[i][j].getColorid().get(0)]);
					}
					
					for (int k = 0; k < kmap.getMap()[i][j].getColorid().size(); k++) {						
						ttstr = ttstr + colorinfo[(int) kmap.getMap()[i][j].getColorid().get(k)] + ", ";											
					}
					
					ttstr = ttstr.substring(0, ttstr.length() - 2);					
					tt = new Tooltip(ttstr);
					txt.setTooltip(tt);
					
					karnaugh.add(txt, j, i);
				} 
				else if (kmap.getMap()[i][j].getValue() == 0) {
					txt = new TextField("0");
					
					if(kmap.getMap()[i][j].getColorid().size() > 1){
						txt.setStyle(crossingColor);
					}
					else if(kmap.getMap()[i][j].getColorid().size() == 1){
						txt.setStyle(colors[(int) kmap.getMap()[i][j].getColorid().get(0)]);
					}
					
					for (int k = 0; k < kmap.getMap()[i][j].getColorid().size(); k++) {						
						ttstr = ttstr + colorinfo[(int) kmap.getMap()[i][j].getColorid().get(k)] + ", ";											
					}
					
					ttstr = ttstr.substring(0, ttstr.length() - 2);					
					tt = new Tooltip(ttstr);
					txt.setTooltip(tt);
					
					karnaugh.add(txt, j, i);
				}
				else if (kmap.getMap()[i][j].getValue() == -1) {
					txt = new TextField("X");
					
					if(kmap.getMap()[i][j].getColorid().size() > 1){
						txt.setStyle(crossingColor);
					}
					else if(kmap.getMap()[i][j].getColorid().size() == 1){
						txt.setStyle(colors[(int) kmap.getMap()[i][j].getColorid().get(0)]);
					}
					
					for (int k = 0; k < kmap.getMap()[i][j].getColorid().size(); k++) {						
						ttstr = ttstr + colorinfo[(int) kmap.getMap()[i][j].getColorid().get(k)] + ", ";											
					}
					
					ttstr = ttstr.substring(0, ttstr.length() - 2);					
					tt = new Tooltip(ttstr);
					txt.setTooltip(tt);
					
					karnaugh.add(txt, j, i);
				}				
				
				
			}
		}
		
    }
    

}