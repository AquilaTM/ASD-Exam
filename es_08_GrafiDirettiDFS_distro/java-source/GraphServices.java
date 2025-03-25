
import java.util.LinkedList;


public class GraphServices<V> {

    public static <V> void sweep(Graph<V> g) {
        // Reset node's status
        g.resetStatus();
        
        int loctime = 0;
        for(Graph.Node<V> node : g.getNodes()) {
            System.out.println("Root " + g.getNodeValue(node));
            loctime += sweep_aux(node, loctime);
        }
    }

    private static <V> int sweep_aux(Graph.Node<V> node, int time) {
        if(node.state != Graph.Node.Status.UNEXPLORED)
            return 0;
        
        int loctime = 1;
        node.state = Graph.Node.Status.EXPLORING;
        node.timestamp = time;
        
        for(Graph.Node<V> cur : node.outEdges) {
            System.out.print("\t" + node.value + "(" + node.timestamp + ")->" + cur.value + "(" + cur.timestamp + ")");
            
            if (cur.state == Graph.Node.Status.EXPLORED) {
                if (node.timestamp < cur.timestamp)
                    System.out.println("FORWARD");
                else
                    System.out.println("CROSS");
            }
            else if (cur.state == Graph.Node.Status.EXPLORING) {
                System.out.println("BACK");
            }
            else {
                System.out.println("TREE");
                loctime += sweep_aux(cur, time + 1);
            }
        }
        
        node.state = Graph.Node.Status.EXPLORED;
        return loctime;
    }
    
    public static <V> void topologicalSort(Graph<V> g) {
        g.resetStatus();

        LinkedList<Graph.Node<V>> ts = new LinkedList<>();
        for(Graph.Node<V> n : g.getNodes()){
            if(n.state == Graph.Node.Status.UNEXPLORED){
                if(DDFS(n, ts) > 0){
                    System.out.println("Il grafo non è un DAG!");
                    return;
                }
            }
        }

        for(Graph.Node<V> n : ts)
            System.out.print(n.value + " ");
        System.out.println("");
    }
    
    public static <V> void strongConnectedComponents(Graph<V> g) {

        g.resetStatus();

        LinkedList<Graph.Node<V>> stack = new LinkedList<>();
        for(Graph.Node<V> n : g.getNodes()) {
            if(n.state == Graph.Node.Status.UNEXPLORED)
                DDFS(n, stack);
        }

        g.resetStatus();

        for(Graph.Node<V> n : stack){
            LinkedList<Graph.Node<V>> ret = new LinkedList<>();
            if(n.state == Graph.Node.Status.UNEXPLORED){
                transposedDFS(n, ret);
                System.out.println("Strongly connected component:");
                for(Graph.Node<V> cur : ret)
                    System.out.print(cur.value + " ");
                System.out.println("");
            }
        }
    }

    public static <V> int DDFS(Graph.Node<V> n, LinkedList<Graph.Node<V>> stack){
        if(n.state == Graph.Node.Status.EXPLORED)
            return 0;

        if(n.state == Graph.Node.Status.EXPLORING)
            return 1;

        n.state = Graph.Node.Status.EXPLORING;
        int ret = 0;

        for(Graph.Node<V> edge : n.outEdges) 
            ret += DDFS(edge, stack);

        n.state = Graph.Node.Status.EXPLORED;
        stack.addFirst(n);

        return ret;
    }

    public static <V> void transposedDFS(Graph.Node<V> n, LinkedList<Graph.Node<V>> ret){
        if(n.state == Graph.Node.Status.EXPLORED)
            return;

        if(n.state == Graph.Node.Status.EXPLORING)
            return;

        n.state = Graph.Node.Status.EXPLORING;

        for(Graph.Node<V> edge : n.inEdges){
            transposedDFS(edge, ret);
        }

        ret.addLast(n);
        n.state = Graph.Node.Status.EXPLORED;
    }
}
