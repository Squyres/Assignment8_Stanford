
import java.io.*;
import java.util.*;

public class FlightPlanner {

	private static Scanner scan = new Scanner(System.in);
	private static String startCity;
	private static String currentCity;
	private static HashMap<String, ArrayList<String>> flights;
	private static ArrayList<String> cities;
	private static ArrayList<String> route;

	public static void main(String[] args) {
		flights = new HashMap<String, ArrayList<String>>();
		cities = new ArrayList<String>();
		route = new ArrayList<String>();
		loadDB();
		System.out.println("Welcome to Flight Planner!");
		System.out.println("Here's a list of all the cities in our database:");
		printCityList(cities);
		System.out.println("Let's plan a round-trip route!");
		System.out.print("Enter the starting city: ");
		startCity = scan.nextLine();
		route.add(startCity);
		currentCity = startCity;
		while (true) {
			String nextCity = getNextCity(currentCity);
			route.add(nextCity);
			if (nextCity.equals(startCity))
				break;
			currentCity = nextCity;
		}
		printRoute(route);
	}

	private static void loadDB() {
		BufferedReader reader = null;
		FileReader fileReader = null;
		try {
			fileReader = new FileReader("flights.txt");
			reader = new BufferedReader(fileReader);
			String currentLine;
			while (true) {
				currentLine = reader.readLine();
				if (currentLine == null)
					break;
				if (currentLine.length() != 0) {
					readFlightEntry(currentLine);
				}
			}
		} catch (IOException x) {
			System.out.println("Error loading file");
		}
	}

	private static void readFlightEntry(String line) throws IOException {
		int arrow = line.indexOf("->");
		if (arrow == -1) {
			throw new IOException("Illegal flight entry " + line);
		}
		String fromCity = line.substring(0, arrow).trim();
		String toCity = line.substring(arrow + 2).trim();
		defineCity(fromCity);
		defineCity(toCity);
		getDestinations(fromCity).add(toCity);
	}

	private static void defineCity(String cityName) {
		if (!cities.contains(cityName)) {
			cities.add(cityName);
			flights.put(cityName, new ArrayList<String>());
		}
	}

	private static void printCityList(ArrayList<String> cityList) {
		for (int i = 0; i < cityList.size(); i++) {
			String city = cityList.get(i);
			System.out.println(" " + city);
		}
	}

	private static ArrayList<String> getDestinations(String fromCity) {
		return flights.get(fromCity);
	}

	private static String getNextCity(String city) {
		ArrayList<String> destinations = getDestinations(city);
		String nextCity = null;
		while (true) {
			System.out.println("From " + city + " you can fly directly to:");
			printCityList(destinations);
			System.out.println("Where do you want to go from " + city + "? ");
			nextCity = scan.nextLine();
			if (destinations.contains(nextCity))
				break;
			System.out.println("You can't get to that city by a direct flight.");
		}
		return nextCity;
	}

	private static void printRoute(ArrayList<String> route) {
		System.out.println("The route you've chosen is: ");
		for (int i = 0; i < route.size(); i++) {
			if (i > 0)
				System.out.print(" -> ");
			System.out.print(route.get(i));
		}
		System.out.println();
	}
}
