package org.airtribe.AsyncApiApplicationBELC20.service;

import java.time.Duration;
import java.util.List;
import org.airtribe.AsyncApiApplicationBELC20.entity.ProductResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class AsyncApiService {

  @Autowired
  private RestTemplate _restTemplate;


  @Autowired
  private WebClient _webClient;

  // RestTemplate
  // webClient
  public Mono<String> invokeHelloEndpoint() {
    System.out.println("Thread handling the request " + Thread.currentThread().getName());
     Mono<String> fetchedResult = _webClient.get().uri("http://localhost:3001/hello").retrieve().bodyToMono(String.class)
         .doOnSuccess(result -> {
           System.out.println("Thread handling the request " + Thread.currentThread().getName());
         });
    return fetchedResult;
  }

  public ProductResult fetchProductsSync() {
    System.out.println("Thread handling the request  "  + Thread.currentThread().getName());
    ProductResult result = _restTemplate.getForObject("https://dummyjson.com/products", ProductResult.class);
    return result;
  }

  public Mono<ProductResult> fetchProductsAsync() {
    System.out.println("Thread handling the request  "  + Thread.currentThread().getName());
    Mono<ProductResult> result =  _webClient.get().uri("https://dummyjson.com/products").retrieve().bodyToMono(ProductResult.class)
        .doOnSuccess(products -> {
          System.out.println("Thread handling the request " + Thread.currentThread().getName());
          System.out.println("Response received " + products);
        }).doOnError(error -> {
          System.out.println("Error " + error);
        });

    for (int i=0;i<1000;i++) {
      System.out.println("Doing some work in the background");
    }

    return result;
  }

  public Mono<List<ProductResult>> fetchProductsAsyncParallelAll() {
    Mono<ProductResult> result1 = _webClient.get().uri("https://dummyjson.com/products").retrieve().bodyToMono(ProductResult.class)
        .doOnSuccess(products -> {
          System.out.println("Thread handling the request " + Thread.currentThread().getName());
          System.out.println("Response received " + products);
        }).doOnError(error -> {
          System.out.println("Error " + error);
        }).doOnSubscribe(subscription -> {
          System.out.println("Request 1 subscribed on thread " + Thread.currentThread().getName());
        });

    Mono<ProductResult> result2 = _webClient.get().uri("https://dummyjson.com/products").retrieve().bodyToMono(ProductResult.class)
        .doOnSuccess(products -> {
          System.out.println("Thread handling the request " + Thread.currentThread().getName());
          System.out.println("Response received " + products);
        }).doOnError(error -> {
          System.out.println("Error " + error);
        }).doOnSubscribe(subscription -> {
          System.out.println("Request 2 subscribed on thread " + Thread.currentThread().getName());
        });;

    Mono<ProductResult> result3 = _webClient.get().uri("https://dummyjson.comfghghghjghghghghghghgh/products").retrieve().bodyToMono(ProductResult.class)
        .doOnSuccess(products -> {
          System.out.println("Thread handling the request " + Thread.currentThread().getName());
          System.out.println("Response received " + products);
        }).doOnError(error -> {
          System.out.println("Error " + error);
        }).doOnSubscribe(subscription -> {
          System.out.println("Request 3 subscribed on thread " + Thread.currentThread().getName());
        });;



    Mono<List<ProductResult>> productResult =  Mono.zip(result1, result2, result3).map(tuple -> List.of(tuple.getT1(), tuple.getT2(), tuple.getT3())).doOnSuccess(
        result -> {
          System.out.println("All requests completed successfully " + Thread.currentThread().getName());
        }
    ).doOnError(error -> {
      System.out.println("Parallel API invocation failed " + error);
    });

    System.out.println("Returning the mono from the API call");
    return productResult;
  }

  public Mono<ProductResult> fetchProductsAsyncParallelFastest() {
    Mono<ProductResult> result1 = _webClient.get().uri("https://dummyjson.com/products").retrieve().bodyToMono(ProductResult.class)
        .doOnSuccess(products -> {
          System.out.println("Thread handling the request " + Thread.currentThread().getName());
          System.out.println("Response received " + products);
        }).doOnError(error -> {
          System.out.println("Error " + error);
        }).doOnSubscribe(subscription -> {
          System.out.println("Request 1 subscribed on thread " + Thread.currentThread().getName());
        });

    Mono<ProductResult> result2 = _webClient.get().uri("https://dummyjson.com/products").retrieve().bodyToMono(ProductResult.class)
        .doOnSuccess(products -> {
          System.out.println("Thread handling the request " + Thread.currentThread().getName());
          System.out.println("Response received " + products);
        }).doOnError(error -> {
          System.out.println("Error " + error);
        }).doOnSubscribe(subscription -> {
          System.out.println("Request 2 subscribed on thread " + Thread.currentThread().getName());
        });;

    Mono<ProductResult> result3 = _webClient.get().uri("https://dummyjson.com/products").retrieve().bodyToMono(ProductResult.class)
        .doOnSuccess(products -> {
          System.out.println("Thread handling the request " + Thread.currentThread().getName());
          System.out.println("Response received " + products);
        }).doOnError(error -> {
          System.out.println("Error " + error);
        }).doOnSubscribe(subscription -> {
          System.out.println("Request 3 subscribed on thread " + Thread.currentThread().getName());
        });;

Mono<ProductResult> productResult = Mono.first(result1, result2, result3).doOnSuccess(myResult -> {
  System.out.println("Thread handling the response " + Thread.currentThread().getName());
});

    System.out.println("Returning the mono from the API call");
    return productResult;

  }

  public List<ProductResult> fetchProductsChainedSync() {

    // postman1
    ProductResult result1 = _restTemplate.getForObject("https://dummyjson.com/products", ProductResult.class);

    // postman2
    ProductResult resut2 =  _webClient.get().uri("https://dummyjson.com/products").retrieve().bodyToMono(ProductResult.class).block();

    // postman3
    ProductResult resut3 =  _webClient.get().uri("https://dummyjson.com/products").retrieve().bodyToMono(ProductResult.class).block();

    System.out.println("Thread handling all the requests : " + Thread.currentThread().getName());
    return List.of(result1, resut2, resut3);
  }

  public Mono<ProductResult> singleApiCall(int start, int end) {
    return _webClient.get().uri("https://dummyjson.com/products").retrieve().bodyToMono(ProductResult.class)
        .doOnSuccess(products -> {
          System.out.println("Thread handling the request " + Thread.currentThread().getName());
          System.out.println("Response received " + products);
        }).doOnError(error -> {
          System.out.println("Error " + error);
        }).doOnSubscribe(subscription -> {
          System.out.println("Request 1 subscribed on thread " + Thread.currentThread().getName());
        });
  }

  public Mono<List<ProductResult>> fetchProductsChainedAsync() {
    System.out.println("Thread handling the request  "  + Thread.currentThread().getName());
    Mono<List<ProductResult>> finalResult = singleApiCall(1, 10).flatMap(result1 -> {
          return singleApiCall(11, 20 ).flatMap(result2 -> {
                return singleApiCall(21, 30).map(result3 -> List.of(result1, result2, result3));
              });
        });

    return finalResult;
  }

  public Flux<ProductResult> fetchProductsStream() {
    return Flux.interval(Duration.ofSeconds(5)).take(20).flatMap(i -> {
      return _webClient.get().uri("https://dummyjson.com/products").retrieve().bodyToMono(ProductResult.class)
          .doOnSuccess(products -> {
            System.out.println("Thread handling the request " + Thread.currentThread().getName());
            System.out.println("Response received " + products);
          }).doOnError(error -> {
            System.out.println("Error " + error);
          }).doOnSubscribe(subscription -> {
            System.out.println("Request 1 subscribed on thread " + Thread.currentThread().getName());
          });
    });
  }
}
