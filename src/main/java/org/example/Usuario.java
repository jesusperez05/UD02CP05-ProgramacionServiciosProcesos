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

    public int getIdUsuario() {
        return idUsuario;
    }

    public int getPisoOrigen() {
        return pisoOrigen;
    }

    public int getPisoDestino() {
        return pisoDestino;
    }

    @Override
    public void run() {

        System.out.println("Usuario " + idUsuario + " espera en piso " + pisoOrigen);

        ascensor.agregarUsuario(this);
        ascensor.esperarLlegadaAscensor(this);

        System.out.println("Usuario " + idUsuario + " sube al ascensor.");

        ascensor.confirmarSubida();
        ascensor.esperarLlegadaDestino(this);

        System.out.println("Usuario " + idUsuario + " baja en piso " + pisoDestino);

        ascensor.confirmarBajada();
    }
}