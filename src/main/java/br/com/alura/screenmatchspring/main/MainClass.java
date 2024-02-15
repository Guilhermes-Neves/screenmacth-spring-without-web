package br.com.alura.screenmatchspring.main;

import br.com.alura.screenmatchspring.model.EpisodeData;
import br.com.alura.screenmatchspring.model.SeasonData;
import br.com.alura.screenmatchspring.model.SerieData;
import br.com.alura.screenmatchspring.model.Episode;
import br.com.alura.screenmatchspring.service.ApiConsumer;
import br.com.alura.screenmatchspring.service.DataConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class MainClass {
    private final Scanner scanner = new Scanner(System.in);
    private final String BASE_URL = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=d8270438";
    private final DataConverter converter = new DataConverter();
    public void showMenu() {
        System.out.print("Qual seria deseja buscar? ");
        var titleName = scanner.nextLine();
        var response = ApiConsumer.getData(BASE_URL + titleName.replace(" ", "+")  + API_KEY);

        SerieData serieData = converter.getData(response.body(), SerieData.class);
        System.out.println(serieData);

        List<SeasonData> seasons = new ArrayList<>();
        for (int i = 1; i <= serieData.totalSeasons(); i++) {
            String address = BASE_URL + titleName.replace(" ", "+") + API_KEY + "&Season=" + i;
            response = ApiConsumer.getData(address);
            seasons.add(converter.getData(response.body(), SeasonData.class));
        }

//        seasons.forEach(System.out::println);
//
//        seasons.forEach(s -> s.episodes().forEach(e -> System.out.println(e.title())));

        List<EpisodeData> episodeDataList = seasons.stream()
                .flatMap(s -> s.episodes().stream())
                .collect(Collectors.toList());


        System.out.println("\n Top 5 episódios da temporada: ");
        episodeDataList.stream()
                .filter(e -> !e.rate().equalsIgnoreCase("N/A"))
                        .sorted(Comparator.comparing(EpisodeData::rate).reversed())
                        .limit(5)
                        .forEach(System.out::println);

        List<Episode> episodes = seasons.stream()
                .flatMap(s -> s.episodes().stream()
                        .map(d -> new Episode(s.seasonNumber(), d))
                ).collect(Collectors.toList());

        episodes.forEach(System.out::println);

//        System.out.print("Digite o nome do episódio desejado: ");
//        String partOfTitle = scanner.nextLine();
//
//        Optional<Episode> searchedEpisode = episodes.stream()
//                .filter(e -> e.getTitle().toLowerCase().contains(partOfTitle.toLowerCase()))
//                .findFirst();
//
//        if (searchedEpisode.isPresent()) {
//            System.out.println("Episodio encontrado: na temporada: " + searchedEpisode.get().getSeason());
//        } else {
//            System.out.println("Episodio não encontrado!");
//        }


//        System.out.print("A partir de que ano voce deseja ver os episodios? ");
//        var year = scanner.nextInt();
//        scanner.nextLine();
//
//        LocalDate searchDate = LocalDate.of(year, 1, 1);
//
//        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodes.stream()
//                .filter(e -> e.getReleaseDate() != null && e.getReleaseDate().isAfter(searchDate))
//                .forEach(e -> System.out.println(
//                        "Temporada: " + e.getSeason() +
//                                " Episode: " + e.getTitle() +
//                                " Data de lancamento: " + e.getReleaseDate().format(formatterDate)
//                ));

        Map<Integer, Double> ratesForSeason = episodes.stream()
                .filter(e -> e.getRate() > 0.0)
                .collect(Collectors.groupingBy(Episode::getSeason, Collectors.averagingDouble(Episode::getRate)));

        System.out.println(ratesForSeason);

        DoubleSummaryStatistics est = episodes.stream()
                .filter(e -> e.getRate() > 0.0)
                .collect(Collectors.summarizingDouble(Episode::getRate));

        System.out.println("Media: " + est.getAverage());
        System.out.println("Melhor episódio: " + est.getMax());
        System.out.println("Pior episódio: " + est.getMin());
        System.out.println("Quantidade de episódios: " + est.getCount());

    }
}
