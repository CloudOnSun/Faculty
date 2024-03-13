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
	int segment_size = 0;
	if (world_size > 1)
		segment_size = n / (world_size - 1);


	int convMatrix[3][3];
	int* convMatrixPointer = *convMatrix;


	if (world_rank == 0) {

		//		MPI_Recv(&world_rank, 1, MPI_INT, 0, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

		//auto t_start = chrono::steady_clock::now();

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

	}
	MPI_Bcast(convMatrixPointer, 9, MPI_INT, 0, MPI_COMM_WORLD);

	if (world_rank == 0) {


		/*for (int p = 1; p < world_size; p++) {
			MPI_Send(convMatrixPointer, 9, MPI_INT, p, 99999, MPI_COMM_WORLD);
		}*/

		//read matrix and send to processes
		int** matrix = new int* [n];
		for (int i = 0; i < n; i++) {
			matrix[i] = new int[m];
		}

		int linie = 0;
		ifstream file(filename);

		for (int p = 1; p < world_size; p++) {

			for (int i = 0; i < segment_size; i++) {
				for (int j = 0; j < m; j++) {
					int a;
					file >> a;
					matrix[linie][j] = a;
				}
				linie++;
			}

		}
		auto t_start = chrono::steady_clock::now();

		linie = 0;
		for (int p = 1; p < world_size; p++) {
			for (int i = 0; i < segment_size; i++) {
				MPI_Send(*(matrix + linie), m, MPI_INT, p, linie, MPI_COMM_WORLD);
				linie++;
			}
		}
		file.close();

		//read calculated matrix

		for (int p = 1; p < world_size; p++) {
			for (int i = 0; i < segment_size; i++) {
				MPI_Recv(*(matrix + (p - 1) * segment_size + i), m, MPI_INT, p, (p - 1) * segment_size + i + 999999, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
			}
		}

		auto t_final = chrono::steady_clock::now();

		//write matrix

		ofstream out("output2.txt");
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				out << matrix[i][j] << " ";
			}
			out << endl;
		}
		out.close();

		//auto t_final = chrono::steady_clock::now();
		auto diff = t_final - t_start;
		cout << chrono::duration <double, milli>(diff).count() * 1000000;
		
	}
	else {
		MPI_Status status;
		//MPI_Recv(convMatrixPointer, 9, MPI_INT, 0, 99999, MPI_COMM_WORLD, &status);

		int** matrix = new int* [segment_size + 2];
		for (int i = 0; i < segment_size + 2; i++) {
			matrix[i] = new int[m];
		}

		for (int i = 1; i <= segment_size; i++) {
			MPI_Recv(*(matrix + i), m, MPI_INT, 0, (world_rank - 1) * segment_size + (i-1), MPI_COMM_WORLD, MPI_STATUS_IGNORE);
		}

		for (int i = 0; i < m; i++) {
			matrix[0][i] = 0;
			matrix[segment_size + 1][i] = 0;
		}

		//read from above and send below
		if (world_rank != 1) {
			MPI_Recv(*(matrix + 0), m, MPI_INT, world_rank - 1, (world_rank - 1) * 400000, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
		}
		if (world_rank != (world_size - 1)) {
			MPI_Send(*(matrix + segment_size), m, MPI_INT, world_rank + 1, world_rank * 400000, MPI_COMM_WORLD);
		}

		//read from below and send above
		if (world_rank != (world_size -1)) {
			MPI_Recv(*(matrix + (segment_size + 1)), m, MPI_INT, world_rank + 1, (world_rank + 1) * 700000, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
		}
		if (world_rank != 1) {
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

		//write to process 0
		for (int i = 1; i <= segment_size; i++) {
			MPI_Send(*(matrix + i), m, MPI_INT, 0, (world_rank - 1) * segment_size + (i - 1) + 999999, MPI_COMM_WORLD);
		}

	}

	MPI_Finalize();
}


