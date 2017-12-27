import java.util.ArrayList;
import java.util.HashMap;

class Node{
    private String shortName;
    private String longName;
    private Node parent;
    private HashMap<Node, Integer> actualDistances = new HashMap<>();
    private HashMap<Node, Integer> heuristicDistances = new HashMap<>();
    private int f = 0;
    private int g = 0;
    private int h = 0;


    /**
     * Instantiates a new Node.
     */
    public Node(){
    }

    /**
     * Instantiates a new Node.
     *
     * @param shortName the short name
     * @param longName  the long name
     */
    public Node(String shortName, String longName) {
        this.shortName = shortName;
        this.longName = longName;
    }

    /**
     * Gets short name.
     *
     * @return the short name
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * Gets long name.
     *
     * @return the long name
     */
    public String getLongName() {
        return longName;
    }

    /**
     * Gets g.
     *
     * @return g
     */
    public int getG() {
        return g;
    }

    /**
     * Gets f.
     *
     * @return f
     */
    public int getF() {
        return f;
    }

    /**
     * Gets parent.
     *
     * @return the parent Node
     */
    public Node getParent() {
        return parent;
    }

    /**
     * Gets actual distances.
     *
     * @return the actual distances
     */
    public HashMap<Node, Integer> getActualDistances() {
        return actualDistances;
    }

    /**
     * Set h.
     *
     * @param n the goal Node
     */
    public void setH(Node n){
        if(n.getLongName() != this.longName) {
            h = heuristicDistances.get(n);
        }
    }

    /**
     * Sets g.
     *
     * @param g
     */
    public void setG(int g) {
        this.g = g;
    }

    /**
     * Sets f.
     */
    public void setF() {
        f = g + h;
    }

    /**
     * Sets parent.
     *
     * @param parent Node
     */
    public void setParent(Node parent) {
        this.parent = parent;
    }

    /**
     * Build the graph.
     *
     * @return ArrayList of Nodes
     */
    public static ArrayList<Node> buildGraph(){
        //Record the short name, long name, edges (actualDistances), and heuristic distances into ArrayLists
        ArrayList<String> shortNames = FileParser.getShortNames();
        ArrayList<String> longNames = FileParser.getLongNames();
        ArrayList<String> actualDistances = FileParser.getActualDistances();
        ArrayList<String> heuristicDistances = FileParser.getHeuristicDistances();

        ArrayList<Node> nodes = new ArrayList<>();

        //Create all nodes and add to the graph
        for (int i = 0; i < shortNames.size(); i++) {
            nodes.add(new Node(shortNames.get(i), longNames.get(i)));
        }

        //Set the edges and heuristics per node
        int position = 0;
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = 0; j < nodes.size(); j++) {
                //edges
                if(!(actualDistances.get(j + position).equals("-"))){
                    nodes.get(i).addToActualDistances(nodes.get(j), Integer.valueOf(actualDistances.get(j + position)));

                    for (Node n : nodes) {
                        if(n.getShortName().equals(nodes.get(j).getShortName())){
                            n.addToActualDistances(nodes.get(i), Integer.valueOf(actualDistances.get(j + position)));
                            break;
                        }
                    }
                }
                //heuristics
                if(!(heuristicDistances.get(j + position).equals("-"))){
                    nodes.get(i).addToHeuristicDistances(nodes.get(j), Integer.valueOf(heuristicDistances.get(j + position)));

                    for (Node n : nodes) {
                        if(n.getShortName().equals(nodes.get(j).getShortName())){
                            n.addToHeuristicDistances(nodes.get(i), Integer.valueOf(heuristicDistances.get(j + position)));
                            break;
                        }
                    }
                }
            }
            position += 49;
        }
        return nodes;
    }

    /**
     * Add Nodes to the actual distances HashMap. Basically adds edges.
     *
     * @param n Node.
     * @param i Distance.
     */
    public void addToActualDistances(Node n, Integer i){
        actualDistances.put(n, i);
    }

    /**
     * Add to heuristic distances HashMap.
     *
     * @param n Node.
     * @param i Heuristic distance.
     */
    public void addToHeuristicDistances(Node n, Integer i){
        heuristicDistances.put(n, i);
    }

    /**
     * Compares two Nodes based on f value. Used to sort and choose the lowest f value node.
     *
     * @param n Node.
     * @return int. Positive result means the Node passed as an argument has a lower f value.
     * A negative result means the Node passed as an argument has a higher f value.
     */
    public int compareTo(Node n){
        return this.f - n.getF();

    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (!shortName.equals(node.shortName)) return false;
        return longName.equals(node.longName);
    }

    public int hashCode() {
        int result = shortName.hashCode();
        result = 31 * result + longName.hashCode();
        return result;
    }

    public String toString(){
        return longName + f;
    }
}