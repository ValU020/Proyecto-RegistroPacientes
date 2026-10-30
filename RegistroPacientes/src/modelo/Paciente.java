package modelo;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.LinkedHashMap;
import java.util.Map;

public class Paciente {

	public static final DateTimeFormatter FORMATO_FECHA =
			DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

	private final String tipoDocumento;
	private final String numeroDocumento;
	private final String nombres;
	private final String apellidos;
	private final LocalDate fechaNacimiento;
	private final String genero;
	private final String telefonoFijo;
	private final String telefonoCelular;
	private final String correo;
	private final String direccion;
	private final String eps;
	private final String regimen;
	private final String ocupacion;
	private final String estadoCivil;
	private final String condicionMedica;
	private final String observaciones;

	public Paciente(String tipoDocumento, String numeroDocumento, String nombres, String apellidos,
			LocalDate fechaNacimiento, String genero, String telefonoFijo, String telefonoCelular,
			String correo, String direccion, String eps, String regimen, String ocupacion,
			String estadoCivil, String condicionMedica, String observaciones) {
		this.tipoDocumento = tipoDocumento;
		this.numeroDocumento = numeroDocumento;
		this.nombres = nombres;
		this.apellidos = apellidos;
		this.fechaNacimiento = fechaNacimiento;
		this.genero = genero;
		this.telefonoFijo = telefonoFijo;
		this.telefonoCelular = telefonoCelular;
		this.correo = correo;
		this.direccion = direccion;
		this.eps = eps;
		this.regimen = regimen;
		this.ocupacion = ocupacion;
		this.estadoCivil = estadoCivil;
		this.condicionMedica = condicionMedica;
		this.observaciones = observaciones;
	}

	// ----------------------- Getters -----------------------

	public String getTipoDocumento() { return tipoDocumento; }
	public String getNumeroDocumento() { return numeroDocumento; }
	public String getNombres() { return nombres; }
	public String getApellidos() { return apellidos; }
	public LocalDate getFechaNacimiento() { return fechaNacimiento; }
	public String getGenero() { return genero; }
	public String getTelefonoFijo() { return telefonoFijo; }
	public String getTelefonoCelular() { return telefonoCelular; }
	public String getCorreo() { return correo; }
	public String getDireccion() { return direccion; }
	public String getEps() { return eps; }
	public String getRegimen() { return regimen; }
	public String getOcupacion() { return ocupacion; }
	public String getEstadoCivil() { return estadoCivil; }
	public String getCondicionMedica() { return condicionMedica; }
	public String getObservaciones() { return observaciones; }

	// ----------------------- Lógica propia -----------------------

	public String getNombreCompleto() {
		return nombres + " " + apellidos;
	}

	public int getEdad() {
		return Period.between(fechaNacimiento, LocalDate.now()).getYears();
	}

	// JSON 

	public Map<String, String> toMap() {
		Map<String, String> m = new LinkedHashMap<>();
		m.put("tipoDocumento", tipoDocumento);
		m.put("numeroDocumento", numeroDocumento);
		m.put("nombres", nombres);
		m.put("apellidos", apellidos);
		m.put("fechaNacimiento", fechaNacimiento.toString());
		m.put("genero", genero);
		m.put("telefonoFijo", telefonoFijo);
		m.put("telefonoCelular", telefonoCelular);
		m.put("correo", correo);
		m.put("direccion", direccion);
		m.put("eps", eps);
		m.put("regimen", regimen);
		m.put("ocupacion", ocupacion);
		m.put("estadoCivil", estadoCivil);
		m.put("condicionMedica", condicionMedica);
		m.put("observaciones", observaciones);
		return m;
	}

	public static Paciente fromMap(Map<String, String> m) {
		return new Paciente(
				dato(m, "tipoDocumento"), dato(m, "numeroDocumento"),
				dato(m, "nombres"), dato(m, "apellidos"),
				LocalDate.parse(dato(m, "fechaNacimiento")),
				dato(m, "genero"), dato(m, "telefonoFijo"), dato(m, "telefonoCelular"),
				dato(m, "correo"), dato(m, "direccion"), dato(m, "eps"), dato(m, "regimen"),
				dato(m, "ocupacion"), dato(m, "estadoCivil"), dato(m, "condicionMedica"),
				dato(m, "observaciones"));
	}

	private static String dato(Map<String, String> m, String clave) {
		String v = m.get(clave);
		return v == null ? "" : v;
	}
}