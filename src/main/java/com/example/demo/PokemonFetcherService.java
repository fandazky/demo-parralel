package com.example.demo;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;
import java.util.List;

@Service
public class PokemonFetcherService {

    private static WebClient webClient = WebClient
            .builder()
            .baseUrl("https://pokeapi.co/")
            .build();

    public ParallelFlux<PokemonCharacterInfo> fetchPokemon() {
        List<String> idList = Arrays.asList("1", "2", "3", "4", "5");
        return Flux.fromIterable(idList)
                .parallel(5)
                .runOn(Schedulers.parallel())
                .flatMap(this::fetchPokemon);
    }

    public Mono<PokemonCharacterInfo> fetchPokemon(String id) {

        System.out.println("=== START executed time for id: " + id +" ===");
        System.out.println(System.currentTimeMillis());
        System.out.println("=== END executed time for id: " + id +" ===");

        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("api/v2/pokemon/{id}")
                        .build(id))
                .retrieve()
                .bodyToMono(PokemonCharacterInfo.class);
    }
}