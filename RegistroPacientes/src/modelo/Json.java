package modelo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class Json {

	private Json() {
	}

	public static String escribir(List<Map<String, String>> lista) {
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < lista.size(); i++) {
			sb.append(i == 0 ? "\n" : ",\n").append("  {\n");
			Iterator<Map.Entry<String, String>> it = lista.get(i).entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> e = it.next();
				sb.append("    ").append(comillas(e.getKey())).append(": ").append(comillas(e.getValue()));
				sb.append(it.hasNext() ? ",\n" : "\n");
			}
			sb.append("  }");
		}
		sb.append(lista.isEmpty() ? "]" : "\n]");
		return sb.toString();
	}

	private static String comillas(String s) {
		StringBuilder sb = new StringBuilder("\"");
		for (char c : s.toCharArray()) {
			switch (c) {
			case '"':  sb.append("\\\""); break;
			case '\\': sb.append("\\\\"); break;
			case '\n': sb.append("\\n"); break;
			case '\r': sb.append("\\r"); break;
			case '\t': sb.append("\\t"); break;
			default:
				if (c < 0x20) {
					sb.append(String.format("\\u%04x", (int) c));
				} else {
					sb.append(c);
				}
			}
		}
		return sb.append('"').toString();
	}

	public static List<Map<String, String>> leer(String texto) {
		return new Lector(texto).leerArreglo();
	}

	private static class Lector {
		private final String s;
		private int pos = 0;

		Lector(String s) {
			this.s = s;
		}

		List<Map<String, String>> leerArreglo() {
			List<Map<String, String>> lista = new ArrayList<>();
			saltarEspacios();
			esperar('[');
			saltarEspacios();
			if (actual() == ']') {
				pos++;
				return lista;
			}
			while (true) {
				lista.add(leerObjeto());
				saltarEspacios();
				char c = siguiente();
				if (c == ',') {
					continue;
				}
				if (c == ']') {
					return lista;
				}
				throw error("Se esperaba ',' o ']'");
			}
		}

		private Map<String, String> leerObjeto() {
			Map<String, String> m = new LinkedHashMap<>();
			saltarEspacios();
			esperar('{');
			saltarEspacios();
			if (actual() == '}') {
				pos++;
				return m;
			}
			while (true) {
				saltarEspacios();
				String clave = leerTexto();
				saltarEspacios();
				esperar(':');
				saltarEspacios();
				String valor = leerTexto();
				m.put(clave, valor);
				saltarEspacios();
				char c = siguiente();
				if (c == ',') {
					continue;
				}
				if (c == '}') {
					return m;
				}
				throw error("Se esperaba ',' o '}'");
			}
		}

		private String leerTexto() {
			esperar('"');
			StringBuilder sb = new StringBuilder();
			while (true) {
				char c = siguiente();
				if (c == '"') {
					return sb.toString();
				}
				if (c != '\\') {
					sb.append(c);
					continue;
				}
				char e = siguiente();
				switch (e) {
				case 'n': sb.append('\n'); break;
				case 'r': sb.append('\r'); break;
				case 't': sb.append('\t'); break;
				case 'b': sb.append('\b'); break;
				case 'f': sb.append('\f'); break;
				case '"': sb.append('"'); break;
				case '\\': sb.append('\\'); break;
				case '/': sb.append('/'); break;
				case 'u':
					if (pos + 4 > s.length()) {
						throw error("Secuencia \\u incompleta");
					}
					sb.append((char) Integer.parseInt(s.substring(pos, pos + 4), 16));
					pos += 4;
					break;
				default:
					throw error("Escape inválido: \\" + e);
				}
			}
		}

		private void saltarEspacios() {
			while (pos < s.length() && Character.isWhitespace(s.charAt(pos))) {
				pos++;
			}
		}

		private char actual() {
			if (pos >= s.length()) {
				throw error("Fin inesperado del archivo");
			}
			return s.charAt(pos);
		}

		private char siguiente() {
			char c = actual();
			pos++;
			return c;
		}

		private void esperar(char esperado) {
			char c = siguiente();
			if (c != esperado) {
				pos--;
				throw error("Se esperaba '" + esperado + "' y se encontró '" + c + "'");
			}
		}

		private IllegalArgumentException error(String mensaje) {
			return new IllegalArgumentException("JSON inválido en la posición " + pos + ": " + mensaje);
		}
	}
}