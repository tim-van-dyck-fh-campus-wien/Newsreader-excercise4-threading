package at.ac.fhcampuswien.newsanalyzer.ui;


import at.ac.fhcampuswien.newsanalyzer.ctrl.Controller;
import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.NewsApiBuilder;
import at.ac.fhcampuswien.newsapi.enums.Category;
import at.ac.fhcampuswien.newsapi.enums.Country;
import at.ac.fhcampuswien.newsapi.enums.Endpoint;
import at.ac.fhcampuswien.newsapi.enums.Language;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserInterface 
{
	private Controller ctrl = new Controller();

	public void getDataFromCtrl1(){
		NewsApiBuilder api = new NewsApiBuilder()
				.setQ("Bitcoin")
				.setEndPoint(Endpoint.EVERYTHING)
				.setFrom("2021-06-05");
		ctrl.setData(api);
		ctrl.process();
	}

	public void getDataFromCtrl2(){
	NewsApiBuilder api = new NewsApiBuilder()
			.setQ("Fußball")
			.setEndPoint(Endpoint.EVERYTHING);
	ctrl.setData(api);
	ctrl.process();

	}

	public void getDataFromCtrl3(){
		NewsApiBuilder api = new NewsApiBuilder()
				.setQ("Dogecoin")
				.setEndPoint(Endpoint.EVERYTHING);
		ctrl.setData(api);
		ctrl.process();
	}
	
	public void getDataForCustomInput() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try{
			System.out.println("Please enter the topic");
			String q = reader.readLine();
			NewsApiBuilder api = new NewsApiBuilder().setQ(q).setEndPoint(Endpoint.EVERYTHING);
			ctrl.setData(api);
			ctrl.process();
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}


	public void start() {
		Menu<Runnable> menu = new Menu<>("User Interface");
		menu.setTitle("Wählen Sie aus:");
		menu.insert("a", "Bitcoin News from the past month", this::getDataFromCtrl1);
		menu.insert("b", "Football news headlines", this::getDataFromCtrl2);
		menu.insert("c", "Get Dogecoin news", this::getDataFromCtrl3);
		menu.insert("d", "Search for a topic:",this::getDataForCustomInput);
		menu.insert("q", "Quit", null);
		Runnable choice;
		while ((choice = menu.exec()) != null) {
			 choice.run();
		}
		System.out.println("Program finished");
	}


    protected String readLine() {
		String value = "\0";
		BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
		try {
			value = inReader.readLine();
        } catch (IOException ignored) {
		}
		return value.trim();
	}

	protected Double readDouble(int lowerlimit, int upperlimit) 	{
		Double number = null;
        while (number == null) {
			String str = this.readLine();
			try {
				number = Double.parseDouble(str);
            } catch (NumberFormatException e) {
                number = null;
				System.out.println("Please enter a valid number:");
				continue;
			}
            if (number < lowerlimit) {
				System.out.println("Please enter a higher number:");
                number = null;
            } else if (number > upperlimit) {
				System.out.println("Please enter a lower number:");
                number = null;
			}
		}
		return number;
	}
}
