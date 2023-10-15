package com.example.todolist.controllers;


import com.example.todolist.entities.Task;
import com.example.todolist.repositories.TaskRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskRepository repository;

    @PostMapping("/")
    public ResponseEntity created(@RequestBody Task task, HttpServletRequest request) {
        var userId = request.getAttribute("userId");
        task.setUserId((UUID) userId);

        var currentDate = LocalDateTime.now();
        // 10/11/2023 - Current
        // 10/10/2023 - startAt
        if(currentDate.isAfter(task.getStartAt()) || currentDate.isAfter(task.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de início / data de término deve ser maior do que a data atual");
        }

        if (task.getStartAt().isAfter(task.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio deve ser menor que a de termino");
       }
        var createdtask = repository.save(task);
        return ResponseEntity.status(HttpStatus.OK).body(createdtask);
    }

    @GetMapping("/")
    public List<Task> list(HttpServletRequest request) {
        var userId = request.getAttribute("userId");
        var tasks = repository.findByUserId((UUID) userId);
        return tasks;
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody Task taskModel, HttpServletRequest request, @PathVariable UUID id) {
        var task = this.repository.findById(id).orElse(null);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa não encontrada");
        }

        var idUser = request.getAttribute("idUser");

        if (!task.getUserId().equals(idUser)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não tem permissão para alterar essa tarefa");
        }
        var taskUpdated = this.repository.save(task);
        return ResponseEntity.ok().body(taskUpdated);
    }
}

