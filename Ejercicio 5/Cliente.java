import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {

	public static void main (String[] args) {

                String resultado;

                // Nombre del host donde se ejecuta el servidor
                String host = "localhost";
                // Puerto en el que espera el servidor
                int port = 8091;

                // Socket para la conexión TCP
                Socket socketServicio;
                PrintWriter writer;
                BufferedReader reader;
                Scanner sc;



		try {
                        // Nos conectamos al servidor
                        socketServicio = new Socket(host, port);
                        // Entrada/salida con el socket
                        reader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));
                        writer = new PrintWriter(socketServicio.getOutputStream(), true);

                        // Entrada/salida con el teclado
                        sc = new Scanner(System.in);
                        do {
                            System.out.print("User: ");
                            String user = sc.nextLine();
                            System.out.print("Password: ");
                            String pass = sc.nextLine();

                            writer.println("1 USER "+ user + " PASS " + pass);
                            writer.flush();

                            resultado = reader.readLine();
														resultado = resultado.split(" ")[0];
                              if (resultado.equals("200"))
                                  System.out.println("You were logged!!");
                              else if(resultado.equals("301")){
                                  System.out.println("ERROR user or password doesn't exist.");
                              }
                        } while (!(resultado.equals("200")));

                        System.out.print("Try to guess a number between 1 and 100\n");

                        do {
                            System.out.print("Give me a number: ");

                            String numero = sc.nextLine();
                            if (isNumeric(numero)){
                              writer.print(Integer.toString(Integer.parseInt(numero)));
                              writer.flush();

                              resultado = reader.readLine();
                              if (resultado.equals("="))
                                  System.out.println("Congratulations! " + numero + " is the number you were looking for");
                              else if (resultado.equals(">"))
                                  System.out.println("The number is greater than " + numero + "\n");
															else if (resultado.equals("<"))
		                              System.out.println("The number is lower than " + numero + "\n");
                            }
														else{
                                  System.out.println("That is not a number.");
                                  resultado = "ERROR";
                            }
                        } while (!(resultado.equals("=")));
			// Una vez terminado el servicio, cerramos el socket (automáticamente se cierran el inpuStream  y el outputStream)
                        socketServicio.close();

                // Excepciones:
		} catch (UnknownHostException e) {
			System.err.println("Error: Nombre de host no encontrado.");
		} catch (IOException e) {
			System.err.println("Error de entrada/salida al abrir el socket.");
		}

	}

  public static boolean isNumeric(String cadena) {

          boolean resultado;

          try {
              Integer.parseInt(cadena);
              resultado = true;
          } catch (NumberFormatException excepcion) {
              resultado = false;
          }

          return resultado;
      }

}
