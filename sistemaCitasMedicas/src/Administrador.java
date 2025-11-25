public class Administrador extends Usuario {

    public Administrador(String idUsuario, String nombre, String correo, String contrasena) {
        super(idUsuario, nombre, correo, contrasena, Rol.ADMIN);
    }
}
