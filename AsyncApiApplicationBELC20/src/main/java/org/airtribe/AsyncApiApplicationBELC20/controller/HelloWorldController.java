package org.airtribe.AsyncApiApplicationBELC20.controller;

import java.util.ArrayList;
import java.util.List;
import org.airtribe.AsyncApiApplicationBELC20.entity.ProductResult;
import org.airtribe.AsyncApiApplicationBELC20.service.AsyncApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
public class HelloWorldController {
  @Autowired
  private AsyncApiService _asyncApiService;

  private List<byte[]> bytesStored = new ArrayList<>();


  @GetMapping("/hello")
  public String helloWorld() {
    System.out.println("Thread handling the request " + Thread.currentThread().getName());
    // SYNCRHONOUS CODE
    for (int i=0;i<100000000L;i++) {
      double result = Math.sqrt(i+200 * 3);
    }
    return "Hello world!";
  }

  @GetMapping("/simulateMemoryLeak")
  public String simulateMemoryLeak() {
    byte[] temp = new byte[10 * 1024 * 1024]; // Allocate memory
    bytesStored.add(temp); // Store reference to prevent garbage collection//
    return "Memory leak simulated. Current stored chunks: " + bytesStored.size();
  }

  @GetMapping("/hello2")
  public Mono<String> helloWorld2() {
    System.out.println("Thread handling the request " + Thread.currentThread().getName());
    return _asyncApiService.invokeHelloEndpoint();
  }

  @GetMapping("/productsSync")
  public ProductResult fetchProductsSync() {
    System.out.println("Thread handling the request " + Thread.currentThread().getName());
    return _asyncApiService.fetchProductsSync();
  }

  @GetMapping("/productsAsync")
  // MONO / FLUX / FUTURE / COMPLETABLE FUTURE -> Promise of future
  public Mono<ProductResult> fetchProductsAsync() {
    System.out.println("Thread handling the request " + Thread.currentThread().getName());
    return _asyncApiService.fetchProductsAsync();
  }


  @GetMapping("/productsAsyncParallelAll")
  public Mono<List<ProductResult>> fetchProductsParallelAsyncAll() {
    System.out.println("Thread handling the request " + Thread.currentThread().getName()); // tomcat server thread
    return _asyncApiService.fetchProductsAsyncParallelAll();
  }

  @GetMapping("/productsAsyncParallelFastest")
  public Mono<ProductResult> fetchProductsParallelAsyncFastest() {
    System.out.println("Thread handling the request " + Thread.currentThread().getName()); // tomcat server thread
    return _asyncApiService.fetchProductsAsyncParallelFastest();
  }

  @GetMapping("/productsChainedSync")
  public List<ProductResult> fetchProductsChainedSync() {
    System.out.println("Thread handling the request " + Thread.currentThread().getName()); // tomcat server thread
    return _asyncApiService.fetchProductsChainedSync();
  }

  @GetMapping("/productsChainedAsync")
  public Mono<List<ProductResult>> fetchProductsChainedAsync() {
    System.out.println("Thread handling the request " + Thread.currentThread().getName()); // tomcat server thread
    return _asyncApiService.fetchProductsChainedAsync();
  }

  @GetMapping(value = "/productsFluxStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<ProductResult> getProductsFluxStream() {
    System.out.println("Thread handling the request " + Thread.currentThread().getName()); // tomcat server thread
    return _asyncApiService.fetchProductsStream();
  }

  // API Invocation from within an API
  // Synchronously
  // Asynchronously
  // RestTemplate -> Sychrnous API calls
  // WebClient  -> Sync & async API calls
}
// TOMCAT SERVER THREADS -> THREADPOOL