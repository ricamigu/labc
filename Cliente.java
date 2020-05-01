import java.util.*;
import java.io.*;
import java.nio.*;
import java.lang.*;
import java.util.concurrent.TimeUnit;

public class Cliente {

    private static Scanner x;   // Scanners dos ficheiros
    private static Scanner y;
    private static String user; // variavel que guarda o nome do utilizador logado


    public static void clearScreen() {  // funcao que limpa o ecrã 
        System.out.println("\033[H\033[2J");
        System.out.flush();
    }


    public static boolean menuDeRegisto() throws IOException, InterruptedException {        // primeiro menu apresentado

        clearScreen();

        System.out.println("\033[1mMenu de registo:\033[0m");
        System.out.println("\033[1m1)\033[0m Login / Autenticação");
        System.out.println("\033[1m2)\033[0m Pedido de registo de novo utilizador");
        System.out.println("\033[1m3)\033[0m Sair");

            
        boolean estado = false;
        Scanner stdin = new Scanner(System.in);
        System.out.print("> ");
        int num = stdin.nextInt();

        switch (num) {

            case 1: estado = login();
                    break;

            case 2: registar();
                    menuDeRegisto();
                    estado = false;
                    break;

            case 3: System.out.println("\033[3mA encerrar o sistema...\033[0m");
                    estado = false;
                    break;

            default: System.out.println("\033[1mOpção inválida!\033[0m");
                    TimeUnit.SECONDS.sleep(2);
                    estado = menuDeRegisto();
                    break;
                        
            }
        return estado;
    }

    public static boolean menu() throws IOException, InterruptedException {         // menu apresentado de pois do login ser bem sucedido

        clearScreen();

        System.out.println("\033[1mMenu:\033[0m\n");
        System.out.println("\033[1m1)\033[0m Ver feed");
        System.out.println("\033[1m2)\033[0m Ver tópicos");
        System.out.println("\033[1m3)\033[0m Ver mensagens de um tópico");
        System.out.println("\033[1m4)\033[0m Procurar tópicos mais ativos");
        System.out.println("\033[1m5)\033[0m Subscrever tópico");
        System.out.println("\033[1m6)\033[0m Publicar num tópico");
        System.out.println("\033[1m7)\033[0m Gerir lista de subscrições");
        System.out.println("\033[1m8)\033[0m Ver estatísticas");
        System.out.println("\033[1m9)\033[0m Logout");
        System.out.print("\033[1m>\033[0m ");
        boolean estado = true;
        Scanner input = new Scanner(System.in);
        int num = input.nextInt();

        switch(num){
            case 1: verFeed();
                    break;
            case 2: verTopicos();
                    break;
            case 3: verMensagens();
                    break;
            case 4: topicosMaisUtilizados();
                    break;
            case 5: subscreverTopico();
                    break;
            case 6: publicarTopico();
                    break;
            case 7: gerirSubscricoes();
                    break;
            case 8: estatisticas();
                    break;
            case 9: System.out.println("\n\033[3mFinalizando sessão...\033[0m");
                    TimeUnit.SECONDS.sleep(1);
                    estado = menuDeRegisto();
                    break;
            default: System.out.println("\033[1mOpção inválida!\033[0m");
                    TimeUnit.SECONDS.sleep(1);
                    clearScreen();
                    menu();
                    return estado;
        }
        TimeUnit.SECONDS.sleep(3);
        return estado;
    }

    public static boolean login() throws InterruptedException{      // verificacao do login

        Scanner input = new Scanner(System.in);
        boolean estado = false;
        Console console = System.console();

        if(console == null){
            System.out.println("Erro");
            TimeUnit.SECONDS.sleep(2);
            System.exit(0);
        }


        try {
            x = new Scanner(new File("Clientes.txt"));  // criar um scanner do ficheiro clientes, ficheiro onde estão guardados os dados dos utilizadores registados
            } 
        catch(Exception e){
            System.out.println("Ficheiro nao encontrado");
            TimeUnit.SECONDS.sleep(2);
            estado = login();
            return estado;
            }

        System.out.println("\n\033[1mLogin\033[0m\n");
        System.out.print("Username: ");
        String tempUsername = input.next();
        char passwordArray[] = console.readPassword("Password: ");      
        String tempPassword = new String(passwordArray);

        while(x.hasNext()){         // cada um destas strings correspondem aos parametros de cada utilizador (modo muito utilizado pelo nosso grupo ao longo destes programas):
            String a = x.next();    // nome proprio do utilizador
            String b = x.next();    // email
            String c = x.next();    // username 
            String d = x.next();    // password
            String e = x.next();    // estado de verificado ou nao (predefinido como false)

        if(tempUsername.equals(c) && tempPassword.equals(d) && e.equals("true")){      // se o utilizador existir e estiver validado, executa isto
            System.out.println("\n\033[1mSessão inicializada!\033[0m");
            estado = true;
        }

        if(tempUsername.equals(c) && tempPassword.equals(d) && e.equals("false"))      // se o  utilizador existir e NAO estiver validado, executa isto
            System.out.println("O utlizador existe, mas ainda não foi validado");
        }

        if(estado == false){
            System.out.println("Erro ao iniciar sessão.");      // mensagem de erro se a sessao nao for bem sucedida
            TimeUnit.SECONDS.sleep(2);
            clearScreen();
            estado = login();
        }

        user = tempUsername;
        
        TimeUnit.SECONDS.sleep(1);
        return estado;
    }

    

    public static void registar () throws IOException, InterruptedException {         // metodo de pedido de registo de utilizador

        clearScreen();

        Scanner input = new Scanner(System.in);
        File myFile = new File ("Clientes.txt");

        try {
            if(!myFile.exists()){           
                myFile.createNewFile();     // criar o ficheiro "Clientes.txt" caso ainda nao exista, ou seja, caso seja o primeiro utilizador a registar-se
            }


        FileWriter writer = new FileWriter(myFile, true);      // variaveis para ler e escrever no ficheiro
        BufferedWriter buf = new BufferedWriter(writer);

        try {
            x = new Scanner(new File("Clientes.txt"));
            } 
        catch(Exception e){
            System.out.println("couldnt find file");    // erro caso o ficheiro nao seja encontrado
            TimeUnit.SECONDS.sleep(2);
            registar();
            return;
            } 

        System.out.println("\033[1mMenu de registo: \033[0m\n");
        System.out.print("Primeiro Nome: ");
        String nome = input.next();

        System.out.print("Email: ");
        String email = input.next();
            
        while(x.hasNext()){     // mais uma vez, cada string corresponde a um dos parametros dos utilizadores

        String a = x.next();
        String b = x.next();
        String c = x.next();
        String d = x.next();
        String e = x.next();

        if(email.equals(b)){
            System.out.println("\n\033[1mEmail already exists!\033[0m\n");          // verificacao se o email já foi registado
            TimeUnit.SECONDS.sleep(2);
            registar();
            return;
            }
        }


        System.out.print("Username: ");
        String username = input.next();

        try {
            y = new Scanner(new File("Clientes.txt"));
            } 
        catch(Exception e){
            System.out.println("couldnt find file");    // erro caso o ficheiro nao seja encontrado
            TimeUnit.SECONDS.sleep(2);
            registar();
            return;
        }

        while(y.hasNext()){

            String a = y.next();
            String b = y.next();
            String c = y.next();
            String d = y.next();
            String e = y.next();

        if(username.equals(c)){
            System.out.println("\n\033[1mUsername already exists!\033[0m\n");        // verificao se o username ja foi registado
            TimeUnit.SECONDS.sleep(2);
            registar();
            return;
        }
        }

        // escrever os parametros do utilizador para o ficheiro, para ficarem assim guardados

        buf.write(nome);            // escrever o nome
        buf.write(" ");             // deixar um espaço
        buf.write(email);           // escrever email
        buf.write(" ");             // ...
        buf.write(username);


        buf.write(" ");
        System.out.print("Password: ");
        String password = input.next();
        buf.write(password);
        buf.write(" ");
        buf.write("false");
        buf.write("\n");
           
        buf.flush();
        buf.close();

        System.out.println("\n\033[1mPedido de utilizador criado com sucesso!\033[0m");
        TimeUnit.SECONDS.sleep(2);



        } catch(IOException e){
            System.out.println("Erro");
            registar();
            return;
        }
    }

    public static void verTopicos() throws IOException, InterruptedException {      // listar os topicos existentes

        clearScreen();

        System.out.println("Topicos: ");
        Scanner input = new Scanner(System.in);
        File file = new File("Topicos.txt");
        FileWriter writer = new FileWriter("Topicos.txt",true);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int contador = 0;

        try {
            x = new Scanner(new File("Topicos.txt"));
            } 
        catch(Exception e){
            System.out.println("Ficheiro nao encontrado");
            }

        while(x.hasNext()){         // parametros dos topicos

            String a = x.next();    // nome do topico
            String b = x.next();    // descricao
            String c = x.next();    // numero maximo de mensagens

            contador++;
            System.out.println("> " + contador + ": " + a);     // listagem dos topicos  1 a 1
        }

        System.out.println("Existem " + contador + " topicos");
        
    }

    public static void verFeed() throws IOException, InterruptedException  {       // metodo para ver o feed 

        clearScreen();

        Scanner input = new Scanner(System.in);
        File file = new File(user+".txt");
        FileWriter writer = new FileWriter(user+".txt",true);
        BufferedReader reader = new BufferedReader(new FileReader(file));  
        int cont = 1;
        System.out.println("\033[1mFeed: \033[0m\n");

        try
            {

            try {
                x = new Scanner(new File(user+".txt"));     // criar um scanner para o ficheiro de texto com o nome do utizador para ver quais topicos o utilizador subscreve
            } 
            catch(Exception e){
                System.out.println("Ficheiro nao encontrado");
                TimeUnit.SECONDS.sleep(2);
                verFeed();
                return;
            }
            
            String line = "", oldtext = "",linha="", newTexto="";

            while(x.hasNext()){ // parametros do ficheiro do utilizador dos topicos que subescreve

                String a = x.next();        // nome do topico
                String b = x.next();        // topico favoritado ou nao (true ou false)

                File topico = new File(a+".txt");       
                Scanner top = new Scanner(new File(a+".txt"));  // criar um scanner para o ficheiro de texto com o nome do topico 

                BufferedReader read = new BufferedReader(new FileReader(topico));      // ler o ficheiro de texto do topico

                while((line = read.readLine()) != null)
                {  
                    linha = line + "\n";
                    if(b.equals("true")){              // se um determinado topico for favorito, as mensagens desse topico vao aparecer a negrito
                        System.out.print(cont +": \033[1m"+linha+"\033[0m");
                        cont++;
                    }
                    else{
                    System.out.println(cont +": "+ linha);
                    cont++;
                }
                }
            }

            System.out.println("Quer colocar like em alguma mensagem? (sair 0) ");
            System.out.print("> ");
            int num = input.nextInt();
            colocarLikes(num);              // colocar "like" na linha que selecionou

            reader.close();
        }
         
        catch (IOException ioe)
            {
            ioe.printStackTrace();
        }
    }

    public static void colocarLikes(int num) throws IOException, InterruptedException  {       // colocar "like" numa determinada mensagem

        int contador = 1;
        int likes = 0;
        String like = "";

            if(num != 0){

                try {
                    x = new Scanner(new File(user+".txt"));
                } 
                catch(Exception e){
                    System.out.println("Ficheiro nao encontrado");
                    TimeUnit.SECONDS.sleep(2);
                    verFeed();
                    return;
                }

                while(x.hasNext()){

                String a = x.next();
                String b = x.next();
                String line = "", oldtext = "",linha="", newTexto="";

                File topico = new File(a+".txt");
                FileWriter likesWriter = new FileWriter(a+".txt",true);     // alterar o valor de likes na mensagem do topico desse ficheiro


                y = new Scanner(new File(a+".txt"));
                
                BufferedReader read = new BufferedReader(new FileReader(topico));       // ler as mensagens do topico

                while((line = read.readLine()) != null)   
                {

                    oldtext += line + "\r\n";

                    String aa = y.next();       // nome de utilizador
                    String ab = y.next();       // numeros de likes
                    y.nextLine();

                    PrintWriter pw = new PrintWriter(a+".txt");

                    if(contador != num){
                        likesWriter.write(line+"\n");
                    }


                    if(contador == num){        // quando chegar à linha da mensagem que quer alterar
                        linha = line + "\n";
                        likes = Integer.parseInt(ab);
                        likes++;                        // incrementar o numero de likes em 1
                        like = Integer.toString(likes);
                        String newLinha = linha.replaceAll(ab,like);
                        likesWriter.write(newLinha);
                        System.out.println("Like colocado!\n");
                    }
                    contador++;
                }
                likesWriter.close();
            }
        }
    }
    public static void verMensagens() throws IOException, InterruptedException {        // ver mensagens de um topico especifico

        clearScreen();

        Scanner input = new Scanner(System.in);
        System.out.println("Qual tópico quer ver? (Sair 0)");
        System.out.print("> ");
        String topico = input.next();

        if(!topico.equals("0")){

        File file = new File(topico+".txt");

        if(!file.exists()){                                                 // verificar se o topico que foi introduzido pelo cliente existe, caso nao exista executa este codigo
            System.out.println("\033[1mO tópico não existe!\033[0m");
            TimeUnit.SECONDS.sleep(2);
            verMensagens();
            return;
        }
        BufferedReader reader = new BufferedReader(new FileReader(file));

        
        try
            {
             
            String line = "", oldtext = "",linha="", newTexto="";
            while((line = reader.readLine()) != null)
                {
                oldtext += line + "\r\n";               // ler o conteudo do topico
            }
            reader.close();
            System.out.println(oldtext);                // imprimir o conteudo do texto
        }
         
        catch (IOException ioe)
            {
            ioe.printStackTrace();
        }
    }
}

    public static void subscreverTopico() throws IOException, InterruptedException {        // subscrever um topico

        clearScreen();
        Scanner input = new Scanner(System.in);
        File file = new File("Topicos.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));

        try{
        x = new Scanner(new File("Topicos.txt"));
        }
        catch(Exception e){
            System.out.println("Ficheiro nao existente");
            subscreverTopico();
            return;
        }

        File myFile = new File(user+".txt");

        
        if(!myFile.exists()){           
            myFile.createNewFile();  }   // criar o ficheiro "Clientes.txt" caso ainda nao exista, ou seja, caso seja o primeiro utilizador a registar-se

        y = new Scanner(new File(user+".txt"));
        




        System.out.print("Que tópico deseja subscrever? (Sair 0) \n> ");
        String subs = input.next();

        if(!subs.equals("0")){
        while (y.hasNext()){

            String a = y.next();
            String b = y.next();

            if(a.equals(subs)){         // verificar se o topico ja foi subscrito
                System.out.println("Já subscreveu esse tópico!");
                TimeUnit.SECONDS.sleep(2);
                subscreverTopico();
                return;
            }
                
        }

        while(x.hasNext()){

            String a = x.next();
            String b = x.next();
            String c = x.next();

            if(subs.equals(a)){

                File sub = new File(user+".txt");
                FileWriter writer = new FileWriter(user+".txt",true);

                writer.write(subs);     // escrever para o ficheiro do utilizador o topico a subscrever
                writer.write (" ");
                writer.write("false");  // escrever se é favorito ou nao (predefinido false)
                writer.write("\n");
                writer.flush();
                writer.close();

                System.out.println("\033[1mTopico subscrito com sucesso\033[0m");

            }
        }
    }
    }

    public static void publicarTopico() throws IOException, InterruptedException {  // metodo para publicar uma mensagem num topico

        clearScreen();
        Scanner input = new Scanner(System.in);
        File subscritos = new File(user+".txt");   
        File topicos = new File("Topicos.txt");
        BufferedReader reader = new BufferedReader(new FileReader(subscritos));
        BufferedReader read = new BufferedReader(new FileReader(topicos));
        boolean subscrito = false;  // variavel para verificar se já está subscrito
        System.out.print("Em que tópico deseja publicar? (Sair 0) \n> ");
        String escolha = input.next();

        if(!escolha.equals("0")){
        int maximo = 100;
        String mensagem = "";
        File topico = new File (escolha+".txt");

        if(!topico.exists()){
            System.out.println("O topico não existe!");
            TimeUnit.SECONDS.sleep(2);
            publicarTopico();
            return;

        }

        BufferedReader topicoReader = new BufferedReader(new FileReader(escolha+".txt"));
        FileWriter topicoWriter = new FileWriter(escolha+".txt",true);

        x = new Scanner(new File("Topicos.txt"));
    
        
        while(x.hasNext()){
            String a = x.next();
            String b = x.next();
            String c = x.next();

            if(escolha.equals(a))
                maximo = Integer.parseInt(c);       // maximo de mensagens no topico
        }

        try
            {
             
            String line = "", oldtext = "",newTexto="";

            while((line = reader.readLine()) != null)
                {
                if(line.contains(escolha)){             // se o topico estiver subscrito, guardar a mensagem
                    System.out.print("Escrever mensagem: ");
                    input.nextLine();
                    mensagem = input.nextLine();
                    subscrito = true;
                }
            }
            reader.close();

            BufferedReader reader1 = new BufferedReader(new FileReader(escolha+".txt"));
            BufferedReader reader2 = new BufferedReader(new FileReader(escolha+".txt"));

            int cont = 0;

            while((line = reader1.readLine()) != null){
                cont++;
            }

            while((line = reader2.readLine()) != null){ 
                if(cont < maximo){              // verificar o maximo de mensagens possiveis
                    newTexto += line + "\r\n";
                }
                cont--;
            }

            PrintWriter pw = new PrintWriter(escolha+".txt");

            topicoWriter.write(newTexto);           // escrever no ficheiro que guarda as mensagens do topico
            topicoWriter.write(user+":");
            topicoWriter.write(" ");
            topicoWriter.write("0 likes: ");
            topicoWriter.write("em "+escolha+ "->  ");
            topicoWriter.write(mensagem);
            topicoWriter.write("\n");

            topicoWriter.flush();
            topicoWriter.close();

            if (subscrito == false){
                System.out.println("\033[1mNão estás subscrito nesse tópico!\033[0m");
                TimeUnit.SECONDS.sleep(2);
                publicarTopico();
                return;
            }

            System.out.println("\n\033[1mMensagem publicada\033[0m");
        }
   
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    }

    public static void gerirSubscricoes() throws IOException, InterruptedException {        // metodo para exibir o menu de gerir as subscricoes

        clearScreen();
        System.out.println("\033[1mGerir as subscricões:\033[0m\n");
        Scanner input = new Scanner(System.in);

        System.out.println("1) Deixar de seguir um tópico");
        System.out.println("2) Favoritar/desfavoritar um tópico");
        System.out.println("3) Voltar ao menu");
        int escolha = input.nextInt();

        switch(escolha){
            case 1: deixarDeSeguir();
                    break;
            case 2: favoritarSub();
                    break;
            case 3: menu();
                    break;
            default: System.out.println("\033[1mOpção inválida\033[0m");
                    TimeUnit.SECONDS.sleep(2);
                    gerirSubscricoes();
                    return;
        }

    }

    public static void deixarDeSeguir() throws IOException, InterruptedException {      //metodo para deixar de seguir algum topico

        clearScreen();
        Scanner input = new Scanner(System.in);
        System.out.println("Qual tópico deseja deixar de seguir?");
        System.out.print("> ");
        String topico = input.next();

        File file = new File(user+".txt");
        FileWriter writer = new FileWriter(user+".txt",true);
        BufferedReader reader = new BufferedReader(new FileReader(file));

        x = new Scanner(new File(user+".txt"));     // ficheiro com o nome do utilizador onde guarda as suas subscricoes
            

        boolean existe = false;

        while(x.hasNext()){
            String a = x.next();
            String b = x.next();

            if(a.equals(topico))        // verificar se segue o topico que colocou
                existe = true;
        }

        if(existe == false){
            System.out.println("\033[1mO tópico "+ topico + " não existe!\033[0m"); // verificar se o topico existe
            TimeUnit.SECONDS.sleep(2);
            deixarDeSeguir();
            return;
        }
        
        try
            {
             
            String line = "", oldtext = "",linha="", newTexto="";
            while((line = reader.readLine()) != null)
                {
                oldtext += line + "\r\n";
                    if(line.contains(topico))
                        linha = line + "\r\n";
            }
            reader.close();

            newTexto = oldtext.replaceAll(linha,"");            // apagar a linha que contem o topico subscrito, apagando-o
            PrintWriter pw = new PrintWriter(user+".txt");
            writer.write(newTexto);writer.close();

            System.out.println("\033[1mDeixou de seguir " + topico + " com sucesso!\033[0m");
                
        }
         
        catch (IOException ioe)
            {
            ioe.printStackTrace();
        }

    }
    
    public static void favoritarSub() throws IOException, InterruptedException {    // favoritar um topico

        clearScreen();
        Scanner input = new Scanner(System.in);
        System.out.println("Qual tópico quer favoritar/defavoritar? (Sair 0)");
        System.out.print("> ");
        String topico = input.next();

        if(!topico.equals("0")){
        File file = new File(user+".txt");
        FileWriter writer = new FileWriter(user+".txt",true);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String newLinha = "";

        File file2 = new File(topico+".txt");


        if(!file2.exists()){    // verificar se o topico existe
            System.out.println("\033[1mO tópico não existe!\033[0m");
            TimeUnit.SECONDS.sleep(2);
            favoritarSub();
            return;
        }

        try
             {
             
             String line = "", oldtext = "",linha="", newTexto="";
             while((line = reader.readLine()) != null)
                 {
                 oldtext += line + "\r\n";
                 if(line.contains(topico))
                    linha = line + "\r\n";
             }
             reader.close();

            if(linha.contains("true")){     // se o topico esta favoritado, desfavoritar
                newLinha = linha.replaceAll("true","false");
                System.out.println("\033[1mTópico desfavoritado!\033[0m");
            }
            
            if(linha.contains("false")){        // se o topico esta desfavoritado, favoritar
                newLinha = linha.replaceAll("false","true");
                System.out.println("\033[1mTópico favoritado!\033[0m");
            }


            newTexto = oldtext.replaceAll(linha,newLinha);            

             PrintWriter pw = new PrintWriter(user+".txt");
             writer.write(newTexto);writer.close();

                
         }
         
         catch (IOException ioe)
             {
             ioe.printStackTrace();
         }
     }
    }

    public static void topicosMaisUtilizados() throws IOException {     // metodo para visualizar os topicos mais utilizados

                clearScreen();
                int contador = 0;
                int maximo = 0;
                String nomes[] = new String[1000];
                int mensagens[] = new int[1000];
                int i = 0, j = 0;
                int temp = 0; 
                String tempo="";
                int cont = 0;

                x = new Scanner(new File("Topicos.txt"));
                
                while(x.hasNext()){

                String a = x.next();
                String b = x.next();
                String c = x.next();
                cont++;

                String line = "";

                File topico = new File(a+".txt");
                Scanner top = new Scanner(new File(a+".txt"));

                y = new Scanner(new File(a+".txt"));

                BufferedReader read = new BufferedReader(new FileReader(topico)); 

                while((line = read.readLine()) != null)
                {
                    contador++; // contar quantas mensagens o topico tem
                }

                mensagens[i] = contador;    // atribuir a um array o numero de mensagens e a outro o nome dos topicos
                nomes[i] = a;
                contador = 0;
                i++;
            }
            for(i = 0; i < 100; i++){
                for(j = i+1; j < 100; j++){
                    if(mensagens[i] < mensagens[j]){        // ordenar os topicos com mais mensagens para os que têm menos
                        temp = mensagens[i];
                        mensagens[i] = mensagens[j];
                        mensagens[j]=temp;

                        tempo = nomes[i];
                        nomes[i] = nomes[j];
                        nomes[j] = tempo;
                }
            }
        }
            System.out.println("\033[1mTópicos mais ativos: \033[0m\n");
            if(cont < 3){
                for(j = 0;j<cont;j++){
                   System.out.println(nomes[j] + " com " + mensagens[j] + " mensagens");        // imprimir os 3 topicos mais ativos, exceto se existirem menos de 3 elementos, e aí apresenta os que existem
                }
            }
            else{
            for(j = 0;j<3;j++){
                System.out.println(nomes[j] + " com " + mensagens[j] + " mensagens");
            }
        }
    }

    public static void estatisticas() throws IOException, InterruptedException {    // metodo das estatisticas

        clearScreen();
        System.out.println("\033[1mEstatísticas do utilizador: \033[0m");
        mensagensUtilizador();

    }

    public static void mensagensUtilizador() throws IOException, InterruptedException { // estatisticas do utilizador

                int contador = 1;
                int total = 0;
                
                x = new Scanner(new File(user+".txt"));
                

                while(x.hasNext()){

                String a = x.next();
                String b = x.next();
                String line = "";

                File topico = new File(a+".txt");
                Scanner top = new Scanner(new File(a+".txt"));

                y = new Scanner(new File(a+".txt"));
               

                BufferedReader read = new BufferedReader(new FileReader(topico)); 
                if(!(read.readLine() == null)){
                    String aa = y.next();

                while((line = read.readLine()) != null)
                {
                    if(aa.contains(user))
                        contador++; // contar as mensagens do utilizador
                }
                System.out.println(a + ": " + contador + " mensagens");
                total += contador;
                contador = 1;
            }
        }
        System.out.println("Total de " + total + " mensagens em todos os tópicos");
    }

    public static void main(String[] args) throws IOException, InterruptedException {  

        boolean estado = menuDeRegisto();   // exibir o menu inicial

        while(estado == true){              // enquanto o estado do login for true, o programa nao vai encerrar

            estado = menu();           
        }
    }
}