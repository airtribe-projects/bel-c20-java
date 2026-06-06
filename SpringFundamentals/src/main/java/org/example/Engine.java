package org.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


public class Engine {

  private String engineType;

  private String enginePower;

  public Engine(String engineType, String enginePower) {
    this.engineType = engineType;
    this.enginePower = enginePower;
  }

  public Engine() {

  }

  public String getEngineType() {
    return engineType;
  }

  public void setEngineType(String engineType) {
    this.engineType = engineType;
  }

  public String getEnginePower() {
    return enginePower;
  }

  public void setEnginePower(String enginePower) {
    this.enginePower = enginePower;
  }

  public void startEngine() {
    System.out.println("Engine of type " + engineType + " with power " + enginePower + " is starting.");
  }
}
