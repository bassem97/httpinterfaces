package com.spring6.httpinterfaces;

import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.annotation.*;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;

@SpringBootApplication
public class HttpinterfacesApplication {

	public static void main(String[] args) {
		SpringApplication.run(HttpinterfacesApplication.class, args);
	}

	@Bean
	TodoClient todoClient() {
		WebClient webClient = WebClient.builder()
				.baseUrl("https://jsonplaceholder.typicode.com")
				.build();
		HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(new WebClientAdapter(webClient)).build();
		return factory.createClient(TodoClient.class);
	}

	@Bean
	ApplicationRunner applicationRunner (TodoClient todoClient) {
		return args -> {
			System.out.println(todoClient.todos());
			Todo fix_buugs = todoClient.create(new Todo(null, "fix buugs", false, 1L));
			System.out.println(fix_buugs);
			System.out.println(todoClient.get(5L));

		};
	}

//	{
//		"userId": 1,
//			"id": 1,
//			"title": "delectus aut autem",
//			"completed": false
//	}
}
@HttpExchange("/todos/")
interface TodoClient{

	@GetExchange
	List<Todo> todos();

	@PostExchange
	Todo create(@RequestBody Todo todo);

	@GetExchange("/{id}")
	ResponseEntity<Todo> get(@PathVariable("id") Long id);

}



record Todo(Long id, String title, boolean completed,Long userId) {
}
