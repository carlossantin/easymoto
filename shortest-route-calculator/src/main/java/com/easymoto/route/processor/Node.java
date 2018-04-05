package com.easymoto.route.processor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Graph node. It is used by shortest route algorithm
 * to calculate the shortest path.
 */
public class Node {

    private Integer id;
    private String name;
    private List<Node> shortestPath = new LinkedList<>();
    private Integer distance = Integer.MAX_VALUE;
    final Map<Node, Integer> adjacentNodes = new HashMap<>();
 
    public Node(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addDestination(Node destination, int distance) {
        adjacentNodes.put(destination, distance);
    }
    
    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getDistance() {
        return distance;
    }

    public Map<Node, Integer> getAdjacentNodes() {
        return adjacentNodes;
    }

    public void setShortestPath(List<Node> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public List<Node> getShortestPath() {
        return shortestPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Node && ((Node)o).getId().equals(this.id)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode(){
        return 37 * this.id;
    }
    
}
