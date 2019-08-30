package com.kraken.runtime.docker;

import com.kraken.runtime.container.ContainerService;
import com.kraken.runtime.entity.Task;

public class DockerService implements ContainerService {

//  TODO Reprendre le fonctionnement du DockerService actuel: lancement via Docker-compose (et des commandes)
//  TODO command logs generique
//  TODO SSE => regrouper des flux d'events (englober les events dans un objet qui contient le type)
//  TODO Lancer des containers
//  TODO Lister les logs
//  TODO Ajouter / stoper les logs pour un container
//  TODO Lister les containers => en déduire une liste(avec un seul element) de Task
//  TODO Watch qui met ca a jour périodiquement
//  Les conteneurs écoutent et ne démarent que quand tout le monde est prêt ? ou le serveur lance un commande ?

  public void execute(final Task task){

  }
}
