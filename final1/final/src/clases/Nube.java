package clases;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Nube {
	private double x;
	private double y;
	private double velocidad;
	private double ancho;
	private double alto;

	public Nube(double x, double y, double velocidad, double ancho, double alto) {
		this.x = x;
		this.y = y;
		this.velocidad = velocidad;
		this.ancho = ancho;
		this.alto = alto;
	}

	public void mover() {
		x -= velocidad;
		if (x + ancho < 0)
			x = 1050;
	}

	public void pintar(GraphicsContext g) {
		g.setFill(Color.rgb(190, 210, 255, 0.25));
		g.fillOval(x, y + alto * 0.30, ancho * 0.68, alto * 0.60);
		g.fillOval(x + ancho * 0.22, y, ancho * 0.52, alto * 0.62);
		g.fillOval(x + ancho * 0.50, y + alto * 0.18, ancho * 0.50, alto * 0.58);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
}