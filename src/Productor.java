/**
 * Hilo que genera datos y los inserta en el bufer compartido.
 * No sabe (ni le importa) que mecanismo de sincronizacion usa el bufer
 * por dentro, porque solo conoce la interfaz BuferCompartido.
 */
public class Productor extends Thread {

    private final BuferCompartido bufer;
    private final int cantidadAProducir;
    private final String nombre;

    public Productor(String nombre, BuferCompartido bufer, int cantidadAProducir) {
        this.nombre = nombre;
        this.bufer = bufer;
        this.cantidadAProducir = cantidadAProducir;
    }

    @Override
    public void run() {
        for (int i = 1; i <= cantidadAProducir; i++) {
            try {
                int dato = Integer.parseInt(nombre.replaceAll("[^0-9]", "")) * 100 + i;
                bufer.producir(dato);
                Thread.sleep(50); // simula que producir algo toma un poco de tiempo
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(nombre + " fue interrumpido.");
                return;
            }
        }
        System.out.println(nombre + " termino de producir.");
    }
}
