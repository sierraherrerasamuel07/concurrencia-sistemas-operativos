import java.util.LinkedList;
import java.util.Queue;

public class BuferMonitor implements BuferCompartido {

    private final Queue<Integer> cola = new LinkedList<>();
    private final int capacidad;

    public BuferMonitor(int capacidad) {
        this.capacidad = capacidad;
    }

    @Override
    public synchronized void producir(int dato) throws InterruptedException {
        while (cola.size() == capacidad) {
            wait();
        }
        cola.add(dato);
        System.out.println("[Monitor] Productor inserto " + dato + " (tamaño bufer=" + cola.size() + ")");
        notifyAll();
    }

    @Override
    public synchronized int consumir() throws InterruptedException {
        while (cola.isEmpty()) {
            wait();
        }
        int dato = cola.poll();
        System.out.println("[Monitor] Consumidor retiro " + dato + " (tamaño bufer=" + cola.size() + ")");
        notifyAll();
        return dato;
    }
}
