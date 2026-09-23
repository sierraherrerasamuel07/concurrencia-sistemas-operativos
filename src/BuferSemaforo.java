import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

/**
 * Implementacion del bufer compartido usando SEMAFOROS.
 * Se usan tres semaforos, la forma clasica de resolver este problema:
 *  - espacios: cuenta cuantos lugares libres quedan en el bufer.
 *  - elementos: cuenta cuantos datos hay listos para consumir.
 *  - mutex: semaforo binario (0 o 1) que actua como candado para que
 *    productores y consumidores no toquen la cola al mismo tiempo.
 */
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
        espacios.acquire();   // pide un espacio libre; si no hay, se bloquea aqui
        mutex.acquire();      // pide el candado para tocar la cola
        cola.add(dato);
        System.out.println("[Semaforo] Productor inserto " + dato + " (tamaño bufer=" + cola.size() + ")");
        mutex.release();      // suelta el candado
        elementos.release();  // avisa que hay un elemento nuevo disponible
    }

    @Override
    public int consumir() throws InterruptedException {
        elementos.acquire();  // pide un elemento disponible; si no hay, se bloquea aqui
        mutex.acquire();      // pide el candado para tocar la cola
        int dato = cola.poll();
        System.out.println("[Semaforo] Consumidor retiro " + dato + " (tamaño bufer=" + cola.size() + ")");
        mutex.release();      // suelta el candado
        espacios.release();   // avisa que quedo un espacio libre
        return dato;
    }
}
