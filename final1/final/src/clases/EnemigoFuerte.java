package clases;

public class EnemigoFuerte extends Enemigo {

	public EnemigoFuerte(int x, int y) {
		super(x, y, 60, 60, "goku", 1, 3, 150);
	}

	@Override
	public void mover(int jugadorX, int jugadorY) {
		if (!activo)
			return;
		// Movimiento lento directo
		int dx = jugadorX - this.x;
		int dy = jugadorY - this.y;
		double distancia = Math.sqrt(dx * dx + dy * dy);
		if (distancia > 0) {
			this.x += (int) (velocidad * dx / distancia);
			this.y += (int) (velocidad * dy / distancia);
		}
		if (this.y > 520)
			this.activo = false;
	}

	@Override
	public void pintar(javafx.scene.canvas.GraphicsContext graficos) {
		if (activo) {
			// Barra de vida visual
			graficos.setFill(javafx.scene.paint.Color.RED);
			graficos.fillRect(this.x, this.y - 8, this.ancho, 5);
			graficos.setFill(javafx.scene.paint.Color.GREEN);
			graficos.fillRect(this.x, this.y - 8, (int) (this.ancho * (vida / 3.0)), 5);
			super.pintar(graficos);
		}
	}
}