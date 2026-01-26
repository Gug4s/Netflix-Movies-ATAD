/**
 * Classe de arranque da aplicação.
 * Apenas instancia o gestor e inicia a execução.
 * @author Simão Ferreira / Miguel Eusébio
 * @version 1.1
 */
public class Main {

    /**
     * Ponto de entrada da aplicação.
     * @param args Argumentos de linha de comandos.
     */
    public static void main(String[] args) {
        // Cria a instância do gestor NetflixManager.
        NetflixManager manager = new NetflixManager();
        
        // Inicia o programa.
        manager.run();
    }
}