package clases;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Proyectil {
	private int x;
	private int y;
	private int ancho;
	private int alto;
	private String indiceImagen;
	private int velocidad;
	private boolean activo = true;

	public Proyectil(int x, int y, int ancho, int alto, String indiceImagen, int velocidad) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.indiceImagen = indiceImagen;
		this.velocidad = velocidad;
	}

	public void mover() {
		this.y -= velocidad; // dispara hacia arriba
		if (this.y < -20) {
			this.activo = false;
		}
	}

	public void pintar(GraphicsContext graficos) {
		if (activo) {
			// Dibujar disparo rojo
			graficos.setFill(Color.RED);
			graficos.fillOval(this.x, this.y, this.ancho, this.alto);
			graficos.setFill(Color.WHITE);
			graficos.fillOval(this.x + 2, this.y + 2, this.ancho - 4, this.alto - 4);
		}
	}

	public Rectangle obtenerRectangulo() {
		return new Rectangle(this.x, this.y, this.ancho, this.alto);
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
