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
                aux += rs.getString("id") + "    "
                        + rs.getString("name") + "    "
                        + rs.getString("cat") + "    "
                        + Integer.toString(rs.getInt("inventario")) + "\n";
            }
            return aux;
        } catch (SQLException e) {
            e.printStackTrace();
            invocarAlerta("Error al recuperar datos", Alert.AlertType.ERROR);
        }
        return null;
    }

    public void insertarPelicula(Film film, ArrayList<String> inventario){
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM registrar_film(?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, film.titulo);
            ps.setString(2, film.descripcion);
            ps.setInt(3, film.year);
            ps.setInt(4, film.lenguaje);
            ps.setInt(6, film.duracion);
            ps.setInt(5, film.duracionPrestamo);
            ps.setFloat(7, film.costoRemplazo);
            ps.setString(8, film.mpaaRating);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                if(rs.getInt(1) == -1){
                    invocarAlerta("Error al registrar", Alert.AlertType.ERROR);
                }
                else{
                    int filmID = rs.getInt(1);
                    for (String cat : film.cats) {
                        insertarCategoriasFilm(filmID, Integer.parseInt(Character.toString(cat.charAt(0))));
                    }
                    for (String act : film.acts) {
                        insertarActorFilm(filmID, Integer.parseInt(Character.toString(act.charAt(0))));
                    }
                    for (String s : inventario) {
                        insertarInventario(s, filmID);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            invocarAlerta("Error al recuperar datos", Alert.AlertType.ERROR);
        }
    }

    public void insertarCategoriasFilm(int filmID, int idCat){
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM registra_film_categoria(?, ?)");
            ps.setInt(1, filmID);
            ps.setInt(2, idCat);
            ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            invocarAlerta("Error al recuperar datos", Alert.AlertType.ERROR);
        }
    }

    public void insertarActorFilm(int filmID, int idAct){
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM registra_film_actor(?, ?)");
            ps.setInt(1, idAct);
            ps.setInt(2, filmID);
            ps.executeQuery();
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
            invocarAlerta("Error al recuperar datos", Alert.AlertType.ERROR);
        }
    }

    public String olapALQUILERESxMESxCATEGORIA(String mes){
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM alquiler_realizado_por_categoria(?)");
            ps.setString(1, mes);
            ResultSet rs = ps.executeQuery();
            String aux = "NumeroAlquileres    NombreCategoria\n";
            while(rs.next()){
                aux +=  Integer.toString(rs.getInt(1)) + "    "
                        + rs.getString(2) + "\n";
            }
            return aux;
        } catch (SQLException e) {
            e.printStackTrace();
            invocarAlerta("Error al recuperar datos", Alert.AlertType.ERROR);
        }
        return null;
    }

    public String olapALQUILERESyMONTOxDURACION(String duracion){
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM alquiler_monto_por_prestamo(?)");
            ps.setInt(1, Integer.parseInt(duracion));
            ResultSet rs = ps.executeQuery();
            String aux = "NumeroAlquileres MontoAlquileres Duracion\n";
            while(rs.next()){
                aux +=  Integer.toString(rs.getInt("numeroalquileres")) + "    "
                        + Double.toString(rs.getDouble("montoalquileres")) + "    "
                        + Integer.toString(rs.getInt("duracion")) + "\n";
            }
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
            return aux;
        } catch (SQLException e) {
            e.printStackTrace();
            invocarAlerta("Error al recuperar datos", Alert.AlertType.ERROR);
        }
        return null;
    }

    public void insertarInventario(String s, int filmID){
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM insertar_inventario(?, ?)");
            ps.setInt(1, Integer.parseInt(Character.toString(s.charAt(0))));
            ps.setInt(2, filmID);
            ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            invocarAlerta("Error al recuperar datos", Alert.AlertType.ERROR);
        }
    }


}