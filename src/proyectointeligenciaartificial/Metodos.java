package proyectointeligenciaartificial;

import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * @author-Pablo-PC
 */
public class Metodos {
    private String aux=""; //En esta variable se guarda cada una de las lineas del documento
    private String dir = ""; //Direccion de archivo que se carga para encadenamientos
    private String matriz[][]; //Esta matriz contiene cada uno de los datos del documento, ejemplo:
                                                                // 'A','B','C','D'
    private String[] inferencia; //Este arreglo contiene cada una de las reglas, ejemplo:
                                                                //SI A Y B Y C ENTONCES D
    private int tamanio = 0;//Esta variable contiene el tamaño de el documento contando las lineas que tiene
    private int cont = 0;//Este es un contador usado en el metodo condicionesPorLinea(String matriz[][])
    private int[] contarCondiciones;//Este arreglo de enteros contendra el numero de condiciones por regla que se 
                                                     //deben cumplir
    private int[] comparaCondiciones;//Este arreglo de enteros contendra las condiciones que se iran cumpliendo 
                                                        //cada vez que entre a algun metodo
    private int cantidad = 0;//
    private int inferenciaP = 0;
    ArrayList<String> agregarMemoria = new ArrayList();//Este arreglo de listas contendra las nuevas premisas que
                                                                                       //que se vallan descubriendo al hacer los encadenamientos
    /*
     * Ejemplo de matriz la cual tendra un tamaño contando el numero de lineas del documento y un largo de 15 elementos
        "A"  "B"  "C"  ""  ""  ""  ""  ""  ""  ""  ""  ""  ""  ""  "D" 
        "F"  "H"  ""  ""  ""  ""  ""  ""  ""  ""  ""  ""  ""  ""  "J" 
        "D"  "J"  ""  ""  ""  ""  ""  ""  ""  ""  ""  ""  ""  ""  "K" 
        "F"  "M"  "H"  ""  ""  ""  ""  ""  ""  ""  ""  ""  ""  ""  "L" 
        "L"  ""  ""  ""  ""  ""  ""  ""  ""  ""  ""  ""  ""  ""  "C" 
        "M"  "D"  "G"  ""  ""  ""  ""  ""  ""  ""  ""  ""  ""  ""  "N" 
        "S"  ""  ""  ""  ""  ""  ""  ""  ""  ""  ""  ""  ""  ""  "F" 
        "P"  ""  ""  ""  ""  ""  ""  ""  ""  ""  ""  ""  ""  ""  "S" 
     */
    
    void Metodos(){
        aux = "";
        dir = "";
        matriz = null;
        inferencia = null;
        tamanio = 0;
        cont = 0;
        contarCondiciones = null;
        comparaCondiciones = null;
        cantidad = 0;
        inferenciaP = 0;
        agregarMemoria = null;
    }
    
    /**
     * tamanioDoc(String dir)
     * @param dir esta variable es la direccion del archivo de texto
     * @return tamaño del documento
     * Se encargara de encontrar el tamaño del documento contando cada una de las lineas al momento de que se recorre el documento
     */
    public int tamanioDoc(String dir){
        this.dir = dir;
        File fichero = new File(this.dir);
        
        try {
            if(fichero!=null)
            {    
               FileReader archivos=new FileReader(fichero);
               BufferedReader lee=new BufferedReader(archivos);
               while((aux=lee.readLine())!=null){   
                   tamanio = tamanio+1;
               }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return tamanio;
    }
    
    /**
     * inferencias()
     * Este metodo se encargara de recorrer el archivo e ir guardando cada una de las reglas en la variable
     * inferencia.
     */
    public void inferencias(){
         File fichero = new File(this.dir);
        int contadorInferencias = 0;
        inferencia = new String[tamanio]; //Se fija el tamaño del arreglo que contendra las reglas
        
        try {
            if(fichero!=null)
            {    
               FileReader archivos=new FileReader(fichero);
               BufferedReader lee=new BufferedReader(archivos);
               while((aux=lee.readLine())!=null){
                   inferencia[contadorInferencias] = aux;
                   contadorInferencias++;
               }
               contadorInferencias = 0;
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    /*Ejemplo de arreglo 'inferencia'
        inferencia[0] =SI A Y B Y C ENTONCES D
        inferencia[1] =SI F Y H ENTONCES J
        inferencia[2] =SI D Y J ENTONCES K
        inferencia[3] =SI F Y M Y H ENTONCES L
        inferencia[4] =SI L ENTONCES C
        inferencia[5] =SI M Y D Y G ENTONCES N
        inferencia[6] =SI S ENTONCES F
    */
    
    /**
     * condicionesPorLinea(String matriz[][])
     * @param matriz es la matriz con todos los elementos del documento
     * En este metodo se encuentra las condiciones necesarias para comprobar las reglas 
     */
    public void condicionesPorLinea(String matriz[][]){
        this.matriz = matriz; // Se iguala la matriz que recibe con la matriz global
        //En los siguientes arreglos se define el tamaño que tendran conforme al tamaño del archivo
        contarCondiciones  = new int[tamanio];
        comparaCondiciones = new int[tamanio];
        
        //Se recorre la matriz para contar cuantas condiciones tiene por linea
        for (int i = 0; i < tamanio; i++) {
            for (int j = 0; j < 14; j++) {
                if(!matriz[i][j].equals("")){
                    cont++;
                }
            }
            //Se usa la funcion contarOU para verificar si en la regla en la que esta recorriendo existe algun O o U
            if(contarOU(inferencia[i]) == 1){//Si contiene una "O" o "U" se le resta 1 al numero de condiciones que se tienen que cumplir con el fin de solo dejar
                                                             //la cantidad correcta de condiciones a cumplir, ejemplo de frutas:
                                                             //antes del entonces se encuentran estas condiciones: FORMA=ALARGADA Y COLOR=VERDE O AMARILLO
                                                             //Sino se buscan la O y U serian 3 condiciones que cumplir cuando solo se necesitan 2, al encontrar una O le resta
                                                             //un 1 y con ello se fija el número correcto de condiciones a cumplir
                comparaCondiciones[i] = 0;
                contarCondiciones[i] = cont-1;
                cont = 0;
            }else if(contarOU(inferencia[i]) == 2){
                comparaCondiciones[i] = 0;
                contarCondiciones[i] = cont-2;
                cont = 0;
            }else if(contarOU(inferencia[i]) == 3){
                comparaCondiciones[i] = 0;
                contarCondiciones[i] = cont-3;
                cont = 0;
            }else if(inferencia[i].indexOf(" O ") == -1 || inferencia[i].indexOf(" U ") == -1){
                comparaCondiciones[i] = 0;
                contarCondiciones[i] = cont;
                cont = 0;
            }
        }
    }
    
    /**
     * contarOU(String mem)
     * @param mem contiene la inferencia en la que se quiere ver si existe o no un O o U
     * @return retorna cuantas veces encuentra cada letra
     */
    private int contarOU(String mem){
        int num = 0;
        if(mem.indexOf(" O ") != -1){
                while (mem.indexOf(" O ")  > -1) {
                    mem = mem.substring(mem.indexOf(" O ")+(" O ").length(),mem.length());
                    num++;
                }
        }else if(mem.indexOf(" U ") != -1){
                    mem = mem.substring(mem.indexOf(" U ")+(" U ").length(),mem.length());
                    num++;
        }
        return num;
    }
    
    /**
     * encadenamientoHD(ArrayList<String> memoria, List lista, List lista2)
     * @param memoria es la memoria de trabajo que se asigna en la interfaz
     * @param lista es el componente de la interfaz (List) donde se imprimen resultados
     * @param lista2 es el componente de la interfaz (List) donde se fue agregando la memoria
     */
    public void encadenamientoHD(ArrayList<String> memoria, List lista, List lista2){
        ArrayList<String> nuevaMemoria  = new ArrayList();//En este arrayList se guardara la nueva memoria de trabajo
                                                                                         //con forme se cumplan las reglas
        boolean estado = false;//Esta variable se encargara de continuar o terminar el recorrido dependiendo de su
                                            //estado, falso:continua, verdadero:termina
        do{//Este ciclo se utiliza para recorrer las reglas mientras el valor del estado sea falso
            for (String mem : memoria) {//Este for recorre la memoria de trabajo y va mandando uno por uno a el metodo compara() que se
                                                        //encargara de ir cumpliendo las reglas y de obtener la nueva memoria de trabajo
                comparar(mem, lista, lista2, nuevaMemoria);
            }
            
            if(nuevaMemoria.size() == 0){ //Aqui se valida si el arrayList se encuentra vacio, si es asi entonces el estado se vuelve verdadero
                estado = true;
            }else{//si contiene algun elemento entonces:
                memoria.clear();//Limpia la memoria inicial para evitar que cuando vuelva a hacer recorridos vuelva a usar
                                          //la memoria inicial
                memoria.addAll(nuevaMemoria);//Una vez que la memoria fue limpiada se agregan los valores que enontro en el metodo compara
                nuevaMemoria.clear();//Limpia el arrayList nuevaMemoria para que esta pueda volver a recibir valores cuando pase por el metodo compara().
            }
        }while(estado == false);
    }
    
    
    public void comparar(String valor, List lista, List lista2, ArrayList<String> nuevaMemoria){
        //Se recorre la matriz y se va comparando con la variable 'valor' que contiene la memoria de trabajo
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < 14; j++) {
                if(matriz[i][j].equals(valor)){
                    //Si se encuentra alguna comparacion se va sumando 1 al arreglo comparaCodiciones tomando en cuentra la regla en la que
                    //que se encuentra el valor.
                    comparaCondiciones[i] = 1 + comparaCondiciones[i];
                }
            }
            //Ahora se compara el arreglo contarCondiciones con el arreglo comparaCondiciones para ver si el primer arreglo
            //mencionado ya tiene el numero de condiciones cumplidas que necesita para activar el mensaje.
            if(contarCondiciones[i] == comparaCondiciones[i]){
                lista.add("Se Infirio " + matriz[i][14] + " Por la activacion de la regla " + (i+1));
                lista.add("           "+inferencia[i]);
                lista2.add(matriz[i][14]);
                nuevaMemoria.add(matriz[i][14]);//Se agrega a la memoria la nueva condicion
                contarCondiciones[i] = 10;//se iguala a 10 el arreglo para que cuando vuelva a regresar a la comparacion ya no cumpla y no entre
                                                        //y vuelva a mostrar los mismos mensajes
            }
        }
    }
   
    /**
     * encadenamientoHAtras(ArrayList<String> memoria, String hipotesis, List lista, List lista2)
     * @param memoria es la memoria de trabajo que se asigna en la interfaz
     * @param hipotesis es la hipotesis que se desea verificar
     * @param lista es el componente de la interfaz (List) donde se imprimen resultados
     * @param lista2 es el componente de la interfaz (List) donde se fue agregando la memoria
     */
    public void encadenamientoHAtras(ArrayList<String> memoria, String hipotesis, List lista, List lista2){
        
        boolean estado = false;//Esta variable se encargara de señalar cuando el programa acaba
        int lineaInferencias = 0;//Esta variable se ocupara para guardar durante todo el proceso del metodo el indice o la regla sobre la cual se
                                            //trabajara para que al final se pueda arrojar el mensaje de que se ha cumplido la regla en dado caso que lo logre.
        int contar = 0;//Este contador no se utilizara en este metodo pero sirve para inicializar la variable a 0 cada vez que se
                            //mande a llamar el metodo recorrido()
        ArrayList<String> primerosHijos  = new ArrayList();//En este arrayList se guardaran los hijos/condiciones que necesita la hipotesis para
                                                                                       //poder encontrar la solucion
        lista.clear();//Al iniciar el metodo se debe limpiar el componente lista que contiene los mensajes del proceso que se va siguiendo al
                          //momento de ir realizando el encadenamiento
        //Aqui se va realizando un recorrido en la matriz tomando solo en cuenta los primeros espacios de ella para saber si la hipotesis
        //de verdad se encuentra despues del entonces
        for (int i = 0; i < matriz.length; i++) {
            if(matriz[i][14].equals(hipotesis)){
                lineaInferencias = i;
                lista.add("SE INTENTARA COMPROBAR REGLA "+ (i+1));
                lista.add("         "+inferencia[i]);
            }
         }
        //Ahora se verifica si la hipotesis se encuantra en la memoria de trabajo, si se encuentra entonces el valor del estado se hace
        //verdadero y el metodo terminaria
        for (String hipotesisInicial : memoria) {
            if(hipotesisInicial.equals(hipotesis)){
                estado  = true;
            }
        }
        
        if(estado == false){
        //Si el estado es falso entonces se hace un recorrido en la matriz tomando como indices la linea donde se encuentra la hipotesis
        //y los valores que esten antes de ella sin contar los espacios vacios.
            for (int j = 0; j < 14; j++) {
                if(!matriz[lineaInferencias][j].equals("")){
                    primerosHijos.add(matriz[lineaInferencias][j]);//Se añaden los hijos con forme se van encontrando
                }
            }
            //En este for se van pasando los hijos
            for (String premisa : primerosHijos) {
                    //Se iguala el estado a la funcion recorrido la cual retorna un true o un false
                    estado = recorrido(memoria, premisa, lista, lista2, contar);
                    //En dado caso que al arrayList agregarMemoria se le ayan agregado datos encontrados entonces se le agregan a la memoria de
                    //trabajo para seguir evaluando en sus demas recorridos
                    if(agregarMemoria.size() > 0){
                        for (String agregados : agregarMemoria) {
                            memoria.add(agregados);
                        }
                    }
                    //Despues se limpia el arrayList agregarMemoria por si vuelve a agregar mas condiciones
                    agregarMemoria.clear();
                    //Si en algun momento la funcion retorna un false entonces termina el ciclo usando un break
                    if(estado == false){
                        break;
                    }
             }
        }
        //Dependiendo de cual fue el estado se mandan los mensajes siguientes
        if(estado ==  true){
           lista.add("SE COMPRUEBA R"+ (lineaInferencias+1) + " POR MEMORIA DE TRABAJO");
           lista.add("");
           lista.add("¡HIPOTESIS INICIAL VERIFICADA!");
        }else{
           lista.add("");
           lista.add("¡HIPOTESIS INICIAL FALLIDA!");
        }
    }
    
    /**
     * recorrido(ArrayList<String> memoria, String premisa, List lista, List lista2, int contar) 
     * @param memoria es la memoria de trabajo que se asigna en la interfaz
     * @param premisa es uno de los hijos de la hipotesis inicial
     * @param lista es el componente de la interfaz (List) donde se imprimen resultados
     * @param lista2 es el componente de la interfaz (List) donde se fue agregando la memoria
     * @param contar es un contador
     * @return true or falses
     */
    private boolean recorrido(ArrayList<String> memoria, String premisa, List lista, List lista2, int contar) {
        boolean estado = false, estado2 = false;
        int indice = -1;//Esta variable se iguala a -1 ya que entre los indices que se pueden optener van del 0 a n
        
        //Con este ciclo se verifica si el primer hijo existe en la memoria de trabajo, de ser asi el estado se vuelve verdadero y termina la funcion
        for (String mem : memoria){
            if(mem.equals(premisa)){
                 lista.add("SE COMPRUEBA "+ mem + " POR MEMORIA DE TRABAJO");
                estado = true;
                cantidad = 0;
            }
        }
        if(estado == false){
            //En este recorrido se busca en que regla se encuentra el hijo y se toma el indice
                for (int i = 0; i < matriz.length; i++) {
                    if(matriz[i][14].equals(premisa)){
                        ++cantidad;
                        indice = i;
                    }
                }
                //Si el indice es mayor o igual a 0 significa que si entontro la premisa
                if(indice >= 0){
                //Ahora se recorre la matriz tomando en cuenta el indice en el cual se encontro el hijo (que ahora es padre) 
                //para intentar encontrar a sus hijos
                for (int i = 0; i < 14; i++) {
                        //Se busca en el indice sin contar los espacios vacios
                        if(!matriz[indice][i].equals("")){
                            //Ahora se verifica si la variable contar es igual a 0, de ser asi significa que es la primera vez que entra a la función e imprimira
                            //los siguientes mensajes
                            if(contar == 0){
                                lista.add("SE INTENTARA COMPROBAR REGLA "+ (indice+1));
                                lista.add("         "+inferencia[indice]);
                                inferenciaP = indice;
                                contar++;//Contar se volvera un contador
                            }
                            //Si contar no es igual a cero entonces se continuara con la siguiente condicion donde se verifica que el indice del primer hijo sea
                            //diferente al indice de sus hijos y que el estado2 sea falso
                            if(indice != inferenciaP && estado2 == false){
                                lista.add("SE INTENTARA COMPROBAR REGLA "+ (indice+1));
                                lista.add("         "+inferencia[indice]);
                                estado2 = true;//El estado que era falso se vuelve verdadero
                            }
                            //Ahora en esta condicion se evalua la misma funcion (en este momento se usa la recursividad), si es verdadera va sumando en uno
                            //el arreglo comparaCondiciones en el indice/regla en la que se encuentra
                            if(recorrido(memoria, matriz[indice][i], lista, lista2, contar) == true){
                                comparaCondiciones[indice] = 1 + comparaCondiciones[indice];
                            }
                            
                        }
                    }
                //Se compara el arreglo contarCondiciones con el arreglo comparaCondiciones para ver si el primer arreglo
                //mencionado ya tiene el numero de condiciones cumplidas que necesita para activar el mensaje.
                if(comparaCondiciones[indice] == contarCondiciones[indice]){
                    //Si se cumple la condicion anterior
                    estado = true;//El estado se vuelve verdadero
                    lista2.add(matriz[indice][14]);//Se agrega a al componente de memoria de trabajo la nueva condicion encontrada
                    agregarMemoria.add(matriz[indice][14]);//Se agrega a el arrayList la nueva condicion encontrada
                    //Si el estado2 es verdadero imprime el mensaje, (esto se hace de esta manera para verificar que solo imprima una vez el mensaje
                    //ya que al encontrarse la condicion en un for lo ara varias veces y al estar el estado2 solo una vez en falso durante el proceso ayuda a
                    //evitar que se imprima mas veces de las requeridas)
                    if(estado2 == true ){
                        lista.add("SE COMPRUEBA R" + (indice+1) + " POR MEMORIA DE TRABAJO. AGREGANDO "+ matriz[indice][14] + " A MEMORIA...");
                    }
                    //Si el indice inicial que es inferenciaP (en el caso de las letras: D,cuando evaluamos k) es igual al indice que va cambiando
                    //constantemente en los recorridos significa que la recursividad regreso a su primer recorrido (el cual comenzo siendo con D),
                    //imprime que se logro completar el mismo
                    if(indice == inferenciaP){
                        lista.add("SE COMPRUEBA R" + (inferenciaP+1) + " POR MEMORIA DE TRABAJO. AGREGANDO "+ matriz[inferenciaP][14] + " A MEMORIA...");
                        ++cantidad;
                    }
                    comparaCondiciones[indice] = 0;//Se iguala el arreglo a 0 para evitar que en una comparacion futura vuelva igualarse y
                                                                        //se impriman los mismos valores
               }
            }
        }
        return estado;//Retorna un estado falso o verdadero
    }
}