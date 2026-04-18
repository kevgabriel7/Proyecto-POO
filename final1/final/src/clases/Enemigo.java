package clases;

import implementacion.Juego;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

public abstract class Enemigo {
	protected int x;
	protected int y;
	protected int ancho;
	protected int alto;
	protected String indiceImagen;
	protected int velocidad;
	protected boolean activo = true;
	protected int vida;
	protected int puntos; // puntos al ser destruido

	public Enemigo(int x, int y, int ancho, int alto, String indiceImagen, int velocidad, int vida, int puntos) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.indiceImagen = indiceImagen;
		this.velocidad = velocidad;
		this.vida = vida;
		this.puntos = puntos;
	}

	public abstract void mover(int jugadorX, int jugadorY);

	public void pintar(GraphicsContext graficos) {
		if (activo) {
			graficos.drawImage(Juego.imagenes.get(indiceImagen), this.x, this.y, this.ancho, this.alto);
		}
	}

	public Rectangle obtenerRectangulo() {
		return new Rectangle(this.x, this.y, this.ancho, this.alto);
	}

	public void recibirDano(int dano) {
		this.vida -= dano;
		if (this.vida <= 0) {
			this.activo = false;
		}
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

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public int getPuntos() {
		return puntos;
	}

	public int getVida() {
		return vida;
	}

	public String getIndiceImagen() {
		return indiceImagen;
	}
}