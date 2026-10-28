package org.example;

import java.util.LinkedList;

public class Ascensor implements Runnable {

    private int pisoActual = 0;
    private final LinkedList<Usuario> usuariosPendientes = new LinkedList<>();
    private boolean ascensorActivo = true;

    public synchronized void agregarUsuario(Usuario usuario) {
        usuariosPendientes.add(usuario);
        notifyAll();
    }

    public synchronized void detenerAscensor() {
        ascensorActivo = false;
        notifyAll();
    }

    @Override
    public void run() {
        boolean cicloEnMarcha = true;

        while (cicloEnMarcha) {
            atenderSiguienteUsuario();
            synchronized (this) {
                cicloEnMarcha = ascensorActivo || !usuariosPendientes.isEmpty();
            }
        }

        bajarHastaSotano();
        System.out.println("Ascensor llegó a piso -1. Fin del programa.");
    }

    private void atenderSiguienteUsuario() {
        Usuario usuarioActual = null;
        boolean continuar = true;

        synchronized (this) {
            while (usuariosPendientes.isEmpty() && ascensorActivo) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    continuar = false;
                }
            }

            if (!usuariosPendientes.isEmpty()) {
                usuarioActual = usuariosPendientes.removeFirst();
            } else {
                continuar = false;
            }
        }

        if (continuar) {
            moverHacia(usuarioActual.getPisoOrigen());

            usuarioActual.avisarLlegadaOrigen();
            usuarioActual.esperarUsuarioDentro();

            moverHacia(usuarioActual.getPisoDestino());

            usuarioActual.avisarDestino();
        }
    }

    private void bajarHastaSotano() {
        boolean continuar = true;

        while (pisoActual > -1 && continuar) {
            pisoActual--;
            System.out.println("Ascensor en piso " + pisoActual);

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                continuar = false;
            }
        }
    }

    private void moverHacia(int pisoDestino) {
        boolean continuar = true;

        while (pisoActual != pisoDestino && continuar) {

            if (pisoActual < pisoDestino) {
                pisoActual++;
            } else {
                pisoActual--;
            }

            System.out.println("Ascensor en piso " + pisoActual);

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                continuar = false;
            }
        }
    }
}