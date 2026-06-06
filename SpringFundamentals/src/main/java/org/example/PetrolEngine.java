package org.example;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@Profile("petrolEngine")
public class PetrolEngine extends Engine{
  public PetrolEngine(@Value("${petrolEngine.engineType}")String engineType,
      @Value("${petrolEngine.enginePower}") String enginePower) {
    super(engineType, enginePower);
  }
}
