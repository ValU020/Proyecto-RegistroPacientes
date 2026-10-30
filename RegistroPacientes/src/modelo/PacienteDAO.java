package modelo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PacienteDAO {

	private final Path archivo;
	private final List<Paciente> pacientes = new ArrayList<>();

	public PacienteDAO(String ruta) throws IOException {
		this.archivo = Paths.get(ruta);
		cargar();
	}

	private void cargar() throws IOException {
		if (!Files.exists(archivo)) {
			return;
		}
		String texto = new String(Files.readAllBytes(archivo), StandardCharsets.UTF_8);
		if (texto.trim().isEmpty()) {
			return;
		}
		for (Map<String, String> m : Json.leer(texto)) {
			pacientes.add(Paciente.fromMap(m));
		}
	}

	private void persistir() throws IOException {
		List<Map<String, String>> lista = new ArrayList<>();
		for (Paciente p : pacientes) {
			lista.add(p.toMap());
		}
		Files.write(archivo, Json.escribir(lista).getBytes(StandardCharsets.UTF_8));
	}

	public void agregar(Paciente p) throws IOException {
		pacientes.add(p);
		try {
			persistir();
		} catch (IOException e) {
			pacientes.remove(p);
			throw e;
		}
	}

	public boolean existe(String tipoDocumento, String numeroDocumento) {
		return buscar(tipoDocumento, numeroDocumento) != null;
	}

	public Paciente buscar(String tipoDocumento, String numeroDocumento) {
		for (Paciente p : pacientes) {
			if (p.getTipoDocumento().equals(tipoDocumento)
					&& p.getNumeroDocumento().equalsIgnoreCase(numeroDocumento)) {
				return p;
			}
		}
		return null;
	}

	public List<Paciente> listar() {
		return Collections.unmodifiableList(pacientes);
	}
}