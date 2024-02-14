package database;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoadTable{

    // connecting to database and loading data
    public static void loadData(TableView<ObservableList<String>> homeTable, ResultSet resultSet)
            throws SQLException {
        ObservableList<ObservableList<String>> loadedData = FXCollections.observableArrayList();

        // creating columns dynamically
        if(resultSet != null){
            try{
                for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                    final int index2 = i;

                    // lambda expression
                    TableColumn<ObservableList<String>, String> column = new TableColumn<>(
                            resultSet.getMetaData().getColumnName(i + 1));
                    column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(index2)));

                    String columnName = resultSet.getMetaData().getColumnName(i + 1);
                    if ( columnName.contains("Create_Date") || columnName.contains("Created_By")
                            || columnName.contains("Last_Update") || columnName.contains("Last_Updated_By")){

                        System.out.println("Skipping column "+ columnName);
                    }
                    else{
                        homeTable.getColumns().add(column);
                    }
                }

                // populating rows with data
                while(resultSet.next()){
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for(int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++){
                        row.add(resultSet.getString(i));
                    }
                    loadedData.add(row);
                }
                // loading data
                homeTable.setItems(loadedData);
                System.out.println("Successfully loaded data.");

            }catch (Exception exception){
                exception.printStackTrace();
                System.out.println("Could not populate data!");
            }
        }
    }


}
