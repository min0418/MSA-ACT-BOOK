package com.example.demo;

import com.example.model.Book;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RefreshScope
@RestController
@SpringBootApplication
public class DemoApplication {

    //@Value("${firebaseurl}")
    private final String actBookUrl = "https://act-book.firebaseio.com/books.json";


    private List<Book> callFirebaseAPIwith(String param){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map<String, Book>> books = restTemplate.exchange(actBookUrl + param
                , HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Book>>() {});
        //ResponseEntity<String> response = restTemplate.getForEntity(actBookUrl + param, String.class);

        //return response.getBody();
        return new ArrayList<Book>(books.getBody().values());
    }

    @RequestMapping("/books")
    public List<Book> getAllBooks(){
        return callFirebaseAPIwith("");
    }

    @RequestMapping("/books/status/{status}")
    public List<Book> getBooksByStatus(@PathVariable String status){
        String param = "?orderBy=\"status\"&equalTo=\""+status+"\"";

        return callFirebaseAPIwith(param);
    }

    @RequestMapping("/books/status/{status}/title/{title}")
    public List<Book> getBooksByTitle(@PathVariable String status, @PathVariable String title){
        String param = "?orderBy=\"status\"&equalTo=\""+status+"\"";
        List<Book> tmpBooks = callFirebaseAPIwith(param);
        List<Book> books = new ArrayList<Book>();

        for (Book book: tmpBooks ) {
            if(book.getTitle().contains(title)){
                books.add(book);
            }
        }
        return books;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
