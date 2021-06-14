package at.ac.fhcampuswien.newsanalyzer.ctrl;

import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.NewsApiBuilder;
import at.ac.fhcampuswien.newsapi.NewsApiException;
import at.ac.fhcampuswien.newsapi.beans.Article;
import at.ac.fhcampuswien.newsapi.beans.NewsResponse;
import at.ac.fhcampuswien.newsapi.beans.Source;
import at.ac.fhcampuswien.newsapi.enums.Category;
import at.ac.fhcampuswien.newsapi.enums.Country;
import at.ac.fhcampuswien.newsapi.enums.Endpoint;
import at.ac.fhcampuswien.newsapi.enums.Language;

import java.util.*;
import java.util.stream.Collectors;

public class Controller {

	public static final String APIKEY = "b49d2c8361c14279a17dcf13767377fe";  //TODO add your api key
	private String q;
	private NewsApi newsApi;
	public void process() {
		System.out.println("Start process");


		NewsResponse newsResponse;
		try{
			newsResponse = newsApi.getNews();
		}catch(NewsApiException e){
			System.out.println(e.getMessage());
			return;
		}
		if(newsResponse != null){
			List<Article> articles = newsResponse.getArticles();
			articles.stream().forEach(article -> System.out.println(article.toString()));
			System.out.println("");
			System.out.println("Number of articles:");
			System.out.println(getNumberOfArticles(articles));
			if(getNumberOfArticles(articles)>0){
				System.out.println("Most commmon publisher:");
				System.out.println(findMostCommonProvider(articles));
				System.out.println("Author with shortest name:");
				System.out.println(findAuthorWithShortestName(articles));
				System.out.println("Titles sorted by length and alphabet:");
				sortTitleByLengthAndAlphabet(articles).forEach((res)-> System.out.println(res));
			}


		}
		//TODO implement Error handling

		//TODO load the news based on the parameters

		//TODO implement methods for analysis


		System.out.println("End process");
	}
	public int getNumberOfArticles(List<Article> articles){
		return (int)articles.stream().count();
	}
	public String findMostCommonProvider(List<Article> articles){
		Set<String> providers = new HashSet<>();//A set only contains one occurance...
		//add returns true, if element is not yet contained in set...
		 return articles.stream()//Key value pairs stored via collect... key: provider name, value => number of published articles!
				.collect(Collectors.groupingBy(//Group by source name, and count the number of occurences! Collect to a map!
						(article) ->( article.getSource().getName()),Collectors.counting()))
				.entrySet().stream()
				.max((entry1,entry2)-> {//Find maximum, when comparing two entries >1 => means larger!
					if(entry1.getValue()> entry2.getValue()){//Comparision function, if entry 1 > entry 2 return 1
						return 1;//And thus keep this entry....
					}else{
						return -1;
					}
				}).get().getKey();//return the name of the provider, who published the most articles!
	}
	public String findAuthorWithShortestName(List<Article> articles){
		return articles.stream().
				min(Comparator.comparingInt(article -> article.getAuthor().length()))//Compare the length of authors names
				.get().getAuthor();
	}
	public List<String> sortTitleByLengthAndAlphabet(List<Article> articles){
			return articles.stream().sorted((a1,a2)->{
				int lengthComparison;
				lengthComparison=Integer.compare(a2.getTitle().length(),a1.getTitle().length());
				if(lengthComparison==0) {//when length is equal, Compare by Alphabet!
					return a1.getTitle().compareTo(a2.getTitle());
				}
				//if length was not equal => Comparison by length!
				return lengthComparison;
			}).map((article)->article.getTitle())//Just keep the titles...
			.collect(Collectors.toList());//and collect them to a List of string...
	}
	

	public Object getData() {
		
		return null;
	}
	public void setData(NewsApiBuilder newsApiBuild){
		this.newsApi=newsApiBuild.setApiKey(APIKEY).createNewsApi();
	}
}
