package Controller;

import Model.Film;
import Model.GestorDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ControllerMainView implements Initializable {
    @FXML
    public ComboBox catDis;
    @FXML
    public ComboBox actDis;
    @FXML
    public Button annadirCat;
    @FXML
    public Button annadirAct;
    @FXML
    public ComboBox catAdd;
    @FXML
    public ComboBox actAdd;
    @FXML
    public TextField title;
    @FXML
    public TextField desc;
    @FXML
    public DatePicker releaseDate;
    @FXML
    public ComboBox lenguaje;
    @FXML
    public TextField rentalDuration;
    @FXML
    public TextField lenght;
    @FXML
    public TextField replacementCost;
    @FXML
    public ComboBox rating;
    @FXML
    public Button guardar;
    @FXML
    public TextField fName;
    @FXML
    public TextField lName;
    @FXML
    public TextField email;
    @FXML
    public ComboBox address;
    @FXML
    public Button registrar;
    @FXML
    public TextField tituloBusqueda;
    @FXML
    public Button buscar;
    @FXML
    public TextArea resultados;
    @FXML
    public ComboBox idClienteAlquiler;
    @FXML
    public ComboBox idInventario;
    @FXML
    public ComboBox idStaffAlquiler;
    @FXML
    public ComboBox idClienteDevolucion;
    @FXML
    public ComboBox idRental;
    @FXML
    public ComboBox idStaffDevolucion;
    @FXML
    public TextField monto;
    @FXML
    public Button registrarAlquiler;
    @FXML
    public Button registrarDevolucion;
    @FXML
    public ComboBox idTienda;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setCategorias();
        setActores();
        setLenguajes();
        setRatings();
        setAddress();
        setIdTienda();
        //TODO añadir las categorias a la pelicula creada
        guardar.setOnAction(event -> {
            String titulo = title.getText();
            String descripcion = desc.getText();
            int duracionPrestamo = Integer.parseInt(rentalDuration.getText());
            int duracion = Integer.parseInt(lenght.getText());
            float costoRemplazo = Float.parseFloat(replacementCost.getText());
            String mpaaRating = (String) rating.getSelectionModel().getSelectedItem();
            String l = (String) lenguaje.getSelectionModel().getSelectedItem();
            int idLenguaje = Integer.parseInt(Character.toString(l.charAt(0)));
            ArrayList<String> cats = new ArrayList<>(catAdd.getItems());
            ArrayList<String> acts = new ArrayList<>(actAdd.getItems());
            Film f = new Film(titulo, descripcion, duracionPrestamo, duracion, costoRemplazo, mpaaRating,
                    idLenguaje, cats, acts);
            GestorDB.gestor.insertarPelicula(f);
        });
        annadirAct.setOnAction(event -> {
            ArrayList<String> currentItems = new ArrayList<>(actAdd.getItems());
            currentItems.add((String) actDis.getSelectionModel().getSelectedItem());
            actAdd.setItems(FXCollections.observableArrayList(currentItems));

        });
        annadirCat.setOnAction(event -> {
            ArrayList<String> currentItems = new ArrayList<>(catAdd.getItems());
            currentItems.add((String) catDis.getSelectionModel().getSelectedItem());
            catAdd.setItems(FXCollections.observableArrayList(currentItems));
        });
        registrar.setOnAction(event -> {
            String nombre = fName.getText();
            String apellido = lName.getText();
            String mail = email.getText();
            String tienda = (String) idTienda.getSelectionModel().getSelectedItem();
            int idStore = Integer.parseInt(Character.toString(tienda.charAt(0)));
            String direccion = (String) address.getSelectionModel().getSelectedItem();
            int idDireccion = Integer.parseInt(Character.toString(direccion.charAt(0)));
            GestorDB.gestor.insertarCliente(nombre, apellido, mail, idDireccion, idStore);
        });
        buscar.setOnAction(event -> {
            String rs = "ID    NOMBRE  CATEGORIA   INVENTARIO\n" + GestorDB.gestor.buscarPelicula(tituloBusqueda.getText());
            resultados.setText(rs);
        });
        registrarAlquiler.setOnAction(event -> {
            String idCliente = (String)idClienteAlquiler.getSelectionModel().getSelectedItem();
            String idI = (String) idInventario.getSelectionModel().getSelectedItem();
            String idStaff = (String)idStaffAlquiler.getSelectionModel().getSelectedItem();
            int aux1 = Integer.parseInt(Character.toString(idCliente.charAt(0)));
            int aux2 = Integer.parseInt(Character.toString(idI.charAt(0)));
            int aux3 = Integer.parseInt(Character.toString(idStaff.charAt(0)));
            GestorDB.gestor.registrarAlquiler(aux1, aux2, aux3);
        });
        registrarDevolucion.setOnAction(event -> {
            String idCliente = (String)idClienteDevolucion.getSelectionModel().getSelectedItem();
            String idR = (String) idRental.getSelectionModel().getSelectedItem();
            String idStaff = (String)idStaffDevolucion.getSelectionModel().getSelectedItem();
            int aux1 = Integer.parseInt(Character.toString(idCliente.charAt(0)));
            int aux2 = Integer.parseInt(Character.toString(idR.charAt(0)));
            int aux3 = Integer.parseInt(Character.toString(idStaff.charAt(0)));
            float aux4 = Float.parseFloat(monto.getText());
            GestorDB.gestor.registrarDevolucion(aux1, aux2, aux3, aux4);
        });
    }

    private void setCategorias(){
        ArrayList<String> categorias = GestorDB.gestor.getStaticTable("SELECT * FROM get_categorias()");
        ObservableList<String> catObservable = FXCollections.observableArrayList(categorias);
        catDis.setItems(catObservable);
    }
    
    private void setActores(){
        ArrayList<String> actores = GestorDB.gestor.getStaticTable("SELECT * FROM get_actores()");
        ObservableList<String> actObservable = FXCollections.observableArrayList(actores);
        actDis.setItems(actObservable);
    }

    private void setLenguajes(){
        ArrayList<String> lenguajes = GestorDB.gestor.getStaticTable("SELECT * FROM get_language()");
        ObservableList<String> lenguajesObservable = FXCollections.observableArrayList(lenguajes);
        lenguaje.setItems(lenguajesObservable);
    }

    private void setRatings(){
        String[] aux = {"G", "PG", "PG-13", "R", "NC-17"};
        ArrayList<String> ratings = new ArrayList<>();
        ratings.addAll(Arrays.asList(aux));
        rating.setItems(FXCollections.observableArrayList(ratings));
    }

    private void setAddress(){
        ArrayList<String> add = GestorDB.gestor.getStaticTable("SELECT * FROM get_address()");
        ObservableList<String> addObservable = FXCollections.observableArrayList(add);
        address.setItems(addObservable);
    }

    private void setIdTienda(){
        ArrayList<String> tiendas = GestorDB.gestor.getStaticTable("SELECT * FROM get_tiendas()");
        ObservableList<String> tiendasObservable = FXCollections.observableArrayList(tiendas);
        idTienda.setItems(tiendasObservable);
    }
}
