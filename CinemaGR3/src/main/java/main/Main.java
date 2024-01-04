package main;

import model.Client;
import model.Movie;
import model.Ticket;
import model.exceptions.CassandraConfigNotFound;
import model.exceptions.delete_exceptions.RepositoryDeleteException;
import model.exceptions.read_exceptions.RepositoryReadException;
import model.managers.*;
import model.repositories.implementations.ClientRepository;
import model.repositories.implementations.MovieRepository;
import model.repositories.implementations.TicketRepository;

import java.time.Instant;
import java.util.Calendar;
import java.util.List;

public class Main {

    public static void main(String[] args) throws CassandraConfigNotFound {
        ClientRepository clientRepository = new ClientRepository();
        MovieRepository movieRepository = new MovieRepository();
        TicketRepository ticketRepository = new TicketRepository();

        ClientManager clientManager = new ClientManager(clientRepository);
        MovieManager movieManager = new MovieManager(movieRepository);
        TicketManager ticketManager = new TicketManager(ticketRepository);

        Instant movieTimeNo1 = new Calendar.Builder().setDate(2023, 10, 1).setTimeOfDay(10, 15, 0).build().getTime().toInstant();
        Instant movieTimeNo2 = new Calendar.Builder().setDate(2023, 10, 8).setTimeOfDay(16, 13, 0).build().getTime().toInstant();
        Instant movieTimeNo3 = new Calendar.Builder().setDate(2023, 10, 16).setTimeOfDay(20, 5, 0).build().getTime().toInstant();

        Instant reservationTimeNo1 = new Calendar.Builder().setDate(2023, 9, 29).setTimeOfDay(12, 37, 0).build().getTime().toInstant();
        Instant reservationTimeNo2 = new Calendar.Builder().setDate(2023, 9, 31).setTimeOfDay(14, 15, 0).build().getTime().toInstant();
        Instant reservationTimeNo3 = new Calendar.Builder().setDate(2023, 10, 11).setTimeOfDay(18, 7, 15).build().getTime().toInstant();

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

        int screeningRoomNo1Number = 10;
        int screeningRoomNo1NumberOfAvailSeats = 45;
        int screeningRoomNo2Number = 5;
        int screeningRoomNo2NumberOfAvailSeats = 90;
        int screeningRoomNo3Number = 19;
        int screeningRoomNo3NumberOfAvailSeats = 120;

        String movieNo1Title = "Harry Potter and The Goblet of Fire";
        double movieNo1BasePrice = 20.05;
        String movieNo2Title = "The Da Vinci Code";
        double movieNo2BasePrice = 40.5;
        String movieNo3Title = "A Space Odyssey";
        double movieNo3BasePrice = 59.99;

        Movie movieNo1 = movieManager.register(movieNo1Title, movieNo1BasePrice, screeningRoomNo1NumberOfAvailSeats, screeningRoomNo1Number);
        Movie movieNo2 = movieManager.register(movieNo2Title, movieNo2BasePrice, screeningRoomNo2NumberOfAvailSeats, screeningRoomNo2Number);
        Movie movieNo3 = movieManager.register(movieNo3Title, movieNo3BasePrice, screeningRoomNo3NumberOfAvailSeats, screeningRoomNo3Number);

        ticketManager.registerNormalTicket(movieTimeNo1, reservationTimeNo1, movieNo1BasePrice, movieNo1.getMovieID(), clientNo1.getClientID());
        ticketManager.registerNormalTicket(movieTimeNo2, reservationTimeNo2, movieNo2BasePrice, movieNo2.getMovieID(), clientNo2.getClientID());
        ticketManager.registerReducedTicket(movieTimeNo3, reservationTimeNo3, movieNo3BasePrice, movieNo3.getMovieID(), clientNo3.getClientID());

        try {
            List<Ticket> ticketList = ticketManager.getTicketRepository().findAll();
            int numOfTicketsBefore = ticketList.size();
            for (Ticket ticket : ticketList) {
                System.out.println(ticket.toString());
            }

            for (Ticket ticket : ticketList) {
                ticketManager.getTicketRepository().delete(ticket);
            }

            int numOfTicketsAfter = ticketManager.getTicketRepository().findAll().size();

            System.out.println("Number of tickets before: " + numOfTicketsBefore);
            System.out.println("Number of tickets after: " + numOfTicketsAfter);

            List<Client> listOfClients = clientManager.getClientRepository().findAll();
            for (Client client : listOfClients) {
                System.out.println(client.toString());
            }

            List<Movie> listOfMovies = movieManager.getMovieRepository().findAll();
            for (Movie movie : listOfMovies) {
                System.out.println(movie.toString());
            }
        } catch (RepositoryReadException exception) {
            throw new RuntimeException("Error while reading from repositories.", exception);
        } catch (RepositoryDeleteException exception) {
            throw new RuntimeException("Error while removing objects from repository.", exception);
        }

        ticketRepository.close();
        clientRepository.close();
        movieRepository.close();
    }
}
