package org.example;

import java.util.LinkedList;

public class Ascensor implements Runnable {
    private int pisoActual = 0;
    private final LinkedList<Usuario> usuariosPendientes = new LinkedList<>();
    private Usuario usuarioActual;
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
            cicloEnMarcha = ascensorActivo || !usuariosPendientes.isEmpty();
        }
        bajarHastaSotano();
        System.out.println("Ascensor llegó a piso -1. Fin del programa.");
    }

    private synchronized void atenderSiguienteUsuario() {
        boolean colaVacia = usuariosPendientes.isEmpty();
        while (colaVacia && ascensorActivo) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                ascensorActivo = false;
            }
            colaVacia = usuariosPendientes.isEmpty();
        }

        boolean hayPendientes = !usuariosPendientes.isEmpty();
        if (hayPendientes) {
            usuarioActual = usuariosPendientes.removeFirst();

            moverHacia(usuarioActual.getPisoOrigen());
            System.out.println("Usuario " + usuarioActual.getIdUsuario() + " sube al ascensor.");

            moverHacia(usuarioActual.getPisoDestino());
            System.out.println("Usuario " + usuarioActual.getIdUsuario() + " baja en piso " + usuarioActual.getPisoDestino());

            usuarioActual.despertarUsuario();
        }
    }

    private synchronized void bajarHastaSotano() {
        boolean pisoSuperior = pisoActual > -1;
        while (pisoSuperior) {
            pisoActual--;
            System.out.println("Ascensor en piso " + pisoActual);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                pisoSuperior = false;
            }
            pisoSuperior = pisoActual > -1;
        }
    }

    private void moverHacia(int pisoDestino) {
        boolean enDestino = pisoActual == pisoDestino;
        while (!enDestino) {
            boolean subir = pisoActual < pisoDestino;
            if (subir) {
                pisoActual++;
            } else {
                pisoActual--;
            }
            System.out.println("Ascensor en piso " + pisoActual);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                enDestino = true;
            }
            enDestino = pisoActual == pisoDestino;
        }
    }
}