import java.util.LinkedList;
import java.util.Queue;

/**
 * Implementacion del bufer compartido usando un MONITOR: la palabra clave
 * "synchronized" de Java convierte cada metodo en una region critica donde
 * solo un hilo puede estar a la vez. wait() y notifyAll() son las VARIABLES
 * DE CONDICION que permiten a un hilo "dormirse" cuando la condicion que
 * necesita (bufer no lleno / no vacio) todavia no se cumple, y despertar
 * a los demas cuando la condicion cambia.
 */
public class BuferMonitor implements BuferCompartido {

    private final Queue<Integer> cola = new LinkedList<>();
    private final int capacidad;

    public BuferMonitor(int capacidad) {
        this.capacidad = capacidad;
    }

    @Override
    public synchronized void producir(int dato) throws InterruptedException {
        // Mientras el bufer este lleno, el productor se duerme aqui.
        while (cola.size() == capacidad) {
            wait();
        }
        cola.add(dato);
        System.out.println("[Monitor] Productor inserto " + dato + " (tamaño bufer=" + cola.size() + ")");
        // Avisa a los consumidores dormidos que ya hay algo nuevo que consumir.
        notifyAll();
    }

    @Override
    public synchronized int consumir() throws InterruptedException {
        // Mientras el bufer este vacio, el consumidor se duerme aqui.
        while (cola.isEmpty()) {
            wait();
        }
        int dato = cola.poll();
        System.out.println("[Monitor] Consumidor retiro " + dato + " (tamaño bufer=" + cola.size() + ")");
        // Avisa a los productores dormidos que ya hay espacio libre.
        notifyAll();
        return dato;
    }
}
