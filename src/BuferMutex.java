import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Implementacion del bufer compartido usando un MUTEX EXPLICITO
 * (ReentrantLock). A diferencia de "synchronized" (que es un candado
 * implicito manejado automaticamente por Java), aqui el candado se pide
 * y se suelta a mano con lock() y unlock(). Las Condition son el
 * equivalente explicito de wait()/notify() para este tipo de candado.
 */
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
        candado.lock(); // pide el mutex de forma explicita
        try {
            while (cola.size() == capacidad) {
                hayEspacio.await(); // duerme al productor hasta que haya espacio
            }
            cola.add(dato);
            System.out.println("[Mutex] Productor inserto " + dato + " (tamaño bufer=" + cola.size() + ")");
            hayElementos.signalAll(); // despierta a los consumidores dormidos
        } finally {
            candado.unlock(); // suelta el mutex SIEMPRE, incluso si hubo un error
        }
    }

    @Override
    public int consumir() throws InterruptedException {
        candado.lock();
        try {
            while (cola.isEmpty()) {
                hayElementos.await(); // duerme al consumidor hasta que haya datos
            }
            int dato = cola.poll();
            System.out.println("[Mutex] Consumidor retiro " + dato + " (tamaño bufer=" + cola.size() + ")");
            hayEspacio.signalAll(); // despierta a los productores dormidos
            return dato;
        } finally {
            candado.unlock();
        }
    }
}
