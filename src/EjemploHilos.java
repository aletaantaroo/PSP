public class EjemploHilos extends Thread {
    private String nombre;
    public EjemploHilos(String nombre) {
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
        EjemploHilos hilo1 = new EjemploHilos("Hilo 1");
        EjemploHilos hilo2 = new EjemploHilos("Hilo 2");
        hilo1.start();
        hilo2.start();
    }
}