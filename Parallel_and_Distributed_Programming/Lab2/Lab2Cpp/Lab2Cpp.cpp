#include <iostream>
#include <thread>
#include <string>
#include <fstream>
#include <chrono>
#include <typeinfo>
#include <exception>
#include <stdexcept>
#include <mutex>
using namespace std;

class my_barrier {
    public: 
        my_barrier(int count) : thread_count(count), counter(0), waiting(0) {} 
          void wait() { 
              //fence mechanism 
              std::unique_lock<std::mutex> lk(m); 
              ++counter; 
              ++waiting; 
              cv.wait(lk, [&]{return counter >= thread_count;}); 
              cv.notify_one(); 
              --waiting; 
              if(waiting == 0) {
                  //reset barrier 
                  counter = 0;
              } 
              lk.unlock();
          } 
    private: 
        std::mutex m; 
        std::condition_variable cv; 
        int counter; 
        int waiting; 
        int thread_count; 
};

int p;
int n;
int m;
int k = 3;
int seqOrParam;
string fileName;

int calcElementConv(int** matrix, int** convMatrix, int linie, int col) {
    int linInceput = linie - k / 2;
    int linFinal = linie + k / 2;
    int colInceput = col - k / 2;
    int colFinal = col + k / 2;
    int suma = 0;
    for (int i = linInceput; i <= linFinal; i++) {
        for (int j = colInceput; j <= colFinal; j++) {
            if (i >= 0 && j >= 0 && i < n && j < m) {
                suma += matrix[i][j] * convMatrix[i - linInceput][j - colInceput];
            }
        }
    }
    return suma;
}

void citireMatrici(int** matrix, int** convMatrix) {
    ifstream file(fileName);
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            int a;
            file >> a;
            matrix[i][j] = a;
        }
    }
    for (int i = 0; i < k; i++) {
        for (int j = 0; j < k; j++) {
            int a;
            file >> a;
            convMatrix[i][j] = a;
        }
    }
    file.close();
}

void writeFile(string numeFisier, int** newMatrix) {
    ofstream out(numeFisier);
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            out << newMatrix[i][j] << " ";
        }
        out << endl;
    }
    out.close();
}

void func_thread(int linieStart, int linieFinal, int** matrix, int** matrixConv, my_barrier* barrier) {
    int* firstAux = new int[m];
    int* secondAux = new int[m];
    int* firstRow = new int[m];
    int* lastRow = new int[m];

    //calc elem first row
    for (int j = 0; j < m; j++) {
        firstRow[j] = calcElementConv(matrix, matrixConv, linieStart, j);
    }

    //calc elem second row
    for (int j = 0; j < m; j++) {
        lastRow[j] = calcElementConv(matrix, matrixConv, linieFinal - 1, j);
    }

    //calc intern rows
    for (int i = linieStart + 1; i < linieFinal - 1; i++) {
        if (i > linieStart + 2) {
            for (int j = 0; j < m; j++) {
                matrix[i - 2][j] = firstAux[j];
            }
        }
        for (int j = 0; j < m; j++) {
            firstAux[j] = secondAux[j];
        }
        for (int j = 0; j < m; j++) {
            secondAux[j] = calcElementConv(matrix, matrixConv, i, j);
        }
    }
    for (int j = 0; j < m; j++) {
        matrix[linieFinal - 3][j] = firstAux[j];
        matrix[linieFinal - 2][j] = secondAux[j];
    }

    (*barrier).wait();

    //write first last rows
    for (int j = 0; j < m; j++) {
        matrix[linieStart][j] = firstRow[j];
        matrix[linieFinal - 1][j] = lastRow[j];
    }

    delete firstAux;
    delete secondAux;
    delete firstRow;
    delete lastRow;
}

void calcParalel(int** matrix, int** convMatrix) {
    my_barrier* barrier = new my_barrier(p);
    int cat = n / p;
    int rest = n % p;
    thread* threads = new thread[p];
    int start = 0;
    int end = 0;
    int i;
    for (i = 0; i < p; i++) {
        end = start + cat;
        if (rest > 0) {
            end++;
            rest--;
        }
        threads[i] = thread(func_thread, start, end, matrix, convMatrix, std::ref(barrier));
        start = end;
    }

    for (int i = 0; i < p; i++) {
        threads[i].join();
    }
}

void calcSeq(int** matrix, int** convMatrix) {
    int* firstAux = new int[m];
    int* secondAux = new int[m];
    for (int i = 0; i < n; i++) {
        if (i > 1) {
            for (int j = 0; j < m; j++) {
                matrix[i - 2][j] = firstAux[j];
            }
        }
        for (int j = 0; j < m; j++) {
            firstAux[j] = secondAux[j];
        }
        for (int j = 0; j < m; j++) {
            secondAux[j] = calcElementConv(matrix, convMatrix, i, j);
        }
    }
    for (int j = 0; j < m; j++) {
        matrix[n - 2][j] = firstAux[j];
        matrix[n - 1][j] = secondAux[j];
    }
    delete firstAux;
    delete secondAux;
}

int main(int argc, char* argv[])
{
    p = stoi(argv[1]);
    fileName = argv[2];
    seqOrParam = stoi(argv[3]);
    n = stoi(argv[4]);
    m = stoi(argv[5]);

    int** matrix = new int* [n];
    for (int i = 0; i < n; i++) {
        matrix[i] = new int[m];
    }
    int** convMatrix = new int* [k];
    for (int i = 0; i < k; i++) {
        convMatrix[i] = new int[k];
    }

    citireMatrici(matrix, convMatrix);

    if (seqOrParam == 0) {

        auto t_start = chrono::steady_clock::now();
        calcSeq(matrix, convMatrix);
        auto t_final = chrono::steady_clock::now();
        writeFile("output1.txt", matrix);
        auto diff = t_final - t_start;
        cout << chrono::duration <double, milli>(diff).count() * 1000000;

    }
    else {
        auto t_start = chrono::steady_clock::now();
        calcParalel(matrix, convMatrix);
        auto t_final = chrono::steady_clock::now();
        writeFile("output2.txt", matrix);
        auto diff = t_final - t_start;
        cout << chrono::duration <double, milli>(diff).count() * 1000000;
    }


    for (int i = 0; i < n; i++) {
        delete matrix[i];
    }
    delete matrix;
    for (int i = 0; i < k; i++) {
        delete convMatrix[i];
    }
    delete convMatrix;
}


