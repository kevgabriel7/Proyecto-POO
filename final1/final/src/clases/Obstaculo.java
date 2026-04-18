package clases;

import implementacion.Juego;
import javafx.scene.canvas.GraphicsContext;

public class Obstaculo {
	public static final int LADO = 70; // tamaño del bloque

	private int x;
	private int y;
	private int xImagen;
	private int yImagen;
	private boolean activo = true;

	/*
	 * Del del tilemap: tipo 0 → señal de advertencia amarilla (col 0, fila 0) = (
	 * 0, 0) tipo 1 → señal de advertencia 2 (col 0, fila 3) = ( 0, 210) tipo 2 →
	 * cofre verde (col 1, fila 8) = ( 70, 558) tipo 3 → señal de advertencia cafe
	 * (col 0, fila 6) = ( 0, 420)
	 */
	public Obstaculo(int x, int y, int tipo) {
		this.x = x;
		this.y = y;
		switch (tipo % 4) {
		case 0:
			xImagen = 0;
			yImagen = 0;
			break;
		case 1:
			xImagen = 0;
			yImagen = 210;
			break;
		case 2:
			xImagen = 70;
			yImagen = 558;
			break;
		case 3:
			xImagen = 0;
			yImagen = 420;
			break;
		default:
			xImagen = 0;
			yImagen = 0;
			break;
		}
	}

	public void pintar(GraphicsContext g) {
		if (!activo)
			return;
		g.drawImage(Juego.imagenes.get("tilemap"), xImagen, yImagen, LADO, LADO, x, y, LADO, LADO);
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
}