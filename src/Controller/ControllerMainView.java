package Controller;

import Model.Consultas;
import Model.Film;
import Model.GestorDB;
import javafx.collections.FXCollections;
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
    public TextField releaseDate;
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
    @FXML
    public ComboBox consultas;
    @FXML
    public Button consultar;
    @FXML
    public TextArea olapResults;
    @FXML
    public TextField variables;
    @FXML
    public Button saveParam;
    @FXML
    public ComboBox inventarioFilm;
    @FXML
    public ComboBox tiendaFilm;
    @FXML
    public Button annadirInventario;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setRatings();
        setAddress();
        setIdTienda();
        setConsultas();
        setClientes();
        setStaff();
        setInventario();
        setRentals();
        try{
            setCategorias();
            setActores();
            setLenguajes();
        }
        catch (Exception e){ }
        variables.setDisable(true);
        saveParam.setDisable(true);
        guardar.setOnAction(event -> {
            String titulo = title.getText();
            String descripcion = desc.getText();
            int duracionPrestamo = Integer.parseInt(rentalDuration.getText());
            int duracion = Integer.parseInt(lenght.getText());
            float costoRemplazo = Float.parseFloat(replacementCost.getText());
            String mpaaRating = (String) rating.getSelectionModel().getSelectedItem();
            String l = (String) lenguaje.getSelectionModel().getSelectedItem();
            int idLenguaje = Integer.parseInt(Character.toString(l.charAt(0)));
            int year = Integer.parseInt(releaseDate.getText());
            ArrayList<String> cats = new ArrayList<>(catAdd.getItems());
            ArrayList<String> acts = new ArrayList<>(actAdd.getItems());
            ArrayList<String> inventario = new ArrayList<>(inventarioFilm.getItems());
            Film f = new Film(titulo, descripcion, duracionPrestamo, duracion, costoRemplazo, mpaaRating,
                    idLenguaje, cats, acts, year);
            GestorDB.gestor.insertarPelicula(f, inventario);
        });
        annadirAct.setOnAction(event -> {
            ArrayList<String> currentItems = new ArrayList<>(actAdd.getItems());
            currentItems.add((String) actDis.getSelectionModel().getSelectedItem());
            actAdd.setItems(FXCollections.observableArrayList(currentItems));
            actDis.getSelectionModel().clearSelection();
        });
        annadirCat.setOnAction(event -> {
            ArrayList<String> currentItems = new ArrayList<>(catAdd.getItems());
            currentItems.add((String) catDis.getSelectionModel().getSelectedItem());
            catAdd.setItems(FXCollections.observableArrayList(currentItems));
            catDis.getSelectionModel().clearSelection();
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
            String rs = "ID    NOMBRE    CATEGORIA    INVENTARIO\n" + GestorDB.gestor.buscarPelicula(tituloBusqueda.getText());
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
        consultar.setOnAction(event -> {
            GestorDB gestorMaestro = GestorDB.gestor;
            GestorDB.gestor = new GestorDB("jdbc:postgresql://localhost:5434/Progra2", "consultas", "9545");
            String aux = "";
            consultas.setDisable(true);
            switch ((Consultas) consultas.getSelectionModel().getSelectedItem()){
                case ALQUILERESxMESxCATEGORIA:
                    variables.setDisable(false);
                    saveParam.setDisable(false);
                    variables.setPromptText("Mes a Consultar");
                    break;
                case ALQUILERESyMONTOxDURACION:
                    variables.setDisable(false);
                    saveParam.setDisable(false);
                    variables.setPromptText("Duración a Consultar");
                    break;
                case ROLLUPxMESxANNO:
                    aux = "MontoAlquiler    Año    Mes\n"
                            + GestorDB.gestor.olapROLLUPxMESxANNO();
                    consultas.setDisable(false);
                    variables.setPromptText("");
                    variables.setDisable(true);
                    saveParam.setDisable(true);
                    break;
                case CUBExANNOxCATEGORIA:
                    aux = "Año    Categoría    NumeroAlquileres    MontoAlquileres\n"
                            + GestorDB.gestor.olapCUBExANNOxCATEGORIA();
                    consultas.setDisable(false);
                    variables.setPromptText("");
                    variables.setDisable(true);
                    saveParam.setDisable(true);
                    break;
                default:
                    aux = "Error de consulta";
                    consultas.setDisable(false);
                    variables.setPromptText("");
                    variables.setDisable(true);
                    saveParam.setDisable(true);
                    break;
            }
            GestorDB.gestor = gestorMaestro;
            olapResults.setText(aux);
        });
        saveParam.setOnAction(event -> {
            GestorDB gestorMaestro = GestorDB.gestor;
            GestorDB.gestor = new GestorDB("jdbc:postgresql://localhost:5434/Progra2", "consultas", "9545");
            String aux = "";
            String param = variables.getText();
            switch ((Consultas) consultas.getSelectionModel().getSelectedItem()) {
                case ALQUILERESxMESxCATEGORIA:
                    aux = GestorDB.gestor.olapALQUILERESxMESxCATEGORIA(param);
                    break;
                case ALQUILERESyMONTOxDURACION:
                    aux = GestorDB.gestor.olapALQUILERESyMONTOxDURACION(param);
                    break;
                default:
                    aux = "Error";
                    break;
            }
            olapResults.setText(aux);
            consultas.setDisable(false);
            saveParam.setDisable(true);
            variables.setDisable(true);
            GestorDB.gestor = gestorMaestro;
        });
        annadirInventario.setOnAction(event -> {
            ArrayList<String> aux = new ArrayList<>(inventarioFilm.getItems());
            aux.add((String) tiendaFilm.getSelectionModel().getSelectedItem());
            inventarioFilm.setItems(FXCollections.observableArrayList(aux));
            tiendaFilm.getSelectionModel().clearSelection();
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
        tiendaFilm.setItems(tiendasObservable);
    }

    private void setConsultas(){
        consultas.setItems(FXCollections.observableArrayList(Consultas.values()));
    }

    private void setClientes(){
        ArrayList<String> clientes = GestorDB.gestor.getClientes();
        idClienteDevolucion.setItems(FXCollections.observableArrayList(clientes));
        idClienteAlquiler.setItems(FXCollections.observableArrayList(clientes));
    }

    private void setStaff(){
        ArrayList<String> staff = GestorDB.gestor.getStaticTable("SELECT * FROM get_staff()");
        idStaffDevolucion.setItems(FXCollections.observableArrayList(staff));
        idStaffAlquiler.setItems(FXCollections.observableArrayList(staff));
    }

    private void setInventario(){
        ArrayList<String> inventario = GestorDB.gestor.getStaticTable("SELECT * FROM get_inventory()");
        idInventario.setItems(FXCollections.observableArrayList(inventario));
    }

    private void setRentals(){
        ArrayList<String> inventario = GestorDB.gestor.getStaticTable("SELECT * FROM get_rentals()");
        idRental.setItems(FXCollections.observableArrayList(inventario));
    }
}
