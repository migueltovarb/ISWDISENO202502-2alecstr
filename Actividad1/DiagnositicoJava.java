import java.util.Scanner;
import java.util.ArrayList;

public class DiagnosticoJava {
    public static final int MAX_PRODUCTOS = 5;
    private static ArrayList<String> productos = new ArrayList<>();
    private static ArrayList<Integer> cantidades = new ArrayList<>();
    private static Scanner escaner = new Scanner(System.in);

    public static void main(String[] args) {
        agregarproductos();
        int opcion;
        do {
            mostrarmenu();
            opcion = obteneropcion();
            switch (opcion) {
                case 1:
                    mostrartodo();
                    break;
                case 2:
                    buscarproducto();
                    break;
                case 3:
                    actualizarstock();
                    break;
                case 4:
                    alertabajaexistencia();
                    break;
                case 5:
                    System.out.println("Saliendo");
                    break;
                default:
                    System.out.println("Opcion invalida");
            }
        } while (opcion != 5);
        escaner.close();
    }

    private static void agregarproductos() {
        System.out.println("ingrese detalles para " + MAX_PRODUCTOS + " productos.");
        for (int i = 0; i < MAX_PRODUCTOS; i++) {
            System.out.print("Nombre del producto " + (i + 1) + ": ");
            String nombre = escaner.nextLine();
            int cantidad;
            while (true) {
                try {
                    System.out.print("Ingrese cantidad para " + nombre + ": ");
                    cantidad = Integer.parseInt(escaner.nextLine());
                    if (cantidad >= 0) {
                        break;
                    } else {
                        System.out.println("Cantidad invlida");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("entrada invalida");
                }
            }
            productos.add(nombre);
            cantidades.add(cantidad);
        }
    }

    private static void mostrarmenu() {
        System.out.println("\n Sitema de inventario de Supermercado");
        System.out.println("1. Mostrar todos los productos y existencias");
        System.out.println("2. Buscar un producto");
        System.out.println("3. Actualizar existencia del producto");
        System.out.println("4. Generar alerta de baja existencia");
        System.out.println("5. Salir");
        System.out.print("Ingrese su opcion: ");
    }

    private static int obteneropcion() {
        try {
            return Integer.parseInt(escaner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void mostrartodo() {
        if (productos.isEmpty()) {
            System.out.println("No hay existencias en el inventario.");
            return;
        }

        int totalproductos = 0;
        System.out.println("\nInventario actual:");
        for (int i = 0; i < productos.size(); i++) {
            System.out.println("Producto: " + productos.get(i) + ", cantidad: " + cantidades.get(i));
            totalproductos += cantidades.get(i);
        }
        System.out.println("\n Existencia total de productos en inventario: " + totalproductos);
    }

    private static void buscarproducto() {
        System.out.print("Ingrese el nombre del producto: ");
        String nombre = escaner.nextLine();
        int indice = productos.indexOf(nombre);
        if (indice != -1) {
            System.out.println("producto: " + productos.get(indice) + ", cantidad: " + cantidades.get(indice));
        } else {
            System.out.println("Producto no encontrado");
        }
    }

    private static void actualizarstock() {
        System.out.print("Ingrese el nombre del producto para actualizar: ");
        String nombre = escaner.nextLine();
        int indice = productos.indexOf(nombre);
        if (indice != -1) {
            int cantidadactual = cantidades.get(indice);
            System.out.println("La cantidad actual de " + nombre + " es " + cantidadactual + ".");
            int cambio;
            while (true) {
                try {
                    System.out.print("Ingrese la cantidad a sumar o restar: ");
                    cambio = Integer.parseInt(escaner.nextLine());
                    int nuevacantidad = cantidadactual + cambio;
                    if (nuevacantidad >= 0) {
                        cantidades.set(indice, nuevacantidad);
                        System.out.println("Existencias actualizado. las existencias para " + nombre + " es " + nuevacantidad + ".");
                        break;
                    } else {
                        System.out.println("Ingrese un valor valido.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Entrada invalida. ingrese un numero valido.");
                }
            }
        } else {
            System.out.println("Producto no encontrado.");
        }
    }

    private static void alertabajaexistencia() {
        System.out.println("\n--- Alerta de baja existencia ---");
        boolean alertabajaencontrada = false;
        for (int i = 0; i < productos.size(); i++) {
            if (cantidades.get(i) < 10) {
                System.out.println("ALERTA: el producto '" + productos.get(i) + "' Tiene baja existencia (" + cantidades.get(i) + ").");
                alertabajaencontrada = true;
            }
        }
        if (!alertabajaencontrada) {
            System.out.println("todos los productos tienen suficiente existencia.");
        }
    }
}