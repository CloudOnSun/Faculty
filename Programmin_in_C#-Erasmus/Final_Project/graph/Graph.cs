using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Final_Project.graph
{
    public class Graph<T>
    {
        public Dictionary<T, List<T>> map = new Dictionary<T, List<T>>();

        /***
         * Adds a node to the graph
         * for the node we're adding, then we'll associate an empty one.
         * @param t T - the node we'll add
         */
        public void addNode(T t)
        {
            if (!map.ContainsKey(t))
                map.Add(t, new List<T>());
        }

        public Graph(List<Tuple<T, T>> nodes)
        {
            foreach (var node in nodes)
            {
                addNode(node.Item1);
                addNode(node.Item2);
            }

            foreach (var node in nodes)
            {
                map[node.Item1].Add(node.Item2);
                map[node.Item2].Add(node.Item1);
            }
        }
    }
}
