/**
 * Representa um nó genérico numa lista ligada.
 * Contém o dado armazenado e uma referência para o próximo nó.
 * @param <T> O tipo de dado armazenado no nó.
 * @author Simão Ferreira / Miguel Eusébio
 * @version 1.1
 */
public class Node<T> {
    
    /** O dado armazenado neste nó. */
    public T data;
    
    /** Referência para o próximo nó na lista. */
    public Node<T> next;

    /**
     * Constrói um novo nó com o dado especificado.
     * @param data O dado a armazenar no nó.
     */
    public Node(T data) {
        this.data = data;
        this.next = null;
    }
}