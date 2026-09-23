import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class BuferSemaforo implements BuferCompartido {

    private final Queue<Integer> cola = new LinkedList<>();
    private final Semaphore espacios;
    private final Semaphore elementos;
    private final Semaphore mutex;

    public BuferSemaforo(int capacidad) {
        this.espacios = new Semaphore(capacidad);
        this.elementos = new Semaphore(0);
        this.mutex = new Semaphore(1);
    }

    @Override
    public void producir(int dato) throws InterruptedException {
        espacios.acquire();
        mutex.acquire();
        cola.add(dato);
        System.out.println("[Semaforo] Productor inserto " + dato + " (tamaño bufer=" + cola.size() + ")");
        mutex.release();
        elementos.release();
    }

    @Override
    public int consumir() throws InterruptedException {
        elementos.acquire();
        mutex.acquire();
        int dato = cola.poll();
        System.out.println("[Semaforo] Consumidor retiro " + dato + " (tamaño bufer=" + cola.size() + ")");
        mutex.release();
        espacios.release();
        return dato;
    }
}
