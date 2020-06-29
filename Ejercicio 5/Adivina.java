import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;


//
// Nota: si esta clase extendiera la clase Thread, y el procesamiento lo hiciera el método "run()",
// ¡Podríamos realizar un procesado concurrente!
//
public class Adivina extends Thread{

	// Referencia a un socket para enviar/recibir las peticiones/respuestas
	private final Socket socketServicio;
        BufferedReader reader;
        PrintWriter writer;
        private final int rango = 100;
        private int numeroAcertar;
        private final Random random;


	// Constructor que tiene como parámetro una referencia al socket abierto en por otra clase
	public Adivina (Socket socketServicio) {
                this.socketServicio = socketServicio;
                random = new Random();
                numeroAcertar = random.nextInt(rango) + 1;
                System.out.println("The number to be guessed is " + numeroAcertar);
                try {
                        reader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));
                        writer = new PrintWriter(socketServicio.getOutputStream(), true);
                } catch(IOException e) {
                        System.err.println("Error reader/writer");
                }
	}


        @Override
        public void run (){
                char [] datosRecibidos = new char[1024];
                int bytesRecibidos;
                String respuesta;

                try {
                        do {

                                bytesRecibidos = reader.read(datosRecibidos);
                                String peticion = new String(datosRecibidos, 0, bytesRecibidos);

                                String user = peticion.split(" ")[2];
                                String pass = peticion.split(" ")[4].replace(System.getProperty("line.separator"), "");

                                if(user.equals("usuario") && pass.equals("finisterre"))
                                  respuesta = "200 OK";
                                else
                                  respuesta = "301 ERROR User or password doesn't exists";

                                writer.println(respuesta);
                                writer.flush();
                        } while (!respuesta.equals("200 OK"));
                } catch(IOException e) {
                        System.err.println("Error al obtener los flujos de entrada/salida.");
                }

                try {
                        do {

                                bytesRecibidos = reader.read(datosRecibidos);
                                  int peticion = Integer.parseInt(new String(datosRecibidos, 0, bytesRecibidos));

                                  System.out.println("Number " + peticion + " received");

                                  if (peticion < numeroAcertar)
                                          respuesta = ">";
                                  else if (peticion > numeroAcertar)
                                          respuesta = "<";
                                  else
                                          respuesta = "=";

                                  writer.println(respuesta);
                                  writer.flush();

                                  if (respuesta.equals("=")) {
                                      System.out.println("Connection closed");
                                      socketServicio.close();
                                  }

                        } while (!respuesta.equals("="));
                } catch(IOException e) {
                        System.err.println("Error al obtener los flujos de entrada/salida.");
                }
        }
}
