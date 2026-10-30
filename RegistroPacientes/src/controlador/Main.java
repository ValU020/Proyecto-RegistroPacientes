package controlador;

import java.awt.EventQueue;
import javax.swing.JOptionPane;
import modelo.PacienteDAO;
import vista.Registro;

public class Main {

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				PacienteDAO dao = new PacienteDAO("pacientes.json");
				Registro vista = new Registro();
				new ControladorRegistro(vista, dao);
				vista.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"No se pudo iniciar la aplicación:\n" + e.getMessage(),
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		});
	}
}