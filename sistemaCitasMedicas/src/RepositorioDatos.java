import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RepositorioDatos {

    private List<Paciente> pacientes = new ArrayList<>();
    private List<Medico> medicos = new ArrayList<>();
    private List<Administrador> administradores = new ArrayList<>();
    private List<Cita> citas = new ArrayList<>();
    private int secuenciaCita = 1;

    public void agregarPaciente(Paciente p) {
        pacientes.add(p);
    }

    public void agregarMedico(Medico m) {
        medicos.add(m);
    }

    public void agregarAdministrador(Administrador a) {
        administradores.add(a);
    }

    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public List<Medico> getMedicos() {
        return medicos;
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public Usuario buscarUsuarioPorCorreo(String correo) {
        for (Paciente p : pacientes) {
            if (p.getCorreo().equalsIgnoreCase(correo)) return p;
        }
        for (Medico m : medicos) {
            if (m.getCorreo().equalsIgnoreCase(correo)) return m;
        }
        for (Administrador a : administradores) {
            if (a.getCorreo().equalsIgnoreCase(correo)) return a;
        }
        return null;
    }

    public int generarIdCita() {
        return secuenciaCita++;
    }

    public void agregarCita(Cita c) {
        citas.add(c);
    }

    public boolean existeCitaEnHorario(Medico medico, LocalDateTime fechaHora) {
        for (Cita c : citas) {
            if (c.getMedico().equals(medico)
                    && c.getFechaHora().equals(fechaHora)
                    && c.getEstado().equals("PROGRAMADA")) {
                return true;
            }
        }
        return false;
    }

    public List<Cita> obtenerCitasPorPaciente(Paciente paciente) {
        List<Cita> resultado = new ArrayList<>();
        for (Cita c : citas) {
            if (c.getPaciente().equals(paciente)) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    public List<Cita> obtenerCitasPorMedico(Medico medico) {
        List<Cita> resultado = new ArrayList<>();
        for (Cita c : citas) {
            if (c.getMedico().equals(medico)) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    public List<Cita> obtenerCitasPorMedicoYFecha(Medico medico, LocalDate fecha) {
        List<Cita> resultado = new ArrayList<>();
        for (Cita c : citas) {
            if (c.getMedico().equals(medico)
                    && c.getFechaHora().toLocalDate().equals(fecha)) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    public Cita buscarCitaPorId(int idCita) {
        for (Cita c : citas) {
            if (c.getIdCita() == idCita) {
                return c;
            }
        }
        return null;
    }
}
