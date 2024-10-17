package com.example.barberlab6.Controllers;

import com.example.barberlab6.Models.Client;
import com.example.barberlab6.Repository.ClientsRepository;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ClientController {
    private final ClientsRepository clientsRepository;

    public ClientController(ClientsRepository clientsRepository) {
        this.clientsRepository = clientsRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Главная Страница");
        return "greeting";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "Страница входа");
        return "login";
    }

    @GetMapping("/Clients")
    public String clients(Model model) {
        Iterable<Client> clients = clientsRepository.findAll();
        model.addAttribute("title", "Страница клиентов");
        model.addAttribute("Clients", clients);
        return "Plays"; // Используем оригинальное имя HTML-шаблона
    }

    @GetMapping("/addClient")
    public String addClient(Model model) {
        model.addAttribute("title", "Страница добавления клиента");
        return "addPlay"; // Используем оригинальное имя HTML-шаблона
    }

    @GetMapping("/Clients/{id}")
    public String updateClient(@PathVariable long id, Model model) {
        if (!clientsRepository.existsById(id)) {
            return "redirect:/Clients";
        }
        Optional<Client> client = clientsRepository.findById(id);
        ArrayList<Client> res = new ArrayList<>();
        client.ifPresent(res::add);
        model.addAttribute("Clients", res);
        model.addAttribute("title", "Страница редактирования");
        return "PlayDetails"; // Используем оригинальное имя HTML-шаблона
    }

    @GetMapping("/Clients/filter")
    public String searchClients(
            @RequestParam(required = false) String cliname,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start_date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end_date,
            @RequestParam(required = false) String service,
            @RequestParam(required = false, defaultValue = "asc") String sort,
            Model model) {

        Sort.Direction sortDirection = sort.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sortBy = Sort.by(sortDirection, "sessionDateTime");

        LocalDateTime startDateTime = start_date != null ? start_date.atStartOfDay() : null;
        LocalDateTime endDateTime = end_date != null ? end_date.atTime(23, 59, 59) : null;

        List<Client> clients;

        if (cliname != null || start_date != null || end_date != null || service != null) {
            clients = clientsRepository.findByParams(cliname, startDateTime, endDateTime, service, sortBy);
        } else {
            clients = clientsRepository.findAll(sortBy);
        }

        model.addAttribute("Clients", clients);
        return "Plays"; // Используем оригинальное имя HTML-шаблона
    }

    @GetMapping("/Clients/stats")
    public String stats(Model model) {
        List<Object[]> stats = clientsRepository.findPlayIssueStats();

        List<String> dates = new ArrayList<>();
        List<Long> counts = new ArrayList<>();

        for (Object[] row : stats) {
            dates.add(row[0].toString());
            counts.add((Long) row[1]);
        }

        model.addAttribute("dates", dates);
        model.addAttribute("counts", counts);
        return "Play_stats"; // Используем оригинальное имя HTML-шаблона
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/addClient")
    public String addClient(@RequestParam String cliname,
                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime sessionDateTime,
                            @RequestParam String service,
                            @RequestParam String mastername,
                            Model model) {
        Client client = new Client(cliname, sessionDateTime, service, mastername);
        clientsRepository.save(client);
        return "redirect:/Clients";
    }

    @PostMapping("/Clients/save")
    public String saveClient(
            @RequestParam("id") long id,
            @RequestParam String cliname,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime sessionDateTime,
            @RequestParam String service,
            @RequestParam String mastername,
            Model model) {
        Client client = clientsRepository.findById(id).orElseThrow();
        client.setCliname(cliname);
        client.setSessionDateTime(sessionDateTime);
        client.setService(service);
        client.setMastername(mastername);
        clientsRepository.save(client);
        return "redirect:/Clients";
    }

    @PostMapping("/Clients/{id}/remove")
    public String removeClient(@PathVariable long id, Model model) {
        Client client = clientsRepository.findById(id).orElseThrow();
        clientsRepository.delete(client);
        return "redirect:/Clients";
    }
}
