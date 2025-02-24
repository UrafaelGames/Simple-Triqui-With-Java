package modelo;

public class Jugador {
    private int x, y;

    public Jugador() {
        this.x = 0;
        this.y = 0;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public void mover(char direccion, Tablero tablero) {
        int nuevoX = x, nuevoY = y;

        switch (Character.toUpperCase(direccion)) {
            case 'W': nuevoX--; break;
            case 'S': nuevoX++; break;
            case 'A': nuevoY--; break;
            case 'D': nuevoY++; break;
            default: return;
        }

        if (tablero.esMovimientoValido(nuevoX, nuevoY)) {
            tablero.limpiarPosicion(x, y);
            x = nuevoX;
            y = nuevoY;
            tablero.colocarJugador(x, y);
        }
    }
}
