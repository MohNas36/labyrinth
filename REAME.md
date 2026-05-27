# Labyrinthe

Ce projet est une adaptation en JavaFX du jeu **Labyrinthe**.  
Le but est simple : déplacer son pion dans le labyrinthe, trouver tous ses trésors, puis revenir à sa case de départ avant les autres joueurs.

L’application gère les joueurs humains, les bots, les profils utilisateurs, la sauvegarde/le chargement, ainsi que la reprise automatique de la dernière partie.


---

## Lancer l’archive JAR

Une fois le projet exporté sous forme d’archive `.jar`, le fichier à lancer est :

```text
labyrinth-e84504u-nasralla3u.jar
```

Comme le projet utilise JavaFX, il faut lancer l’archive avec les options JavaFX.

Commande à utiliser :

```bash
java --module-path /chemin/vers/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -jar labyrinth-e84504u-nasralla3u.jar
```

Il faut remplacer :

```text
/chemin/vers/javafx-sdk/lib
```

par le chemin du dossier `lib` de JavaFX sur la machine utilisée.

Par exemple, si JavaFX est dans le dossier `javafx-sdk-23.0.2/lib`, la commande ressemble à :

```bash
java --module-path javafx-sdk-23.0.2/lib --add-modules javafx.controls,javafx.fxml -jar labyrinth-e84504u-nasralla3u.jar
```

---

## Lancement d’une partie

Au démarrage, l’application peut reprendre automatiquement la dernière partie enregistrée.

Donc, si aucune nouvelle partie n’est créée et qu’aucune sauvegarde n’est chargée manuellement, le bouton **Start game** permet de continuer la dernière partie connue par l’application.

Pour commencer une vraie nouvelle partie, il faut passer par :

```text
File > New
```

Ensuite, il faut configurer les joueurs, puis cliquer sur **Start game**.

---

## Nouvelle partie et configuration des joueurs

Après avoir cliqué sur **File > New**, il faut configurer les joueurs avant de lancer la partie.

La partie fonctionne toujours avec **4 emplacements de joueurs**.  
Si un emplacement n’est pas modifié, il est considéré comme un joueur humain par défaut.

Si on veut utiliser des bots, il faut donc les ajouter manuellement avant de cliquer sur **Start game**.

Exemple :

1. Cliquer sur **File > New**.
2. Supprimer le joueur sur l’emplacement voulu.
3. Ajouter un bot à cet emplacement.
4. Répéter l’opération pour chaque bot souhaité.
5. Cliquer sur **Start game**.

Important : si on clique directement sur **Start game** sans reconfigurer les joueurs après **File > New**, les emplacements non modifiés restent des joueurs humains.

---

## Joueurs et bots

Un joueur peut être :

- un joueur humain ;
- un bot ;
- un joueur associé à un profil utilisateur.

Les bots jouent automatiquement. Quand c’est le tour d’un bot, le programme choisit un coup à jouer, insère la tuile supplémentaire, déplace le pion du bot, puis passe au joueur suivant.

Les bots doivent être configurés au début d’une nouvelle partie. Ils ne sont pas ajoutés automatiquement.

---

## Correspondance des numéros de joueurs

Sur le plateau, les joueurs sont affichés avec un indice qui commence à `0`.

Dans le menu des joueurs à droite, l’affichage commence à `1`.

Donc :

```text
P([0]) sur le plateau = Joueur 1 dans le menu
P([1]) sur le plateau = Joueur 2 dans le menu
P([2]) sur le plateau = Joueur 3 dans le menu
P([3]) sur le plateau = Joueur 4 dans le menu
```

C’est seulement une différence d’affichage. Le joueur reste bien le même.

---

## Affichage du plateau

Le plateau contient plusieurs types d’indications visuelles.

### Carrés jaunes

Les carrés jaunes indiquent les joueurs présents sur une tuile.

Exemple :

```text
P([0])
```

signifie que le joueur d’indice `0`, donc **Joueur 1**, est sur cette tuile.

Si plusieurs joueurs sont sur la même tuile, plusieurs indices peuvent apparaître au même endroit.

### Carrés verts

Les carrés verts représentent les trésors.

Exemple :

```text
TR7
```

signifie que la tuile contient le trésor numéro `7`.

Les trésors restent visibles sur le plateau. Quand un joueur trouve un trésor, on ne retire pas le trésor de la tuile. C’est la carte objectif du joueur qui est considérée comme trouvée.

### Carrés rouges

Les carrés rouges représentent les cases de départ.

Exemple :

```text
SP0
```

correspond à la case de départ du joueur d’indice `0`.

### Carré blanc

Le carré blanc indique le joueur courant, c’est-à-dire le joueur dont c’est le tour.

---

## Menus de l’application

### Menu File

Le menu **File** permet de gérer la partie.

Il contient :

```text
New   : créer une nouvelle partie
Load  : charger une sauvegarde
Save  : sauvegarder la partie actuelle
Exit  : quitter l’application
```

### Menu Help

Le menu **Help** affiche les informations générales sur l’application.

### Menu Edit

Le menu **Edit** permet de gérer les profils utilisateurs.  
On peut y ouvrir la fenêtre de gestion des profils, créer ou modifier des profils, puis les utiliser pour ajouter des joueurs identifiés dans une partie.

---

## Déroulement d’un tour

Un tour se déroule en deux étapes.

### 1. Modifier le labyrinthe

Le joueur commence toujours par insérer la tuile supplémentaire dans l’un des slots autour du plateau.

Quand on clique sur un slot, la tuile libre est insérée dans la ligne ou la colonne correspondante. Les tuiles sont alors poussées, et une tuile est expulsée de l’autre côté du plateau.

La tuile expulsée devient ensuite la nouvelle tuile supplémentaire.

### 2. Déplacer son pion

Après avoir modifié le labyrinthe, le joueur peut déplacer son pion.

Il peut aller sur n’importe quelle tuile accessible, tant qu’il existe un chemin continu entre sa position actuelle et la tuile choisie.

Il est aussi possible de rester sur place si aucun déplacement intéressant n’est possible.

---

## Slots et tuile supplémentaire

Les slots autour du plateau indiquent les endroits où la tuile supplémentaire peut être insérée.

Quand on clique sur un slot :

1. la tuile supplémentaire est insérée ;
2. la ligne ou la colonne est décalée ;
3. une tuile sort de l’autre côté ;
4. cette tuile devient la nouvelle tuile supplémentaire.

Si un joueur est expulsé du plateau avec une tuile, il est replacé automatiquement sur la tuile qui vient d’être insérée de l’autre côté.

---

## Mouvement interdit

Après chaque insertion, le mouvement inverse est interdit au tour suivant.

Par exemple, si la tuile a été insérée d’un côté et qu’une autre tuile a été expulsée de l’autre côté, il est interdit de réinsérer immédiatement la tuile expulsée à l’endroit d’où elle vient de sortir.

Cela évite d’annuler directement le coup précédent.

Dans l’interface, le slot interdit est désactivé ou marqué comme non utilisable.

---

## Trésors, pioche et défausse

Chaque joueur possède une pile de trésors à trouver.

Dans le code, cette pile est séparée en deux parties :

```text
drawPile    = trésors encore à trouver
discardPile = trésors déjà trouvés
```

Le joueur ne cherche que le trésor situé au sommet de sa `drawPile`.

Quand il atteint une tuile contenant ce trésor :

1. le trésor est considéré comme trouvé ;
2. il est retiré de la `drawPile` ;
3. il est ajouté dans la `discardPile` ;
4. le joueur passe ensuite à son prochain trésor.

Le trésor reste affiché sur la tuile du plateau, car il fait partie du décor du labyrinthe.

---

## Affichage du deck et de la défausse

Dans le panneau des joueurs, chaque joueur affiche maintenant deux informations liées aux trésors :

```text
Deck      : prochain trésor à chercher
Défausse  : trésors déjà trouvés
```

Par exemple :

```text
Deck : TR19
Défausse : vide
```

signifie que le joueur doit actuellement chercher le trésor `TR19`, et qu’il n’a encore trouvé aucun trésor.

Autre exemple :

```text
Deck : TR4
Défausse : TR6, TR0, TR18
```

signifie que le prochain objectif du joueur est `TR4`, et qu’il a déjà trouvé `TR6`, `TR0` et `TR18`.

Si la défausse est vide, c’est normal : cela veut simplement dire que le joueur n’a encore trouvé aucun trésor.

Si le deck est vide, cela veut dire que le joueur a trouvé tous ses trésors. Il doit alors revenir sur sa propre case de départ pour gagner la partie.

---

## Conditions de victoire

Un joueur gagne quand :

1. il a trouvé tous ses trésors ;
2. sa `drawPile` est vide ;
3. il revient sur sa propre case de départ.

Autrement dit :

```text
drawPile vide
+
joueur sur sa StartingPosition
=
victoire
```

Quand ces conditions sont réunies, la partie passe en fin de jeu et un message indique le gagnant.

---

## Sauvegarde et chargement

L’application permet de sauvegarder et charger une partie.

### Sauvegarder

Pour sauvegarder la partie actuelle :

```text
File > Save
```

Le programme demande alors où enregistrer la sauvegarde (format json).

### Charger

Pour charger une partie :

```text
File > Load
```

Le programme demande alors de choisir un fichier de sauvegarde (format json).

### Reprise automatique

En plus des sauvegardes manuelles, l’application peut reprendre la dernière partie enregistrée automatiquement.  
C’est pour cela que, si on lance directement **Start game** sans faire **File > New**, on peut continuer l’ancienne partie.

---

## Profils utilisateurs

Les profils utilisateurs peuvent être gérés depuis :

```text
Edit > Manage Profiles
```

Un profil permet d’associer un joueur à un nom enregistré.

Les joueurs peuvent être ajoutés comme invités, comme bots, ou avec un profil existant selon les options disponibles dans l’interface.

---

## Remarques sur les bots

Les bots jouent automatiquement quand leur tour arrive.

Pour tester les bots correctement :

1. faire **File > New** ;
2. supprimer les joueurs qu’on veut remplacer ;
3. ajouter les bots manuellement ;
4. cliquer sur **Start game**.

Si un bot joue plusieurs coups d’affilée, c’est normal si plusieurs bots se suivent dans l’ordre des joueurs.

---

## Remarques connues

- Les joueurs sont affichés avec un indice à partir de `0` sur le plateau, mais avec un numéro à partir de `1` dans le menu.
- Les trésors restent visibles sur les tuiles, même quand ils ont déjà été trouvés par un joueur.
- Dans le panneau joueur, `Deck` indique le prochain trésor à chercher, tandis que `Défausse` indique les trésors déjà trouvés.
- Après **File > New**, il faut reconfigurer les bots avant de lancer la partie.
- Si aucune nouvelle partie n’est créée, **Start game** peut reprendre la dernière partie connue par l’application.

---

## Résumé rapide

Pour jouer :

1. lancer l’application ;
2. utiliser **File > New** pour créer une nouvelle partie si besoin ;
3. configurer les joueurs et les bots ;
4. cliquer sur **Start game** ;
5. insérer la tuile supplémentaire à chaque tour ;
6. déplacer le joueur vers une tuile accessible ;
7. trouver tous ses trésors ;
8. revenir à sa case départ pour gagner.