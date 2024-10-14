public class EjemploRunnable implements Runnable {
    private String nombre;
    public EjemploRunnable(String nombre) {
        this.nombre = nombre;
    }
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(nombre + ": Iteración " + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        Thread hilo1 = new Thread(new EjemploRunnable("Hilo 1"));
        Thread hilo2 = new Thread(new EjemploRunnable("Hilo 2"));
        hilo1.start();
        hilo2.start();
    }
}

