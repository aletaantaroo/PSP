import java.util.Random;

public class Coche implements Runnable {
    private String nombre;
    private int distanciaRecorrida;
    private static final int DISTANCIA_META = 100;
    private static boolean carreraTerminada = false;
    public Coche(String nombre) {
        this.nombre = nombre;
        this.distanciaRecorrida = 0;
    }
    @Override
    public void run() {
        while(!carreraTerminada){
            int avance = new Random().nextInt(11);
            distanciaRecorrida += avance;

            System.out.println("El "+ this.nombre+" ha avanzado "+avance+ " unidades"+"("+this.distanciaRecorrida+")");

            if(this.distanciaRecorrida >= DISTANCIA_META){
                System.out.println("¡El "+this.nombre+" ha ganado la carrera!");
                carreraTerminada = true;
            }
            try{
                Thread.sleep(1000);
            }catch(InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(nombre + " ha sido interrumpido.");
            }
        }
    }
// Otros métodos necesarios
}
