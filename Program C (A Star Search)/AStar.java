import java.util.ArrayList;


public  class AStar {
    /**
     * Use A* search to find the path between the start and end nodes.
     * Resources used:
     * http://www.geeksforgeeks.org/a-search-algorithm/
     * http://www.cokeandcode.com/main/tutorials/path-finding
     *
     * @param start Start Node.
     * @param end   End (goal) node.
     * @param nodes All available nodes.
     */
    public static void startSearch(Node start, Node end, ArrayList<Node> nodes){
        ArrayList<Node> openList = new ArrayList<>();
        ArrayList<Node> closedList = new ArrayList<>();
        ArrayList<Node> visitedNodes = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<String>>> outputQueue = new ArrayList<>();
        openList.add(start);

        //Set h values
        for (Node n : nodes) {
            n.setH(end);
        }

        //Set g and f values
        for (Node n : start.getActualDistances().keySet()) {
            n.setG(start.getActualDistances().get(n));
            n.setF();
        }

        start.setF();

        //While openList is not empty
        mainLoop:
        while(!openList.isEmpty()){
            ArrayList<String> shortName = new ArrayList<>();
            ArrayList<String> fValue = new ArrayList<>();
            ArrayList<String> prevNode = new ArrayList<>();
            ArrayList<ArrayList<String>> oneLine = new ArrayList<>();
            //Sort by f value
            openList.sort(Node::compareTo);
            Node q = openList.get(0);

            //Pop q off of openList
            openList.remove(q);
            closedList.add(q);
            visitedNodes.add(q);

            //For each node directly connect to q
            for (Node n : q.getActualDistances().keySet()) {
                n.setParent(q);
                n.setG(q.getG() + q.getActualDistances().get(n));
                n.setF();

                //if openList contains n
                if(openList.contains(n)){
                    //If current n has a lower f cost than the openList n
                    if(n.getF() < openList.get(openList.indexOf(n)).getF()){
                        openList.remove(n);
                        if(closedList.contains(n)){
                            closedList.remove(n);
                        }
                    }
                }

                //If n is not found in openList or closedList
                if(!(openList.contains(n)) && !(closedList.contains(n))){
                    openList.add(n);
                }

                //if n is the goal
                if(n.equals(end)){
                    visitedNodes.add(n);
                    openList.sort(Node::compareTo);
                    for (Node m : openList) {
                        shortName.add(m.getShortName());
                        fValue.add(Integer.toString(m.getF()));
                        prevNode.add(m.getParent().getShortName());
                        oneLine.add(shortName);
                        oneLine.add(fValue);
                        oneLine.add(prevNode);
                    }
                    outputQueue.add(new ArrayList<>(oneLine));
                    break mainLoop;
                }
            }

            openList.sort(Node::compareTo);
            for (Node m : openList) {
                shortName.add(m.getShortName());
                fValue.add(Integer.toString(m.getF()));
                prevNode.add(m.getParent().getShortName());
                oneLine.add(shortName);
                oneLine.add(fValue);
                oneLine.add(prevNode);
            }
            outputQueue.add(new ArrayList<>(oneLine));
        }

        //generate output filename
        String fileName = start.getLongName() + "-" + end.getLongName() + ".txt";
        //Set up data to be written to file
        String fileData = formatResults(visitedNodes, outputQueue);
        //Write to file
        FileParser.exportToFile(fileName, fileData);
    }

    /**
     * Format the results so they look nice. Uses StringBuilder.
     *
     * @param visitedNodes the visited nodes
     * @param outputQueue  the queues of nodes
     * @return String. Formatted output string ready to be written to a file.
     */
    public static String formatResults(ArrayList<Node> visitedNodes, ArrayList<ArrayList<ArrayList<String>>> outputQueue){
        StringBuilder outputText = new StringBuilder();
        outputText.append("Node|");

        int columnCount = 0;
        for (ArrayList<ArrayList<String>> queue : outputQueue) {
            if(queue.get(0).size() > columnCount){
                columnCount = queue.get(0).size();
            }
        }

        for (int i = 0; i < columnCount; i++) {
            outputText.append("|Node|f()|Prev|");
        }
        outputText.append("\n----|");
        for (int i = 0; i < columnCount; i++) {
            outputText.append("|----|---|----|");
        }

        for (int i = 0; i < visitedNodes.size() - 1; i++) {
            outputText.append("\n ");
            outputText.append(visitedNodes.get(i).getShortName());
            outputText.append("|");

            for (int j = 0; j < outputQueue.get(i).get(0).size(); j++) {
                String toBeAppended = "|%4s|%3s|%4s|";
                toBeAppended = String.format(toBeAppended,
                        outputQueue.get(i).get(0).get(j),
                        outputQueue.get(i).get(1).get(j),
                        outputQueue.get(i).get(2).get(j));
                outputText.append(toBeAppended);
            }
        }

        outputText.append("\n ");
        outputText.append(visitedNodes.get(visitedNodes.size() - 1).getShortName());
        outputText.append("||\n\n");

        StringBuilder finalTrip = new StringBuilder();
        finalTrip.append(visitedNodes.get(0).getLongName());
        for (int i = 1; i < visitedNodes.size() - 1; i++) {
            if(outputQueue.get(i).get(2).get(0).equals(visitedNodes.get(i).getShortName())){
                finalTrip.append(" -> ");
                finalTrip.append(visitedNodes.get(i).getLongName());
            }
        }
        finalTrip.append(" -> ");
        finalTrip.append(visitedNodes.get(visitedNodes.size() - 1).getLongName());
        outputText.append(finalTrip);

        return outputText.toString();
    }
}
