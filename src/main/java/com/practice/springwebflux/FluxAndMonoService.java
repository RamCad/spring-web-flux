package com.practice.springwebflux;

import java.util.Arrays;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FluxAndMonoService {

  public static void main(String[] args) {
    FluxAndMonoService fluxAndMonoService = new FluxAndMonoService();
    //Flux
    fluxAndMonoService.techStackFlux()
        .subscribe(tech -> System.out.println(tech));
    System.out.println(fluxAndMonoService.techStackFlux().collectList().block());
    //Mono
    fluxAndMonoService.techStackMono().subscribe(tech -> System.out.println(tech));
    System.out.println(fluxAndMonoService.techStackMono().block());
  }

  public Flux<String> techStackFlux() {
    return Flux.fromIterable(Arrays.asList("SpringBoot", "SpringData", "SpringWebFlux")).log();
  }

  public Mono<String> techStackMono() {
    return Mono.just("SpringWebFlux").log();
  }
}
