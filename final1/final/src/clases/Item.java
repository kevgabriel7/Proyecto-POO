package clases;

import implementacion.Juego;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

public class Item {
	private int x;
	private int y;
	private int ancho;
	private int alto;
	private String indiceImagen;
	private boolean capturado;
	private int valor; // puntos que otorga al recolectarlo

	public Item(int x, int y, int ancho, int alto, String indiceImagen) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.indiceImagen = indiceImagen;
		this.valor = 10;
	}

	public Item(int x, int y, int ancho, int alto, String indiceImagen, int valor) {
		this(x, y, ancho, alto, indiceImagen);
		this.valor = valor;
	}

	public void pintar(GraphicsContext graficos) {
		if (!capturado) {
			graficos.drawImage(Juego.imagenes.get(indiceImagen), this.x, this.y, 32, 32);
		}
	}

	public Rectangle obtenerRectangulo() {
		return new Rectangle(this.x, this.y, 32, 32);
	}

	public boolean isCapturado() {
		return capturado;
	}

	public void setCapturado(boolean capturado) {
		this.capturado = capturado;
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

	public int getValor() {
		return valor;
	}
}
