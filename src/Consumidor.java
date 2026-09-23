public class Consumidor extends Thread {

    private final BuferCompartido bufer;
    private final int cantidadAConsumir;
    private final String nombre;

    public Consumidor(String nombre, BuferCompartido bufer, int cantidadAConsumir) {
        this.nombre = nombre;
        this.bufer = bufer;
        this.cantidadAConsumir = cantidadAConsumir;
    }

    @Override
    public void run() {
        for (int i = 1; i <= cantidadAConsumir; i++) {
            try {
                int dato = bufer.consumir();
                System.out.println("   -> " + nombre + " procesa el dato " + dato);
                Thread.sleep(70);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(nombre + " fue interrumpido.");
                return;
            }
        }
        System.out.println(nombre + " termino de consumir.");
    }
}
