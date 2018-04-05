package com.easymoto.route.processor;
        
import java.util.HashSet;
import java.util.Set;

/**
 * Graph used by shortest route algorithm
 */
public class Graph {
 
    private Set<Node> nodes = new HashSet<>();
     
    public void addNode(Node nodeA) {
        nodes.add(nodeA);
    }
 
    public Set<Node> getNodes() {
        return nodes;
    }
    
}
