

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
    public String ultimo="";
    public String nickver="";
    public String nick="";
    private static int port = 1001; /* port to connect to */
    private static String host = "localhost"; /* host to connect to */
    public String IP;
 
    private static String minick;
 
    
    
    public AppCliente() throws UnknownHostException, IOException{
        this.IP = InetAddress.getLocalHost().getHostAddress();
        this.server = new Socket("localhost", port);
        in = new BufferedReader(new InputStreamReader(
                    server.getInputStream()));
        
    }
    
    
 
  public void  EnviarMensaje(String msg) throws IOException{
      ultimo="";
      nickver="";
      PrintWriter out = new PrintWriter(this.server.getOutputStream(), true);
      out.println(msg);
  }
  public String PedirUltimo(){
      return this.ultimo;
  }
  public String  PedirNick(){
      return this.nickver;
  }
  
  
  
  public void run() {
        
        String msg;
        try {
            
            /* loop reading messages from the server and show them 
             * on stdout */
            while ((msg = in.readLine()) != null) {
                if(msg.startsWith("-") ){
                    ultimo+="\r\n"+msg;
                    
                }
                if(msg.startsWith("END")){
                    ultimo="END "+ultimo;
                    
                }
                if(msg.startsWith("OK")){
                    nick="OK";
                    
                }
                if(msg.startsWith("ENUSO")){
                    nick="ENUSO";
                }
                if(msg.startsWith("NICKEND")){
                    nickver="NICKEND";
                }
            }
            
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}



 
