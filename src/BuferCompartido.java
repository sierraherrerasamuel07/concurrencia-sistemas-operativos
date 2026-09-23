public interface BuferCompartido {
    void producir(int dato) throws InterruptedException;
    int consumir() throws InterruptedException;
}
