package labyrinth.util.graph;

import java.util.*;

public class Paths<T> {
    private Map<T, T> predecessors;

    public Paths(Graph<T> graph, T from) {
        predecessors = new HashMap<>();

        Queue<T> queue = new ArrayDeque<>();
        predecessors.put(from, null);

        queue.add(from);

        while (!queue.isEmpty()) {
            T node = queue.remove();
            for(T successor : graph.getSuccessors(node)) {
                if(!predecessors.containsKey(successor)) {
                    predecessors.put(successor, node);
                    queue.add(successor);
                }
            }
        }
    }

    public boolean isAccessible(T node) {
        return predecessors.containsKey(node);
    }

    public List<T> getPath(T node) {
        if(!isAccessible(node)) {
            return null;
        }

        List<T> path = new ArrayList<>();
        T current = node;

        while(current != null) {
            path.add(current);
            current = predecessors.get(current);
        }

        Collections.reverse(path);
        return path;
    }
}
