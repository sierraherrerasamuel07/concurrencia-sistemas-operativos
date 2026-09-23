/**
 * Contrato comun que deben cumplir todas las implementaciones del bufer
 * compartido productor-consumidor, sin importar el mecanismo de
 * sincronizacion que usen por dentro (monitor, semaforo o mutex).
 */
public interface BuferCompartido {
    /** Inserta un dato en el bufer. Debe bloquear al productor si esta lleno. */
    void producir(int dato) throws InterruptedException;

    /** Retira y devuelve un dato del bufer. Debe bloquear al consumidor si esta vacio. */
    int consumir() throws InterruptedException;
}
