package clases;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class EnemigoMiniBoss extends Enemigo {
	private double angulo = 0;
	public static final int VIDA_TOTAL = 10;

	public EnemigoMiniBoss(int x, int y) {
		super(x, y, 90, 90, "goku-furioso", 2, VIDA_TOTAL, 500);
	}

	@Override
	public void mover(int jugadorX, int jugadorY) {
		if (!activo)
			return;
		angulo += 0.05;
		int dx = jugadorX - this.x;
		int dy = jugadorY - this.y;
		double dist = Math.sqrt(dx * dx + dy * dy);
		if (dist > 0) {
			this.x += (int) (velocidad * dx / dist + Math.sin(angulo) * 4);
			this.y += (int) (velocidad * dy / dist);
		}
		if (this.y > 540)
			this.activo = false;
	}

	@Override
	public void pintar(GraphicsContext g) {
		if (!activo)
			return;

		// circulo como de advertencia
		g.setFill(Color.rgb(255, 100, 0, 0.15));
		g.fillOval(this.x - 10, this.y - 10, this.ancho + 20, this.alto + 20);

		// Barra de vida
		g.setFill(Color.rgb(50, 0, 0));
		g.fillRect(this.x, this.y - 16, this.ancho, 9);
		g.setFill(Color.LIMEGREEN);
		double porcentajeVida = (double) vida / VIDA_TOTAL;
		g.fillRect(this.x, this.y - 16, (int) (this.ancho * porcentajeVida), 9);

		// Borde de la barra
		g.setStroke(Color.WHITE);
		g.setLineWidth(1);
		g.strokeRect(this.x, this.y - 16, this.ancho, 9);

		// Etiqueta
		g.setFill(Color.GOLD);
		g.setFont(Font.font("Arial", 10));
		g.fillText("★ MINI BOSS ★", this.x + 5, this.y - 19);

		super.pintar(g);
	}
}