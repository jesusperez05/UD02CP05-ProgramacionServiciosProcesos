package org.example;

import java.util.LinkedList;

public class Ascensor implements Runnable {
    private int pisoActual = 0;

    private final LinkedList<Usuario> usuariosPendientes = new LinkedList<>();

    private Usuario usuarioActual;

    private boolean ascensorActivo = true;

    private boolean llegoOrigen = false;
    private boolean usuarioDentro = false;
    private boolean llegoDestino = false;
    private boolean usuarioFuera = false;

    public synchronized void agregarUsuario(Usuario usuario) {
        usuariosPendientes.add(usuario);
        notifyAll();
    }

    public synchronized void detenerAscensor() {
        ascensorActivo = false;
        notifyAll();
    }

    public synchronized void esperarLlegadaAscensor(Usuario usuario) {
        while (!(usuario == usuarioActual && llegoOrigen)) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public synchronized void confirmarSubida() {
        usuarioDentro = true;
        notifyAll();
    }

    public synchronized void esperarLlegadaDestino(Usuario usuario) {
        while (!(usuario == usuarioActual && llegoDestino)) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public synchronized void confirmarBajada() {
        usuarioFuera = true;
        notifyAll();
    }

    @Override
    public void run() {
        boolean continuar = true;

        while (continuar) {
            atenderSiguienteUsuario();

            synchronized (this) {
                continuar = ascensorActivo || !usuariosPendientes.isEmpty();
            }
        }

        bajarHastaSotano();

        System.out.println("Ascensor llegó a piso -1. Fin del programa.");
    }

    private void atenderSiguienteUsuario() {
        synchronized (this) {
            while (usuariosPendientes.isEmpty() && ascensorActivo) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (usuariosPendientes.isEmpty()) {
                return;
            }

            usuarioActual = usuariosPendientes.removeFirst();

            llegoOrigen = false;
            usuarioDentro = false;
            llegoDestino = false;
            usuarioFuera = false;
        }

        moverHacia(usuarioActual.getPisoOrigen());

        synchronized (this) {
            llegoOrigen = true;
            notifyAll();

            while (!usuarioDentro) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        moverHacia(usuarioActual.getPisoDestino());

        synchronized (this) {
            llegoDestino = true;
            notifyAll();

            while (!usuarioFuera) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void moverHacia(int pisoDestino) {
        boolean continuar = true;

        while (continuar) {
            synchronized (this) {
                if (pisoActual == pisoDestino) {
                    continuar = false;
                } else if (pisoActual < pisoDestino) {
                    pisoActual++;
                } else {
                    pisoActual--;
                }

                if (continuar) {
                    System.out.println("Ascensor en piso " + pisoActual);
                }
            }

            if (continuar) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    continuar = false;
                }
            }
        }
    }

    private void bajarHastaSotano() {
        boolean continuar = true;

        while (continuar) {
            synchronized (this) {
                if (pisoActual <= -1) {
                    continuar = false;
                } else {
                    pisoActual--;
                    System.out.println("Ascensor en piso " + pisoActual);
                }
            }

            if (continuar) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    continuar = false;
                }
            }
        }
    }
}
