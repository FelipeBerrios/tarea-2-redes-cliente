

package AppCliente;

/**
 *
 * @author felipe
 */
/* ChatClient.java */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.InetAddress;
 
public class AppCliente implements Runnable {
    Socket server;
    private BufferedReader in = null;
    
    public String nick="";
    public String controllogin="";
    public String controlagregar="";
    public String controlcontactos="";
    public String respuesta="";
    public  int port = 1001; /* port to connect to */
    private static String host = "localhost"; /* host to connect to */
    public String IP;
 
    private static String minick;
 
    
    /* Se crea el constructor, creando un buffer de lectura*/
    public AppCliente() throws UnknownHostException, IOException{
        this.IP = InetAddress.getLocalHost().getHostAddress();
        this.server = new Socket("localhost", port);
        in = new BufferedReader(new InputStreamReader(
                    server.getInputStream()));
        
    }
    

/* Metodo para enviar mensajes al servidor */
 
  public void  EnviarMensaje(String msg) throws IOException{
      controllogin="";
      controlagregar="";
      respuesta="";
      PrintWriter out = new PrintWriter(this.server.getOutputStream(), true);
      out.println(msg);
  }
  
  
  /* metodo run que lee respuestas desde el servidor*/
  @Override
  public void run() {
        
        String msg;
        try {
            while ((msg = in.readLine()) != null) {
                    if(!msg.equals("")){
                        this.respuesta=msg;
                    }
                    
             
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}



 
