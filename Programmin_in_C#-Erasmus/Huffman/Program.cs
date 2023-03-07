using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Security.Cryptography;
using System.Text;
using static System.Net.WebRequestMethods;

namespace Lab5
{
    class Node
    {
        Node left; // the left descendant
        Node right; // the right descendant
        long prob; // the frequency (probability)
        int code; // the decimal code

        public Node(Node left, Node right, long prob, int code)
        {
            this.left = left;
            this.right = right;
            this.prob = prob;
            this.code = code;           
        }
        public Node(long prob, int code) 
        {
            left = null;
            right = null;
            this.prob = prob;
            this.code = code;
        }

        //for inner nodes we put the code 256, so that they will be choose after the leaf nodes
        public Node(Node left, Node right, long prob)
        {
            this.left = left;
            this.right = right;
            this.prob = prob;
            code = 256;
        }

        public Node getLeft()
        {
            return left;
        }

        public Node getRight()
        {
            return right;
        }

        public void setLeft(Node l)
        {
            left = l;
        }

        public void setRight(Node r)
        {
            right = r;
        }

        public long getProbability()
        {
            return prob;
        }

        public int getCode()
        {
            return code;
        }
    }

    class Reader
    {
        String file;
        long[] characters = new long[256];

        public Reader(String file)
        {
            this.file = file;
            for (int i = 0; i < 255; i++)
            {
                characters[i] = 0;
            }
        }

        /* reads all characters under the form of bytes and computes their frequencies
         * 
         * return: int[] - of size 256 - frequencies of all 256 ASCII characters
         */
        public long[] readCharacters_computeFrequency()
        {
            var b = new byte[1024];

            using (FileStream fs = new FileStream(file, FileMode.Open, FileAccess.Read))
            {
                int nr = fs.Read(b, 0, 1024);
                while (nr != 0)
                {
                    for (int i = 0; i < nr; i++)
                    {
                        characters[(int)b[i]]++;
                    }
                    nr = fs.Read(b, 0, 1024);
                }
            }
            
            return characters;
        }
    }

    class HuffmanControler
    {
        long[] characters = new long[256];
        private Reader reader;
        private Printer writer;
        private String file;
        List<Node> nodes;
        List<StringBuilder> hufCodes;


        /* initialize frequencies of all characters with 0
         */
        private void init_codes()
        {
            hufCodes = new List<StringBuilder>();
            for (int i=0; i< 256; i++)
            {
                hufCodes.Add(new StringBuilder(""));
            }
        }

        private void init_chars()
        {
            for (int i = 0; i < 255; i++)
            {
                characters[i] = 0;
            }
        }

        public HuffmanControler(String file, TextWriter writer)
        {
            this.reader = new Reader(file);
            nodes = new List<Node>();
            this.file = file;
            init_chars();
        }


        /*
         * get the frequencies of all characters
         */
        private void create_frequencies()
        {
            characters = reader.readCharacters_computeFrequency();
        }

        /*
         * inserts a node into the right place, so the list stays sorted
         * param: Node node - the given node
         */
        private void insert_Node(Node node)
        {
            int i = 0;
            while (i < nodes.Count && 
                (nodes[i].getProbability() < node.getProbability() || // compare by probabilty
                (nodes[i].getProbability() ==  node.getProbability() && nodes[i].getCode() <= node.getCode()))) // then compare by code
            {
                i++;
            }
            if (i == nodes.Count)
            {
                nodes.Add(node);
            }
            else
            {
                nodes.Insert(i, node);
            }
        }

        /* for each node that has a freq > 0 we create a node and insert it into the nodes: List<Node>
         * the list stays sorted
         */
        private void create_nodes()
        {
            for (int i = 0; i < 256; i++)
            {
                if (characters[i] != 0)
                {
                    Node node = new Node(characters[i], i);
                    insert_Node(node);
                }
            }
        }
        /*
         * we create the huffman tree by the known algorithm
         * at the end, List<Node> nodes.size() == 1 and it contains the root of the tree
         */
        private void create_Huff_tree()
        {
            int n = nodes.Count;

            // we know for sure that there will be n-1 steps (mathematically)
            for (int i = 1; i < n; i++)
            {
                Node left = nodes[0];
                nodes.RemoveAt(0);
                Node right = nodes[0];
                nodes.RemoveAt(0);
                Node newNode = new Node(left, right, left.getProbability() + right.getProbability());
                insert_Node(newNode);
            }
        }

        private void compute_HufCodes(string code, Node node)
        {
            if(node.getLeft() == null && node.getRight() == null)
            {
                hufCodes[node.getCode()].Append(code);
            }
            else
            {
                code = code + "0";
                compute_HufCodes(code, node.getLeft());
                code = code.Substring(0, code.Length - 1) + '1';
                compute_HufCodes(code, node.getRight());
            }
        }


        public void printHuffTree()
        {
            create_frequencies();
            create_nodes();
            create_Huff_tree();
            // if (nodes.Count > 0)
            //    writer.printHufTree(nodes[0]);
            init_codes();
            String code = "";
            compute_HufCodes(code, nodes[0]);
            FileCoder coder = new FileCoder(file, nodes[0], hufCodes);
            coder.printCodedFile();
            
        }
    }

    class FileCoder
    {
        private string infile;
        private string outfile;
        Node root; // the root of the huffman tree
        List<StringBuilder> hufCodes; // a list of huffman codes of every byte from 0 to 255
        StringBuilder bufferCodes = new StringBuilder(); // a string generated by the coded bytes; max size = 1024*(length of code of byte)
        byte[] buffer; // the buffer where we will add the bytes in order to be written; max size = 1024
        int indexBuf; // the index of the last byte in the buffer

        public FileCoder(string infile, Node root, List<StringBuilder> hufCodes)
        {
            this.infile = infile;
            outfile = infile + ".huff";
            this.root = root;
            this.hufCodes = hufCodes;
            buffer = new byte[1024];
            indexBuf = 0;
        }
        //prints the buffer and resets the index to 0
        private void printBuffer(FileStream o)
        {
            o.Write(buffer, 0, indexBuf);
            indexBuf = 0;
        }



        public void printCodedFile()
        {
            using (FileStream outF = new FileStream(outfile, FileMode.Create, FileAccess.Write))
            {
                printHeader(outF);
                printHufTree(outF);
                printFile(outF);
            }
        }

        //prints the header format
        private void printHeader(FileStream o)
        {
            buffer[indexBuf++] = Convert.ToByte("0x7B", 16);
            buffer[indexBuf++] = Convert.ToByte("0x68", 16);
            buffer[indexBuf++] = Convert.ToByte("0x75", 16);
            buffer[indexBuf++] = Convert.ToByte("0x7C", 16);
            buffer[indexBuf++] = Convert.ToByte("0x6D", 16);
            buffer[indexBuf++] = Convert.ToByte("0x7D", 16);
            buffer[indexBuf++] = Convert.ToByte("0x66", 16);
            buffer[indexBuf++] = Convert.ToByte("0x66", 16);
            printBuffer(o);
        }

        // goes in preorder through the huffman tree, creates a string with everycode concatenated and then prints the bytes
        private void printHufTree(FileStream o)
        {
            Stack<Node> nodeStack = new Stack<Node>();
            nodeStack.Push(root);
            StringBuilder nodeBits = new StringBuilder();
            while (nodeStack.Count > 0)
            {
                Node node = nodeStack.Peek();
                nodeBits.Clear();
                if (node.getCode() != 256) // a leaf node
                {
                    nodeBits.Append("1");
                    var weigh = node.getProbability();
                    for(int i = 0; i < 55; i++)
                    {
                        nodeBits.Append(weigh % 2);
                        weigh /= 2;
                    }
                    var code = node.getCode();
                    for(int i = 0; i < 8; i++)
                    {
                        nodeBits.Append(code % 2);
                        code /= 2;
                    }
                }
                else // a inner node
                {
                    nodeBits.Append("0");
                    var weigh = node.getProbability();
                    for (int i = 0; i < 55; i++)
                    {
                        nodeBits.Append(weigh % 2);
                        weigh /= 2;
                    }
                    nodeBits.Append("00000000");
                }
                bufferCodes.Append(nodeBits);
                nodeStack.Pop();
                if (node.getRight() != null)
                {
                    nodeStack.Push(node.getRight());
                }
                if (node.getLeft() != null)
                {
                    nodeStack.Push(node.getLeft());
                }
            }
            nodeBits.Clear();
            for(int i = 0; i < 64; i++)
                nodeBits.Append("0");
            bufferCodes.Append(nodeBits);
            emptyBufferCodes(o);
            printBuffer(o);
        }

        //from the bufferCodes, a string of 0 and 1, creates mathematically the bytes and adds them to the buffer
        //if the buffer gets full, we print it
        //in bufferCodes remains only the last bits which are too few for a byte, if they exists
        private void emptyBufferCodes(FileStream o)
        {
            int index = 0;
            for(index = 0; index < bufferCodes.Length - (bufferCodes.Length % 8); index = index + 8)
            {
                int byteCode = 0;
                int ex = 1;
                for (int i = index; i < index + 8; i ++)
                {
                    int bit = bufferCodes[i] - 48;
                    byteCode += bit * ex;
                    ex = ex * 2;
                }
                if (indexBuf == 1024)
                {
                    printBuffer(o);
                }
                buffer[indexBuf++] = (byte)byteCode;
            }
            StringBuilder reamining = new StringBuilder();
            if (bufferCodes.Length % 8 != 0)
            {
                reamining.Append(bufferCodes, index, bufferCodes.Length % 8);
                bufferCodes = reamining;
            }
            else
            {
                bufferCodes.Clear();
            }
        }
            
        //we take the bytes read from the file, with the given length, and construct a string (bufferCodes) from each byte's huffman code
        private void codeFile(FileStream o, byte[] initialBytes, int length)
        {
            int contor = 0;
            var nextCode = hufCodes[initialBytes[contor]];
            contor++;
            while (contor < length)
            {
                bufferCodes.Append(nextCode);
                nextCode = hufCodes[initialBytes[contor]];
                contor++;
            }
            bufferCodes.Append(nextCode);
            emptyBufferCodes(o);
        }

        //we add 0's to the end of bufferCodes until it's enough for a byte
        private void addZeroToEnd()
        {
            while (bufferCodes.Length < 8)
                bufferCodes.Append("0");
        }

        //prints the body of the file coded
        private void printFile(FileStream o)
        {
            var b = new byte[1024];
            using (FileStream inF = new FileStream(infile, FileMode.Open, FileAccess.Read))
            {
                int nr = inF.Read(b, 0, 1024);
                while (nr != 0)
                {
                    codeFile(o, b, nr);
                    nr = inF.Read(b, 0, 1024);
                }
                //if we still have some codes unwritten
                if (bufferCodes.Length > 0)
                {
                    emptyBufferCodes(o);
                    if (bufferCodes.Length > 0)
                    {
                        addZeroToEnd();
                        emptyBufferCodes(o);
                    }
                }
                if (indexBuf > 0)
                    printBuffer(o);
            }
        }
    }

    class Printer
    {
        private TextWriter writer;
        StringBuilder huffTree = new StringBuilder("");
        Node root;


        public Printer(TextWriter writer, Node root)
        {
            this.writer = writer;
            this.root = root;
        }

        /*
         * prints the huffman tree in preorder
         */
        private void getHuffTree()
        {

            if (root == null)
            {
                return;
            }
            Stack<Node> nodeStack = new Stack<Node>();
            nodeStack.Push(root);
            while (nodeStack.Count > 0)
            {
                Node node = nodeStack.Peek();
                if (node.getCode() != 256)
                {
                    huffTree.Append("*");
                    huffTree.Append(node.getCode());
                    huffTree.Append(":");
                    huffTree.Append(node.getProbability());
                    huffTree.Append(" ");
                }
                else
                {
                    huffTree.Append(node.getProbability());
                    huffTree.Append(" ");
                }
                nodeStack.Pop();
                if (node.getRight() != null)
                {
                    nodeStack.Push(node.getRight());
                }
                if (node.getLeft() != null)
                {
                    nodeStack.Push(node.getLeft());
                }
            }
        }

        public void printHufTree()
        {
            huffTree.Clear();
            getHuffTree();
            huffTree.Remove(huffTree.Length - 1, 1); // deletes the last space of the string
            writer.Write(huffTree);
        }


    }

    internal class Program
    {
        private static void Main(string[] args)
        {
            if (args.Length != 1)
            {
                Console.WriteLine("Argument Error");
            }
            else
            {
                try
                {
                    
                    //TextWriter writer = new StreamWriter("out.txt");
                    HuffmanControler ctrl = new HuffmanControler(args[0], Console.Out);
                    ctrl.printHuffTree();
                    //writer.Close();
                }
                catch(IOException)
                {
                    Console.WriteLine("File Error");
                }
            }
        }
    }
}