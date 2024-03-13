#include <iostream>
#include <thread>
#include <string>
#include <fstream>
#include <chrono>
#include <typeinfo>
#include <exception>
#include <stdexcept>
using namespace std;

int p;
int n;
int m;
int k;
int seqOrParam;
string fileName;
int impartireThreaduri;
int alocareMatrici;

int matrix1[10][10];
int newMatrix1[10][10];
int convMatrix1[3][3];

int matrix1000[1000][1000];
int newMatrix1000[1000][1000];
int convMatrix1000[5][5];

int matrix10[10][10000];
int newMatrix10[10][10000];
int convMatrix10[5][5];

int matrix10000[10000][10];
int newMatrix10000[10000][10];
int convMatrix10000[5][5];

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

void calcMatrix(int** matrixInitial, int** matrixFinal, int** convMatrix,
    int linieInceput, int linieFinal, int colInceput, int colFinal) {

    for (int i = linieInceput; i < linieFinal; i++) {
        for (int j = colInceput; j < colFinal; j++) {
            matrixFinal[i][j] = calcElementConv(matrixInitial, convMatrix, i, j);
        }
    }
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

void paralelLinii(int** matrix, int** newMatrix, int** convMatrix) {
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
        threads[i] = thread(calcMatrix, matrix, newMatrix, convMatrix, start, end, 0, m);
        start = end;
    }

    for (int i = 0; i < p; i++) {
        threads[i].join();
    }
}

void paralelCol(int** matrix, int** newMatrix, int** convMatrix) {
    int cat = m / p;
    int rest = m % p;
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
        threads[i] = thread(calcMatrix, matrix, newMatrix, convMatrix, 0, n, start, end);
        start = end;
    }

    for (int i = 0; i < p; i++) {
        threads[i].join();
    }
}




/////////////////////alocare statica 10 10/////////////////////////
int calcElementConvStatic1(int linie, int col) {
    int linInceput = linie - k / 2;
    int linFinal = linie + k / 2;
    int colInceput = col - k / 2;
    int colFinal = col + k / 2;
    int suma = 0;
    for (int i = linInceput; i <= linFinal; i++) {
        for (int j = colInceput; j <= colFinal; j++) {
            if (i >= 0 && j >= 0 && i < n && j < m) {
                suma += matrix1[i][j] * convMatrix1[i - linInceput][j - colInceput];
            }
        }
    }
    return suma;
}

void calcMatrixStatic1(int linieInceput, int linieFinal, int colInceput, int colFinal) {

    for (int i = linieInceput; i < linieFinal; i++) {
        for (int j = colInceput; j < colFinal; j++) {
            newMatrix1[i][j] = calcElementConvStatic1(i, j);
        }
    }
}

void citireMatriciStatic1() {
    ifstream file(fileName);
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            int a;
            file >> a;
            matrix1[i][j] = a;
        }
    }
    for (int i = 0; i < k; i++) {
        for (int j = 0; j < k; j++) {
            int a;
            file >> a;
            convMatrix1[i][j] = a;
        }
    }
    file.close();
}

void writeFileStatic1(string numeFisier) {
    ofstream out(numeFisier);
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            out << newMatrix1[i][j] << " ";
        }
        out << endl;
    }
    out.close();
}

void paralelLiniiStatic1() {
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
        threads[i] = thread(calcMatrixStatic1, start, end, 0, m);
        start = end;
    }

    for (int i = 0; i < p; i++) {
        threads[i].join();
    }
}

void paralelColStatic1() {
    int cat = m / p;
    int rest = m % p;
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
        threads[i] = thread(calcMatrixStatic1, 0, n, start, end);
        start = end;
    }

    for (int i = 0; i < p; i++) {
        threads[i].join();
    }
}




///////////////////////alocare statica 1000 1000/////////////////////////
int calcElementConvStatic1000(int linie, int col) {
    int linInceput = linie - k / 2;
    int linFinal = linie + k / 2;
    int colInceput = col - k / 2;
    int colFinal = col + k / 2;
    int suma = 0;
    for (int i = linInceput; i <= linFinal; i++) {
        for (int j = colInceput; j <= colFinal; j++) {
            if (i >= 0 && j >= 0 && i < n && j < m) {
                suma += matrix1000[i][j] * convMatrix1000[i - linInceput][j - colInceput];
            }
        }
    }
    return suma;
}

void calcMatrixStatic1000(int linieInceput, int linieFinal, int colInceput, int colFinal) {

    for (int i = linieInceput; i < linieFinal; i++) {
        for (int j = colInceput; j < colFinal; j++) {
            newMatrix1000[i][j] = calcElementConvStatic1000(i, j);
        }
    }
}

void citireMatriciStatic1000() {
    ifstream file(fileName);
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            int a;
            file >> a;
            matrix1000[i][j] = a;
        }
    }
    for (int i = 0; i < k; i++) {
        for (int j = 0; j < k; j++) {
            int a;
            file >> a;
            convMatrix1000[i][j] = a;
        }
    }
    file.close();
}

void writeFileStatic1000(string numeFisier) {
    ofstream out(numeFisier);
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            out << newMatrix1000[i][j] << " ";
        }
        out << endl;
    }
    out.close();
}

void paralelLiniiStatic1000() {
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
        threads[i] = thread(calcMatrixStatic1000, start, end, 0, m);
        start = end;
    }

    for (int i = 0; i < p; i++) {
        threads[i].join();
    }
}

void paralelColStatic1000() {
    int cat = m / p;
    int rest = m % p;
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
        threads[i] = thread(calcMatrixStatic1000, 0, n, start, end);
        start = end;
    }

    for (int i = 0; i < p; i++) {
        threads[i].join();
    }
}



///////////////////////alocare statica 10 10000/////////////////////////
int calcElementConvStatic10(int linie, int col) {
    int linInceput = linie - k / 2;
    int linFinal = linie + k / 2;
    int colInceput = col - k / 2;
    int colFinal = col + k / 2;
    int suma = 0;
    for (int i = linInceput; i <= linFinal; i++) {
        for (int j = colInceput; j <= colFinal; j++) {
            if (i >= 0 && j >= 0 && i < n && j < m) {
                suma += matrix10[i][j] * convMatrix10[i - linInceput][j - colInceput];
            }
        }
    }
    return suma;
}

void calcMatrixStatic10(int linieInceput, int linieFinal, int colInceput, int colFinal) {

    for (int i = linieInceput; i < linieFinal; i++) {
        for (int j = colInceput; j < colFinal; j++) {
            newMatrix10[i][j] = calcElementConvStatic10(i, j);
        }
    }
}

void citireMatriciStatic10() {
    ifstream file(fileName);
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            int a;
            file >> a;
            matrix10[i][j] = a;
        }
    }
    for (int i = 0; i < k; i++) {
        for (int j = 0; j < k; j++) {
            int a;
            file >> a;
            convMatrix10[i][j] = a;
        }
    }
    file.close();
}

void writeFileStatic10(string numeFisier) {
    ofstream out(numeFisier);
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            out << newMatrix10[i][j] << " ";
        }
        out << endl;
    }
    out.close();
}

void paralelLiniiStatic10() {
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
        threads[i] = thread(calcMatrixStatic10, start, end, 0, m);
        start = end;
    }

    for (int i = 0; i < p; i++) {
        threads[i].join();
    }
}

void paralelColStatic10() {
    int cat = m / p;
    int rest = m % p;
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
        threads[i] = thread(calcMatrixStatic10, 0, n, start, end);
        start = end;
    }

    for (int i = 0; i < p; i++) {
        threads[i].join();
    }
}



///////////////////////alocare statica 10000 10/////////////////////////
int calcElementConvStatic10000(int linie, int col) {
    int linInceput = linie - k / 2;
    int linFinal = linie + k / 2;
    int colInceput = col - k / 2;
    int colFinal = col + k / 2;
    int suma = 0;
    for (int i = linInceput; i <= linFinal; i++) {
        for (int j = colInceput; j <= colFinal; j++) {
            if (i >= 0 && j >= 0 && i < n && j < m) {
                suma += matrix10000[i][j] * convMatrix10000[i - linInceput][j - colInceput];
            }
        }
    }
    return suma;
}

void calcMatrixStatic10000(int linieInceput, int linieFinal, int colInceput, int colFinal) {

    for (int i = linieInceput; i < linieFinal; i++) {
        for (int j = colInceput; j < colFinal; j++) {
            newMatrix10000[i][j] = calcElementConvStatic10000(i, j);
        }
    }
}

void citireMatriciStatic10000() {
    ifstream file(fileName);
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            int a;
            file >> a;
            matrix10000[i][j] = a;
        }
    }
    for (int i = 0; i < k; i++) {
        for (int j = 0; j < k; j++) {
            int a;
            file >> a;
            convMatrix10000[i][j] = a;
        }
    }
    file.close();
}

void writeFileStatic10000(string numeFisier) {
    ofstream out(numeFisier);
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            out << newMatrix10000[i][j] << " ";
        }
        out << endl;
    }
    out.close();
}

void paralelLiniiStatic10000() {
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
        threads[i] = thread(calcMatrixStatic10000, start, end, 0, m);
        start = end;
    }

    for (int i = 0; i < p; i++) {
        threads[i].join();
    }
}

void paralelColStatic10000() {
    int cat = m / p;
    int rest = m % p;
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
        threads[i] = thread(calcMatrixStatic10000, 0, n, start, end);
        start = end;
    }

    for (int i = 0; i < p; i++) {
        threads[i].join();
    }
}



//4 n1000m1000k5.txt 1 1000 1000 5 1 1
int main(int argc, char* argv[])
{
    p = stoi(argv[1]);
    fileName = argv[2];
    seqOrParam = stoi(argv[3]);
    n = stoi(argv[4]);
    m = stoi(argv[5]);
    k = stoi(argv[6]);
    impartireThreaduri = stoi(argv[7]);
    alocareMatrici = stoi(argv[8]);

    if (alocareMatrici == 1) {
        int** matrix = new int* [n];
        int** newMatrix = new int* [n];
        for (int i = 0; i < n; i++) {
            matrix[i] = new int[m];
            newMatrix[i] = new int[m];
        }
        int** convMatrix = new int* [k];
        for (int i = 0; i < k; i++) {
            convMatrix[i] = new int[k];
        }

        citireMatrici(matrix, convMatrix);

        if (seqOrParam == 0) {

            auto t_start = chrono::steady_clock::now();
            calcMatrix(matrix, newMatrix, convMatrix, 0, n, 0, m);
            auto t_final = chrono::steady_clock::now();
            writeFile("output1.txt", newMatrix);
            auto diff = t_final - t_start;
            cout << chrono::duration <double, milli>(diff).count() * 1000000;

        }
        else {
            auto t_start = chrono::steady_clock::now();
            if (impartireThreaduri == 0) {
                paralelLinii(matrix, newMatrix, convMatrix);
            }
            else if (impartireThreaduri == 1) {
                paralelCol(matrix, newMatrix, convMatrix);
            }
            auto t_final = chrono::steady_clock::now();
            writeFile("output2.txt", newMatrix);
            auto diff = t_final - t_start;
            cout << chrono::duration <double, milli>(diff).count() * 1000000;
        }


        for (int i = 0; i < n; i++) {
            delete matrix[i];
            delete newMatrix[i];
        }
        delete matrix;
        delete newMatrix;
        for (int i = 0; i < k; i++) {
            delete convMatrix[i];
        }
        delete convMatrix;
    }
    else {
        if (n == 10 && m == 10) {

            citireMatriciStatic1();

            if (seqOrParam == 0) {

                auto t_start = chrono::steady_clock::now();
                calcMatrixStatic1(0, n, 0, m);
                auto t_final = chrono::steady_clock::now();
                writeFileStatic1("output1.txt");
                auto diff = t_final - t_start;
                cout << chrono::duration <double, milli>(diff).count() * 1000000;

            }
            else {
                auto t_start = chrono::steady_clock::now();
                if (impartireThreaduri == 0) {
                    paralelLiniiStatic1();
                }
                else if (impartireThreaduri == 1) {
                    paralelColStatic1();
                }
                auto t_final = chrono::steady_clock::now();
                writeFileStatic1("output2.txt");
                auto diff = t_final - t_start;
                cout << chrono::duration <double, milli>(diff).count() * 1000000;
            }
        }
        else if (n == 1000 && m == 1000) {

            citireMatriciStatic1000();

            if (seqOrParam == 0) {

                auto t_start = chrono::steady_clock::now();
                calcMatrixStatic1000(0, n, 0, m);
                auto t_final = chrono::steady_clock::now();
                writeFileStatic1000("output1.txt");
                auto diff = t_final - t_start;
                cout << chrono::duration <double, milli>(diff).count() * 1000000;

            }
            else {
                auto t_start = chrono::steady_clock::now();
                if (impartireThreaduri == 0) {
                    paralelLiniiStatic1000();
                }
                else if (impartireThreaduri == 1) {
                    paralelColStatic1000();
                }
                auto t_final = chrono::steady_clock::now();
                writeFileStatic1000("output2.txt");
                auto diff = t_final - t_start;
                cout << chrono::duration <double, milli>(diff).count() * 1000000;
            }
        }
        else if (n == 10 && m == 10000) {

            citireMatriciStatic10();

            if (seqOrParam == 0) {

                auto t_start = chrono::steady_clock::now();
                calcMatrixStatic10(0, n, 0, m);
                auto t_final = chrono::steady_clock::now();
                writeFileStatic10("output1.txt");
                auto diff = t_final - t_start;
                cout << chrono::duration <double, milli>(diff).count() * 1000000;

            }
            else {
                auto t_start = chrono::steady_clock::now();
                if (impartireThreaduri == 0) {
                    paralelLiniiStatic10();
                }
                else if (impartireThreaduri == 1) {
                    paralelColStatic10();
                }
                auto t_final = chrono::steady_clock::now();
                writeFileStatic10("output2.txt");
                auto diff = t_final - t_start;
                cout << chrono::duration <double, milli>(diff).count() * 1000000;
            }
        }
        else if (n == 10000 && m == 10) {

            citireMatriciStatic10000();

            if (seqOrParam == 0) {

                auto t_start = chrono::steady_clock::now();
                calcMatrixStatic10000(0, n, 0, m);
                auto t_final = chrono::steady_clock::now();
                writeFileStatic10000("output1.txt");
                auto diff = t_final - t_start;
                cout << chrono::duration <double, milli>(diff).count() * 1000000;

            }
            else {
                auto t_start = chrono::steady_clock::now();
                if (impartireThreaduri == 0) {
                    paralelLiniiStatic10000();
                }
                else if (impartireThreaduri == 1) {
                    paralelColStatic10000();
                }
                auto t_final = chrono::steady_clock::now();
                writeFileStatic10000("output2.txt");
                auto diff = t_final - t_start;
                cout << chrono::duration <double, milli>(diff).count() * 1000000;
            }
        }
    }
}
