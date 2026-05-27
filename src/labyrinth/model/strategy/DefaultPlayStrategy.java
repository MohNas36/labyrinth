package labyrinth.model.strategy;

import labyrinth.model.MazeMove;
import labyrinth.model.strategy.simulation.GameSimulation;
import labyrinth.model.strategy.simulation.PlayerSimulation;
import labyrinth.model.strategy.simulation.TileSimulation;
import labyrinth.service.serialization.GameDTO;
import labyrinth.util.collection.Grid;
import labyrinth.util.collection.Position;
import labyrinth.util.graph.Paths;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DefaultPlayStrategy implements PlayStrategy {

    private GameSimulation game;
    private PlayerSimulation initialPlayer;

    private final Random random = new Random();
    private static final int NB_RANDOM_MOVES = 4;
    private static final int NB_SIMULATIONS = 6;
    private static final double ALPHA = 0.5;
    private static final int NB_CANDIDATS = 8;
    private static final int MAX_PROFONDEUR = 4;


    /**
     * Point d'entrée de la stratégie.
     *
     * On reçoit un GameDTO, donc une copie de l'état actuel du jeu.
     * À partir de cette copie, on crée une simulation légère pour tester
     * plusieurs coups sans modifier la vraie partie.
     */

    @Override
    public Move getNextMove(GameDTO snapshot) {
        game = new GameSimulation(snapshot);
        initialPlayer = game.getCurrentPlayer();
        return computeNextMove();
    }

    /**
     * Calcule le meilleur coup à jouer pour le joueur courant.
     *
     * La méthode commence par générer tous les coups possibles, puis garde
     * seulement les meilleurs candidats. Ensuite, elle ajuste leur score avec
     * des simulations supplémentaires avant de choisir le meilleur coup final.
     */
    private Move computeNextMove() {
        if (game.hasWon(initialPlayer)) {
            return null;
        }

        List<ScoredMove> moves = computeMoves();

        if (moves.isEmpty()) {
            return null;
        }

        List<ScoredMove> candidates = getBestCandidates(moves, NB_CANDIDATS);
        List<ScoredMove> adjustedMoves = new ArrayList<>();

        for (ScoredMove scoredMove : candidates) {
            adjustedMoves.add(adjust(scoredMove, MAX_PROFONDEUR));
        }

        return getBestMove(adjustedMoves);
    }

    /**
     * Donne un score à l'état actuel de la simulation.
     *
     * Plus le score est grand, plus la situation est bonne pour le joueur initial.
     * On récompense surtout la victoire, puis le nombre de trésors trouvés,
     * et on pénalise la distance restante jusqu'au prochain objectif.
     */
    private int getScore(){
        if(game.hasWon(initialPlayer)){
            return 10000;
        }

        if(game.isGameOver()){
            return -10000;
        }

        int nbTreasuresFound = initialPlayer.getDiscardPile().size();
        int distance = game.getDistanceToTarget(initialPlayer);

        return 100 * nbTreasuresFound - distance;
    }

    /**
     * Génère tous les coups valides possibles à partir de l'état actuel.
     *
     * Pour chaque déplacement possible du labyrinthe et chaque rotation possible
     * de la tuile libre, on calcule les cases accessibles. Chaque case accessible
     * devient une destination possible pour le joueur.
     *
     * Après chaque test, on annule les modifications pour revenir à l'état initial
     * de la simulation.
     */
    private List<ScoredMove> computeMoves(){
        List<ScoredMove> moves = new ArrayList<>();

        for(MazeMove mazeMove : game.getValidMazeMoves()) {
            for (int nbRotations = 0; nbRotations < 4; nbRotations++) {

                game.modifyMaze(nbRotations, mazeMove);

                Paths<TileSimulation> paths = game.computePaths();

                for (Grid.Entry<TileSimulation> entry : game.getMaze().getTiles()) {
                    TileSimulation destinationTile = entry.value();

                    if(paths.isAccessible(destinationTile)) {
                        Position destinationPosition = destinationTile.getPosition();

                        Move move = new Move(nbRotations, mazeMove, destinationPosition);

                        game.movePlayer(destinationTile);

                        int score = getScore();
                        moves.add(new ScoredMove(move, score));

                        game.undoLastPlayerMove();
                    }
                }

                game.undoLastMazeModification();

            }
        }

        return moves;

    }

    /**
     * Produit un coup semi-aléatoire valide depuis l'état courant.
     *
     * On choisit d'abord un déplacement de labyrinthe et une rotation au hasard.
     * Ensuite, au lieu de prendre une destination totalement aléatoire, on garde
     * les destinations accessibles les plus proches de l'objectif du joueur.
     */
    private ScoredMove getRandomMove() {
        List<MazeMove> mazeMoves = game.getValidMazeMoves();

        if (mazeMoves.isEmpty()) {
            return null;
        }

        MazeMove mazeMove = mazeMoves.get(random.nextInt(mazeMoves.size()));
        int nbRotations = random.nextInt(4);

        game.modifyMaze(nbRotations, mazeMove);

        Paths<TileSimulation> paths = game.computePaths();
        PlayerSimulation currentPlayer = game.getCurrentPlayer();

        List<TileSimulation> bestDestinations = new ArrayList<>();
        int bestDistance = Integer.MAX_VALUE;

        for (Grid.Entry<TileSimulation> entry : game.getMaze().getTiles()) {
            TileSimulation tile = entry.value();

            if (paths.isAccessible(tile)) {
                int distance = game.getDistanceToTarget(tile.getPosition(), currentPlayer);

                if (distance < bestDistance) {
                    bestDistance = distance;
                    bestDestinations.clear();
                    bestDestinations.add(tile);
                } else if (distance == bestDistance) {
                    bestDestinations.add(tile);
                }
            }
        }

        if (bestDestinations.isEmpty()) {
            game.undoLastMazeModification();
            return null;
        }

        TileSimulation destinationTile = bestDestinations.get(random.nextInt(bestDestinations.size()));
        Move move = new Move(nbRotations, mazeMove, destinationTile.getPosition());

        game.movePlayer(destinationTile);

        int score = getScore();

        game.undoLastMove();

        return new ScoredMove(move, score);
    }

    /**
     * Compare plusieurs coups semi-aléatoires et garde le meilleur.
     *
     * Cette méthode est utilisée pendant les simulations Monte-Carlo pour éviter
     * de jouer un coup complètement mauvais au hasard.
     */
    private Move getBestRandomMove(int nb) {
        List<ScoredMove> moves = new ArrayList<>();

        for (int i = 0; i < nb; i++) {
            ScoredMove move = getRandomMove();

            if (move != null) {
                moves.add(move);
            }
        }

        return getBestMove(moves);
    }

    /**
     * Renvoie le meilleur coup d'une liste de coups scorés.
     *
     * Si plusieurs coups ont le même meilleur score, on en choisit un au hasard.
     * Cela évite que le bot joue toujours exactement le même coup en cas d'égalité.
     */
    private Move getBestMove(List<ScoredMove> moves) {
        if (moves.isEmpty()) {
            return null;
        }

        int bestScore = moves.get(0).score();
        List<ScoredMove> bestMoves = new ArrayList<>();

        for (ScoredMove move : moves) {
            if (move.score() > bestScore) {
                bestScore = move.score();
                bestMoves.clear();
                bestMoves.add(move);
            } else if (move.score() == bestScore) {
                bestMoves.add(move);
            }
        }

        return bestMoves.get(random.nextInt(bestMoves.size())).move();
    }

    /**
     * Lance une simulation de plusieurs coups à partir de l'état actuel.
     *
     * On applique des coups semi-aléatoires jusqu'à atteindre la profondeur donnée
     * ou jusqu'à la fin de la partie. À la fin, on calcule le score obtenu, puis
     * on annule tous les coups simulés pour revenir exactement à l'état de départ.
     */
    private int simulate(int maxDepth) {
        int nbMoves = 0;

        while (nbMoves < maxDepth && !game.isGameOver()) {
            Move move = getBestRandomMove(NB_RANDOM_MOVES);

            if (move == null) {
                break;
            }

            game.apply(move);
            nbMoves++;
        }

        int score = getScore();

        for (int i = 0; i < nbMoves; i++) {
            game.undoLastMove();
        }

        return score;
    }

    /**
     * Ajuste le score d'un coup grâce à plusieurs simulations.
     *
     * On applique d'abord le coup à évaluer, puis on lance plusieurs simulations
     * à partir de cette position. Le score final mélange le score initial du coup
     * et la moyenne des scores obtenus pendant les simulations.
     */
    private ScoredMove adjust(ScoredMove scoredMove, int maxDepth) {
        game.apply(scoredMove.move());

        int sum = 0;

        for (int i = 0; i < NB_SIMULATIONS; i++) {
            sum += simulate(maxDepth);
        }

        int adjustedScore = (int) Math.round(
                scoredMove.score() + (ALPHA * sum / NB_SIMULATIONS)
        );

        game.undoLastMove();

        return new ScoredMove(scoredMove.move(), adjustedScore);
    }

    /**
     * Sélectionne les meilleurs candidats avant de lancer les simulations coûteuses.
     *
     * Au lieu d'ajuster tous les coups possibles, on garde seulement les meilleurs
     * selon leur score initial. Cela rend l'IA beaucoup plus rapide.
     */
    private List<ScoredMove> getBestCandidates(List<ScoredMove> moves, int nb) {
        List<ScoredMove> candidates = new ArrayList<>();

        for (ScoredMove move : moves) {
            if (candidates.size() < nb) {
                candidates.add(move);
            } else {
                int worstIndex = getWorstIndex(candidates);

                if (move.score() > candidates.get(worstIndex).score()) {
                    candidates.set(worstIndex, move);
                }
            }
        }

        return candidates;
    }

    /**
     * Cherche l'indice du moins bon coup dans une liste.
     *
     * Cette méthode sert à remplacer le pire candidat lorsqu'on trouve un meilleur
     * coup pendant la sélection des candidats.
     */
    private int getWorstIndex(List<ScoredMove> moves) {
        int worstIndex = 0;

        for (int i = 1; i < moves.size(); i++) {
            if (moves.get(i).score() < moves.get(worstIndex).score()) {
                worstIndex = i;
            }
        }

        return worstIndex;
    }

}