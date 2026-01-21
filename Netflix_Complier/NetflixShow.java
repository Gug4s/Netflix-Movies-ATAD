import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Representa um título da Netflix (Filme ou Série de TV).
 * Implementa {@link Comparable} para permitir a ordenação natural por título (case-insensitive).
 * * Esta classe é responsável por fazer o parsing de strings para tipos concretos
 * (como LocalDate e int) durante a construção.
 * * @author Simão Ferreira / Miguel Eusébio
 * @version 1.0
 */
public class NetflixShow implements Comparable<NetflixShow> {
    private String showId;
    private String type;
    private String title;
    private String director;
    private String cast;
    private String country;
    private LocalDate dateAdded;
    private int releaseYear;
    private String rating;
    private int duration; // Em caso de filmes: minutos; em séries: número de temporadas
    private String listedIn;
    private String description;

    /**
     * Constrói uma nova instância de NetflixShow com todos os atributos.
     * Realiza o parsing automático da data e da duração a partir das strings fornecidas.
     * * @param showId Identificador único do título (ex: "s1").
     * @param type Tipo de título ("Movie" ou "TV Show").
     * @param title Título do filme/série.
     * @param director Diretor/Diretores do título.
     * @param cast Elenco principal.
     * @param country País de origem.
     * @param dateStr Data de adição no formato "mm, dd, yyyy".
     * @param releaseYear Ano de lançamento.
     * @param rating Classificação etária.
     * @param durationStr Duração em string bruta (ex: "90 min" para filmes, "2 Seasons" ).
     * @param listedIn Categorias onde o título se enquadra.
     * @param description Descrição ou sinopse.
     */
    public NetflixShow(String showId, String type, String title, String director, String cast,
                       String country, String dateStr, int releaseYear, String rating, 
                       String durationStr, String listedIn, String description) {
        this.showId = showId;
        this.type = type;
        this.title = title;
        this.director = director;
        this.cast = cast;
        this.country = country;
        this.releaseYear = releaseYear;
        this.rating = rating;
        this.listedIn = listedIn;
        this.description = description;

        // Conversão da Data (Ex: "September 25, 2021")
        try {
            if (dateStr != null && !dateStr.isBlank()) {
                // Locale.ENGLISH é vital para ler "September", "October", etc.
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
                this.dateAdded = LocalDate.parse(dateStr.trim(), formatter);
            }
        } catch (Exception e) {
            this.dateAdded = null;
        }

        // Conversão da Duração para int (Ex: "90 min" -> 90)
        try {
            if (durationStr != null) {
                String numbers = durationStr.replaceAll("[^0-9]", "");
                this.duration = Integer.parseInt(numbers);
            }
        } catch (Exception e) {
            this.duration = 0;
        }
    }

    /**
     * Compara este título com outro baseando-se no nome do título.
     * A comparação não é sensível a maiúsculas/minúsculas.
     * * @param other O outro objeto NetflixShow a comparar.
     * @return Um valor negativo, zero ou positivo conforme este título seja
     * menor, igual ou maior que o outro.
     */
    @Override
    public int compareTo(NetflixShow other) {
        return this.title.compareToIgnoreCase(other.title);
    }

    // Getters

    /** @return O identificador único do show. */
    public String getShowId() { return showId; }
    
    /** @return O título do show. */
    public String getTitle() { return title; }
    
    /** @return O tipo ("Movie" ou "TV Show"). */
    public String getType() { return type; }
    
    /** @return A duração em valor numérico (minutos ou temporadas). */
    public int getDuration() { return duration; }
    
    /** @return A classificação etária (rating). */
    public String getRating() { return rating; }
    
    /** @return O ano de lançamento. */
    public int getReleaseYear() { return releaseYear; }
    
    /** @return A lista de atores (como string única). */
    public String getCast() { return cast; }
    
    /** @return O diretor (como string única). */
    public String getDirector() { return director; }
    
    /** @return As categorias do título. */
    public String getListedIn() { return listedIn; }
    
    /** @return A data de adição na plataforma como objeto LocalDate. */
    public LocalDate getDateAdded() { return dateAdded; }

    /**
     * Devolve uma representação textual formatada do objeto, adequada para listagens em tabela.
     * Trunca o título se for demasiado longo.
     * * @return String formatada com ID, Tipo, Título, Data, Rating e Duração.
     */
    @Override
    public String toString() {
        String dateStr = (dateAdded != null) ? dateAdded.toString() : "N/A";
        String durUnit = type.equalsIgnoreCase("Movie") ? " min" : " Seasons";
        
        // Truncar título para caber na tabela
        String displayTitle = title.length() > 30 ? title.substring(0, 27) + "..." : title;
        
        return String.format("%-8s | %-8s | %-30s | %-12s | %-6s | %s", 
            showId, type, displayTitle, dateStr, rating, duration + durUnit);
    }
}