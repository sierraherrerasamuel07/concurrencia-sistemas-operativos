import java.util.Scanner;

public class Main {

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("==================================================");
        System.out.println("   CONCURRENCIA Y SINCRONIZACION DE PROCESOS/HILOS");
        System.out.println("==================================================");

        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            String opcion = sc.nextLine().trim();
            System.out.println();

            switch (opcion) {
                case "1" -> ejecutarProductorConsumidor(new BuferMonitor(5), "MONITOR (synchronized + wait/notify)");
                case "2" -> ejecutarProductorConsumidor(new BuferSemaforo(5), "SEMAFOROS");
                case "3" -> ejecutarProductorConsumidor(new BuferMutex(5), "MUTEX EXPLICITO (ReentrantLock)");
                case "4" -> DemoBarrera.ejecutar(4);
                case "5" -> AlgoritmoPeterson.ejecutar();
                default -> System.out.println("Opcion invalida.");
            }

            System.out.print("\n¿Desea ejecutar otra demostracion? (S/N): ");
            continuar = sc.nextLine().trim().equalsIgnoreCase("S");
        }

        System.out.println("\nFin del programa.");
    }

    private static void mostrarMenu() {
        System.out.println("\n--- SELECCIONE EL MECANISMO A DEMOSTRAR ---");
        System.out.println("1. Productor-Consumidor con Monitor (synchronized/wait/notify)");
        System.out.println("2. Productor-Consumidor con Semaforos");
        System.out.println("3. Productor-Consumidor con Mutex explicito (ReentrantLock)");
        System.out.println("4. Demostracion de Barreras (CyclicBarrier)");
        System.out.println("5. Demostracion del Algoritmo de Peterson");
        System.out.print("Opcion: ");
    }

    private static void ejecutarProductorConsumidor(BuferCompartido bufer, String etiqueta) throws InterruptedException {
        System.out.println("=== PRODUCTOR-CONSUMIDOR usando " + etiqueta + " ===");

        Productor p1 = new Productor("Productor-1", bufer, 5);
        Productor p2 = new Productor("Productor-2", bufer, 5);
        Consumidor c1 = new Consumidor("Consumidor-1", bufer, 5);
        Consumidor c2 = new Consumidor("Consumidor-2", bufer, 5);

        p1.start();
        p2.start();
        c1.start();
        c2.start();

        p1.join();
        p2.join();
        c1.join();
        c2.join();

        System.out.println("=== FIN: todos los hilos terminaron correctamente ===");
    }
}
