package Model;

import java.util.ArrayList;

public class Film {
    String titulo;
    String descripcion;
    int duracionPrestamo;
    int duracion;
    float costoRemplazo;
    String mpaaRating;
    int lenguaje;
    ArrayList<String> cats;
    ArrayList<String> acts;

    public Film(String titulo, String descripcion, int duracionPrestamo, int duracion, float costoRemplazo,
                String mpaaRating, int lenguaje, ArrayList cats, ArrayList acts) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.duracionPrestamo = duracionPrestamo;
        this.duracion = duracion;
        this.costoRemplazo = costoRemplazo;
        this.mpaaRating = mpaaRating;
        this.lenguaje = lenguaje;
        this.acts = acts;
        this.cats = cats;
    }
}
