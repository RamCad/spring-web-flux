package com.practice.springwebflux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import lombok.var;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FluxAndMonoService {

  public static void main(String[] args) {
    FluxAndMonoService fluxAndMonoService = new FluxAndMonoService();
    //Flux - deals with multiple elements
    //Once subscribed - sent in form of stream
    // IterableSubscription
    fluxAndMonoService.techStackFlux()
        .subscribe(tech -> System.out.println(tech));
    System.out.println(fluxAndMonoService.techStackFlux().collectList().block());
    //Mono - deals with 1 element
    // ScalarSubscription
    fluxAndMonoService.techStackMono().subscribe(tech -> System.out.println(tech));
    System.out.println(fluxAndMonoService.techStackMono().block());
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
   * Flattens
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
   * more processing time than flatMap
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

  /**
   * Use flatMap if transformation returns a Mono
   * if it involves making REST API call
   * or it can be done asynchronously
   */
  public Mono<List<String>> techStackMonoFlatMap() {
    return Mono.just("SpringWebFlux")
        .flatMap(this::splitMono)
        .log();
  }

  /**
   * flatMapMany() - similar to flatMap()
   * When Mono transformation logic returns Flux
   */
  public Flux<String> techStackMonoFlatMapMany() {
    return Mono.just("SpringWebFlux")
        .flatMapMany(this::splitString)
        .log();
  }

  /**
   * transform()
   * Accepts Function Functional Interface
   * * i/p - Publisher
   * * o/p - Publisher
   */
  public Flux<String> stackFluxTransform() {
    Function<Flux<String>, Flux<String>> filterMap = str -> str.map(String::toUpperCase)
        .filter(s -> s.length() > 3);
    return Flux.fromIterable(Arrays.asList("Spring", "Boot"))
        .transform(filterMap)
        .flatMap(this::splitString)
        .log();
  }

  /**
   * combine different reactive streams - concat() & concatWith()
   * Subscribes in sequence
   * concat() - static method in Flux
   * concatWith() - instance method in Flux and Mono
   */
  public Flux<String> combineConcat() {
    var flux1 = Flux.just("A", "B", "C");
    var flux2 = Flux.just("D", "E", "F");

    return Flux.concat(flux1, flux2).log();
  }

  public Flux<String> combineFluxConcatWith() {
    var flux1 = Flux.just("A", "B", "C");
    var flux2 = Flux.just("D", "E", "F");

    return flux1.concatWith(flux2).log();
  }

  public Flux<String> combineMonoConcatWith() {
    var mono1 = Mono.just("A");
    var mono2 = Mono.just("D");

    return mono1.concatWith(mono2).log();
  }

  /**
   * combine two publishers into one - merge() & mergeWith()
   * both publishers are subscribed at same time
   * merge() - static method in Flux
   * mergeWith() - instance method in Flux and Mono
   */
  public Flux<String> combineMerge() {
    var flux1 = Flux.just("A", "B", "C")
        .delayElements(Duration.ofMillis(100));
    var flux2 = Flux.just("D", "E", "F")
        .delayElements(Duration.ofMillis(125));

    //AD, BE, CF
    return Flux.merge(flux1, flux2).log();
  }

  public Flux<String> combineFluxMergeWith() {
    var flux1 = Flux.just("A", "B", "C")
        .delayElements(Duration.ofMillis(100));
    var flux2 = Flux.just("D", "E", "F")
        .delayElements(Duration.ofMillis(125));

    //AD, BE, CF
    return flux1.mergeWith(flux2).log();
  }

  public Flux<String> combineMonoMergeWith() {
    var mono1 = Mono.just("A");
    var mono2 = Mono.just("D");

    //A, D
    return mono1.mergeWith(mono2).log();
  }

  /**
   * merge publishers using - mergeSequential() - static method in Flux
   * merge happens in sequence
   */
  public Flux<String> combineMergeSequential() {
    var flux1 = Flux.just("A", "B", "C")
        .delayElements(Duration.ofMillis(100));
    var flux2 = Flux.just("D", "E", "F")
        .delayElements(Duration.ofMillis(125));

    //A, B, C, D, E, F
    return Flux.mergeSequential(flux1, flux2).log();
  }

  /**
   * merge publishers using - zip() and zipWith()
   * publishers are subscribed eagerly
   * waits for all publishers involved in transformation to emit one element
   * zip() - 3 args - 3rd arg is combinator lambda - static method in Flux
   *       - merge upto 2 to 8
   * zipWith() - instance method in Flux and Mono
   */
  public Flux<String> combineZip() {
    var flux1 = Flux.just("A", "B", "C");
    var flux2 = Flux.just("D", "E", "F");

    //AD, BE, CF
    return Flux.zip(flux1, flux2, (f1, f2) -> f1 + f2)
        .log();
  }

  public Flux<String> combineZip4() {
    var flux1 = Flux.just("A", "B", "C");
    var flux2 = Flux.just("D", "E", "F");
    var flux3 = Flux.just("1", "2", "3");
    var flux4 = Flux.just("4", "5", "6");

    //AD14, BE25, CF36
    return Flux.zip(flux1, flux2, flux3, flux4)
        .map(tuple -> tuple.getT1()+tuple.getT2()+tuple.getT3()+tuple.getT4())
        .log();
  }

  public Flux<String> combineZipWith() {
    var flux1 = Flux.just("A", "B", "C");
    var flux2 = Flux.just("D", "E", "F");

    //AD, BE, CF
    return flux1.zipWith(flux2, (t1, t2) -> t1+t2)
        .log();
  }

  public Mono<String> combineMonoZipWith() {
    var mono1 = Mono.just("A");
    var mono2 = Mono.just("D");

    //AD
    return mono1.zipWith(mono2)
        .map(t -> t.getT1()+t.getT2())
        .log();
  }


  public Mono<String> techStackMono() {
    return Mono.just("SpringWebFlux").log();
  }

  public Flux<String> techStackFlux() {
    //.log() - logs every event that happens b/w subscriber and publisher communication
    return Flux.fromIterable(Arrays.asList("SpringBoot", "SpringData", "SpringWebFlux")).log();
  }

  public Flux<String> splitString(String str) {
    var charArray = str.split("");
    return Flux.fromArray(charArray);
  }

  private Mono<List<String>> splitMono(String str) {
    String[] charArray = str.split("");
    List<String> charList = Arrays.asList(charArray);
    return Mono.just(charList);
  }
}
