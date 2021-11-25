package com.practice.springwebflux;

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
    StepVerifier.create(flux)
        .expectNext("SpringBoot", "SpringData", "SpringWebFlux")
        .verifyComplete();
  }
}
