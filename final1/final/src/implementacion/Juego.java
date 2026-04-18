package implementacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;
import java.util.Random;

import clases.Enemigo;
import clases.EnemigoBasico;
import clases.EnemigoFuerte;
import clases.EnemigoMiniBoss;
import clases.EnemigoRapido;
import clases.GestorPuntuaciones;
import clases.Item;
import clases.JugadorAnimado;
import clases.Nube;
import clases.Obstaculo;
import clases.Proyectil;
import clases.Tile;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Juego extends Application {

	private Scene escena;
	private Group root;
	private Canvas canvas;
	private GraphicsContext graficos;

	private enum EstadoJuego {
		JUGANDO, GAME_OVER, RANKING
	}

	private EstadoJuego estado = EstadoJuego.JUGANDO;

	public static boolean derecha = false;
	public static boolean izquierda = false;
	public static boolean arriba = false;
	public static boolean abajo = false;
	public static boolean disparo = false;

	public static HashMap<String, Image> imagenes;

	private JugadorAnimado jugadorAnimado;
	private ArrayList<Tile> tilesFondo;
	private ArrayList<Item> items;
	private ArrayList<Enemigo> enemigos;
	private ArrayList<Proyectil> proyectiles;
	private ArrayList<Obstaculo> obstaculos;
	private ArrayList<Nube> nubes;

	private double tiempoUltimoDisparo = 0;
	private static final double VELOCIDAD_DISPARO = 0.30;

	private Random random = new Random();
	private double tiempoUltimoEnemigo = 0;
	private static final double INTERVALO_ENEMIGO = 3.0;
	private int ordaEnemigos = 0;

	private GestorPuntuaciones gestorPuntuaciones;

	private int scrollY = 0;
	private static final int SCROLL_VELOCIDAD = 2;
	private int scrollObstaculo = 0;
	private int scrollItem = 0;
	private static final int DIST_OBSTACULO = 200;
	private static final int DIST_ITEM = 140;

	private String notificacion = "";
	private double tiempoNotificacion = -99;
	private static final double DURACION_NOTIF = 2.2;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage ventana) throws Exception {
		inicializarComponentes();
		graficos = canvas.getGraphicsContext2D();
		root.getChildren().add(canvas);
		ventana.setScene(escena);
		ventana.setTitle("MEGA-ULTRA-INSTINTO-2.0");
		gestionarEventos();
		ventana.show();
		cicloJuego();
	}

	public void inicializarComponentes() {
		jugadorAnimado = new JugadorAnimado(460, 380, "megaman", 3, "descanso");
		root = new Group();
		escena = new Scene(root, 1000, 500);
		canvas = new Canvas(1000, 500);
		imagenes = new HashMap<>();
		tilesFondo = new ArrayList<>();
		items = new ArrayList<>();
		enemigos = new ArrayList<>();
		proyectiles = new ArrayList<>();
		obstaculos = new ArrayList<>();
		nubes = new ArrayList<>();
		gestorPuntuaciones = new GestorPuntuaciones();

		cargarImagenes();
		cargarTilesFondo();
		cargarItems();
		inicializarNubes();
		generarBloques(3 + random.nextInt(2), 80, 380);
	}

	public void cargarImagenes() {
		imagenes.put("goku", new Image("goku.png"));
		imagenes.put("goku-furioso", new Image("goku-furioso.png"));
		imagenes.put("tilemap", new Image("tilemap.png"));
		imagenes.put("megaman", new Image("megaman.png"));
		imagenes.put("item", new Image("item.png"));
	}

	public void cargarTilesFondo() {
		tilesFondo.clear();
		for (int x = 0; x < 1050; x += 70) {
			tilesFondo.add(new Tile(x, 465, 70, 70, 560, 420, "tilemap", 0));
		}
	}

	public void cargarItems() {
		items.clear();
		items.add(new Item(200, 130, 0, 0, "item", 10));
		items.add(new Item(480, 70, 0, 0, "item", 15));
		items.add(new Item(730, 200, 0, 0, "item", 20));
		items.add(new Item(340, 250, 0, 0, "item", 10));
		items.add(new Item(660, 40, 0, 0, "item", 25));
	}

	private void inicializarNubes() {
		nubes.clear();
		nubes.add(new Nube(80, 62, 0.25, 130, 52));
		nubes.add(new Nube(320, 108, 0.40, 155, 58));
		nubes.add(new Nube(600, 70, 0.18, 115, 46));
		nubes.add(new Nube(830, 130, 0.30, 140, 54));
		nubes.add(new Nube(200, 210, 0.45, 122, 50));
		nubes.add(new Nube(520, 185, 0.22, 105, 44));
		nubes.add(new Nube(740, 38, 0.35, 100, 42));
	}

	private void generarBloques(int cantidad, int yMin, int yMax) {
		int intentos = 0, generados = 0;
		while (generados < cantidad && intentos < 80) {
			intentos++;
			int bx = random.nextInt(13) * 70 + 35;
			int by = yMin + random.nextInt(Math.max(1, yMax - yMin));
			int tipo = random.nextInt(4);

			boolean solapa = false;
			for (Obstaculo o : obstaculos) {
				if (Math.abs(o.getX() - bx) < 85 && Math.abs(o.getY() - by) < 85) {
					solapa = true;
					break;
				}
			}
			if (Math.abs(bx - 460) < 120 && Math.abs(by - 380) < 120)
				solapa = true;

			if (!solapa) {
				obstaculos.add(new Obstaculo(bx, by, tipo));
				generados++;
			}
		}
	}

	public void gestionarEventos() {
		escena.setOnKeyPressed((KeyEvent e) -> {
			switch (e.getCode().toString()) {
			case "RIGHT":
				derecha = true;
				break;
			case "LEFT":
				izquierda = true;
				break;
			case "UP":
				arriba = true;
				break;
			case "DOWN":
				abajo = true;
				break;
			case "SPACE":
				disparo = true;
				break;
			case "R":
				if (estado == EstadoJuego.GAME_OVER || estado == EstadoJuego.RANKING)
					reiniciarJuego();
				break;
			}
		});
		escena.setOnKeyReleased((KeyEvent e) -> {
			switch (e.getCode().toString()) {
			case "RIGHT":
				derecha = false;
				break;
			case "LEFT":
				izquierda = false;
				break;
			case "UP":
				arriba = false;
				break;
			case "DOWN":
				abajo = false;
				break;
			case "SPACE":
				disparo = false;
				break;
			}
		});
	}

	public void cicloJuego() {
		long tiempoInicial = System.nanoTime();
		new AnimationTimer() {
			@Override
			public void handle(long now) {
				double t = (now - tiempoInicial) / 1_000_000_000.0;
				pintar(t);
				if (estado == EstadoJuego.JUGANDO)
					actualizar(t);
			}
		}.start();
	}

	public void pintar(double t) {
		graficos.setFill(Color.rgb(8, 8, 28));
		graficos.fillRect(0, 0, 1000, 500);

		long seed = (long) (t * 8);
		for (int i = 0; i < 60; i++) {
			int sx = (int) ((i * 173 + seed) % 1000);
			int sy = (int) ((i * 97 + (long) (scrollY * 0.15)) % 500);
			double b = 0.4 + 0.6 * Math.abs(Math.sin(t * 1.5 + i));
			graficos.setFill(Color.rgb(255, 255, 255, b));
			int tam = (i % 5 == 0) ? 2 : 1;
			graficos.fillOval(sx, sy, tam, tam);
		}

		for (Nube n : nubes)
			n.pintar(graficos);
		for (Obstaculo o : obstaculos)
			o.pintar(graficos);
		for (Item item : items)
			item.pintar(graficos);
		for (Proyectil p : proyectiles)
			p.pintar(graficos);
		for (Enemigo e : enemigos)
			e.pintar(graficos);

		jugadorAnimado.actualizarAnimacion(t);
		jugadorAnimado.pintar(graficos);

		// Línea de tierra/césped — siempre encima del fondo
		for (Tile tile : tilesFondo)
			tile.pintar(graficos);

		pintarHUD();

		if (!notificacion.isEmpty() && t - tiempoNotificacion < DURACION_NOTIF) {
			double alpha = 1.0 - (t - tiempoNotificacion) / DURACION_NOTIF;
			graficos.setFill(Color.rgb(255, 215, 0, alpha));
			graficos.setFont(Font.font("Arial", 26));
			graficos.fillText(notificacion, 290, 260);
		}

		if (estado == EstadoJuego.GAME_OVER)
			pintarGameOver();
		else if (estado == EstadoJuego.RANKING)
			pintarRanking();
	}

	private void pintarHUD() {
		graficos.setFill(Color.rgb(0, 0, 0, 0.62));
		graficos.fillRect(0, 0, 1000, 46);

		graficos.setFont(Font.font("Courier New", 14));
		graficos.setFill(Color.CYAN);
		graficos.fillText("PUNTUACIÓN: " + jugadorAnimado.getPuntuacion(), 10, 18);
		graficos.setFill(Color.ORANGE);
		graficos.fillText("ORDA: " + ordaEnemigos, 230, 18);
		graficos.setFill(Color.LIGHTBLUE);
		graficos.fillText("DISTANCIA: " + scrollY, 380, 18);

		graficos.setFill(Color.RED);
		graficos.setFont(Font.font(15));
		for (int i = 0; i < jugadorAnimado.getVidas(); i++)
			graficos.fillText("♥", 12 + i * 22, 39);

		graficos.setFill(Color.rgb(180, 180, 180));
		graficos.setFont(Font.font("Arial", 11));
		graficos.fillText("← → ↑ ↓ moverse  |  ESPACIO disparar", 660, 18);
	}

	private void pintarGameOver() {
		graficos.setFill(Color.rgb(0, 0, 0, 0.74));
		graficos.fillRect(0, 0, 1000, 500);
		graficos.setFill(Color.RED);
		graficos.setFont(Font.font("Impact", 58));
		graficos.fillText("GAME OVER", 310, 215);
		graficos.setFont(Font.font("Arial", 20));
		graficos.setFill(Color.WHITE);
		graficos.fillText("Puntuación final: " + jugadorAnimado.getPuntuacion(), 385, 270);
		graficos.setFill(Color.LIGHTGREEN);
		graficos.fillText("Presiona  R  para reiniciar", 375, 310);
	}

	private void pintarRanking() {
		graficos.setFill(Color.rgb(0, 5, 50, 0.90));
		graficos.fillRoundRect(170, 30, 660, 440, 20, 20);
		graficos.setStroke(Color.CYAN);
		graficos.setLineWidth(2);
		graficos.strokeRoundRect(170, 30, 660, 440, 20, 20);

		graficos.setFill(Color.GOLD);
		graficos.setFont(Font.font("Impact", 28));
		graficos.fillText("-------------TOP 10 PUNTUACIONES-------------", 248, 82);

		graficos.setFont(Font.font("Courier New", 15));
		var registros = gestorPuntuaciones.getRegistros();
		for (int i = 0; i < registros.size() && i < 10; i++) {
			Color c = (i == 0) ? Color.GOLD : (i <= 2) ? Color.SILVER : Color.WHITE;
			graficos.setFill(c);
			graficos.fillText(String.format("%2d.  %-16s  %7s pts", i + 1, registros.get(i)[0], registros.get(i)[1]),
					250, 118 + i * 30);
		}
		if (registros.isEmpty()) {
			graficos.setFill(Color.GRAY);
			graficos.fillText("Aún no hay puntuaciones registradas.", 240, 200);
		}
		graficos.setFill(Color.LIMEGREEN);
		graficos.setFont(Font.font("Arial", 14));
		graficos.fillText("Presiona  R  para jugar de nuevo", 335, 438);
	}

	public void actualizar(double t) {
		int yAntes = jugadorAnimado.getY();
		jugadorAnimado.mover(obstaculos);
		boolean jugadorSubio = arriba && (jugadorAnimado.getY() < yAntes);

		// Scroll SOLO si el jugador realmente avanzó hacia arriba.
		// Si un obstáculo bloqueó el movimiento, su Y no cambió → no scrollear
		if (jugadorSubio) {
			scrollY += SCROLL_VELOCIDAD;
			for (Obstaculo o : obstaculos)
				o.setY(o.getY() + SCROLL_VELOCIDAD);
			for (Item item : items)
				item.setY(item.getY() + SCROLL_VELOCIDAD);
			for (Nube n : nubes)
				n.setY(n.getY() + SCROLL_VELOCIDAD * 0.35);

			int jx = jugadorAnimado.getX();
			int jy = jugadorAnimado.getY();
			int jw = jugadorAnimado.getAnchoImagen();
			int jh = jugadorAnimado.getAltoImagen();

			for (Obstaculo o : obstaculos) {
				if (!o.isActivo())
					continue;
				boolean solapaX = jx < o.getX() + Obstaculo.LADO && jx + jw > o.getX();
				boolean solapaY = jy < o.getY() + Obstaculo.LADO && jy + jh > o.getY();
				if (solapaX && solapaY) {
					jugadorAnimado.setY(o.getY() + Obstaculo.LADO);
				}
			}

			// Generar nuevos bloques y ítems al avanzar
			if (scrollY - scrollObstaculo >= DIST_OBSTACULO) {
				generarBloques(3 + random.nextInt(2), -70, -10);
				scrollObstaculo = scrollY;
			}
			if (scrollY - scrollItem >= DIST_ITEM) {
				items.add(new Item(80 + random.nextInt(840), -35, 0, 0, "item", 10 + random.nextInt(25)));
				scrollItem = scrollY;
			}
		}

		for (Nube n : nubes)
			n.mover();
		obstaculos.removeIf(o -> o.getY() > 530);
		items.removeIf(i -> i.isCapturado() || i.getY() > 530);

		// Disparar
		if (disparo && t - tiempoUltimoDisparo > VELOCIDAD_DISPARO) {
			jugadorAnimado.dispararProyectil(proyectiles);
			tiempoUltimoDisparo = t;
		}

		// Proyectiles vs enemigos
		Iterator<Proyectil> itP = proyectiles.iterator();
		while (itP.hasNext()) {
			Proyectil p = itP.next();
			p.mover();
			if (!p.isActivo()) {
				itP.remove();
				continue;
			}
			for (Enemigo e : enemigos) {
				if (!e.isActivo() || !p.isActivo())
					continue;
				if (p.obtenerRectangulo().getBoundsInLocal().intersects(e.obtenerRectangulo().getBoundsInLocal())) {
					boolean eraVivo = e.isActivo();
					e.recibirDano(1);
					p.setActivo(false);
					if (eraVivo && !e.isActivo()) {
						jugadorAnimado.addPuntuacion(e.getPuntos());
						if (e instanceof EnemigoMiniBoss) {
							jugadorAnimado.setVidas(Math.min(jugadorAnimado.getVidas() + 1, 5));
							notificacion = "¡MINI BOSS DERROTADO!  +1 VIDA ♥";
							tiempoNotificacion = t;
						}
					}
					break;
				}
			}
		}

		// Ordas de enemigos
		if (t - tiempoUltimoEnemigo > INTERVALO_ENEMIGO) {
			generarOrdaEnemigos();
			tiempoUltimoEnemigo = t;
		}

		// Mover enemigos y colisión con jugador
		for (Enemigo e : enemigos) {
			if (!e.isActivo())
				continue;
			e.mover(jugadorAnimado.getX(), jugadorAnimado.getY());
			if (jugadorAnimado.verificarColisionEnemigo(e)) {
				jugadorAnimado.setVidas(jugadorAnimado.getVidas() - 1);
				e.setActivo(false);
				if (jugadorAnimado.getVidas() <= 0) {
					terminarJuego();
					return;
				}
			}
		}
		enemigos.removeIf(e -> !e.isActivo());

		// Ítems
		for (Item item : items)
			jugadorAnimado.verificarColisiones(item);
	}

	private void generarOrdaEnemigos() {
		int cantidad = Math.min(1 + ordaEnemigos / 3, 5);
		for (int i = 0; i < cantidad; i++) {
			int px = random.nextInt(900) + 50, py = -60 - i * 80;
			Enemigo nuevo;
			switch (random.nextInt(3)) {
			case 0:
				nuevo = new EnemigoBasico(px, py);
				break;
			case 1:
				nuevo = new EnemigoRapido(px, py);
				break;
			default:
				nuevo = new EnemigoFuerte(px, py);
				break;
			}
			enemigos.add(nuevo);
		}
		if (ordaEnemigos > 0 && ordaEnemigos % 5 == 0)
			enemigos.add(new EnemigoMiniBoss(random.nextInt(800) + 100, -130));
		ordaEnemigos++;
	}

	private void terminarJuego() {
		estado = EstadoJuego.GAME_OVER;
		final int puntuacion = jugadorAnimado.getPuntuacion();
		Platform.runLater(() -> {
			if (gestorPuntuaciones.esNuevoPuntajeAlto(puntuacion)) {
				TextInputDialog d = new TextInputDialog("Jugador");
				d.setTitle("¡Nuevo récord!");
				d.setHeaderText("¡Entraste al Top 10!  Puntuación: " + puntuacion);
				d.setContentText("Tu nombre:");
				gestorPuntuaciones.guardarPuntuacion(d.showAndWait().orElse("Anónimo"), puntuacion);
			}
			estado = EstadoJuego.RANKING;
		});
	}

	private void reiniciarJuego() {
		jugadorAnimado = new JugadorAnimado(460, 380, "megaman", 3, "descanso");
		enemigos.clear();
		proyectiles.clear();
		obstaculos.clear();
		items.clear();
		nubes.clear();

		scrollY = scrollObstaculo = scrollItem = ordaEnemigos = 0;
		tiempoUltimoEnemigo = 0;
		notificacion = "";
		derecha = izquierda = arriba = abajo = disparo = false;
		estado = EstadoJuego.JUGANDO;

		cargarTilesFondo();
		cargarItems();
		inicializarNubes();
		generarBloques(3 + random.nextInt(2), 80, 380);
	}
}