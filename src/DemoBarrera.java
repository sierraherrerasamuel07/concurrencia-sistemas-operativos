import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Demostracion del mecanismo de BARRERA: varios hilos deben llegar todos
 * a un mismo punto de control antes de que cualquiera de ellos pueda
 * seguir avanzando. Es util para sincronizar fases de un trabajo, por
 * ejemplo cuando varios procesos deben terminar de "prepararse" antes
 * de que cualquiera empiece a "trabajar".
 */
public class DemoBarrera {

    public static void ejecutar(int numeroDeHilos) throws InterruptedException {
        System.out.println("\n=== DEMOSTRACION DE BARRERA (" + numeroDeHilos + " hilos) ===");

        // La barrera se abre automaticamente cuando TODOS los hilos hayan llamado a await()
        CyclicBarrier barrera = new CyclicBarrier(numeroDeHilos, () ->
                System.out.println(">>> Todos los hilos llegaron a la barrera. ¡Arrancan juntos!")
        );

        Thread[] hilos = new Thread[numeroDeHilos];
        for (int i = 0; i < numeroDeHilos; i++) {
            final int id = i + 1;
            hilos[i] = new Thread(() -> {
                try {
                    System.out.println("Hilo " + id + " se esta preparando...");
                    Thread.sleep((long) (Math.random() * 500)); // cada hilo tarda distinto en prepararse
                    System.out.println("Hilo " + id + " esta listo y espera en la barrera.");
                    barrera.await(); // aqui se bloquea hasta que todos los demas tambien lleguen
                    System.out.println("Hilo " + id + " continua su trabajo despues de la barrera.");
                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                }
            });
            hilos[i].start();
        }

        for (Thread h : hilos) {
            h.join(); // espera (JOIN) a que todos los hilos terminen antes de seguir
        }
        System.out.println("=== FIN DE LA DEMOSTRACION DE BARRERA ===\n");
    }
}
