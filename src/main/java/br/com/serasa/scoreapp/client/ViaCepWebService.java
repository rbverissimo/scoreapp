package br.com.serasa.scoreapp.client;

import br.com.serasa.scoreapp.domain.Endereco;
import br.com.serasa.scoreapp.dto.EnderecoViaCepResponseDto;
import br.com.serasa.scoreapp.exceptions.ViaCepException;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Component
public class ViaCepWebService {


    public Optional<EnderecoViaCepResponseDto> fetchEnderecoByCep(String cep)
            throws IOException, InterruptedException, ViaCepException {

        EnderecoViaCepResponseDto dto = null;
        String viaCepAPIEndpoint = "https://viacep.com.br/ws/" + cep +"/json/";

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(viaCepAPIEndpoint))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() == 200){
            Gson gson = new Gson();
            dto = gson.fromJson(response.body(), EnderecoViaCepResponseDto.class);
        }
        if(response.statusCode() >= 400) throw new ViaCepException(response.body());
        return Optional.ofNullable(dto);

    }

}
