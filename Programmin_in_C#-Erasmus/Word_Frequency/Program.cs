using System;
using System.IO;
using System.Collections.Generic;
using System.Diagnostics.Metrics;
using System.Runtime.CompilerServices;
using static System.Net.Mime.MediaTypeNames;
using System.Linq;

namespace LabRecodEx1._1
{
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
                    Dictionary<string, int> wordFreq = new Dictionary<string, int>();
                    foreach (string line in System.IO.File.ReadLines(args[0]))
                    {
                        char[] separators = new char[] { ' ', '\n', '\t', };
                        string[] words = line.Split(separators);
                        for (int i = 0; i < words.Length; i++)
                        {
                            if (words[i].Length > 0)
                            {
                                if(wordFreq.ContainsKey(words[i]))
                                {
                                    wordFreq[words[i]]++;
                                }
                                else
                                {
                                    wordFreq.Add(words[i], 1);
                                }

                            }
                        }
                    }
                    wordFreq = wordFreq.OrderBy(obj => obj.Key).ToDictionary(obj => obj.Key, obj => obj.Value);
                    foreach (string word in wordFreq.Keys)
                    {
                        Console.WriteLine(word + ": " + wordFreq[word]);
                    }
                }
                catch (Exception)
                {
                    Console.WriteLine("File Error");
                }
            }
        }
    }
}