package labyrinth.util.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph<T> {
    private Set<T> nodes;
    private Map<T, Set<T>> edges;

    public Graph() {
        this.nodes = new HashSet<T>();
        this.edges = new HashMap<T, Set<T>>();
    }

    public void addNode(T node) {
        this.nodes.add(node);
    }

    public void addEdge(T from, T to) {
        this.nodes.add(from);
        this.nodes.add(to);

        edges.putIfAbsent(from, new HashSet<>());
        edges.get(from).add(to);
    }

    public Set<T> getNodes() {
        return this.nodes;
    }

    public Set<T> getSuccessors(T node) {
        return this.edges.getOrDefault(node, new HashSet<>());
    }
}

