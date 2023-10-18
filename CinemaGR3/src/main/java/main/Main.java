package main;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Client;
import model.Movie;
import model.ScreeningRoom;
import model.Ticket;
import model.exceptions.model_exceptions.TicketReservationException;
import model.managers.*;
import model.repositories.*;
import model.ticket_types.Normal;
import model.ticket_types.Reduced;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("test");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Repository<Client> clientRepository = new ClientRepository(entityManager);
        Repository<ScreeningRoom> screeningRoomRepository = new ScreeningRoomRepository(entityManager);
        Repository<Movie> movieRepository = new MovieRepository(entityManager);
        Repository<Ticket> ticketRepository = new TicketRepository(entityManager);

        Manager<Client> clientManager = new ClientManager(clientRepository);
        Manager<ScreeningRoom> screeningRoomManager = new ScreeningRoomManager(screeningRoomRepository);
        Manager<Movie> movieManager = new MovieManager(movieRepository);
        Manager<Ticket> ticketManager = new TicketManager(ticketRepository);

        ScreeningRoom screeningRoomNo1 = new ScreeningRoom(UUID.randomUUID(), 1, 10, 45);
        ScreeningRoom screeningRoomNo2 = new ScreeningRoom(UUID.randomUUID(), 2, 5, 90);
        ScreeningRoom screeningRoomNo3 = new ScreeningRoom(UUID.randomUUID(), 0, 19, 120);

        Movie movieNo1 = new Movie(UUID.randomUUID(), "Harry Potter and The Goblet of Fire", screeningRoomNo1);
        Movie movieNo2 = new Movie(UUID.randomUUID(), "The Da Vinci Code", screeningRoomNo2);
        Movie movieNo3 = new Movie(UUID.randomUUID(), "A Space Odyssey", screeningRoomNo3);

        Client clientNo1 = new Client(UUID.randomUUID(), "John", "Smith", 21);
        Client clientNo2 = new Client(UUID.randomUUID(), "Mary", "Jane", 18);
        Client clientNo3 = new Client(UUID.randomUUID(), "Vincent", "Vega", 40);

        Date movieTimeNo1 = new Calendar.Builder().setDate(2023, 10, 1).setTimeOfDay(10, 15, 0).build().getTime();;
        Date movieTimeNo2 = new Calendar.Builder().setDate(2023, 10, 8).setTimeOfDay(16, 13, 0).build().getTime();;
        Date movieTimeNo3 = new Calendar.Builder().setDate(2023, 10, 16).setTimeOfDay(20, 5, 0).build().getTime();;

        Date reservationTimeNo1 = new Calendar.Builder().setDate(2023, 9, 29).setTimeOfDay(12, 37, 0).build().getTime();;
        Date reservationTimeNo2 = new Calendar.Builder().setDate(2023, 9, 31).setTimeOfDay(14, 15, 0).build().getTime();;
        Date reservationTimeNo3 = new Calendar.Builder().setDate(2023, 10, 11).setTimeOfDay(18, 7, 15).build().getTime();;

        Ticket ticketNo1;
        Ticket ticketNo2;
        Ticket ticketNo3;

        try {
            ticketNo1 = new Ticket(UUID.randomUUID(), movieTimeNo1, reservationTimeNo1, movieNo1, clientNo1, new Normal(UUID.randomUUID(), 30));;
            ticketNo2 = new Ticket(UUID.randomUUID(), movieTimeNo2, reservationTimeNo2, movieNo2, clientNo2, new Reduced(UUID.randomUUID(), 25));;
            ticketNo3 = new Ticket(UUID.randomUUID(), movieTimeNo3, reservationTimeNo3, movieNo3, clientNo3, new Normal(UUID.randomUUID(), 40));;
        } catch (TicketReservationException exception) {
            throw new RuntimeException("Fatal error.");
        }


        screeningRoomManager.getObjectRepository().create(screeningRoomNo1);
        screeningRoomManager.getObjectRepository().create(screeningRoomNo2);
        screeningRoomManager.getObjectRepository().create(screeningRoomNo3);

        clientManager.getObjectRepository().create(clientNo1);
        clientManager.getObjectRepository().create(clientNo2);
        clientManager.getObjectRepository().create(clientNo3);

        movieManager.getObjectRepository().create(movieNo1);
        movieManager.getObjectRepository().create(movieNo2);
        movieManager.getObjectRepository().create(movieNo3);

        ticketManager.getObjectRepository().create(ticketNo1);
        ticketManager.getObjectRepository().create(ticketNo2);
        ticketManager.getObjectRepository().create(ticketNo3);

        List<Ticket> ticketList = ticketManager.getObjectRepository().findAll();
        int numOfTicketsBefore = ticketList.size();
        for (Ticket ticket : ticketList) {
            System.out.println(ticket.getTicketInfo());
        }

        for (Ticket ticket : ticketList) {
            ticketManager.getObjectRepository().delete(ticket);
        }

        int numOfTicketsAfter = ticketManager.getObjectRepository().findAll().size();

        System.out.println("Liczba biletów przed: " + numOfTicketsBefore);
        System.out.println("Liczba biletów po: " + numOfTicketsAfter);

        List<Client> listOfClients = clientManager.getObjectRepository().findAll();
        for (Client client : listOfClients) {
            System.out.println(client.getClientInfo());
        }

        List<Movie> listOfMovies = movieManager.getObjectRepository().findAll();
        for (Movie movie : listOfMovies) {
            System.out.println(movie.getMovieInfo());
        }

        List<ScreeningRoom> listOfScreeningRooms = screeningRoomManager.getObjectRepository().findAll();
        for (ScreeningRoom screeningRoom : listOfScreeningRooms) {
            System.out.println(screeningRoom.getScreeningRoomInfo());
        }

        // entityManager.getTransaction().commit();
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}
