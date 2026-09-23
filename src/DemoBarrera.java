import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class DemoBarrera {

    public static void ejecutar(int numeroDeHilos) throws InterruptedException {
        System.out.println("\n=== DEMOSTRACION DE BARRERA (" + numeroDeHilos + " hilos) ===");

        CyclicBarrier barrera = new CyclicBarrier(numeroDeHilos, () ->
                System.out.println(">>> Todos los hilos llegaron a la barrera. ¡Arrancan juntos!")
        );

        Thread[] hilos = new Thread[numeroDeHilos];
        for (int i = 0; i < numeroDeHilos; i++) {
            final int id = i + 1;
            hilos[i] = new Thread(() -> {
                try {
                    System.out.println("Hilo " + id + " se esta preparando...");
                    Thread.sleep((long) (Math.random() * 500));
                    System.out.println("Hilo " + id + " esta listo y espera en la barrera.");
                    barrera.await();
                    System.out.println("Hilo " + id + " continua su trabajo despues de la barrera.");
                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                }
            });
            hilos[i].start();
        }

        for (Thread h : hilos) {
            h.join();
        }
        System.out.println("=== FIN DE LA DEMOSTRACION DE BARRERA ===\n");
    }
}
