/**
 * Interface que define o contrato para o Tipo Abstrato de Dados (ADT) List.
 * Permite adicionar, remover e aceder a elementos de forma abstrata.
 * * @param <T> O tipo de dados a ser armazenado na lista.
 * @author Simão Ferreira / Miguel Eusébio
 * @version 1.1
 */
public interface List<T> {

    /**
     * Adiciona um elemento à lista.
     * @param element O elemento a adicionar.
     */
    void add(T element);

    /**
     * Remove a primeira ocorrência do elemento especificado.
     * @param element O elemento a remover.
     * @return true se o elemento foi removido, false caso contrário.
     */
    boolean remove(T element);

    /**
     * Devolve o número de elementos atualmente na lista.
     * @return O tamanho da lista.
     */
    int size();

    /**
     * Verifica se a lista está vazia.
     * @return true se a lista não tiver elementos, false caso contrário.
     */
    boolean isEmpty();

    /**
     * Obtém o elemento numa posição específica.
     * @param index O índice do elemento.
     * @return O elemento na posição indicada ou null se o índice for inválido.
     */
    T get(int index);

    /**
     * Remove todos os elementos da lista, deixando-a vazia.
     */
    void clear();
}