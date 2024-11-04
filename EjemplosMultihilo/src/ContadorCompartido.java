public class ContadorCompartido {
    private int contador = 0;
    public synchronized void incrementar() {
        contador++;
        System.out.println(Thread.currentThread().getName() + ": " +
                contador);
    }
    public static void main(String[] args) {
        ContadorCompartido contador = new ContadorCompartido();
        Runnable tarea = () -> {
            for (int i = 0; i < 5; i++) {
                contador.incrementar();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread hilo1 = new Thread(tarea, "Hilo 1");
        Thread hilo2 = new Thread(tarea, "Hilo 2");
        hilo1.start();
        hilo2.start();
    }
}