/**
 * Demostracion del ALGORITMO DE PETERSON: una forma clasica de lograr
 * exclusion mutua entre EXACTAMENTE DOS procesos/hilos, sin usar ningun
 * mecanismo del lenguaje (nada de synchronized, Lock ni Semaphore) sino
 * solo dos variables compartidas: "bandera" (quien quiere entrar) y
 * "turno" (a quien le toca ceder el paso si ambos quieren entrar a la vez).
 *
 * Por diseño, Peterson solo funciona para 2 procesos, por eso se muestra
 * como una demostracion independiente y no como parte del productor-consumidor.
 */
public class AlgoritmoPeterson {

    // volatile asegura que los cambios que hace un hilo se vean de inmediato en el otro
    private static volatile boolean[] bandera = new boolean[2];
    private static volatile int turno;

    private static int contadorCompartido = 0;
    private static final int ITERACIONES = 100000;

    private static void entrarRegionCritica(int id) {
        int otro = 1 - id;
        bandera[id] = true;   // "yo quiero entrar"
        turno = otro;         // "te cedo el turno a ti primero"
        // Espera activa: mientras el otro tambien quiera entrar Y sea su turno, me quedo aqui
        while (bandera[otro] && turno == otro) {
            // busy-wait (espera activa) — asi funciona el algoritmo de Peterson originalmente
        }
    }

    private static void salirRegionCritica(int id) {
        bandera[id] = false; // "ya termine, ya no quiero entrar"
    }

    public static void ejecutar() throws InterruptedException {
        System.out.println("\n=== DEMOSTRACION DEL ALGORITMO DE PETERSON ===");
        contadorCompartido = 0;

        Runnable tarea0 = () -> {
            for (int i = 0; i < ITERACIONES; i++) {
                entrarRegionCritica(0);
                contadorCompartido++; // region critica: solo un hilo a la vez debe estar aqui
                salirRegionCritica(0);
            }
        };
        Runnable tarea1 = () -> {
            for (int i = 0; i < ITERACIONES; i++) {
                entrarRegionCritica(1);
                contadorCompartido++; // region critica: solo un hilo a la vez debe estar aqui
                salirRegionCritica(1);
            }
        };

        Thread hiloA = new Thread(tarea0, "Proceso-0");
        Thread hiloB = new Thread(tarea1, "Proceso-1");

        hiloA.start();
        hiloB.start();

        hiloA.join(); // JOIN: el hilo principal espera a que ambos terminen
        hiloB.join();

        int esperado = ITERACIONES * 2;
        System.out.println("Contador esperado (sin condiciones de carrera): " + esperado);
        System.out.println("Contador obtenido:                              " + contadorCompartido);
        System.out.println(contadorCompartido == esperado
                ? ">>> Coinciden: el algoritmo de Peterson protegio correctamente la region critica."
                : ">>> ¡No coinciden! Hubo una condicion de carrera.");
        System.out.println("=== FIN DE LA DEMOSTRACION DE PETERSON ===\n");
    }
}
