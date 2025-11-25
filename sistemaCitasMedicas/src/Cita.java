import java.time.LocalDateTime;

public class Cita {
    private int idCita;
    private Paciente paciente;
    private Medico medico;
    private LocalDateTime fechaHora;
    private String motivo;
    private String estado; // PROGRAMADA, CANCELADA, COMPLETADA

    public Cita(int idCita, Paciente paciente, Medico medico,
                LocalDateTime fechaHora, String motivo) {
        this.idCita = idCita;
        this.paciente = paciente;
        this.medico = medico;
        this.fechaHora = fechaHora;
        this.motivo = motivo;
        this.estado = "PROGRAMADA";
    }

    public int getIdCita() {
        return idCita;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
