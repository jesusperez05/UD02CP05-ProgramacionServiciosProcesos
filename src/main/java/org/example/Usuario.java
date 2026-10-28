package org.example;

public class Usuario implements Runnable {

    private int idUsuario;
    private int pisoOrigen;
    private int pisoDestino;
    private Ascensor ascensor;

    private boolean ascensorEnOrigen = false;
    private boolean dentroAscensor = false;
    private boolean llegoDestino = false;

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

    public synchronized void avisarLlegadaOrigen() {
        ascensorEnOrigen = true;
        notifyAll();
    }

    public synchronized void esperarAscensor() {
        boolean continuar = true;

        while (!ascensorEnOrigen && continuar) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                continuar = false;
            }
        }

        if (continuar) {
            System.out.println("Usuario " + idUsuario + " sube al ascensor.");
            dentroAscensor = true;
            notifyAll();
        }
    }

    public synchronized void esperarUsuarioDentro() {
        boolean continuar = true;

        while (!dentroAscensor && continuar) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                continuar = false;
            }
        }
    }

    public synchronized void avisarDestino() {
        llegoDestino = true;
        notifyAll();
    }

    public synchronized void esperarDestino() {
        boolean continuar = true;

        while (!llegoDestino && continuar) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                continuar = false;
            }
        }

        if (continuar) {
            System.out.println("Usuario " + idUsuario + " baja en piso " + pisoDestino);
        }
    }

    @Override
    public void run() {
        System.out.println("Usuario " + idUsuario + " espera en piso " + pisoOrigen);

        ascensor.agregarUsuario(this);

        esperarAscensor();
        esperarDestino();
    }
}