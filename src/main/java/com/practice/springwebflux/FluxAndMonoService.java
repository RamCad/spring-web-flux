package com.practice.springwebflux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import lombok.var;
import org.springframework.util.CollectionUtils;
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

  /**
   * Reactive streams are immutable
   */
  public Flux<String> techStackFluxImmutable() {
    var stack = Flux.fromIterable(Arrays.asList("SpringBoot", "SpringData", "SpringWebFlux")).log();
    stack.map(String::toUpperCase);
    return stack;
  }

  /**
   * flatMap() - transform one source to Flux of 1 or 1 to n elements
   * "Spring" -> Flux.just("S", "p", "r", "i", "n", "g")
   */
  public Flux<String> techStackFluxFlatMap() {
    return Flux.fromIterable(Arrays.asList("Spring", "Boot"))
        .flatMap(this::splitString)
        .log();
  }

  public Flux<String> techStackFluxFlatMapAsync() {
    return Flux.fromIterable(Arrays.asList("Spring", "Boot"))
        .flatMap(this::splitStringWithDelay)
        .log();
  }

  /**
   * similar to flatMap but preserves the sequence of reactive stream
   */
  public Flux<String> techStackFluxConcatMap() {
    return Flux.fromIterable(Arrays.asList("Spring", "Boot"))
        .concatMap(this::splitStringWithDelay)
        .log();
  }

  public Flux<String> splitStringWithDelay(String str) {
    var charArray = str.split("");
    var delay = new Random().nextInt(1000);
    return Flux.fromArray(charArray)
        .delayElements(Duration.ofMillis(delay));
  }

  public Flux<String> splitString(String str) {
    var charArray = str.split("");
    return Flux.fromArray(charArray);
  }

  public Mono<String> techStackMono() {
    return Mono.just("SpringWebFlux").log();
  }

  public Mono<List<String>> techStackMonoFlatMap() {
    return Mono.just("SpringWebFlux")
        .flatMap(this::splitMono)
        .log();
  }

  private Mono<List<String>> splitMono(String str) {
    String[] charArray = str.split("");
    List<String> charList = Arrays.asList(charArray);
    return Mono.just(charList);
  }
}
