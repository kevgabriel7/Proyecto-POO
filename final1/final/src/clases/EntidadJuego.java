package clases;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

public abstract class EntidadJuego {
	protected int x;
	protected int y;
	protected int ancho;
	protected int alto;
	protected String indiceImagen;
	protected int velocidad;
	protected boolean activo = true;

	public EntidadJuego(int x, int y, int ancho, int alto, String indiceImagen, int velocidad) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.indiceImagen = indiceImagen;
		this.velocidad = velocidad;
	}

	public abstract void pintar(GraphicsContext graficos);

	public abstract void mover();

	public Rectangle obtenerRectangulo() {
		return new Rectangle(this.x, this.y, this.ancho, this.alto);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getAncho() {
		return ancho;
	}

	public void setAncho(int ancho) {
		this.ancho = ancho;
	}

	public int getAlto() {
		return alto;
	}

	public void setAlto(int alto) {
		this.alto = alto;
	}

	public String getIndiceImagen() {
		return indiceImagen;
	}

	public void setIndiceImagen(String indiceImagen) {
		this.indiceImagen = indiceImagen;
	}

	public int getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}
}
