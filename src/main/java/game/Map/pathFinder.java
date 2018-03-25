package game.Map;

import java.util.LinkedList;
import java.util.List;

public class pathFinder extends LinkedList {
    public void add(Comparable object) {
        for (int i = 0; i < size(); i++) {
            if (object.compareTo(get(i)) <= 0) {
                add(i, object);
                return;
            }
        }
        addLast(object);
    }

    protected List constructPath(AStarNode node) {
        LinkedList path = new LinkedList();
        while (node.pathParent != null) {
            path.addFirst(node);
            node = node.pathParent;
        }
        return path;
    }


    /**
     * Find the path from the start node to the end node. A list
     * of AStarNodes is returned, or null if the path is not
     * found.
     */
    public List findPath(AStarNode startNode, AStarNode goalNode) {
        pathFinder openList = new pathFinder();
        LinkedList closedList = new LinkedList();

        startNode.costFromStart = 0;
        startNode.estimatedCostToGoal =
                startNode.getEstimatedCost(goalNode);
        startNode.pathParent = null;
        openList.add(startNode);

        while (!openList.isEmpty()) {
            AStarNode node = (AStarNode) openList.removeFirst();
            if (node == goalNode) {
                // construct the path from start to goal
                return constructPath(goalNode);
            }

            List neighbors = node.getNeighbors();
            for (Object neighbor : neighbors) {
                AStarNode neighborNode =
                        (AStarNode) neighbor;
                boolean isOpen = openList.contains(neighborNode);
                boolean isClosed =
                        closedList.contains(neighborNode);
                float costFromStart = node.costFromStart +
                        node.getCost(neighborNode);

                // check if the neighbor node has not been
                // traversed or if a shorter path to this
                // neighbor node is found.
                if ((!isOpen && !isClosed) ||
                        costFromStart < neighborNode.costFromStart) {
                    neighborNode.pathParent = node;
                    neighborNode.costFromStart = costFromStart;
                    neighborNode.estimatedCostToGoal =
                            neighborNode.getEstimatedCost(goalNode);
                    if (isClosed) {
                        closedList.remove(neighborNode);
                    }
                    if (!isOpen) {
                        openList.add(neighborNode);
                    }
                }
            }
            closedList.add(node);
        }

        // no path found
        return null;
    }
}