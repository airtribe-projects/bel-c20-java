package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


// Objects of car will be made beans
// Beans can be created in three ways
// XML Based configuration
// Java Based Configuration
// Annotation Based Configuration -> SPRING BOOT\



@Component
public class Car {

  @Value("${car.carType}")
  private String carType;

  @Value("${car.carModel}")
  private String carModel;

  @Autowired
  private Engine engine; // has-a -> composition or aggregation

  public Car(String carType, String carModel, Engine engine) {
    this.carType = carType;
    this.carModel = carModel;
    this.engine = engine;
  }

  public Car() {

  }

  public String getCarType() {
    return carType;
  }

  public void setCarType(String carType) {
    this.carType = carType;
  }

  public String getCarModel() {
    return carModel;
  }

  public void setCarModel(String carModel) {
    this.carModel = carModel;
  }

  public Engine getEngine() {
    return engine;
  }

  public void setEngine(Engine engine) {
    this.engine = engine;
  }

  public void startCar() {
    engine.startEngine();
    System.out.println("Car of type " + carType + " and model " + carModel + " is starting.");
  }
}
