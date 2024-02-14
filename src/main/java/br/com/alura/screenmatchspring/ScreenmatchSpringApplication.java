package br.com.alura.screenmatchspring;

import br.com.alura.screenmatchspring.model.SerieData;
import br.com.alura.screenmatchspring.service.ApiConsumer;
import br.com.alura.screenmatchspring.service.DataConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchSpringApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchSpringApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String baseUrl = "https://www.omdbapi.com/";
		String apiKey = "d8270438";
		String titleName = "gilmore girls";
		String season = "1";
		String address = baseUrl + "?t=" + titleName.replace(" ", "+") + "&apikey=" + apiKey;

		var response = ApiConsumer.getData(address);
		var converter = new DataConverter();
		SerieData serieData = converter.getData(response.body(), SerieData.class);

		System.out.println(serieData);
	}
}
