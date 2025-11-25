public class Paciente extends Usuario {

    public Paciente(String idUsuario, String nombre, String correo, String contrasena) {
        super(idUsuario, nombre, correo, contrasena, Rol.PACIENTE);
    }
}
