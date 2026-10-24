package org.example;

public class Main {
    static void main(String[] args) throws InterruptedException {
        Ascensor ascensor = new Ascensor();
        Thread hiloAscensor = new Thread(ascensor);
        hiloAscensor.start();

        Usuario u1 = new Usuario(1, 0, 3, ascensor);
        Usuario u2 = new Usuario(2, 2, -1, ascensor);
        Usuario u3 = new Usuario(3, 1, 5, ascensor);

        Thread h1 = new Thread(u1);
        Thread h2 = new Thread(u2);
        Thread h3 = new Thread(u3);

        h1.start();
        Thread.sleep(500);
        h2.start();
        Thread.sleep(500);
        h3.start();
        h1.join();
        h2.join();
        h3.join();
        ascensor.detenerAscensor();
        hiloAscensor.join();
    }
}