package org.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Main {
  public static void main(String[] args) {
    // Spring container
    //ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
    ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
    Car bean1 = applicationContext.getBean(Car.class);
    bean1.startCar();
    System.out.println(bean1.hashCode());


    Engine engine1 = new Engine("Petrol", "200 HP");
    Car car1 = new Car("Sedan", "BMW", engine1);
    Car car2 = new Car("Sedan", "BMW", engine1);
    car1.startCar();
    car2.startCar();
    System.out.println(car1.hashCode());
    System.out.println(car2.hashCode());
  }
}