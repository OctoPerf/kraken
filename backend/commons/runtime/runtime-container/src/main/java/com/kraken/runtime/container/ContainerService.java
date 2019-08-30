package com.kraken.runtime.container;

import com.kraken.runtime.entity.Container;
import com.kraken.runtime.entity.Task;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ContainerService {

  void execute(Task task);

  Flux<Container> list();

  Flux<List<Container>> watchContainers();

//  TODO créer un projet runtime-log et reprendre les command logs
//  Flux<Log> logs();

  void attachLogs(String id);

  void detachLogs(String id);


  //  TODO Reprendre le fonctionnement du DockerService actuel: lancement via Docker-compose (et des commandes)
//  TODO command logs generique
//  TODO SSE => regrouper des flux d'events (englober les events dans un objet qui contient le type)
//  TODO Lancer des containers
//  TODO Lister les logs
//  TODO Ajouter / stoper les logs pour un container
//  TODO Lister les containers => en déduire une liste(avec un seul element) de Task
//  TODO Watch qui met ca a jour périodiquement
//  Les conteneurs écoutent et ne démarent que quand tout le monde est prêt ? ou le serveur lance un commande ?


}
