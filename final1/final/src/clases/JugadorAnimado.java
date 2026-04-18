package clases;

import java.util.ArrayList;
import java.util.HashMap;

import implementacion.Juego;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

public class JugadorAnimado {
	private int x;
	private int y;
	private String indiceImagen;
	private int velocidad;
	private HashMap<String, Animacion> animaciones;
	private String animacionActual;
	private int puntuacion = 0;
	private int vidas = 3;

	private int xImagen;
	private int yImagen;
	private int anchoImagen;
	private int altoImagen;

	private static final int LIMITE_IZQUIERDO = 5;
	private static final int LIMITE_DERECHO = 970;
	private static final int LIMITE_SUPERIOR = 50;
	private static final int LIMITE_INFERIOR = 432;

	public JugadorAnimado(int x, int y, String indiceImagen, int velocidad, String animacionActual) {
		this.x = x;
		this.y = y;
		this.indiceImagen = indiceImagen;
		this.velocidad = velocidad;
		this.animacionActual = animacionActual;
		inicializarAnimaciones();
	}

	private boolean colisionAABB(int px, int py, int pw, int ph, Obstaculo o) {
		return px < o.getX() + Obstaculo.LADO && px + pw > o.getX() && py < o.getY() + Obstaculo.LADO
				&& py + ph > o.getY();
	}

	public void mover(ArrayList<Obstaculo> obstaculos) {
		boolean moviendose = Juego.derecha || Juego.izquierda || Juego.arriba || Juego.abajo;

		int prevX = x;
		if (Juego.derecha && x + anchoImagen < LIMITE_DERECHO)
			x += velocidad;
		if (Juego.izquierda && x > LIMITE_IZQUIERDO)
			x -= velocidad;
		for (Obstaculo o : obstaculos) {
			if (o.isActivo() && colisionAABB(x, y, anchoImagen, altoImagen, o)) {
				x = prevX;
				break;
			}
		}

		int prevY = y;
		if (Juego.arriba && y > LIMITE_SUPERIOR)
			y -= velocidad;
		if (Juego.abajo && y + altoImagen < LIMITE_INFERIOR)
			y += velocidad;
		for (Obstaculo o : obstaculos) {
			if (o.isActivo() && colisionAABB(x, y, anchoImagen, altoImagen, o)) {
				y = prevY;
				break;
			}
		}

		setAnimacionActual(moviendose ? "correr" : "descanso");
	}

	public void mover() {
		mover(new ArrayList<>());
	}

	public void addPuntuacion(int puntos) {
		this.puntuacion += puntos;
	}

	public void actualizarAnimacion(double t) {
		if (animaciones.containsKey(animacionActual)) {
			Rectangle coord = animaciones.get(animacionActual).calcularFrame(t);
			xImagen = (int) coord.getX();
			yImagen = (int) coord.getY();
			anchoImagen = (int) coord.getWidth();
			altoImagen = (int) coord.getHeight();
		}
	}

	public void pintar(GraphicsContext g) {
		g.drawImage(Juego.imagenes.get(indiceImagen), xImagen, yImagen, anchoImagen, altoImagen, x, y, anchoImagen,
				altoImagen);
	}

	public Rectangle obtenerRectangulo() {
		return new Rectangle(x, y, anchoImagen, altoImagen);
	}

	// ─── COLISIONES CON OTRAS ENTIDADES ──────────────────────────
	public void verificarColisiones(Item item) {
		if (!item.isCapturado()
				&& obtenerRectangulo().getBoundsInLocal().intersects(item.obtenerRectangulo().getBoundsInLocal())) {
			puntuacion += item.getValor();
			item.setCapturado(true);
		}
	}

	public boolean verificarColisionEnemigo(Enemigo e) {
		return e.isActivo()
				&& obtenerRectangulo().getBoundsInLocal().intersects(e.obtenerRectangulo().getBoundsInLocal());
	}

	public boolean verificarColisionProyectil(Proyectil p) {
		return p.isActivo()
				&& obtenerRectangulo().getBoundsInLocal().intersects(p.obtenerRectangulo().getBoundsInLocal());
	}

	public void dispararProyectil(ArrayList<Proyectil> proyectiles) {
		proyectiles.add(new Proyectil(x + anchoImagen / 2 - 5, y - 10, 10, 20, "bala", 8));
	}

	public void inicializarAnimaciones() {
		animaciones = new HashMap<>();
		Rectangle[] correr = { new Rectangle(13, 229, 75, 68), new Rectangle(100, 229, 75, 68),
				new Rectangle(171, 229, 75, 68), new Rectangle(230, 229, 75, 68), new Rectangle(287, 224, 75, 73),
				new Rectangle(423, 229, 75, 68), new Rectangle(500, 229, 75, 68), new Rectangle(576, 229, 75, 68),
				new Rectangle(640, 229, 75, 68), new Rectangle(699, 229, 75, 68), new Rectangle(764, 229, 75, 68),
				new Rectangle(836, 229, 75, 73), new Rectangle(907, 229, 75, 68) };
		animaciones.put("correr", new Animacion("correr", correr, 0.05));

		Rectangle[] descanso = { new Rectangle(26, 16, 63, 73), new Rectangle(89, 16, 63, 73),
				new Rectangle(154, 16, 63, 73), new Rectangle(226, 16, 63, 73) };
		animaciones.put("descanso", new Animacion("descanso", descanso, 0.20));
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

	public int getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(int v) {
		velocidad = v;
	}

	public String getIndiceImagen() {
		return indiceImagen;
	}

	public void setIndiceImagen(String i) {
		indiceImagen = i;
	}

	public int getPuntuacion() {
		return puntuacion;
	}

	public int getVidas() {
		return vidas;
	}

	public void setVidas(int v) {
		vidas = v;
	}

	public String getAnimacionActual() {
		return animacionActual;
	}

	public void setAnimacionActual(String a) {
		animacionActual = a;
	}

	public int getAnchoImagen() {
		return anchoImagen;
	}

	public int getAltoImagen() {
		return altoImagen;
	}
}