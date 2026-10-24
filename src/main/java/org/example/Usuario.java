package org.example;

public class Usuario implements Runnable {
    private int idUsuario;
    private int pisoOrigen;
    private int pisoDestino;
    private Ascensor ascensor;

    public Usuario(int idUsuario, int pisoOrigen, int pisoDestino, Ascensor ascensor) {
        this.idUsuario = idUsuario;
        this.pisoOrigen = pisoOrigen;
        this.pisoDestino = pisoDestino;
        this.ascensor = ascensor;
    }

    public int getPisoOrigen() {
        return pisoOrigen;
    }

    public int getPisoDestino() {
        return pisoDestino;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public synchronized void despertarUsuario() {
        notifyAll();
    }

    private synchronized void esperarLlegadaDestino() {
        boolean esperando = true;
        while (esperando) {
            try {
                wait();
                esperando = false;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                esperando = false;
            }
        }
    }

    @Override
    public void run() {
        System.out.println("Usuario " + idUsuario + " espera en piso " + pisoOrigen);
        ascensor.agregarUsuario(this);
        esperarLlegadaDestino();
    }
}