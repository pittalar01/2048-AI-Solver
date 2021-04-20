/**
 * 
 */
package ai2048;

/**
 * @author Rachitha Pittala
 *
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import game2048.GameBoard;
import game2048.Constants;


public class SearchTree {
    private SearchTreeNode rootNode;
    private ArrayList<SearchTreeNode> leafNode;

    public SearchTree(GameBoard currentBoard, int treeDepth) {
        rootNode = new SearchTreeNode(currentBoard);
        leafNode = new ArrayList<>();
         Queue<SearchTreeNode> queue = new LinkedList<>();
        queue.offer(rootNode);
        while (!queue.isEmpty()) {
            SearchTreeNode x = queue.peek();
            if (x.depth < treeDepth) {
                queue.poll();
                x.createChildNodes();
                for (Constants.Directions direction : Constants.Directions.values()) {
                    int directionValue = direction.getDirectionValue();
                    if (x.childNodes[directionValue] != null)
                        queue.offer(x.childNodes[directionValue]);
                }
            } 
            else {
                for (int count = 0; count < leafNode.size(); count++)
                    leafNode.add(queue.poll());
                queue.clear();
            }
        }
        if (leafNode.isEmpty())
            getLeafNodes(treeDepth);
    }

    
    private int getTreeHeight(SearchTreeNode rootNode) {
        if (rootNode == null || rootNode.childNodes == null)
            return 0;
        else {
            boolean isLeaf = true;
            for (Constants.Directions direction : Constants.Directions.values())
                if (rootNode.childNodes[direction.getDirectionValue()] != null) {
                    isLeaf = false;
                    break;
                }
            if (!isLeaf) {
                int tree_height = 0;
                for (Constants.Directions direction : Constants.Directions.values())
                    tree_height = Math.max(tree_height, getTreeHeight(rootNode.childNodes[direction.getDirectionValue()]));
                return 1 + tree_height;
            } 
            else
                return 0;
        }
    }


    private void getLeafNodes(int depth) {
        int level = Math.min(getTreeHeight(rootNode), depth);
        Queue<SearchTreeNode> queue = new LinkedList<>();
        queue.offer(rootNode);
        while (!queue.isEmpty()) {
            SearchTreeNode x = queue.poll();
            if (x.depth == level) {
                leafNode.add(x);
                continue;
            }
            for (Constants.Directions direction : Constants.Directions.values()) {
                int directionIndex = direction.getDirectionValue();
                if (x.childNodes[directionIndex] != null)
                    queue.offer(x.childNodes[directionIndex]);
            }
        }
    }


    private GameBoard findNextStep(SearchTreeNode leafNode) {
        SearchTreeNode x = leafNode;
        while (x.depth != 1)
            x = x.parentNode;
        return x.board;
    }


    public GameBoard findNextStep(int moves) {
        double maxValue = 0;
        int index = 0;
        for (int i = 0; i < leafNode.size(); i++) {
            double x = leafNode.get(i).board.estimate(moves);
            if (x > maxValue) {
                maxValue = x;
                index = i;
            }
        }
        return findNextStep(leafNode.get(index));
    }


    private class SearchTreeNode {
        private SearchTreeNode parentNode;
        private SearchTreeNode[] childNodes;
        private int depth;
        private GameBoard board;

        SearchTreeNode(GameBoard board) {
            depth = 0;
            this.board = board;
        }

        void createChildNodes() {
            childNodes = new SearchTreeNode[Constants.number_of_directions];
            for (Constants.Directions direction : Constants.Directions.values()) {
                GameBoard moved_board = board.move(direction);
             
                if (moved_board != null) {
                    int directionValue = direction.getDirectionValue();
                    childNodes[directionValue] = new SearchTreeNode(moved_board);
                    childNodes[directionValue].parentNode = this;
                    childNodes[directionValue].depth = depth + 1;
                }
            }
        }
    }
}

