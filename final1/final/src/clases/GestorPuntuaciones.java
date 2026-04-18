package clases;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/*
  Gestiona las 10 mejores puntuaciones en archivo de texto. El puntaje se
  agrega al morir, nunca interrumpe el juego en curso.
 */
public class GestorPuntuaciones {
	private static final String ARCHIVO = "puntuaciones.txt";
	private static final int MAX_PUNTUACIONES = 10;

	private List<String[]> registros; // cada registro = {nombre, puntos}

	public GestorPuntuaciones() {
		registros = new ArrayList<>();
		cargarPuntuaciones();
	}

	public void cargarPuntuaciones() {
		registros.clear();
		File archivo = new File(ARCHIVO);
		if (!archivo.exists())
			return;
		try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				String[] partes = linea.split(",");
				if (partes.length == 2) {
					registros.add(new String[] { partes[0].trim(), partes[1].trim() });
				}
			}
		} catch (IOException e) {
			System.out.println("Error cargando puntuaciones: " + e.getMessage());
		}
		ordenar();
	}

	/**
	 * Guarda la puntuación y mantiene solo el top MAX_PUNTUACIONES. Siempre llama
	 * esto al terminar el juego.
	 */
	public void guardarPuntuacion(String nombre, int puntos) {
		registros.add(new String[] { nombre, String.valueOf(puntos) });
		ordenar();
		if (registros.size() > MAX_PUNTUACIONES) {
			registros = new ArrayList<>(registros.subList(0, MAX_PUNTUACIONES));
		}
		try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
			for (String[] r : registros) {
				pw.println(r[0] + "," + r[1]);
			}
		} catch (IOException e) {
			System.out.println("Error guardando puntuaciones: " + e.getMessage());
		}
	}

	/*
	 * Retorna true si la puntuación merece entrar al top 10. Cuando el top no está
	 * lleno, siempre devuelve true. Nunca se usa para interrumpir el juego – solo
	 * para decidir si pedir nombre.
	 */
	public boolean esNuevoPuntajeAlto(int puntos) {
		if (registros.size() < MAX_PUNTUACIONES)
			return true;
		int minimo = Integer.parseInt(registros.get(registros.size() - 1)[1]);
		return puntos > minimo;
	}

	private void ordenar() {
		registros.sort((a, b) -> Integer.parseInt(b[1]) - Integer.parseInt(a[1]));
	}

	public List<String[]> getRegistros() {
		return registros;
	}
}