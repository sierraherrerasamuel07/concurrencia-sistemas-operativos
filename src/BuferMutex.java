import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BuferMutex implements BuferCompartido {

    private final Queue<Integer> cola = new LinkedList<>();
    private final int capacidad;

    private final Lock candado = new ReentrantLock();
    private final Condition hayEspacio = candado.newCondition();
    private final Condition hayElementos = candado.newCondition();

    public BuferMutex(int capacidad) {
        this.capacidad = capacidad;
    }

    @Override
    public void producir(int dato) throws InterruptedException {
        candado.lock();
        try {
            while (cola.size() == capacidad) {
                hayEspacio.await();
            }
            cola.add(dato);
            System.out.println("[Mutex] Productor inserto " + dato + " (tamaño bufer=" + cola.size() + ")");
            hayElementos.signalAll();
        } finally {
            candado.unlock();
        }
    }

    @Override
    public int consumir() throws InterruptedException {
        candado.lock();
        try {
            while (cola.isEmpty()) {
                hayElementos.await();
            }
            int dato = cola.poll();
            System.out.println("[Mutex] Consumidor retiro " + dato + " (tamaño bufer=" + cola.size() + ")");
            hayEspacio.signalAll();
            return dato;
        } finally {
            candado.unlock();
        }
    }
}
