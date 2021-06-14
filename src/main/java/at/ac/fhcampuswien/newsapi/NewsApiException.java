package at.ac.fhcampuswien.newsapi;

public class NewsApiException extends Exception{
    NewsApiException(String message){
        super(message);
    }
}
