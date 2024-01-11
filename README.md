# Projet-Go
Note 15/20
IBoard ne sert à rien


Periode B : Projet Go <br />
Groupe 205-206 : <br />
Gobigan MATHIALAHAN, Grp 205 <br />
Riad FAROUZI, Grp 206 <br />
Félix WANG, Grp 206<br />
John LI, Grp 206 <br />


Ce qui marche:
SPRINT 1 : les commandes showboard, quit, boardsize<br />
SPRINT 2 : play  <br />
SPRINT 3 : faire une IA qui joue aléatoirement <br />
une commande privée à choisir exemple : player black random , player white human


Pas fait ou qui ne marche pas: 
SUICIDE ET KO (pas obligatoire)<br />
3.interdire le suicide sauf si prise d'une pierre <br />
4. règle du ko <br />
5.Une commande qui récupère un txt pour faire des tests d'un seul coup au lieu d'écrire chauque commande une par une
6. passer son tour 
7. arrêt du jeu entre bot vs bot

Diagramme d'architecture :


Liste synthétique des tests unitaires:
Dans la classe BoardTest.java, on teste les fonctionnalités du jeu : 
isPlaceable() , showBoard(), play(), testGetNbLiberties(), on teste également des états de plateau avec getExpectedBoardState1()

<b>Bilan du projet :</b> <br />

Difficultés : <br />
On n'a pas réussi à implémenter toutes les fonctionnalités telles que : <br />
Suicide et Ko <br />
Commande pour tester en une seule fois:On n'a pas automatisé les tests, cela a rendu les tests plus laborieux à effectuer. <br />
Arrêt du jeu entre bot vs bot: Si le jeu ne se termine pas proprement lorsqu'il est joué entre deux bots et boucle à l'infini.<br />

Réussite : On a réussi à mettre en place chaque SPRINTs dans les délais, le code est documenté et lisible.<br />

Ce qui peut être améliorable : <br />
Gestion du Suicide et Ko:  il pourrait être intéressant de les considérer pour une version future, car elles sont des aspects essentiels du jeu de Go.<br />
Commande pour tester en une seule fois: Automatiser les tests autant que possible peut faciliter le processus de développement. <br />
Ajout d'une interface utilisateur: facilité les interactions avec les commandes pour les utilisateurs.<br />

Diagramme : 
![src (2)](https://github.com/idflxw0/Projet-Go/assets/98221552/cea019ad-a38e-46d8-9032-23a6209057cb)

