/**
 * Implementação do ADT List que mantém os elementos ordenados automaticamente
 * aquando da inserção.
 * * Esta implementação utiliza uma lista simplesmente ligada.
 * * @param <T> O tipo de dados (deve implementar Comparable).
 * @author Simão Ferreira / Miguel Eusébio
 * @version 1.1
 */
public class SortedList<T extends Comparable<T>> implements List<T> {
    
    /** Referência para o primeiro nó da lista. */
    private Node<T> head;
    
    /** Contador do número de elementos na lista. */
    private int size;

    /**
     * Construtor padrão que inicializa uma lista vazia.
     */
    public SortedList() {
        this.head = null;
        this.size = 0;
    }

    /**
     * Insere um elemento na lista mantendo a ordem crescente.
     * Percorre a lista até encontrar a posição correta de inserção.
     * * @param element O elemento a adicionar.
     */
    @Override
    public void add(T element) {
        Node<T> newNode = new Node<>(element);
        
        // Caso 1: Inserir no início.
        if (head == null || element.compareTo(head.data) < 0) {
            newNode.next = head;
            head = newNode;
        } else {
            // Caso 2: Procurar a posição correta no meio/fim.
            Node<T> current = head;
            while (current.next != null && element.compareTo(current.next.data) > 0) {
                current = current.next;
            }
            // Inserir após o nó current.
            newNode.next = current.next;
            current.next = newNode;
        }
        size++;
    }

    /**
     * Remove a primeira ocorrência do elemento especificado.
     * @return true se encontrou e removeu o elemento, false caso contrário.
     */
    @Override
    public boolean remove(T element) {
        if (head == null) return false;

        // Remover da cabeça
        if (head.data.equals(element)) {
            head = head.next;
            size--;
            return true;
        }

        Node<T> current = head;
        while (current.next != null && !current.next.data.equals(element)) {
            current = current.next;
        }

        if (current.next != null) {
            current.next = current.next.next;
            size--;
            return true;
        }
        return false;
    }

    /**
     * Obtém o elemento numa posição específica percorrendo a lista.
     * @return O elemento na posição ou null se índice inválido.
     */
    @Override
    public T get(int index) {
        if (index < 0 || index >= size) return null;
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    /**
     * Limpa a lista removendo todos os elementos.
     */
    @Override
    public void clear() {
        head = null;
        size = 0;
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }
}