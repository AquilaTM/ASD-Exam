import java.util.*;

public class GraphServices {

    public static <V> void bfs(Graph<V> g) {
        for(Node<V> n : g.getNodes()){
            if(n.state == Node.Status.UNEXPLORED){
                bfs_from_node(g, n);
            }
        }
        System.out.println("");
    }

    public static <V> void bfs_from_node(Graph<V> g, Node<V> source){
        if(source.state != Node.Status.UNEXPLORED) return;

        Queue<Node<V>> q = new ArrayDeque<>();
        source.state = Node.Status.EXPLORED;
        q.add(source);

        while(!q.isEmpty()){
            Node<V> n = q.remove();
            n.state = Node.Status.EXPLORED;
            System.out.print(n.getValue()+" ");

            for(Edge<V> e : g.getOutEdges(n)){
                Node<V> v = e.getTarget();

                if(v.state == Node.Status.UNEXPLORED){
                    q.add(v);
                }
            }
        }
    }

    public static <V> void sssp(Graph<V> g, Node<V> source) {
        MinHeap<Node<V>> pqueue = new MinHeap<>();
        HashMap<Node<V>, HeapEntry<Node<V>>> dist = new HashMap<>();

        int INFINITY = 100000;

        for(Node<V> n : g.getNodes()){
            HeapEntry<Node<V>> h = pqueue.insert(n == source ? 0 : INFINITY, n);
            dist.put(n, h);
        }

        while(!pqueue.isEmpty()){
            HeapEntry<Node<V>> min = pqueue.removeMin();
            Node<V> min_node = min.getValue();

            for(Edge<V> e : g.getOutEdges(min_node)){
                Node<V> target_node = e.getTarget();
                if(dist.get(min_node).getKey() + e.getWeight() < dist.get(target_node).getKey()){
                    pqueue.replaceKey(dist.get(target_node), dist.get(min_node).getKey() + e.getWeight());
                }
            }
        }

        for(Node<V> n : dist.keySet()){
            System.out.print("SP to "+n.getValue()+": "+ dist.get(n).getKey()+"\n");
        }
        System.out.println("");
    }
    
    public static <V> void mst(Graph<V> G) {
        Partition<V> P;
        MinHeap<Edge<V>> Q;

        int i = 0;
        for(Node<V> n : G.getNodes())
            n.map = i++;

        P = new Partition<V>(G.getNodes());
        Q = new MinHeap<Edge<V>>();
        for(Edge<V> e : G.getEdges())
            Q.insert(e.getWeight(), e);

        while(!Q.isEmpty())	{
            Edge<V> e = Q.removeMin().getValue();
            Node<V> u = e.getSource(), v = e.getTarget();
            
            if(P.find(u.map) != P.find(v.map)) {
                System.out.println(u.getValue() + " " + v.getValue());
                P.union(u.map, v.map);
            }
        }
    }
}
