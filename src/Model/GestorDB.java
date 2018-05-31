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
    public Connection connection;
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
            ps.close();
            rs.close();
            return array;
        } catch (SQLException e) {
            e.printStackTrace();
            //invocarAlerta("Error al recuperar datos", Alert.AlertType.ERROR);
        }
        return new ArrayList<String>();
    }

    public ArrayList<String> getClientes(){
        try {
            ArrayList<String> array = new ArrayList<>();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM get_clientes()");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String nombre = rs.getString("id") + " - " +
                        rs.getString("nombre") + " " +
                        rs.getString("apellido");
                array.add(nombre);
            }
            ps.close();
            rs.close();
            return array;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<String>();
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

                else{
                    invocarAlerta("Insertado", Alert.AlertType.INFORMATION);
                }
            }
            ps.close();
            rs.close();
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
                int aux = rs.getInt(1);
                if(aux != 1){
                    invocarAlerta("Error al registrar", Alert.AlertType.ERROR);
                }
                else{
                    invocarAlerta("Insertado", Alert.AlertType.INFORMATION);
                }
            }
            ps.close();
            rs.close();
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
                aux += rs.getString("id") + "    "
                        + rs.getString("name") + "    "
                        + rs.getString("cat") + "    "
                        + Integer.toString(rs.getInt("inventario")) + "\n";
            }
            ps.close();
            rs.close();
            return aux;
        } catch (SQLException e) {
            e.printStackTrace();
            invocarAlerta("Error al recuperar datos", Alert.AlertType.ERROR);
        }
        return null;
    }

    public void insertarPelicula(Film film){
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM registrar_film(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, film.titulo);
            ps.setString(2, film.descripcion);
            ps.setInt(3, film.year);
            ps.setInt(4, film.lenguaje);
            ps.setInt(6, film.duracion);
            ps.setInt(5, film.duracionPrestamo);
            ps.setFloat(7, film.costoRemplazo);
            ps.setString(8, film.mpaaRating);
            ps.setArray(9, connection.createArrayOf("INT", film.acts.toArray()));
            ps.setArray(10, connection.createArrayOf("INT", film.cats.toArray()));
            ps.setArray(11, connection.createArrayOf("INT", film.cats.toArray()));
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                if(rs.getInt(1) == -1){
                    invocarAlerta("Error al registrar", Alert.AlertType.ERROR);
                }
                else{
                    invocarAlerta("Insertado", Alert.AlertType.INFORMATION);
                }
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            invocarAlerta("Error al recuperar datos", Alert.AlertType.ERROR);
        }
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
                else{
                    invocarAlerta("Insertado", Alert.AlertType.INFORMATION);
                }
            }
            ps.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            invocarAlerta("Error al recuperar datos", Alert.AlertType.ERROR);
        }
    }

    public String olapALQUILERESxMESxCATEGORIA(String mes){
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM alquiler_realizado_por_categoria(?)");
            ps.setInt(1, Integer.parseInt(mes));
            ResultSet rs = ps.executeQuery();
            String aux = "NumeroAlquileres    NombreCategoria\n";
            while(rs.next()){
                aux +=  Integer.toString(rs.getInt(1)) + "    "
                        + rs.getString(2) + "\n";
            }
            ps.close();
            rs.close();
            return aux;
        } catch (SQLException e) {
            e.printStackTrace();
            invocarAlerta("Error al recuperar datos", Alert.AlertType.ERROR);
        }
        return null;
    }

    public String olapALQUILERESyMONTOxDURACION(){
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM alquiler_monto_por_prestamo()");
            ResultSet rs = ps.executeQuery();
            String aux = "NumeroAlquileres MontoAlquileres Duracion\n";
            while(rs.next()){
                aux +=  Integer.toString(rs.getInt("numeroalquileres")) + "    "
                        + Double.toString(rs.getDouble("montoalquileres")) + "    "
                        + Integer.toString(rs.getInt("duracion")) + "\n";
            }
            ps.close();
            rs.close();
            return aux;
        } catch (SQLException e) {
            e.printStackTrace();
            invocarAlerta("Error al recuperar datos", Alert.AlertType.ERROR);
        }
        return null;
    }

    public String olapROLLUPxMESxANNO(){
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM rollup_por_anno()");
            ResultSet rs = ps.executeQuery();
            String aux = "";
            while(rs.next()){
                aux +=  Double.toString(rs.getDouble("montoalquileres")) + "    "
                        + rs.getString("anno") + "    "
                        + rs.getString("mes") + "\n";
            }
            ps.close();
            rs.close();
            return aux;
        } catch (SQLException e) {
            e.printStackTrace();
            invocarAlerta("Error al recuperar datos", Alert.AlertType.ERROR);
        }
        return null;
    }

    public String olapCUBExANNOxCATEGORIA(){
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM cube__por_anno_cat()");
            ResultSet rs = ps.executeQuery();
            String aux = "";
            while(rs.next()){
                aux +=  rs.getString("anno") + "    "
                        + rs.getString("categoria") + "    "
                        + Integer.toString(rs.getInt(3)) + "    "
                        + Double.toString(rs.getDouble(4)) + "\n";
            }
            ps.close();
            rs.close();
            return aux;
        } catch (SQLException e) {
            e.printStackTrace();
            invocarAlerta("Error al recuperar datos", Alert.AlertType.ERROR);
        }
        return null;
    }

}