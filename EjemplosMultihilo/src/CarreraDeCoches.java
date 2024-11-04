import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CarreraDeCoches {
    public static void main(String[] args) {
        List<Thread> hilos = new ArrayList<>();
        System.out.println("Indica el número de coches que van a competir:");
        int numCoches= new Scanner(System.in).nextInt();
        for (int i = 0; i < numCoches; i++) {
            Thread coche = new Thread(new Coche("coche "+(i+1)));
            hilos.add(coche);
            coche.start();
        }

        for (Thread hilo : hilos) {
            try {
                //Esto nos ayudara para que el programa termine cuando termine todos los hilos
                hilo.join();
            } catch (InterruptedException e) {
                System.out.println("Hilo principal interrumpido.");
            }
        }
        System.out.println("Carrera terminada");
    }
}
