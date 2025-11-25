public class Medico extends Usuario {

    private String especialidad;

    public Medico(String idUsuario, String nombre, String correo, String contrasena, String especialidad) {
        super(idUsuario, nombre, correo, contrasena, Rol.MEDICO);
        this.especialidad = especialidad;
    }

    public String getEspecialidad() {
        return especialidad;
    }
}
