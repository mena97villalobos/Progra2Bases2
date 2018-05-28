package Model;

import Model.*;
import javafx.scene.control.Alert;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class GestorDB {
    private final String url;
    private String user;
    private String password;
    private Connection connection;
    public static GestorDB gestor;

    public GestorDB(String connectionString, String user, String password) {
        this.url = connectionString;
        try{
            this.user = user;
            this.password = password;
            this.connection = connect();
        }
        catch (Exception e){
            invocarAlerta("Error, usuario invalido", Alert.AlertType.ERROR);
        }
    }

    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
            System.out.println("Connected as: " + user);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private void invocarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert nuevaAlerta = new Alert(tipo);
        nuevaAlerta.setTitle("");
        nuevaAlerta.setContentText(mensaje);
        nuevaAlerta.showAndWait();
    }

    public ArrayList<String> getStaticTable(String SQL){
        try {
            ArrayList<String> array = new ArrayList<>();
            PreparedStatement ps = connection.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String nombre = rs.getString("id") + " - " + rs.getString("nombre");
                array.add(nombre);
            }
            return array;
        } catch (SQLException e) {
            e.printStackTrace();
            invocarAlerta("Error al recuperar datos", Alert.AlertType.ERROR);
        }
        return null;
    }

    public void registrarAlquiler(int idCliente, int idInventario, int idStaff){
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM registrar_alquiler(?, ?, ?)");
            ps.setInt(1, idCliente);
            ps.setInt(2, idInventario);
            ps.setInt(3, idStaff);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                if(rs.getInt(1) == -1){
                    invocarAlerta("Error al registrar", Alert.AlertType.ERROR);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            invocarAlerta("Error al recuperar datos", Alert.AlertType.ERROR);
        }
    }

    public void registrarDevolucion(int idCliente, int idRental, int idStaff, float monto){
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM registrar_pago(?, ?, ?, ?)");
            ps.setInt(1, idCliente);
            ps.setInt(2, idRental);
            ps.setInt(3, idStaff);
            ps.setFloat(4, monto);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                if(rs.getInt(1) != 1){
                    invocarAlerta("Error al registrar", Alert.AlertType.ERROR);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            invocarAlerta("Error al recuperar datos", Alert.AlertType.ERROR);
        }
    }

    public String buscarPelicula(String titulo){
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM buscar_pelicula(?)");
            ps.setString(1, titulo);
            ResultSet rs = ps.executeQuery();
            String aux = "";
            while(rs.next()){
                aux += rs.getString("id") + " "
                        + rs.getString("name") + " "
                        + rs.getString("cat") + " "
                        + Integer.toString(rs.getInt("inventario")) + "\n";
            }
            return aux;
        } catch (SQLException e) {
            e.printStackTrace();
            invocarAlerta("Error al recuperar datos", Alert.AlertType.ERROR);
        }
        return null;
    }

    public void insertarPelicula(Film film){

    }

    public void insertarCliente(String nombre, String apellido, String email, int idAddress, int idTienda){
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM crear_cliente(?, ?, ?, ?, ?)");
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, email);
            ps.setInt(4, idAddress);
            ps.setInt(5, idTienda);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                if(rs.getInt(1) == -1){
                    invocarAlerta("Error al registrar", Alert.AlertType.ERROR);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            invocarAlerta("Error al recuperar datos", Alert.AlertType.ERROR);
        }
    }

}