#include <mpi.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fstream>
#include <iostream>
#include <chrono>

using namespace std;

int n = 1000;
int m = 1000;
int k = 3;


int calcElementConv(int** matrix, int convMatrix[3][3], int linie, int col) {
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

int main(int argc, char** argv)
{
	int err = 0;
	string filename = "D:\\Facultate\\Faculta\\An_3_sem_1\\PPD\\La3_MPI\\x64\\Debug\\n1000m1000.txt";
	string convfile = "D:\\Facultate\\Faculta\\An_3_sem_1\\PPD\\La3_MPI\\x64\\Debug\\convFile.txt";
	err = MPI_Init(&argc, &argv);
	if (err != MPI_SUCCESS) {
		MPI_Abort(MPI_COMM_WORLD, err);
	}

	int world_size;
	
	MPI_Comm_size(MPI_COMM_WORLD, &world_size);

	int world_rank;

	MPI_Comm_rank(MPI_COMM_WORLD, &world_rank);
	int segment_size = n / world_size;

	int convMatrix[3][3];
	int* convMatrixPointer = *convMatrix;
	int* matrixInit = 0;

	if (world_rank == 0) {

		//Kernel matrix
		ifstream convF(convfile);
		for (int i = 0; i < k; i++) {
			for (int j = 0; j < k; j++) {
				int a;
				convF >> a;
				convMatrix[i][j] = a;
			}
		}
		convF.close();

		matrixInit = new int [n*m];

		//matrix
		int linie = 0;
		ifstream file(filename);

		for (int i = 0; i < n * m; i++) {
			int a;
			file >> a;
			matrixInit[i] = a;
		}

		file.close();

	}
	MPI_Bcast(convMatrixPointer, 9, MPI_INT, 0, MPI_COMM_WORLD);

	int* matrixReceiver = new int [m*(segment_size + 2)];

	auto t_start = chrono::steady_clock::now();

	MPI_Scatter(matrixInit, segment_size*m, MPI_INT, matrixReceiver + m, segment_size*m, MPI_INT, 0, MPI_COMM_WORLD);

	for (int i = 0; i < m; i++) {
		matrixReceiver[i] = 0;
		matrixReceiver[(segment_size + 1) * m + i] = 0;
	}

	int** matrix = new int*[segment_size + 2];
	for (int i = 0; i < segment_size + 2; i++) {
		matrix[i] = new int[m];
	}
	int count = 0;
	for (int i = 0; i < segment_size + 2; i++) {
		for (int j = 0; j < m; j++) {
			int aaa = matrixReceiver[count];
			matrix[i][j] = aaa;
			count++;
		}
	}

	//read from above and send below
	if (world_rank != 0) {
		MPI_Recv(*(matrix + 0), m, MPI_INT, world_rank - 1, (world_rank - 1) * 400000, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
	}
	if (world_rank != (world_size - 1)) {
		MPI_Send(*(matrix + segment_size), m, MPI_INT, world_rank + 1, world_rank * 400000, MPI_COMM_WORLD);
	}

	//read from below and send above
	if (world_rank != (world_size - 1)) {
		MPI_Recv(*(matrix + (segment_size + 1)), m, MPI_INT, world_rank + 1, (world_rank + 1) * 700000, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
	}
	if (world_rank != 0) {
		MPI_Send(*(matrix + 1), m, MPI_INT, world_rank - 1, world_rank * 700000, MPI_COMM_WORLD);
	}

	//calc matrix
	int* firstAux = new int[m];
	int* secondAux = new int[m];
	for (int i = 1; i <= segment_size; i++) {
		if (i > 2) {
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
		matrix[segment_size - 1][j] = firstAux[j];
		matrix[segment_size][j] = secondAux[j];
	}
	delete firstAux;
	delete secondAux;

	count = 0;
	for (int i = 0; i < segment_size + 2; i++) {
		for (int j = 0; j < m; j++) {
			matrixReceiver[count] = matrix[i][j];
			count++;
		}
	}

	MPI_Gather(matrixReceiver + m, segment_size*m, MPI_INT, matrixInit, segment_size*m, MPI_INT, 0, MPI_COMM_WORLD);

	auto t_final = chrono::steady_clock::now();

	if (world_rank == 0) {
		ofstream out("output2.txt");
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				out << matrixInit[i*m + j] << " ";
			}
			out << endl;
		}
		out.close();

		auto diff = t_final - t_start;
		cout << chrono::duration <double, milli>(diff).count() * 1000000;
	}

	MPI_Finalize();
}

// Run program: Ctrl + F5 or Debug > Start Without Debugging menu
// Debug program: F5 or Debug > Start Debugging menu

// Tips for Getting Started: 
//   1. Use the Solution Explorer window to add/manage files
//   2. Use the Team Explorer window to connect to source control
//   3. Use the Output window to see build output and other messages
//   4. Use the Error List window to view errors
//   5. Go to Project > Add New Item to create new code files, or Project > Add Existing Item to add existing code files to the project
//   6. In the future, to open this project again, go to File > Open > Project and select the .sln file
