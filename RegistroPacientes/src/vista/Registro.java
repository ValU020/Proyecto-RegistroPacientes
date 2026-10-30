package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class Registro extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	// Datos personales
	private JComboBox<String> cmbTipoDoc;
	private JTextField txtNumDocumento;
	private JTextField txtNombres;
	private JTextField txtApellidos;
	private JTextField txtFechaNacimiento;
	private JTextField txtEdad;
	private JComboBox<String> cmbGenero;

	// Contacto
	private JTextField txtTelFijo;
	private JTextField txtTelCelular;
	private JTextField txtCorreo;
	private JTextField txtDireccion;

	// Información adicional
	private JComboBox<String> cmbEps;
	private JComboBox<String> cmbRegimen;
	private JTextField txtOcupacion;
	private JComboBox<String> cmbEstadoCivil;
	private JComboBox<String> cmbCondicion;
	private JTextArea txtObservaciones;

	// Botones
	private JButton btnCancelar;
	private JButton btnGuardar;

	// Etiquetas del panel de resumen
	private JLabel lblPacienteNombre;
	private JLabel lblPacienteDocResumen;
	private JLabel lblResFecha;
	private JLabel lblResTel;
	private JLabel lblResCorreo;
	private JLabel lblResDir;
	private JLabel lblResEps;
	private JLabel lblResRegimen;

	private static String req(String texto) {
		return "<html>" + texto + " <font color='red'>*</font></html>";
	}

	public Registro() {
		setTitle("Laboratorio Clínico - Registro de Pacientes");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1280, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		// 1. Menú de Navegación
		JPanel panelMenu = new JPanel();
		panelMenu.setBackground(new Color(24, 43, 73));
		contentPane.add(panelMenu, BorderLayout.WEST);
		panelMenu.setLayout(new GridLayout(10, 1, 0, 5));
		panelMenu.setBorder(new EmptyBorder(20, 15, 20, 15));

		JLabel lblLogo = new JLabel("Laboratorio Clínico");
		lblLogo.setForeground(Color.WHITE);
		lblLogo.setFont(new Font("Tahoma", Font.BOLD, 14));
		panelMenu.add(lblLogo);

		JLabel lblInicio = new JLabel("Inicio");
		lblInicio.setForeground(Color.WHITE);
		panelMenu.add(lblInicio);

		JLabel lblPacientes = new JLabel("Pacientes");
		lblPacientes.setForeground(Color.WHITE);
		panelMenu.add(lblPacientes);

		JLabel lblMuestras = new JLabel("Toma de muestras");
		lblMuestras.setForeground(Color.WHITE);
		panelMenu.add(lblMuestras);

		JLabel lblResultados = new JLabel("Resultados");
		lblResultados.setForeground(Color.WHITE);
		panelMenu.add(lblResultados);

		JLabel lblReportes = new JLabel("Reportes");
		lblReportes.setForeground(Color.WHITE);
		panelMenu.add(lblReportes);

		JLabel lblConfig = new JLabel("Configuración");
		lblConfig.setForeground(Color.WHITE);
		panelMenu.add(lblConfig);

		// 2. Panel Central Principal
		JPanel panelPrincipal = new JPanel();
		contentPane.add(panelPrincipal, BorderLayout.CENTER);
		panelPrincipal.setLayout(new BorderLayout(0, 0));

		JPanel panelTopBar = new JPanel();
		panelPrincipal.add(panelTopBar, BorderLayout.NORTH);
		panelTopBar.setLayout(new BorderLayout());
		panelTopBar.setBorder(new EmptyBorder(10, 20, 10, 20));

		JLabel lblTituloVentana = new JLabel("Registrar paciente");
		lblTituloVentana.setFont(new Font("Tahoma", Font.BOLD, 20));
		panelTopBar.add(lblTituloVentana, BorderLayout.WEST);

		JLabel lblUsuario = new JLabel("👤 Fernando Pérez (Administrador)");
		panelTopBar.add(lblUsuario, BorderLayout.EAST);

		JPanel panelContenidoGrid = new JPanel();
		panelPrincipal.add(panelContenidoGrid, BorderLayout.CENTER);
		panelContenidoGrid.setLayout(null);

		// 3. Formulario de Registro
		JPanel panelFormulario = new JPanel();
		panelFormulario.setBounds(20, 10, 780, 680);
		panelContenidoGrid.add(panelFormulario);
		panelFormulario.setLayout(null);

		// Sección: Datos personales
		JPanel panelDatosPersonales = new JPanel();
		panelDatosPersonales.setBorder(new TitledBorder(null, "Datos personales", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelDatosPersonales.setBounds(0, 0, 780, 210);
		panelFormulario.add(panelDatosPersonales);
		panelDatosPersonales.setLayout(null);

		JLabel lblTipoDoc = new JLabel(req("Tipo de documento:"));
		lblTipoDoc.setBounds(20, 25, 150, 14);
		panelDatosPersonales.add(lblTipoDoc);
		cmbTipoDoc = new JComboBox<>(new String[] {"Cédula de ciudadanía", "Cédula de extranjería", "Pasaporte", "Tarjeta de identidad"});
		cmbTipoDoc.setBounds(20, 45, 340, 25);
		panelDatosPersonales.add(cmbTipoDoc);

		JLabel lblNumDoc = new JLabel(req("Número de documento:"));
		lblNumDoc.setBounds(390, 25, 150, 14);
		panelDatosPersonales.add(lblNumDoc);
		txtNumDocumento = new JTextField();
		txtNumDocumento.setBounds(390, 45, 340, 25);
		panelDatosPersonales.add(txtNumDocumento);

		JLabel lblNombres = new JLabel(req("Nombres:"));
		lblNombres.setBounds(20, 80, 150, 14);
		panelDatosPersonales.add(lblNombres);
		txtNombres = new JTextField();
		txtNombres.setBounds(20, 100, 340, 25);
		panelDatosPersonales.add(txtNombres);

		JLabel lblApellidos = new JLabel(req("Apellidos:"));
		lblApellidos.setBounds(390, 80, 150, 14);
		panelDatosPersonales.add(lblApellidos);
		txtApellidos = new JTextField();
		txtApellidos.setBounds(390, 100, 340, 25);
		panelDatosPersonales.add(txtApellidos);

		JLabel lblFechaNac = new JLabel(req("Fecha de nacimiento (dd/mm/aaaa):"));
		lblFechaNac.setBounds(20, 135, 220, 14);
		panelDatosPersonales.add(lblFechaNac);
		txtFechaNacimiento = new JTextField();
		txtFechaNacimiento.setBounds(20, 155, 160, 25);
		panelDatosPersonales.add(txtFechaNacimiento);

		JLabel lblEdad = new JLabel("Edad:");
		lblEdad.setBounds(237, 135, 80, 14);
		panelDatosPersonales.add(lblEdad);
		txtEdad = new JTextField();
		txtEdad.setEditable(false);
		txtEdad.setBounds(237, 155, 80, 25);
		panelDatosPersonales.add(txtEdad);

		JLabel lblGenero = new JLabel(req("Género:"));
		lblGenero.setBounds(390, 135, 150, 14);
		panelDatosPersonales.add(lblGenero);
		cmbGenero = new JComboBox<>(new String[] {"Masculino", "Femenino", "Otro"});
		cmbGenero.setBounds(390, 155, 340, 25);
		panelDatosPersonales.add(cmbGenero);

		// Sección: Información de contacto
		JPanel panelContacto = new JPanel();
		panelContacto.setBorder(new TitledBorder(null, "Información de contacto", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelContacto.setBounds(0, 220, 780, 155);
		panelFormulario.add(panelContacto);
		panelContacto.setLayout(null);

		JLabel lblTelFijoLbl = new JLabel("Teléfono fijo:");
		lblTelFijoLbl.setBounds(20, 25, 150, 14);
		panelContacto.add(lblTelFijoLbl);
		txtTelFijo = new JTextField();
		txtTelFijo.setBounds(20, 45, 340, 25);
		panelContacto.add(txtTelFijo);

		JLabel lblTelCelLbl = new JLabel(req("Teléfono celular:"));
		lblTelCelLbl.setBounds(390, 25, 150, 14);
		panelContacto.add(lblTelCelLbl);
		txtTelCelular = new JTextField();
		txtTelCelular.setBounds(390, 45, 340, 25);
		panelContacto.add(txtTelCelular);

		JLabel lblCorreoLbl = new JLabel("Correo electrónico:");
		lblCorreoLbl.setBounds(20, 80, 150, 14);
		panelContacto.add(lblCorreoLbl);
		txtCorreo = new JTextField();
		txtCorreo.setBounds(20, 100, 340, 25);
		panelContacto.add(txtCorreo);

		JLabel lblDirLbl = new JLabel(req("Dirección de residencia:"));
		lblDirLbl.setBounds(390, 80, 180, 14);
		panelContacto.add(lblDirLbl);
		txtDireccion = new JTextField();
		txtDireccion.setBounds(390, 100, 340, 25);
		panelContacto.add(txtDireccion);

		// Sección: Información adicional
		JPanel panelAdicional = new JPanel();
		panelAdicional.setBorder(new TitledBorder(null, "Información adicional", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelAdicional.setBounds(0, 385, 780, 220);
		panelFormulario.add(panelAdicional);
		panelAdicional.setLayout(null);

		JLabel lblEps = new JLabel(req("EPS:"));
		lblEps.setBounds(20, 25, 150, 14);
		panelAdicional.add(lblEps);
		cmbEps = new JComboBox<>(new String[] {"Nueva EPS", "Aliansalud", "Capital Salud", "Compensar", "Famisanar", "Salud Total", "Sanitas", "Sura" });
		cmbEps.setSelectedIndex(-1);
		cmbEps.setBounds(20, 45, 340, 25);
		panelAdicional.add(cmbEps);

		JLabel lblRegimen = new JLabel("Régimen");
		lblRegimen.setBounds(390, 25, 150, 14);
		panelAdicional.add(lblRegimen);
		cmbRegimen = new JComboBox<>(new String[] {"Contributivo", "Subsidiado"});
		cmbRegimen.setSelectedIndex(-1);
		cmbRegimen.setBounds(390, 45, 340, 25);
		panelAdicional.add(cmbRegimen);

		JLabel lblOcupacion = new JLabel("Ocupación:");
		lblOcupacion.setBounds(20, 80, 150, 14);
		panelAdicional.add(lblOcupacion);
		txtOcupacion = new JTextField();
		txtOcupacion.setBounds(20, 100, 340, 25);
		panelAdicional.add(txtOcupacion);

		JLabel lblEstadoCivil = new JLabel("Estado civil:");
		lblEstadoCivil.setBounds(390, 80, 150, 14);
		panelAdicional.add(lblEstadoCivil);
		cmbEstadoCivil = new JComboBox<>(new String[] {"Soltero(a)", "Casado(a)", "Unión libre", "Divorciado"});
		cmbEstadoCivil.setBounds(390, 100, 340, 25);
		panelAdicional.add(cmbEstadoCivil);

		JLabel lblCondicion = new JLabel("¿Tiene alguna condición médica relevante?");
		lblCondicion.setBounds(20, 135, 300, 14);
		panelAdicional.add(lblCondicion);
		cmbCondicion = new JComboBox<>(new String[] {"No", "Sí"});
		cmbCondicion.setBounds(20, 155, 340, 25);
		panelAdicional.add(cmbCondicion);

		JLabel lblObservaciones = new JLabel("Observaciones");
		lblObservaciones.setBounds(390, 135, 150, 14);
		panelAdicional.add(lblObservaciones);
		txtObservaciones = new JTextArea();
		txtObservaciones.setLineWrap(true);
		txtObservaciones.setWrapStyleWord(true);
		JScrollPane scrollObs = new JScrollPane(txtObservaciones);
		scrollObs.setBounds(390, 155, 340, 50);
		panelAdicional.add(scrollObs);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(500, 620, 110, 30);
		panelFormulario.add(btnCancelar);

		btnGuardar = new JButton("Guardar paciente");
		btnGuardar.setBackground(new Color(24, 43, 73));
		btnGuardar.setForeground(Color.WHITE);
		btnGuardar.setBounds(620, 620, 140, 30);
		panelFormulario.add(btnGuardar);

		// 4. Resumen y Alerta
		JPanel panelDerecho = new JPanel();
		panelDerecho.setBounds(815, 10, 360, 680);
		panelContenidoGrid.add(panelDerecho);
		panelDerecho.setLayout(null);

		JPanel cardResumen = new JPanel();
		cardResumen.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
		cardResumen.setBackground(Color.WHITE);
		cardResumen.setBounds(0, 0, 360, 420);
		panelDerecho.add(cardResumen);
		cardResumen.setLayout(null);

		JLabel lblResumenTitulo = new JLabel("Resumen");
		lblResumenTitulo.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblResumenTitulo.setBounds(20, 15, 200, 20);
		cardResumen.add(lblResumenTitulo);

		lblPacienteNombre = new JLabel("—");
		lblPacienteNombre.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblPacienteNombre.setBounds(20, 55, 320, 20);
		cardResumen.add(lblPacienteNombre);

		lblPacienteDocResumen = new JLabel("—");
		lblPacienteDocResumen.setForeground(Color.GRAY);
		lblPacienteDocResumen.setBounds(20, 75, 320, 20);
		cardResumen.add(lblPacienteDocResumen);

		lblResFecha = crearDatoResumen(cardResumen, "Fecha de nacimiento", 120);
		lblResTel = crearDatoResumen(cardResumen, "Teléfono celular", 160);
		lblResCorreo = crearDatoResumen(cardResumen, "Correo electrónico", 200);
		lblResDir = crearDatoResumen(cardResumen, "Dirección", 240);
		lblResEps = crearDatoResumen(cardResumen, "EPS", 280);
		lblResRegimen = crearDatoResumen(cardResumen, "Régimen", 320);

		// Tarjeta de Alerta
		JPanel cardAlerta = new JPanel();
		cardAlerta.setBackground(new Color(230, 242, 255));
		cardAlerta.setBorder(new LineBorder(new Color(180, 215, 250), 1, true));
		cardAlerta.setBounds(0, 435, 360, 185);
		panelDerecho.add(cardAlerta);
		cardAlerta.setLayout(null);

		JLabel lblAlertaTitulo = new JLabel("Tenga en cuenta");
		lblAlertaTitulo.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblAlertaTitulo.setBounds(15, 15, 320, 20);
		cardAlerta.add(lblAlertaTitulo);

		JTextArea txtAlertaTexto = new JTextArea(
			"Los datos del paciente son necesarios para la generación de la orden médica, la trazabilidad de las muestras y la entrega de resultados."
		);
		txtAlertaTexto.setWrapStyleWord(true);
		txtAlertaTexto.setLineWrap(true);
		txtAlertaTexto.setOpaque(false);
		txtAlertaTexto.setEditable(false);
		txtAlertaTexto.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtAlertaTexto.setBounds(15, 45, 330, 110);
		cardAlerta.add(txtAlertaTexto);
	}

	private JLabel crearDatoResumen(JPanel card, String titulo, int y) {
		JLabel lblTitulo = new JLabel(titulo);
		lblTitulo.setForeground(Color.GRAY);
		lblTitulo.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblTitulo.setBounds(20, y - 15, 320, 14);
		card.add(lblTitulo);

		JLabel lblValor = new JLabel("—");
		lblValor.setBounds(20, y, 320, 20);
		card.add(lblValor);
		return lblValor;
	}

	public void actualizarResumen(String nombre, String documento, String fecha, String telefono,
			String correo, String direccion, String eps, String regimen) {
		lblPacienteNombre.setText(vacioASeparador(nombre));
		lblPacienteDocResumen.setText(vacioASeparador(documento));
		lblResFecha.setText(vacioASeparador(fecha));
		lblResTel.setText(vacioASeparador(telefono));
		lblResCorreo.setText(vacioASeparador(correo));
		lblResDir.setText(vacioASeparador(direccion));
		lblResEps.setText(vacioASeparador(eps));
		lblResRegimen.setText(vacioASeparador(regimen));
	}

	private String vacioASeparador(String s) {
		return (s == null || s.trim().isEmpty()) ? "—" : s;
	}

	public void limpiarFormulario() {
		cmbTipoDoc.setSelectedIndex(0);
		txtNumDocumento.setText("");
		txtNombres.setText("");
		txtApellidos.setText("");
		txtFechaNacimiento.setText("");
		txtEdad.setText("");
		cmbGenero.setSelectedIndex(0);
		txtTelFijo.setText("");
		txtTelCelular.setText("");
		txtCorreo.setText("");
		txtDireccion.setText("");
		cmbEps.setSelectedIndex(-1);
		cmbRegimen.setSelectedIndex(-1);
		txtOcupacion.setText("");
		cmbEstadoCivil.setSelectedIndex(0);
		cmbCondicion.setSelectedIndex(0);
		txtObservaciones.setText("");
		txtNombres.requestFocusInWindow();
	}

	public void mostrarError(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, "Datos incorrectos", JOptionPane.ERROR_MESSAGE);
	}

	public void mostrarInfo(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
	}

	public JComboBox<String> getCmbTipoDoc() { return cmbTipoDoc; }
	public JTextField getTxtNumDocumento() { return txtNumDocumento; }
	public JTextField getTxtNombres() { return txtNombres; }
	public JTextField getTxtApellidos() { return txtApellidos; }
	public JTextField getTxtFechaNacimiento() { return txtFechaNacimiento; }
	public JTextField getTxtEdad() { return txtEdad; }
	public JComboBox<String> getCmbGenero() { return cmbGenero; }
	public JTextField getTxtTelFijo() { return txtTelFijo; }
	public JTextField getTxtTelCelular() { return txtTelCelular; }
	public JTextField getTxtCorreo() { return txtCorreo; }
	public JTextField getTxtDireccion() { return txtDireccion; }
	public JComboBox<String> getCmbEps() { return cmbEps; }
	public JComboBox<String> getCmbRegimen() { return cmbRegimen; }
	public JTextField getTxtOcupacion() { return txtOcupacion; }
	public JComboBox<String> getCmbEstadoCivil() { return cmbEstadoCivil; }
	public JComboBox<String> getCmbCondicion() { return cmbCondicion; }
	public JTextArea getTxtObservaciones() { return txtObservaciones; }
	public JButton getBtnCancelar() { return btnCancelar; }
	public JButton getBtnGuardar() { return btnGuardar; }
}