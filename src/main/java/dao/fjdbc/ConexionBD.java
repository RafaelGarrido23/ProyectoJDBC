package dao.fjdbc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // Atributos de instancia para las credenciales
    private String url;
    private String user;
    private String password;

    // Nombre y ruta del fichero de configuración
    private final String RUTA_FICHERO = "config.txt";

    public ConexionBD() throws IOException {
        cargarConfiguracionDesdeFichero();
    }

    private void cargarConfiguracionDesdeFichero() throws IOException {

        // Paso 1: Creamos un objeto File para representar la ruta
        File ficheroConfig = new File(RUTA_FICHERO);

        // Paso 2: Comprobamos si el fichero existe y es un archivo válido
        if (!ficheroConfig.exists()) {
            throw new IOException("ERROR: El fichero de configuración '" + RUTA_FICHERO + "' no existe en la raíz del proyecto.");
        }

        if (!ficheroConfig.isFile()) {
            throw new IOException("ERROR: La ruta '" + RUTA_FICHERO + "' no corresponde a un archivo válido.");
        }

        // Paso 3: Leemos el contenido usando try-with-resources
        // Los flujos declarados entre paréntesis se cerrarán automáticamente al salir del try
        try (FileReader fr = new FileReader(ficheroConfig);
             BufferedReader br = new BufferedReader(fr)) {

            String linea;

            // Procesamos el archivo línea a línea
            while ((linea = br.readLine()) != null) {

                // Ignoramos líneas vacías o comentarios que comiencen por #
                if (linea.trim().isEmpty() || linea.startsWith("#")) {
                    continue;
                }

                // Separamos la clave y el valor utilizando el carácter '='
                String[] partes = linea.split("=", 2);

                if (partes.length == 2) {
                    String clave = partes[0].trim();
                    String valor = partes[1].trim();

                    // Asignamos el valor leído al atributo correspondiente
                    switch (clave) {
                        case "url":
                            this.url = valor;
                            break;
                        case "user":
                            this.user = valor;
                            break;
                        case "password":
                            this.password = valor;
                            break;
                    }
                }
            }
        }
    }

    public Connection getConexion() throws SQLException {
        if (this.url == null || this.user == null) {
            throw new SQLException("No se han cargado los parámetros de conexión desde el fichero config.txt");
        }
        return DriverManager.getConnection(this.url, this.user, this.password);
    }
}