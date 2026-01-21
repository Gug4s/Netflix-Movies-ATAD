import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
    private static SortedList<NetflixShow> shows = new SortedList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String command = "";

        System.out.println("Netflix Manager - ATAD 2025. Digite um comando.");

        while (!command.equalsIgnoreCase("QUIT")) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");
            command = parts[0].toUpperCase();

            try {
                switch (command) {
                    case "LOADF":
                        if (parts.length < 2) System.out.println("Erro: LOADF <filename>");
                        else loadFile(parts[1]);
                        break;
                    case "LOADD": loadFile("../netflix_movies/netflix_titles.csv"); break;
                    case "DEL":
                        if (parts.length < 2) System.out.println("Erro: DEL <id>");
                        else deleteShow(parts[1], scanner);
                        break;
                    case "CLEAR":
                        int size = shows.size();
                        shows.clear();
                        System.out.println(size + " shows deleted");
                        break;
                    case "LIST": listShows(shows); break;
                    case "GET":
                        if (parts.length < 2) System.out.println("Erro: GET <id>");
                        else getShow(parts[1]);
                        break;
                    case "STATS": showStats(); break;
                    case "MTIME":
                        if (parts.length < 3) System.out.println("Erro: MTIME <min> <max>");
                        else mtime(parts[1], parts[2]);
                        break;
                    case "SEARCHT":
                        if (parts.length < 2) System.out.println("Erro: SEARCHT <texto>");
                        else searchTitle(line.substring(8));
                        break;
                    case "SEARCHC":
                        if (parts.length < 2) System.out.println("Erro: SEARCHC <texto>");
                        else searchCast(line.substring(8));
                        break;
                    case "RATINGS": showUniqueRatings(); break;
                    case "CATEGORIES": showUniqueCategories(); break;
                    case "SEGMENT": segmentData(); break;
                    case "QUIT": System.out.println("A sair..."); break;
                    default: System.out.println("Comando desconhecido.");
                }
            } catch (Exception e) {
                System.out.println("Erro na execução do comando: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void loadFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            br.readLine(); // Ignorar cabeçalho
            int count = 0;
            while ((line = br.readLine()) != null) {
                String[] data = parseCsvLine(line);
                if (data.length >= 12) {
                    try {
                        int year = Integer.parseInt(data[7].trim());
                        NetflixShow show = new NetflixShow(
                            data[0], data[1], data[2], data[3], data[4], data[5], 
                            data[6], year, data[8], data[9], data[10], data[11]
                        );
                        shows.add(show);
                        count++;
                    } catch (Exception e) {

                    }
                }
            }
            System.out.println(count + " shows imported");
        } catch (Exception e) { System.out.println("File not found"); }
    }

    private static void deleteShow(String id, Scanner scanner) {
        NetflixShow target = findById(id);
        if (target == null) {
            System.out.println("Title not found");
            return;
        }
        System.out.print("Delete " + id + ", are you sure (y/n)? ");
        String confirm = scanner.nextLine();
        if (confirm.equalsIgnoreCase("y")) {
            shows.remove(target);
            System.out.println("Show deleted");
        }
    }

    private static void listShows(MyList<NetflixShow> listToList) {
        if (listToList.isEmpty()) { System.out.println("No data available..."); return; }
        
        int pageSize = 30;
        printHeader();
        Scanner sc = new Scanner(System.in); 

        for (int i = 0; i < listToList.size(); i++) {
            System.out.println(listToList.get(i));
            if ((i + 1) % pageSize == 0) {
                System.out.println("--- ENTER para continuar, 'n' para parar ---");
                if (sc.nextLine().equalsIgnoreCase("n")) break;
                printHeader();
            }
        }
    }

    private static void getShow(String id) {
        NetflixShow s = findById(id);
        if (s != null) {
            printHeader();
            System.out.println(s);
        } else {
            System.out.println("Title not found");
        }
    }

    // MTIME:
    private static void mtime(String minStr, String maxStr) {
        int min = Integer.parseInt(minStr);
        int max = Integer.parseInt(maxStr);
        
        // 1. Filtrar
        SortedList<NetflixShow> temp = new SortedList<>(); 
        for (int i = 0; i < shows.size(); i++) {
            NetflixShow s = shows.get(i);
            if (s.getType().equalsIgnoreCase("Movie") && s.getDuration() >= min && s.getDuration() <= max) {
                temp.add(s);
            }
        }
        // 2. Converter para array e ordenar (Bubble Sort Descendente)
        NetflixShow[] arr = toArray(temp);
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j].getDuration() < arr[j+1].getDuration()) { 
                    NetflixShow aux = arr[j]; arr[j] = arr[j+1]; arr[j+1] = aux;
                }
            }
        }
        listArray(arr);
    }

    // SEARCHT:
    private static void searchTitle(String query) {
        SortedList<NetflixShow> temp = new SortedList<>();
        for (int i = 0; i < shows.size(); i++) {
            NetflixShow s = shows.get(i);
            if (s.getTitle().toLowerCase().contains(query.toLowerCase())) temp.add(s);
        }
        sortByDate(temp);
    }

    // SEARCHC:
    private static void searchCast(String query) {
        SortedList<NetflixShow> temp = new SortedList<>();
        for (int i = 0; i < shows.size(); i++) {
            NetflixShow s = shows.get(i);
            if (s.getCast() != null && s.getCast().toLowerCase().contains(query.toLowerCase())) temp.add(s);
        }
        sortByDate(temp);
    }

    // RATINGS:
    private static void showUniqueRatings() {
        SortedList<String> ratings = new SortedList<>();
        for (int i = 0; i < shows.size(); i++) {
            String r = shows.get(i).getRating();
            if (r != null && !r.isBlank() && !contains(ratings, r)) {
                ratings.add(r);
            }
        }
        for (int i = 0; i < ratings.size(); i++) System.out.println(ratings.get(i));
    }

    // CATEGORIES:
    private static void showUniqueCategories() {
        SortedList<String> cats = new SortedList<>();
        for (int i = 0; i < shows.size(); i++) {
            String listedIn = shows.get(i).getListedIn();
            if (listedIn != null) {
                String[] parts = listedIn.split(",");
                for (String p : parts) {
                    String cat = p.trim();
                    if (!cat.isEmpty() && !contains(cats, cat)) cats.add(cat);
                }
            }
        }
        for (int i = 0; i < cats.size(); i++) System.out.println(cats.get(i));
    }

    // STATS: Contagens e médias
    private static void showStats() {
        if (shows.isEmpty()) { System.out.println("No data available..."); return; }
        
        int movCount=0, tvCount=0;
        int minMov=Integer.MAX_VALUE, maxMov=0, sumMov=0;
        int minTV=Integer.MAX_VALUE, maxTV=0, sumTV=0;

        for (int i=0; i<shows.size(); i++) {
            NetflixShow s = shows.get(i);
            int d = s.getDuration();
            if (s.getType().equalsIgnoreCase("Movie")) {
                movCount++; sumMov += d;
                if (d < minMov) minMov = d;
                if (d > maxMov) maxMov = d;
            } else {
                tvCount++; sumTV += d;
                if (d < minTV) minTV = d;
                if (d > maxTV) maxTV = d;
            }
        }
        System.out.printf("Movie count: %d | Min: %d | Max: %d | Avg: %.1f\n", movCount, minMov==Integer.MAX_VALUE?0:minMov, maxMov, movCount==0?0:(double)sumMov/movCount);
        System.out.printf("TV Show count: %d | Min: %d | Max: %d | Avg: %.1f\n", tvCount, minTV==Integer.MAX_VALUE?0:minTV, maxTV, tvCount==0?0:(double)sumTV/tvCount);
        System.out.println("Totals:\n" + sumMov + " total minutes of movie time\n" + sumTV + " total seasons of tv shows");
    }

    // SEGMENT: Exportar para ficheiros
    private static void segmentData() {
        try {
            // Segmentar por Rating
            exportSegmentFile("segment_by_rating.txt", true);
            // Segmentar por Diretor
            exportSegmentFile("segment_by_directors.txt", false);
            
            System.out.println("Files created: segment_by_rating.txt, segment_by_directors.txt");
        } catch(Exception e) { System.out.println("Error writing files."); }
    }

    // Função auxiliar para evitar código duplicado no SEGMENT
    private static void exportSegmentFile(String filename, boolean byRating) throws Exception {
        PrintWriter pw = new PrintWriter(new FileWriter(filename));
        SortedList<String> keys = new SortedList<>();

        // 1. Recolher chaves únicas
        for(int i=0; i<shows.size(); i++) {
            String val = byRating ? shows.get(i).getRating() : shows.get(i).getDirector();
            if(val == null || val.isBlank()) continue;
            
            // Diretores podem ser vários separados por vírgula? O CSV pode ter, mas simplificamos por campo inteiro ou split se necessário. 
            if(!contains(keys, val)) keys.add(val);
        }

        // 2. Escrever ficheiro
        for(int i=0; i<keys.size(); i++) {
            String key = keys.get(i);
            pw.println(">>> " + (byRating ? "RATING" : "DIRECTOR") + ": " + key);
            
            // Recolher programas desta chave
            SortedList<NetflixShow> group = new SortedList<>();
            for(int k=0; k<shows.size(); k++) {
                String val = byRating ? shows.get(k).getRating() : shows.get(k).getDirector();
                if(key.equals(val)) group.add(shows.get(k));
            }

            // Ordenar por ano de publicação
            NetflixShow[] arr = toArray(group);
            sortByYear(arr);
            for(NetflixShow s : arr) pw.println(s);
        }
        pw.close();
    }

    private static NetflixShow findById(String id) {
        for (int i = 0; i < shows.size(); i++) {
            if (shows.get(i).getShowId().equalsIgnoreCase(id)) return shows.get(i);
        }
        return null;
    }

    private static void sortByDate(SortedList<NetflixShow> list) {
        NetflixShow[] arr = toArray(list);
        // Bubble Sort Crescente por Data
        for (int i=0; i<arr.length; i++) {
            for (int j=0; j<arr.length-1-i; j++) {
                boolean swap = false;
                if (arr[j].getDateAdded() == null) swap = false;
                else if (arr[j+1].getDateAdded() == null) swap = true;
                else if (arr[j].getDateAdded().isAfter(arr[j+1].getDateAdded())) swap = true;
                
                if (swap) { NetflixShow temp=arr[j]; arr[j]=arr[j+1]; arr[j+1]=temp; }
            }
        }
        listArray(arr);
    }

    private static void sortByYear(NetflixShow[] arr) {
        for (int i=0; i<arr.length; i++) {
            for (int j=0; j<arr.length-1-i; j++) {
                if (arr[j].getReleaseYear() > arr[j+1].getReleaseYear()) {
                    NetflixShow temp=arr[j]; arr[j]=arr[j+1]; arr[j+1]=temp;
                }
            }
        }
    }

    private static NetflixShow[] toArray(MyList<NetflixShow> list) {
        NetflixShow[] arr = new NetflixShow[list.size()];
        for(int i=0; i<list.size(); i++) arr[i] = list.get(i);
        return arr;
    }

    private static void listArray(NetflixShow[] arr) {
        int pageSize = 30;
        printHeader();
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
            if ((i + 1) % pageSize == 0) {
                System.out.println("--- ENTER cont, 'n' stop ---");
                if (sc.nextLine().equalsIgnoreCase("n")) break;
            }
        }
    }
    
    private static boolean contains(MyList<String> list, String val) {
        for(int i=0; i<list.size(); i++) if(list.get(i).equals(val)) return true;
        return false;
    }

    private static void printHeader() {
        System.out.println("Show ID  | Type     | Title                          | Date Added   | Rating | Duration");
        System.out.println("---------|----------|--------------------------------|--------------|--------|----------");
    }

    // CSV Parse com regex para virgulas dentro de aspas
    private static String[] parseCsvLine(String line) {
        String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        for(int i=0; i<parts.length; i++) parts[i] = parts[i].replace("\"", "").trim();
        return parts;
    }
}