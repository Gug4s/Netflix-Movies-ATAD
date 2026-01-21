import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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
    private int duration;
    private String listedIn;
    private String description;

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

        try {
            if (dateStr != null && !dateStr.isBlank()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
                this.dateAdded = LocalDate.parse(dateStr.trim(), formatter);
            }
        } catch (Exception e) {
            this.dateAdded = null;
        }

        try {
            if (durationStr != null) {
                String numbers = durationStr.replaceAll("[^0-9]", "");
                this.duration = Integer.parseInt(numbers);
            }
        } catch (Exception e) {
            this.duration = 0;
        }
    }

    @Override
    public int compareTo(NetflixShow other) {
        return this.title.compareToIgnoreCase(other.title);
    }

    // Getters
    public String getShowId() { return showId; }
    public String getTitle() { return title; }
    public String getType() { return type; }
    public int getDuration() { return duration; }
    public String getRating() { return rating; }
    public int getReleaseYear() { return releaseYear; }
    public String getCast() { return cast; }
    public String getDirector() { return director; }
    public String getListedIn() { return listedIn; }
    public LocalDate getDateAdded() { return dateAdded; }

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