using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Final_Project.graph
{
    public class Components<T>
    {
        private Graph<T> graph;

        public Components(Graph<T> graph)
        {
            this.graph = graph;
        }

        /***
         * a classic depth first method, recursively.
         * @param node the current node that is visited.
         * @param visited to remember the nodes that were already visited.
         * @param currentComponent where are stored all the new nods.
         */
        private void DFS(T node, HashSet<T> visited, List<T> currentComponent) 
        {
            if (visited.Contains(node)) return;
            visited.Add(node);
            currentComponent.Add(node);
            foreach (T nextNode in graph.map[node])
            {
                if (!visited.Contains(nextNode))
                    DFS(nextNode, visited, currentComponent);
            }
        }

        /***
         * Computes the number of the connected components in the graph
         * It runs a DFS starting in every single node and when it finds
         * a not already visited node, it adds to a list
         * @return a list of lists of elements representing the list of components
         */
        public List<List<T>> ConnectedComponents()
        {
            List<List<T>> components = new List<List<T>>();
            HashSet<T> visited = new HashSet<T>();
            foreach (var node in graph.map.Keys) 
            {
                if (!visited.Contains(node))
                {
                    List<T> currentComponent = new List<T>();
                    DFS(node, visited, currentComponent);
                    components.Add(currentComponent);
                }
            }
            return components;
        }
    }
}
