package com.practice.springwebflux;

import java.util.Arrays;
import lombok.var;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class FluxAndMonoServiceTest {

  @InjectMocks
  private FluxAndMonoService fluxAndMonoService;

  @Test
  public void testTechStackFlux() {
    var flux = fluxAndMonoService.techStackFlux();
    // create takes care of invoking subscribe call
    StepVerifier.create(flux)
//        .expectNext("SpringBoot", "SpringData", "SpringWebFlux")
        .expectNextCount(3)
        .verifyComplete();

  }

  @Test
  public void testTechStackFluxImmutable() {
    var flux = fluxAndMonoService.techStackFluxImmutable();
    StepVerifier.create(flux)
        .expectNext("SpringBoot", "SpringData", "SpringWebFlux")
        // doesn't work as reactive streams are immutable
//        .expectNext("SPRINGBOOT", "SPRINGDATA", "SPRINGWEBFLUX")
        .verifyComplete();
  }

  @Test
  public void testTechStackFluxFlatMap() {
    var flux = fluxAndMonoService.techStackFluxFlatMap();
    StepVerifier.create(flux)
        .expectNext("S", "p", "r", "i", "n", "g", "B", "o", "o", "t")
        .verifyComplete();
  }

  @Test
  public void testTechStackFluxFlatMapWithAsync() {
    var flux = fluxAndMonoService.techStackFluxFlatMapAsync();
    StepVerifier.create(flux)
//        .expectNext("S", "p", "r", "i", "n", "g", "B", "o", "o", "t")
        .expectNextCount(10)
        .verifyComplete();
  }

  @Test
  public void testTechStackFluxConcatMap() {
    var flux = fluxAndMonoService.techStackFluxConcatMap();
    StepVerifier.create(flux)
        .expectNext("S", "p", "r", "i", "n", "g", "B", "o", "o", "t")
//        .expectNextCount(10)
        .verifyComplete();
  }

  @Test
  public void testTechStackMonoFlatMap() {
    var flux = fluxAndMonoService.techStackMonoFlatMap();
    StepVerifier.create(flux)
        .expectNext(Arrays.asList("S", "p", "r", "i", "n", "g", "W", "e", "b", "F", "l", "u", "x"))
        .verifyComplete();
  }

  @Test
  void techStackMonoFlatMapMany() {
    var flux = fluxAndMonoService.techStackMonoFlatMapMany();
    StepVerifier.create(flux)
        .expectNext("S", "p", "r", "i", "n", "g", "W", "e", "b", "F", "l", "u", "x")
        .verifyComplete();
  }

  @Test
  void stackFluxTransform() {
    var flux = fluxAndMonoService.stackFluxTransform();
    StepVerifier.create(flux)
        .expectNext("S", "p", "r", "i", "n", "g", "B", "o", "o", "t")
        .verifyComplete();
  }

  @Test
  void stackCombineConcat() {
    var flux = fluxAndMonoService.combineConcat();
    StepVerifier.create(flux)
        .expectNext("A", "B", "C", "D", "E", "F")
        .verifyComplete();
  }
}
