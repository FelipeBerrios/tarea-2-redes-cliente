package servidorjava;

import java.net.*;
import java.io.*; 
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException; 
import AppCliente.AppCliente;
import java.net.UnknownHostException;

public class ServidorJava 
{
    String ultimo;
    AppCliente tcpcliente;
    // Se inicializa el puerto, en este caso sera el 8000
   
  static int  puerto = (int)(Math.random()*(8080-8000+1)+8000);
  
  //Un metodo para retornar mensajes del servidor
  void MensajeServidor(String mensaje)
  {
    System.out.println("Mensaje: " + mensaje);
  }
  
  public static void main(String[] array) throws UnknownHostException,IOException
  {
      puerto = (int)(Math.random()*(8080-8000+1)+8000);
      System.out.println(puerto);
    //Primero se verifica si el archivo existe
    try{
        
        String basePath = new File("").getAbsolutePath();
        File archivo = new File(basePath+ "/contactos.txt");
        if(archivo.exists()){
            //no hacer nada
            
        }
        else{
            //Si no existe, se crea uno nuevo
            FileWriter archivoContactos ;
            PrintWriter pw ;
            
            archivoContactos = new FileWriter("contactos.txt");
            pw = new PrintWriter(archivoContactos);
            pw.println("Nombre\t\t\tDireccion IP\t\t\tPuerto");
            pw.close();
        }
       
        
      
    }
    catch (IOException e) {
            e.printStackTrace();
    }
    //Se instancia el servidor y luego se inicia 
    ServidorJava instancia = new ServidorJava();
    
    instancia.IniciarServidor();
  }
  
  public boolean IniciarServidor()
  {
    
    MensajeServidor("Servidor Iniciado");
    try
    {
        // Se abre el host en el puerto  esperando que un cliente llegue
      ServerSocket s = new ServerSocket(puerto);
      MensajeServidor("Esperando Conexion......");
      String url = "http://localhost:"+puerto;

        if(Desktop.isDesktopSupported()){
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("xdg-open " + url);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
      /* Se crea un cliente, con su respectivo metodo run*/
      tcpcliente = new AppCliente();
      Thread t = new Thread(tcpcliente);
      t.start();
      while(true)
      {
        Socket cliente = s.accept();
        //Se instancia un cliente, y se inicia en su respectivo thread con la clase start.
        Peticion pCliente = new Peticion(cliente,tcpcliente);
        pCliente.start();
      }
    }
    catch (IOException e)
    {
      MensajeServidor("Error en servidor\n" + e.toString()); //Si ocurre algun problema al instanciar el servidor se lanza este mensaje de error
    }
    return true;
  }
}
