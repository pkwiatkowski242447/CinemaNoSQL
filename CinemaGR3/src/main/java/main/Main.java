package main;

import model.Client;
import model.Movie;
import model.ScreeningRoom;
import model.Ticket;
import model.exceptions.repository_exceptions.MongoConfigNotFoundException;
import model.managers.*;
import model.repositories.implementations.ClientRepository;
import model.repositories.implementations.MovieRepository;
import model.repositories.implementations.ScreeningRoomRepository;
import model.repositories.implementations.TicketRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) throws MongoConfigNotFoundException {
        String databaseName = "cinema";
        ClientRepository clientRepository = new ClientRepository(databaseName);
        ScreeningRoomRepository screeningRoomRepository = new ScreeningRoomRepository(databaseName);
        MovieRepository movieRepository = new MovieRepository(databaseName);
        TicketRepository ticketRepository = new TicketRepository(databaseName);

        ClientManager clientManager = new ClientManager(clientRepository);
        ScreeningRoomManager screeningRoomManager = new ScreeningRoomManager(screeningRoomRepository);
        MovieManager movieManager = new MovieManager(movieRepository);
        TicketManager ticketManager = new TicketManager(ticketRepository);

        Date movieTimeNo1 = new Calendar.Builder().setDate(2023, 10, 1).setTimeOfDay(10, 15, 0).build().getTime();
        Date movieTimeNo2 = new Calendar.Builder().setDate(2023, 10, 8).setTimeOfDay(16, 13, 0).build().getTime();
        Date movieTimeNo3 = new Calendar.Builder().setDate(2023, 10, 16).setTimeOfDay(20, 5, 0).build().getTime();

        Date reservationTimeNo1 = new Calendar.Builder().setDate(2023, 9, 29).setTimeOfDay(12, 37, 0).build().getTime();
        Date reservationTimeNo2 = new Calendar.Builder().setDate(2023, 9, 31).setTimeOfDay(14, 15, 0).build().getTime();
        Date reservationTimeNo3 = new Calendar.Builder().setDate(2023, 10, 11).setTimeOfDay(18, 7, 15).build().getTime();

        String clientNo1Name = "John";
        String clientNo1Surname = "Smith";
        int clientNo1Age = 21;
        Client clientNo1 = clientManager.register(clientNo1Name, clientNo1Surname, clientNo1Age);
        String clientNo2Name = "Mary";
        String clientNo2Surname = "Jane";
        int clientNo2Age = 18;
        Client clientNo2 = clientManager.register(clientNo2Name, clientNo2Surname, clientNo2Age);
        String clientNo3Name = "Vincent";
        String clientNo3Surname = "Vega";
        int clientNo3Age = 40;
        Client clientNo3 = clientManager.register(clientNo3Name, clientNo3Surname, clientNo3Age);

        int screeningRoomNo1Floor = 1;
        int screeningRoomNo1Number = 10;
        int screeningRoomNo1NumberOfAvailSeats = 45;
        ScreeningRoom screeningRoomNo1 = screeningRoomManager.register(screeningRoomNo1Floor, screeningRoomNo1Number, screeningRoomNo1NumberOfAvailSeats);
        int screeningRoomNo2Floor = 2;
        int screeningRoomNo2Number = 5;
        int screeningRoomNo2NumberOfAvailSeats = 90;
        ScreeningRoom screeningRoomNo2 = screeningRoomManager.register(screeningRoomNo2Floor, screeningRoomNo2Number, screeningRoomNo2NumberOfAvailSeats);
        int screeningRoomNo3Floor = 0;
        int screeningRoomNo3Number = 19;
        int screeningRoomNo3NumberOfAvailSeats = 120;
        ScreeningRoom screeningRoomNo3 = screeningRoomManager.register(screeningRoomNo3Floor, screeningRoomNo3Number, screeningRoomNo3NumberOfAvailSeats);

        String movieNo1Title = "Harry Potter and The Goblet of Fire";
        double movieNo1BasePrice = 20.05;
        String movieNo2Title = "The Da Vinci Code";
        double movieNo2BasePrice = 40.5;
        String movieNo3Title = "A Space Odyssey";
        double movieNo3BasePrice = 59.99;

        Movie movieNo1 = movieManager.register(movieNo1Title, movieNo1BasePrice, screeningRoomNo1);
        Movie movieNo2 = movieManager.register(movieNo2Title, movieNo2BasePrice, screeningRoomNo2);
        Movie movieNo3 = movieManager.register(movieNo3Title, movieNo3BasePrice, screeningRoomNo3);

        ticketManager.register(movieTimeNo1, reservationTimeNo1, movieNo1, clientNo1, "normal");
        ticketManager.register(movieTimeNo2, reservationTimeNo2, movieNo2, clientNo2, "normal");
        ticketManager.register(movieTimeNo3, reservationTimeNo3, movieNo3, clientNo3, "normal");

        List<Ticket> ticketList = ticketManager.getTicketRepository().findAll();
        int numOfTicketsBefore = ticketList.size();
        for (Ticket ticket : ticketList) {
            System.out.println(ticket.getTicketInfo());
        }

        for (Ticket ticket : ticketList) {
            ticketManager.getTicketRepository().delete(ticket);
        }

        int numOfTicketsAfter = ticketManager.getTicketRepository().findAll().size();

        System.out.println("Number of tickets before: " + numOfTicketsBefore);
        System.out.println("Number of tickets after: " + numOfTicketsAfter);

        List<Client> listOfClients = clientManager.getClientRepository().findAll();
        for (Client client : listOfClients) {
            System.out.println(client.getClientInfo());
        }

        List<Movie> listOfMovies = movieManager.getMovieRepository().findAll();
        for (Movie movie : listOfMovies) {
            System.out.println(movie.getMovieInfo());
        }

        List<ScreeningRoom> listOfScreeningRooms = screeningRoomManager.getScreeningRoomRepository().findAll();
        for (ScreeningRoom screeningRoom : listOfScreeningRooms) {
            System.out.println(screeningRoom.getScreeningRoomInfo());
        }

        ticketRepository.close();
        clientRepository.close();
        movieRepository.close();
        screeningRoomRepository.close();
    }
}
