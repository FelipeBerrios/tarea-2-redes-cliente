

import java.net.*;
import java.io.*; 
import java.util.*;


//la clase Peticion extiende a la clase thread
public class Peticion extends Thread
{
    //Denuevo se crea la clase mensaje, esta vez para ver que thread lleva la ejecucion
  void MensajeServidor(String mensaje)
  {
    System.out.println(currentThread().toString() + " - " + mensaje);
  }
  //una variable cliente, que representa al socket del cliente y uno para manejar la response del mensaje.
  private Socket cliente = null;
  private PrintWriter salida = null;
  
  Peticion(Socket clienteExt)
  {
    //Recordar que le pasamos un socket al constructor
    cliente = clienteExt;
    //setPriority(4);
  }
  
  // Metodo run que inicia con el start(), representa un thread.
   public void run()
  {
    MensajeServidor("Procesando conexion");
    try
    {
      //Entrada es el input del cliente, es decir la request en su respectivo formato.
      BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
      //Salida es para enviar la response.
      salida = new PrintWriter(new OutputStreamWriter(cliente.getOutputStream(), "8859_1"), true);
      

      String cadena="" ;
      // Con i se puede reconocer la primera linea, que indica el metodo y version del protocolo
      int i = 0;
      do
      {
        //Se leen las lineas y se imprimen en orden, para ver que esta haciendo cada thread.
        cadena = entrada.readLine();
        if (cadena != null) {
          MensajeServidor("--" + cadena);
        
        // Si es la primera linea se reconoce que metodo es y segun esto se maneja la peticion.
        if (i == 0)
        {
          i++;
          
          StringTokenizer st = new StringTokenizer(cadena);
          String metodo=st.nextToken();
          if ((st.countTokens() >= 2) && (metodo.equals("GET"))) {
            //Si es get se retorna el archivo indicado en la linea principal.
            RetornarArchivo(st.nextToken());
          } 
          else if((st.countTokens() >= 2) && (metodo.equals("POST"))){
              //Si es post se le pasa la entrada y la direccion.
              ManejoPost(entrada,st.nextToken());
          }
          //Si el archivo no existe se devuelve not found en la salida al cliente (cliente.getOutputStream()).
          else {
            salida.println("HTTP/1.1 400 Not Found");
          }
        }
      }
        
      } while (cadena != null && cadena.length() != 0);
    }
    catch (IOException| NullPointerException e)
    {
      //En el caso que no se pueda establecer conexion con la salida, se retorna not found, en teoria debiera ser 501.
      salida.println("HTTP/1.1 400 Not Found");
      salida.close();
    }
    
    //Si todo va correctamente, se termina la peticion y se finaliza.
    MensajeServidor("Peticion Finalizada");
    salida.close();
  }
  //Metodo para retornar el archivo de la url en la peticion GET.
  void RetornarArchivo(String archivo)
  {
    MensajeServidor("Obteniendo el archivo " + archivo);
    //Si el nombre comienza con /, se le quita por un tema de manejo de archivos.
    if (archivo.startsWith("/")) {
      archivo = archivo.substring(1);
    }
    //Si es vacio, quiere decir que es el host inicial "http://localhost:8000" y se le pasa el index.
    if ((archivo.endsWith("/")) || (archivo.equals(""))) {
      archivo = archivo + "index.html";
    }
    //Revisamos la ruta actual del proyecto para ver si el archivo existe.
    String basePath = new File("").getAbsolutePath();
    String nombres="";
    try
    {
      // A continuacion se obtienen los nombres en caso de que el archivo a mostrar sea el de contactos
        if(archivo.startsWith("contactos.html")){
              
              String lineatxt="";
              File archivotxt = new File ("contactos.txt");
              FileReader fr = new FileReader (archivotxt);
              BufferedReader br = new BufferedReader(fr);
              int cont1=1;
              do
                {
                  lineatxt = br.readLine();
                  if(cont1==1){
                      cont1++;
                      continue;
                  }
                  //Se almacenan todos los nombres en un gran string 
                  if(lineatxt!=null){
                  StringTokenizer st = new StringTokenizer(lineatxt,"-");
                  nombres +="-"+st.nextToken();
                  cont1++;
                  }
                  
                } while (lineatxt != null);
              fr.close();
              br.close();
              System.out.println(nombres);
          }
      // Se instancia el archivo fisico para saber su largo.
      File archivofisico = new File(basePath+"/"+archivo);
      if (archivofisico.exists())
      {
        /*A continuacion se diferencian las distintas extensiones, para saber que devolver
          en cada type, asi por medio de threads se pueden cargar multiples elementos simultaneamente.
          Ademas se va imprimiendo el mensaje o response.
          */
        if (archivo.endsWith("html"))
        {
          salida.println("HTTP/1.1 200 ok");
          

          salida.println("Content-Type: text/html");
          salida.println("Content-Length: " + archivofisico.length());
          salida.println("\n");
        }
        else if (archivo.endsWith("css"))
        {
          salida.println("HTTP/1.1 200 ok");
          

          salida.println("Content-Type: text/css");
          salida.println("Content-Length: " + archivofisico.length());
          salida.println("\n");
        }
        else if (archivo.endsWith("js"))
        {
          salida.println("HTTP/1.1 200 ok");
          

          salida.println("Content-Type: application/javascript");
          salida.println("Content-Length: " + archivofisico.length());
          salida.println("\n");
        }
        //Se crea un reader para leer el archivo fisico, linea a linea.(se lee el html)
        BufferedReader archivoL = new BufferedReader(new FileReader(archivofisico));
        

        String linea="" ;
        String lista="<ul class=\" nav nav-sidebar\">";
        //Aqui se va imprimiendo en el cuerpo del mensaje de respuesta.
        do
        {
          
          linea = archivoL.readLine();
          
          if (linea != null) {
              salida.println(linea);
              //Si el archivo es contactos, se crea una lista que se mostrara,
              //a partir del archivo contactos.html que esta pre creado con la lista.
              if(archivo.startsWith("contactos.html")){
                if(linea.indexOf (lista) != -1){
                    String actual="";
                    StringTokenizer st = new StringTokenizer(nombres,"-");
                    while(st.hasMoreTokens()){
                        salida.println("<li ><a href=\"#\">"+st.nextToken()+"</a></li>");
                    }
                }
              }  
          }
        } while (linea != null);
        MensajeServidor("Archivo enviado");
        
        cliente.close();
      }
      else
      {
        MensajeServidor("No encuentro el archivo " + archivofisico.toString());
        cliente.close();
      }
    }
    catch (IOException| NullPointerException e)
    {
      MensajeServidor("Error al retornar archivo");
    }
  }
  
  //Esta funcion es especifica para el manejo del POST
  public void ManejoPost(BufferedReader mensaje ,String archivo){
    /*Mediante los metodos de lectura, no se pueden leer las variables
    del request en el post, hay que ver el content.length y luego leer los bytes especificos
    esta es una de las formas para reconocer las variables en el mensaje.
    */
    int contentLength = -1; //Se inicia el largo en 1
    String contentL = "Content-Length: "; //cuando encuentre esta secuencia en el mensaje, lo partira para obtener los bytes.
    String cadena="" ;
    try{
        do
          {
            cadena = mensaje.readLine();
            if (cadena != null) {
               MensajeServidor("--" + cadena);
            
                if (cadena.startsWith(contentL)) {
                        //Aqui se almacenan los bytes del largo del archivo.
                        contentLength = Integer.parseInt(cadena.substring(contentL.length()));
                }
            }

          } while (cadena != null && cadena.length() != 0);
        
        //Se crea un arreglo de caracteres para manejar los bytes
        char[] content = new char[contentLength];
        //Se almacena la secuencia y se transforma a string
        mensaje.read(content);
        String cadvariables=new String(content);
        //Se inician las variables de escritura de archivos.
        FileWriter archivoContactos;
        PrintWriter pw ;
        archivoContactos = new FileWriter("contactos.txt",true);
        pw = new PrintWriter(archivoContactos);
        int i;
        //Recordar que viene en formato var1=valor1&var2=valor2...
        //se separan por el caracter &.
        String[] arrayvariables=cadvariables.split("&");
        //Se crea un arreglo con el numero de variables.
        for(i=0;i<arrayvariables.length;i++){
            //Ahora se separan en parejas variable-valor
            String[] par=arrayvariables[i].split("=");
            par[1]=par[1].replace("+"," ");
            // se reconocen 3 casos: 0 para nombre; 1 para ip y 2 para puerto.
            switch (i){
                case 0:
                    pw.print(par[1]+" - ");
                    break;
                case 1:
                    pw.print("\t\t\t"+par[1]+" - ");
                    break;
                case 2:
                    pw.println("\t\t\t"+par[1]);
                    break;
            }
                    
            
        }
        //Cerramos el archivo.
        pw.close();
        
    
    }
    catch (IOException| NullPointerException e)
    {
        //Si no se reconocio el mensaje not found.
      salida.println("HTTP/1.1 400 Not Found");
    }
    //Finalmente retornamos el archivo que venia en la request de POST.
    RetornarArchivo(archivo);
  }
  

  
 }


