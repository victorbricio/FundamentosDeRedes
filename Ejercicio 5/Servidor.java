import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//
// YodafyServidorIterativo
//
public class Servidor {

	public static void main (String[] args) {

                // Puerto de escucha
                int port = 8091;
                ServerSocket socketServidor;
                Socket socketServicio;

		 try {
                    socketServidor = new ServerSocket(port);
                    do {
                        socketServicio = socketServidor.accept();
                        Adivina pf = new Adivina(socketServicio);
                        pf.start();
                    } while (true);
                } catch (IOException e) {
                        System.err.println("Error al escuchar en el puerto " + port);
                }

	}

}
