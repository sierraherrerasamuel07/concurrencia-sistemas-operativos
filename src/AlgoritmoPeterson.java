public class AlgoritmoPeterson {

    private static volatile boolean[] bandera = new boolean[2];
    private static volatile int turno;

    private static int contadorCompartido = 0;
    private static final int ITERACIONES = 100000;

    private static void entrarRegionCritica(int id) {
        int otro = 1 - id;
        bandera[id] = true;
        turno = otro;
        while (bandera[otro] && turno == otro) {
            // espera activa
        }
    }

    private static void salirRegionCritica(int id) {
        bandera[id] = false;
    }

    public static void ejecutar() throws InterruptedException {
        System.out.println("\n=== DEMOSTRACION DEL ALGORITMO DE PETERSON ===");
        contadorCompartido = 0;

        Runnable tarea0 = () -> {
            for (int i = 0; i < ITERACIONES; i++) {
                entrarRegionCritica(0);
                contadorCompartido++;
                salirRegionCritica(0);
            }
        };
        Runnable tarea1 = () -> {
            for (int i = 0; i < ITERACIONES; i++) {
                entrarRegionCritica(1);
                contadorCompartido++;
                salirRegionCritica(1);
            }
        };

        Thread hiloA = new Thread(tarea0, "Proceso-0");
        Thread hiloB = new Thread(tarea1, "Proceso-1");

        hiloA.start();
        hiloB.start();

        hiloA.join();
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
