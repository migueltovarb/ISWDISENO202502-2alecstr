import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class SistemaCitasApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static final RepositorioDatos repo = new RepositorioDatos();
    private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        inicializarDatosEjemplo();
        mostrarMenuPrincipal();
    }

    private static void inicializarDatosEjemplo() {
        // Administrador por defecto
        Administrador admin = new Administrador(
                "admin",
                "Administrador General",
                "admin@clinica.com",
                "admin123"
        );
        repo.agregarAdministrador(admin);

        // Médicos de ejemplo
        Medico m1 = new Medico("m1", "Dr. Juan Pérez", "juan@clinica.com", "1234", "Cardiología");
        Medico m2 = new Medico("m2", "Dra. Ana Gómez", "ana@clinica.com", "1234", "Pediatría");
        repo.agregarMedico(m1);
        repo.agregarMedico(m2);
    }

    private static void mostrarMenuPrincipal() {
        int opcion;
        do {
            System.out.println("=======================================");
            System.out.println(" SISTEMA DE GESTIÓN DE CITAS MÉDICAS ");
            System.out.println("=======================================");
            System.out.println("1. Registrarse como paciente");
            System.out.println("2. Iniciar sesión");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = leerEntero();

            switch (opcion) {
                case 1:
                    registrarPaciente();
                    break;
                case 2:
                    iniciarSesion();
                    break;
                case 0:
                    System.out.println("Saliendo del sistema. Hasta pronto.");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private static void registrarPaciente() {
        System.out.println("=== Registro de Paciente ===");
        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine();
        System.out.print("Correo electrónico: ");
        String correo = scanner.nextLine();

        if (repo.buscarUsuarioPorCorreo(correo) != null) {
            System.out.println("Ya existe un usuario registrado con ese correo.");
            return;
        }

        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();

        // Id simple: "p" + número
        String id = "p" + (repo.getPacientes().size() + 1);
        Paciente paciente = new Paciente(id, nombre, correo, contrasena);
        repo.agregarPaciente(paciente);

        System.out.println("Paciente registrado correctamente. Ahora puede iniciar sesión.");
    }

    private static void iniciarSesion() {
        System.out.println("=== Inicio de Sesión ===");
        System.out.print("Correo: ");
        String correo = scanner.nextLine();
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();

        Usuario usuario = repo.buscarUsuarioPorCorreo(correo);
        if (usuario == null || !usuario.getContrasena().equals(contrasena)) {
            System.out.println("Credenciales inválidas.");
            return;
        }

        switch (usuario.getRol()) {
            case PACIENTE:
                menuPaciente((Paciente) usuario);
                break;
            case MEDICO:
                menuMedico((Medico) usuario);
                break;
            case ADMIN:
                menuAdministrador((Administrador) usuario);
                break;
            default:
                System.out.println("Rol no reconocido.");
        }
    }

    // ================== MENÚ PACIENTE ==================

    private static void menuPaciente(Paciente paciente) {
        int opcion;
        do {
            System.out.println("\n=== Menú Paciente ===");
            System.out.println("Bienvenido, " + paciente.getNombre());
            System.out.println("1. Ver médicos");
            System.out.println("2. Agendar cita");
            System.out.println("3. Ver mis citas");
            System.out.println("4. Cancelar cita");
            System.out.println("0. Cerrar sesión");
            System.out.print("Seleccione una opción: ");

            opcion = leerEntero();

            switch (opcion) {
                case 1:
                    listarMedicos();
                    break;
                case 2:
                    agendarCita(paciente);
                    break;
                case 3:
                    verCitasPaciente(paciente);
                    break;
                case 4:
                    cancelarCitaPaciente(paciente);
                    break;
                case 0:
                    System.out.println("Cerrando sesión de paciente...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

        } while (opcion != 0);
    }

    private static void listarMedicos() {
        System.out.println("=== Lista de Médicos ===");
        List<Medico> medicos = repo.getMedicos();
        if (medicos.isEmpty()) {
            System.out.println("No hay médicos registrados.");
            return;
        }
        for (int i = 0; i < medicos.size(); i++) {
            Medico m = medicos.get(i);
            System.out.printf("%d. %s - %s%n", i + 1, m.getNombre(), m.getEspecialidad());
        }
    }

    private static void agendarCita(Paciente paciente) {
        System.out.println("=== Agendar Cita ===");
        listarMedicos();
        List<Medico> medicos = repo.getMedicos();
        if (medicos.isEmpty()) return;

        System.out.print("Seleccione el número de médico: ");
        int indiceMedico = leerEntero();
        if (indiceMedico < 1 || indiceMedico > medicos.size()) {
            System.out.println("Médico inválido.");
            return;
        }
        Medico medico = medicos.get(indiceMedico - 1);

        System.out.println("Ingrese la fecha y hora de la cita (formato: yyyy-MM-dd HH:mm)");
        System.out.print("Fecha y hora: ");
        String fechaHoraStr = scanner.nextLine();
        LocalDateTime fechaHora;
        try {
            fechaHora = LocalDateTime.parse(fechaHoraStr, FORMATO_FECHA_HORA);
        } catch (DateTimeParseException e) {
            System.out.println("Formato de fecha y hora inválido.");
            return;
        }

        System.out.print("Motivo de la consulta: ");
        String motivo = scanner.nextLine();

        // Validar que no haya otra cita para ese médico en ese horario
        if (repo.existeCitaEnHorario(medico, fechaHora)) {
            System.out.println("El médico ya tiene una cita en ese horario.");
            return;
        }

        int idCita = repo.generarIdCita();
        Cita cita = new Cita(idCita, paciente, medico, fechaHora, motivo);
        repo.agregarCita(cita);

        System.out.println("Cita agendada correctamente con el " + medico.getNombre() +
                " el " + fechaHora.format(FORMATO_FECHA_HORA));
    }

    private static void verCitasPaciente(Paciente paciente) {
        System.out.println("=== Mis Citas ===");
        List<Cita> citas = repo.obtenerCitasPorPaciente(paciente);
        if (citas.isEmpty()) {
            System.out.println("No tiene citas registradas.");
            return;
        }
        for (Cita c : citas) {
            System.out.printf("ID: %d | Médico: %s | Fecha/hora: %s | Estado: %s | Motivo: %s%n",
                    c.getIdCita(),
                    c.getMedico().getNombre(),
                    c.getFechaHora().format(FORMATO_FECHA_HORA),
                    c.getEstado(),
                    c.getMotivo());
        }
    }

    private static void cancelarCitaPaciente(Paciente paciente) {
        System.out.println("=== Cancelar Cita ===");
        List<Cita> citas = repo.obtenerCitasPorPaciente(paciente);
        if (citas.isEmpty()) {
            System.out.println("No tiene citas para cancelar.");
            return;
        }
        for (Cita c : citas) {
            System.out.printf("ID: %d | Médico: %s | Fecha/hora: %s | Estado: %s%n",
                    c.getIdCita(),
                    c.getMedico().getNombre(),
                    c.getFechaHora().format(FORMATO_FECHA_HORA),
                    c.getEstado());
        }
        System.out.print("Ingrese el ID de la cita a cancelar: ");
        int idCita = leerEntero();
        Cita cita = repo.buscarCitaPorId(idCita);
        if (cita == null || !cita.getPaciente().equals(paciente)) {
            System.out.println("Cita no encontrada o no pertenece al paciente.");
            return;
        }
        if (!cita.getEstado().equals("PROGRAMADA")) {
            System.out.println("Solo se pueden cancelar citas en estado PROGRAMADA.");
            return;
        }
        cita.setEstado("CANCELADA");
        System.out.println("Cita cancelada correctamente.");
    }

    // ================== MENÚ MÉDICO ==================

    private static void menuMedico(Medico medico) {
        int opcion;
        do {
            System.out.println("\n=== Menú Médico ===");
            System.out.println("Bienvenido, " + medico.getNombre());
            System.out.println("1. Ver mis citas");
            System.out.println("2. Ver mis citas por fecha");
            System.out.println("0. Cerrar sesión");
            System.out.print("Seleccione una opción: ");

            opcion = leerEntero();

            switch (opcion) {
                case 1:
                    verCitasMedico(medico);
                    break;
                case 2:
                    verCitasMedicoPorFecha(medico);
                    break;
                case 0:
                    System.out.println("Cerrando sesión de médico...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

        } while (opcion != 0);
    }

    private static void verCitasMedico(Medico medico) {
        System.out.println("=== Citas del Médico ===");
        List<Cita> citas = repo.obtenerCitasPorMedico(medico);
        if (citas.isEmpty()) {
            System.out.println("No tiene citas registradas.");
            return;
        }
        for (Cita c : citas) {
            System.out.printf("ID: %d | Paciente: %s | Fecha/hora: %s | Estado: %s | Motivo: %s%n",
                    c.getIdCita(),
                    c.getPaciente().getNombre(),
                    c.getFechaHora().format(FORMATO_FECHA_HORA),
                    c.getEstado(),
                    c.getMotivo());
        }
    }

    private static void verCitasMedicoPorFecha(Medico medico) {
        System.out.println("=== Citas del Médico por Fecha ===");
        System.out.print("Ingrese la fecha (yyyy-MM-dd): ");
        String fechaStr = scanner.nextLine();
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaStr, FORMATO_FECHA);
        } catch (DateTimeParseException e) {
            System.out.println("Formato de fecha inválido.");
            return;
        }

        List<Cita> citas = repo.obtenerCitasPorMedicoYFecha(medico, fecha);
        if (citas.isEmpty()) {
            System.out.println("No tiene citas en esa fecha.");
            return;
        }
        for (Cita c : citas) {
            System.out.printf("ID: %d | Paciente: %s | Hora: %s | Estado: %s%n",
                    c.getIdCita(),
                    c.getPaciente().getNombre(),
                    c.getFechaHora().toLocalTime(),
                    c.getEstado());
        }
    }

    // ================== MENÚ ADMINISTRADOR ==================

    private static void menuAdministrador(Administrador admin) {
        int opcion;
        do {
            System.out.println("\n=== Menú Administrador ===");
            System.out.println("Bienvenido, " + admin.getNombre());
            System.out.println("1. Listar pacientes");
            System.out.println("2. Listar médicos");
            System.out.println("3. Registrar médico");
            System.out.println("4. Listar todas las citas");
            System.out.println("0. Cerrar sesión");
            System.out.print("Seleccione una opción: ");

            opcion = leerEntero();

            switch (opcion) {
                case 1:
                    listarPacientes();
                    break;
                case 2:
                    listarMedicos();
                    break;
                case 3:
                    registrarMedico();
                    break;
                case 4:
                    listarTodasLasCitas();
                    break;
                case 0:
                    System.out.println("Cerrando sesión de administrador...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

        } while (opcion != 0);
    }

    private static void listarPacientes() {
        System.out.println("=== Lista de Pacientes ===");
        List<Paciente> pacientes = repo.getPacientes();
        if (pacientes.isEmpty()) {
            System.out.println("No hay pacientes registrados.");
            return;
        }
        for (Paciente p : pacientes) {
            System.out.printf("ID: %s | Nombre: %s | Correo: %s%n",
                    p.getIdUsuario(), p.getNombre(), p.getCorreo());
        }
    }

    private static void registrarMedico() {
        System.out.println("=== Registrar Médico ===");
        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine();
        System.out.print("Correo electrónico: ");
        String correo = scanner.nextLine();

        if (repo.buscarUsuarioPorCorreo(correo) != null) {
            System.out.println("Ya existe un usuario con ese correo.");
            return;
        }

        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();
        System.out.print("Especialidad: ");
        String especialidad = scanner.nextLine();

        String id = "m" + (repo.getMedicos().size() + 1);
        Medico medico = new Medico(id, nombre, correo, contrasena, especialidad);
        repo.agregarMedico(medico);

        System.out.println("Médico registrado correctamente.");
    }

    private static void listarTodasLasCitas() {
        System.out.println("=== Todas las Citas ===");
        List<Cita> citas = repo.getCitas();
        if (citas.isEmpty()) {
            System.out.println("No hay citas registradas.");
            return;
        }
        for (Cita c : citas) {
            System.out.printf("ID: %d | Paciente: %s | Médico: %s | Fecha/hora: %s | Estado: %s%n",
                    c.getIdCita(),
                    c.getPaciente().getNombre(),
                    c.getMedico().getNombre(),
                    c.getFechaHora().format(FORMATO_FECHA_HORA),
                    c.getEstado());
        }
    }

    // ================== UTILIDADES ==================

    private static int leerEntero() {
        while (true) {
            try {
                String linea = scanner.nextLine();
                return Integer.parseInt(linea.trim());
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Ingrese un número: ");
            }
        }
    }
}
