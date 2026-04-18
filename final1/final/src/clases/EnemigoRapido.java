package clases;

public class EnemigoRapido extends Enemigo {
	private double angulo;

	public EnemigoRapido(int x, int y) {
		super(x, y, 40, 40, "goku-furioso", 4, 1, 75);
		this.angulo = 0;
	}

	@Override
	public void mover(int jugadorX, int jugadorY) {
		if (!activo)
			return;
		// Movimiento en zigzag hacia el jugador
		int dx = jugadorX - this.x;
		int dy = jugadorY - this.y;
		double distancia = Math.sqrt(dx * dx + dy * dy);
		if (distancia > 0) {
			angulo += 0.1;
			this.x += (int) (velocidad * dx / distancia + Math.sin(angulo) * 3);
			this.y += (int) (velocidad * dy / distancia);
		}
		if (this.y > 520)
			this.activo = false;
	}
}