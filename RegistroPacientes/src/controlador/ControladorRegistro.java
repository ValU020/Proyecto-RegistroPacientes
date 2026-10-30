package controlador;

import java.awt.Toolkit;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.JTextComponent;
import modelo.Paciente;
import modelo.PacienteDAO;
import vista.Registro;

public class ControladorRegistro {

	// Expresiones regulares permitidas mientras se escribe (se evalúan sobre el texto completo del campo)
	private static final String SOLO_LETRAS = "[\\p{L} ]*";
	private static final String SOLO_DIGITOS = "\\d*";
	private static final String DIGITOS_Y_ESPACIOS = "[0-9 ]*";
	private static final String FECHA_CARACTERES = "[0-9/]*";
	private static final String ALFANUMERICO = "[A-Za-z0-9]*";

	// Expresiones regulares para la validación final
	private static final Pattern NOMBRE = Pattern.compile("\\p{L}{2,}( \\p{L}+)*");
	private static final Pattern CORREO = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)+");

	private final Registro vista;
	private final PacienteDAO dao;

	private JComponent primerError;

	public ControladorRegistro(Registro vista, PacienteDAO dao) {
		this.vista = vista;
		this.dao = dao;
		configurarFiltros();
		configurarEventos();
		actualizarResumen();
	}

	// Restricciones de Escritura

	private void configurarFiltros() {
		// El número de documento depende del tipo: el pasaporte admite letras, los demás solo números
		aplicarFiltro(vista.getTxtNumDocumento(), () -> esPasaporte() ? ALFANUMERICO : SOLO_DIGITOS, 15);

		aplicarFiltro(vista.getTxtNombres(), () -> SOLO_LETRAS, 50);
		aplicarFiltro(vista.getTxtApellidos(), () -> SOLO_LETRAS, 50);
		aplicarFiltro(vista.getTxtFechaNacimiento(), () -> FECHA_CARACTERES, 10);
		aplicarFiltro(vista.getTxtTelFijo(), () -> DIGITOS_Y_ESPACIOS, 12);
		aplicarFiltro(vista.getTxtTelCelular(), () -> DIGITOS_Y_ESPACIOS, 12);
		aplicarFiltro(vista.getTxtOcupacion(), () -> SOLO_LETRAS, 60);

		// Campos de texto libre: solo se limita la longitud
		aplicarFiltro(vista.getTxtCorreo(), () -> ".*", 80);
		aplicarFiltro(vista.getTxtDireccion(), () -> ".*", 100);
	}

	private void aplicarFiltro(JTextComponent campo, Supplier<String> regex, int maxLongitud) {
		((AbstractDocument) campo.getDocument()).setDocumentFilter(new Filtro(regex, maxLongitud));
	}

	private static class Filtro extends DocumentFilter {
		private final Supplier<String> regex;
		private final int max;

		Filtro(Supplier<String> regex, int max) {
			this.regex = regex;
			this.max = max;
		}

		@Override
		public void insertString(FilterBypass fb, int offset, String texto, javax.swing.text.AttributeSet attr)
				throws BadLocationException {
			replace(fb, offset, 0, texto, attr);
		}

		@Override
		public void replace(FilterBypass fb, int offset, int length, String texto, javax.swing.text.AttributeSet attr)
				throws BadLocationException {
			if (texto == null) {
				texto = "";
			}
			String actual = fb.getDocument().getText(0, fb.getDocument().getLength());
			String nuevo = new StringBuilder(actual).replace(offset, offset + length, texto).toString();
			if (nuevo.length() <= max && nuevo.matches(regex.get())) {
				super.replace(fb, offset, length, texto, attr);
			} else {
				Toolkit.getDefaultToolkit().beep();
			}
		}
	}

	private void configurarEventos() {
		// Resumen en tiempo real
		Runnable refrescar = this::actualizarResumen;
		alCambiar(vista.getTxtNombres(), refrescar);
		alCambiar(vista.getTxtApellidos(), refrescar);
		alCambiar(vista.getTxtNumDocumento(), refrescar);
		alCambiar(vista.getTxtTelCelular(), refrescar);
		alCambiar(vista.getTxtCorreo(), refrescar);
		alCambiar(vista.getTxtDireccion(), refrescar);
		alCambiar(vista.getTxtFechaNacimiento(), () -> {
			actualizarEdad();
			actualizarResumen();
		});

		vista.getCmbTipoDoc().addActionListener(e -> {
			// Si cambia a un tipo que no admite letras, se limpia el número si las tenía
			String num = vista.getTxtNumDocumento().getText();
			String permitido = esPasaporte() ? ALFANUMERICO : SOLO_DIGITOS;
			if (!num.matches(permitido)) {
				vista.getTxtNumDocumento().setText("");
			}
			actualizarResumen();
		});
		vista.getCmbEps().addActionListener(e -> actualizarResumen());
		vista.getCmbRegimen().addActionListener(e -> actualizarResumen());

		vista.getBtnGuardar().addActionListener(e -> guardar());
		vista.getBtnCancelar().addActionListener(e -> {
			vista.limpiarFormulario();
			actualizarResumen();
		});
	}

	private void alCambiar(JTextComponent campo, Runnable accion) {
		campo.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) { accion.run(); }
			@Override
			public void removeUpdate(DocumentEvent e) { accion.run(); }
			@Override
			public void changedUpdate(DocumentEvent e) { accion.run(); }
		});
	}

	// Resumen y Edad
	private void actualizarEdad() {
		LocalDate fecha = leerFecha(vista.getTxtFechaNacimiento().getText());
		if (fecha == null || fecha.isAfter(LocalDate.now())) {
			vista.getTxtEdad().setText("");
		} else {
			vista.getTxtEdad().setText(calcularEdad(fecha) + " años");
		}
	}

	private void actualizarResumen() {
		String nombre = (vista.getTxtNombres().getText().trim() + " " + vista.getTxtApellidos().getText().trim()).trim();

		String documento = vista.getTxtNumDocumento().getText().trim();
		if (!documento.isEmpty()) {
			documento = abreviatura() + " " + documento;
		}

		String fecha = vista.getTxtFechaNacimiento().getText().trim();
		LocalDate f = leerFecha(fecha);
		if (f != null && !f.isAfter(LocalDate.now())) {
			fecha += " (" + calcularEdad(f) + " años)";
		}

		vista.actualizarResumen(
				nombre,
				documento,
				fecha,
				vista.getTxtTelCelular().getText().trim(),
				vista.getTxtCorreo().getText().trim(),
				vista.getTxtDireccion().getText().trim(),
				(String) vista.getCmbEps().getSelectedItem(),
				(String) vista.getCmbRegimen().getSelectedItem());
	}

	private String abreviatura() {
		switch (vista.getCmbTipoDoc().getSelectedIndex()) {
		case 0: return "CC";
		case 1: return "CE";
		case 2: return "PA";
		default: return "TI";
		}
	}

	private boolean esPasaporte() {
		return "Pasaporte".equals(vista.getCmbTipoDoc().getSelectedItem());
	}

	private int calcularEdad(LocalDate nacimiento) {
		return Period.between(nacimiento, LocalDate.now()).getYears();
	}

	private LocalDate leerFecha(String texto) {
		try {
			return LocalDate.parse(texto.trim(), Paciente.FORMATO_FECHA);
		} catch (DateTimeParseException e) {
			return null;
		}
	}

	// Validación y Guardado

	private void guardar() {
		List<String> errores = new ArrayList<>();
		primerError = null;

		String tipoDoc = (String) vista.getCmbTipoDoc().getSelectedItem();
		String numDoc = vista.getTxtNumDocumento().getText().trim().toUpperCase();
		String nombres = normalizar(vista.getTxtNombres().getText());
		String apellidos = normalizar(vista.getTxtApellidos().getText());
		String textoFecha = vista.getTxtFechaNacimiento().getText().trim();
		String telFijo = vista.getTxtTelFijo().getText().replace(" ", "");
		String telCel = vista.getTxtTelCelular().getText().replace(" ", "");
		String correo = vista.getTxtCorreo().getText().trim();
		String direccion = normalizar(vista.getTxtDireccion().getText());
		String ocupacion = normalizar(vista.getTxtOcupacion().getText());

		// Número de documento
		boolean documentoValido = false;
		if (numDoc.isEmpty()) {
			error(errores, "El número de documento es obligatorio.", vista.getTxtNumDocumento());
		} else if (esPasaporte()) {
			if (numDoc.matches("[A-Z0-9]{5,15}")) {
				documentoValido = true;
			} else {
				error(errores, "El pasaporte debe tener entre 5 y 15 letras o números.", vista.getTxtNumDocumento());
			}
		} else if (numDoc.matches("\\d{6,11}")) {
			documentoValido = true;
		} else {
			error(errores, "El número de documento debe tener solo números (entre 6 y 11 dígitos).", vista.getTxtNumDocumento());
		}

		//  Nombres y apellidos
		if (nombres.isEmpty()) {
			error(errores, "Los nombres son obligatorios.", vista.getTxtNombres());
		} else if (!NOMBRE.matcher(nombres).matches()) {
			error(errores, "Los nombres solo pueden tener letras (mínimo 2).", vista.getTxtNombres());
		}

		if (apellidos.isEmpty()) {
			error(errores, "Los apellidos son obligatorios.", vista.getTxtApellidos());
		} else if (!NOMBRE.matcher(apellidos).matches()) {
			error(errores, "Los apellidos solo pueden tener letras (mínimo 2).", vista.getTxtApellidos());
		}

		// Fecha de nacimiento 
		LocalDate fecha = null;
		if (textoFecha.isEmpty()) {
			error(errores, "La fecha de nacimiento es obligatoria.", vista.getTxtFechaNacimiento());
		} else {
			fecha = leerFecha(textoFecha);
			if (fecha == null) {
				error(errores, "La fecha de nacimiento no es válida. Use el formato dd/mm/aaaa (ej: 15/04/1985).",
						vista.getTxtFechaNacimiento());
			} else if (fecha.isAfter(LocalDate.now())) {
				error(errores, "La fecha de nacimiento no puede ser futura.", vista.getTxtFechaNacimiento());
				fecha = null;
			} else if (fecha.getYear() < 1900) {
				error(errores, "La fecha de nacimiento no puede ser anterior a 1900.", vista.getTxtFechaNacimiento());
				fecha = null;
			}
		}

		// Teléfonos 
		if (!telFijo.isEmpty() && !telFijo.matches("\\d{7}|\\d{10}")) {
			error(errores, "El teléfono fijo debe tener 7 o 10 dígitos (con indicativo).", vista.getTxtTelFijo());
		}

		if (telCel.isEmpty()) {
			error(errores, "El teléfono celular es obligatorio.", vista.getTxtTelCelular());
		} else if (!telCel.matches("3\\d{9}")) {
			error(errores, "El celular debe tener 10 dígitos y empezar por 3.", vista.getTxtTelCelular());
		}

		// Correo (opcional, pero si se escribe debe ser válido) 
		if (!correo.isEmpty() && !CORREO.matcher(correo).matches()) {
			error(errores, "El correo electrónico no es válido (ej: nombre@dominio.com).", vista.getTxtCorreo());
		}

		// Dirección 
		if (direccion.isEmpty()) {
			error(errores, "La dirección de residencia es obligatoria.", vista.getTxtDireccion());
		} else if (direccion.length() < 5) {
			error(errores, "La dirección es demasiado corta.", vista.getTxtDireccion());
		}

		// EPS 
		if (vista.getCmbEps().getSelectedIndex() < 0) {
			error(errores, "Seleccione la EPS del paciente.", vista.getCmbEps());
		}

		// Paciente repetido 
		if (documentoValido && dao.existe(tipoDoc, numDoc)) {
			error(errores, "Ya existe un paciente registrado con ese tipo y número de documento.",
					vista.getTxtNumDocumento());
		}

		// Si hubo errores, se muestran todos juntos 
		if (!errores.isEmpty()) {
			StringBuilder sb = new StringBuilder("Corrija lo siguiente antes de guardar:\n\n");
			for (String e : errores) {
				sb.append("• ").append(e).append("\n");
			}
			vista.mostrarError(sb.toString());
			if (primerError != null) {
				primerError.requestFocusInWindow();
			}
			return;
		}

		// Todo correcto: se crea el paciente y se guarda
		Paciente paciente = new Paciente(
				tipoDoc, numDoc, nombres, apellidos, fecha,
				(String) vista.getCmbGenero().getSelectedItem(),
				telFijo, telCel, correo.toLowerCase(), direccion,
				seleccion(vista.getCmbEps()),
				seleccion(vista.getCmbRegimen()),
				ocupacion,
				(String) vista.getCmbEstadoCivil().getSelectedItem(),
				(String) vista.getCmbCondicion().getSelectedItem(),
				vista.getTxtObservaciones().getText().trim());

		try {
			dao.agregar(paciente);
			vista.mostrarInfo("Paciente \"" + paciente.getNombreCompleto() + "\" guardado correctamente.");
			vista.limpiarFormulario();
			actualizarResumen();
		} catch (IOException ex) {
			vista.mostrarError("No se pudo guardar el paciente en el archivo JSON:\n" + ex.getMessage());
		}
	}

	private void error(List<String> errores, String mensaje, JComponent campo) {
		errores.add(mensaje);
		if (primerError == null) {
			primerError = campo;
		}
	}

	private String seleccion(javax.swing.JComboBox<String> combo) {
		Object item = combo.getSelectedItem();
		return item == null ? "" : item.toString();
	}

	private String normalizar(String s) {
		return s.trim().replaceAll("\\s+", " ");
	}
}