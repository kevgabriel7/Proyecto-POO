package clases;

public class EnemigoBasico extends Enemigo {

	public EnemigoBasico(int x, int y) {
		super(x, y, 48, 48, "goku", 2, 1, 50);
	}

	@Override
	public void mover(int jugadorX, int jugadorY) {
		if (!activo)
			return;
		// Movimiento directo hacia el jugador
		int dx = jugadorX - this.x;
		int dy = jugadorY - this.y;
		double distancia = Math.sqrt(dx * dx + dy * dy);
		if (distancia > 0) {
			this.x += (int) (velocidad * dx / distancia);
			this.y += (int) (velocidad * dy / distancia);
		}
		// Si sale de pantalla, desactivar
		if (this.y > 520)
			this.activo = false;
	}
}