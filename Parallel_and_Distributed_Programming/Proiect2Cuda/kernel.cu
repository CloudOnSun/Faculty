
#include "cuda_runtime.h"
#include "device_launch_parameters.h"

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


int calcElementConv(int* matrix, int* convMatrix, int linie, int col, int n, int m, int k) {
    int linInceput = linie - k / 2;
    int linFinal = linie + k / 2;
    int colInceput = col - k / 2;
    int colFinal = col + k / 2;
    int suma = 0;
    for (int i = linInceput; i <= linFinal; i++) {
        for (int j = colInceput; j <= colFinal; j++) {
            if (i >= 0 && j >= 0 && i < n && j < m) {
                suma += matrix[i*n + j] * convMatrix[(i - linInceput) * k + j - colInceput];
            }
        }
    }
    return suma;
}

void citireMatrici(int* matrix, int* convMatrix, string fileName, int n, int m, int k) {
    ifstream file(fileName);
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            int a;
            file >> a;
            matrix[i*n + j] = a;
        }
    }
    for (int i = 0; i < k; i++) {
        for (int j = 0; j < k; j++) {
            int a;
            file >> a;
            convMatrix[i*k + j] = a;
        }
    }
    file.close();
}

cudaError_t addWithCuda(int* matrix, int* convMatrix, int n, int m, int k);

__global__ void addKernel(int* dev_matrix, int* dev_conv_matrix, int* new_matrix, int n, int m, int k)
{
    int id = blockIdx.x * blockDim.x + threadIdx.x;
    int linie = id / m;
    int coloana = id % m;
    if (linie < n && linie >= 0 && coloana < m && coloana >= 0) {
        int linInceput = linie - k / 2;
        int linFinal = linie + k / 2;
        int colInceput = coloana - k / 2;
        int colFinal = coloana + k / 2;
        int suma = 0;
        for (int i = linInceput; i <= linFinal; i++) {
            for (int j = colInceput; j <= colFinal; j++) {
                if (i >= 0 && j >= 0 && i < n && j < m) {
                    suma += dev_matrix[i * n + j] * dev_conv_matrix[(i - linInceput) * k + j - colInceput];
                }
            }
        }
        new_matrix[linie * n + coloana] = suma;
    }
}

void writeFile(string numeFisier, int* newMatrix, int n, int m) {
    ofstream out(numeFisier);
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            out << newMatrix[i*n + j] << " ";
        }
        out << endl;
    }
    out.close();
}

void calcSeq(int* matrix, int* convMatrix, int n, int m, int k) {
    int* firstAux = new int[m];
    int* secondAux = new int[m];
    for (int i = 0; i < n; i++) {
        if (i > 1) {
            for (int j = 0; j < m; j++) {
                matrix[(i - 2) * n + j] = firstAux[j];
            }
        }
        for (int j = 0; j < m; j++) {
            firstAux[j] = secondAux[j];
        }
        for (int j = 0; j < m; j++) {
            secondAux[j] = calcElementConv(matrix, convMatrix, i, j, n, m, k);
        }
    }
    for (int j = 0; j < m; j++) {
        matrix[(n - 2) * n + j] = firstAux[j];
        matrix[(n - 1) * n + j] = secondAux[j];
    }
    delete firstAux;
    delete secondAux;
}

int main(int argc, char* argv[])
{
    int n;
    int m;
    int k = 3;
    int seqOrParam;
    string fileName;

    fileName = argv[1];
    seqOrParam = stoi(argv[2]);
    n = stoi(argv[3]);
    m = stoi(argv[4]);

    int* matrix = new int [n*m];
    int* convMatrix = new int [k*k];

    citireMatrici(matrix, convMatrix, fileName, n, m, k);

    if (seqOrParam == 0) {

        auto t_start = chrono::steady_clock::now();
        calcSeq(matrix, convMatrix, n, m, k);
        auto t_final = chrono::steady_clock::now();
        writeFile("output1.txt", matrix, n, m);
        auto diff = t_final - t_start;
        cout << chrono::duration <double, milli>(diff).count() * 1000000;

    }
    else {
        auto t_start = chrono::steady_clock::now();
        // Add vectors in parallel.
        cudaError_t cudaStatus = addWithCuda(matrix, convMatrix, n, m, k);
        if (cudaStatus != cudaSuccess) {
            fprintf(stderr, "addWithCuda failed!");
            return 1;
        }

        auto t_final = chrono::steady_clock::now();
        writeFile("output2.txt", matrix, n, m);
        auto diff = t_final - t_start;
        cout << chrono::duration <double, milli>(diff).count() * 1000000;

        // cudaDeviceReset must be called before exiting in order for profiling and
        // tracing tools such as Nsight and Visual Profiler to show complete traces.
        cudaStatus = cudaDeviceReset();
        if (cudaStatus != cudaSuccess) {
            fprintf(stderr, "cudaDeviceReset failed!");
            free(matrix);
            free(convMatrix);
            return 1;
        }
    }
    free(matrix);
    free(convMatrix);
    return 0;
}

// Helper function for using CUDA to add vectors in parallel.
cudaError_t addWithCuda(int* matrix, int* convMatrix, int n, int m, int k)
{
    int *dev_matrix = 0;
    int *dev_conv_matrix = 0;
    int *new_matrix = 0;
    cudaError_t cudaStatus;

    // Choose which GPU to run on, change this on a multi-GPU system.
    cudaStatus = cudaSetDevice(0);
    if (cudaStatus != cudaSuccess) {
        fprintf(stderr, "cudaSetDevice failed!  Do you have a CUDA-capable GPU installed?");
        goto Error;
    }

    // Allocate GPU buffers.
    cudaStatus = cudaMalloc(&dev_matrix, n * m * sizeof(int));
    if (cudaStatus != cudaSuccess) {
        fprintf(stderr, "cudaMalloc failed for dev_matrix: %s\n", cudaGetErrorString(cudaStatus));
        goto Error;
    }

    cudaStatus = cudaMalloc(&new_matrix, n * m * sizeof(int));
    if (cudaStatus != cudaSuccess) {
        fprintf(stderr, "cudaMalloc failed for new_matrix: %s\n", cudaGetErrorString(cudaStatus));
        goto Error;
    }

    cudaStatus = cudaMalloc(&dev_conv_matrix, k * k * sizeof(int));
    if (cudaStatus != cudaSuccess) {
        fprintf(stderr, "cudaMalloc failed for dev_conv_matrix: %s\n", cudaGetErrorString(cudaStatus));
        goto Error;
    }

    // Copy input vectors from host memory to GPU buffers.
    cudaStatus = cudaMemcpy(dev_matrix, matrix, n * m * sizeof(int), cudaMemcpyHostToDevice);
    if (cudaStatus != cudaSuccess) {
        fprintf(stderr, "cudaMemcpy failed for dev_matrix: %s\n", cudaGetErrorString(cudaStatus));
        goto Error;
    }

    cudaStatus = cudaMemcpy(dev_conv_matrix, convMatrix, k * k * sizeof(int), cudaMemcpyHostToDevice);
    if (cudaStatus != cudaSuccess) {
        fprintf(stderr, "cudaMemcpy failed for dev_conv_matrix: %s\n", cudaGetErrorString(cudaStatus));
        goto Error;
    }

    int threadsPerBlock = 1024;
    int blocks = (int)ceil((float)n * m / threadsPerBlock);

    // Launch a kernel on the GPU with one thread for each element.
    addKernel<<<blocks, threadsPerBlock>>>(dev_matrix, dev_conv_matrix, new_matrix, n, m, k);

    // Check for any errors launching the kernel
    cudaStatus = cudaGetLastError();
    if (cudaStatus != cudaSuccess) {
        fprintf(stderr, "addKernel launch failed: %s\n", cudaGetErrorString(cudaStatus));
        goto Error;
    }
    
    // cudaDeviceSynchronize waits for the kernel to finish, and returns
    // any errors encountered during the launch.
    cudaStatus = cudaDeviceSynchronize();
    if (cudaStatus != cudaSuccess) {
        fprintf(stderr, "cudaDeviceSynchronize returned error code %d after launching addKernel!\n", cudaStatus);
        goto Error;
    }

    // Copy output vector from GPU buffer to host memory.
    cudaStatus = cudaMemcpy(matrix, new_matrix, n * m * sizeof(int), cudaMemcpyDeviceToHost);
    if (cudaStatus != cudaSuccess) {
        fprintf(stderr, "cudaMemcpy failed for dev_matrix (device to host): %s\n", cudaGetErrorString(cudaStatus));
        goto Error;
    }

Error:
    cudaFree(dev_matrix);
    cudaFree(dev_conv_matrix);
    cudaFree(new_matrix);
    
    return cudaStatus;
}
