package com.example.demo;

import com.example.model.Book;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RefreshScope
@RestController
@SpringBootApplication
@EnableSwagger2
public class DemoApplication {

    @Value("${firebaseurl}")
    private String actBookUrl;// = "https://act-book.firebaseio.com/books.json";
    private final String NONE="";

    private List<Book> callFirebaseAPIwith(String param){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map<String, Book>> books = restTemplate.exchange(actBookUrl + param
                , HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Book>>() {});
        //ResponseEntity<String> response = restTemplate.getForEntity(actBookUrl + param, String.class);

        //return response.getBody();
        return new ArrayList<Book>(books.getBody().values());
    }

    @ApiOperation(value ="전체 도서를 조회(상태 무관)")
    @RequestMapping(value="/books", method= RequestMethod.GET)
    public List<Book> getAllBooks(){
        return callFirebaseAPIwith(NONE);
    }
    @ApiOperation(value ="도서명으로 검색하기(상태 무관)")
    @RequestMapping(value="/books/title/{title}", method= RequestMethod.GET)
    public List<Book> getAllBooksByTitle(@PathVariable String title){
        List<Book> tmpBooks = callFirebaseAPIwith(NONE);
        List<Book> books = new ArrayList<Book>();

        for (Book book: tmpBooks ) {
            if(book.getTitle().contains(title)){
                books.add(book);
            }
        }
        return books;
    }
    @ApiOperation(value ="상태별로 도서 조회 하기")
    @RequestMapping(value="/books/status/{status}", method= RequestMethod.GET)
    public List<Book> getBooksByStatus(@PathVariable String status){
        String param = "?orderBy=\"status\"&equalTo=\""+status+"\"";

        return callFirebaseAPIwith(param);
    }
    @ApiOperation(value ="상태와 도서명을 필수 조건으로 조회하기")
    @RequestMapping(value="/books/status/{status}/title/{title}", method= RequestMethod.GET)
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
    @ApiOperation(value ="전체 도서를 조회(삭제 제외)")
    @RequestMapping(value="/unDeletedBooks", method= RequestMethod.GET)
    public List<Book> getUnDeletedBooks(){
        List<Book> tmpBooks = callFirebaseAPIwith(NONE);
        List<Book> books = new ArrayList<Book>();

        for (Book book: tmpBooks ) {
            if(book.getStatus().equals("삭제") == false){
                books.add(book);
            }
        }
        return books;
    }
    @ApiOperation(value ="전체 도서를 도서명으로 조회(삭제 제외)")
    @RequestMapping(value="/unDeletedBooks/title/{title}", method= RequestMethod.GET)
    public List<Book> getUnDeletedBooksBy(@PathVariable String title){
        List<Book> tmpBooks = callFirebaseAPIwith(NONE);
        List<Book> books = new ArrayList<Book>();

        for (Book book: tmpBooks ) {
            if(book.getStatus().equals("삭제") == false){
                if(book.getTitle().contains(title)) {
                    books.add(book);
                }
            }
        }
        return books;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
