import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    private static final int PORT = 12345;
    private static int producers;
    private static int consumers;
    private static int deltaT;
    private static int type;

    private static Lock lockFinale = new ReentrantLock();
    private static Condition notDone = lockFinale.newCondition();

    public static boolean done = false;

    public static boolean compareFiles() throws IOException {
        BufferedReader br1 = new BufferedReader(new FileReader("output1.txt"));
        BufferedReader br2 = new BufferedReader(new FileReader("output2.txt"));
        String line1;
        String line2;
        while ((line1 = br1.readLine()) != null && (line2 = br2.readLine()) != null) {
            if (!line1.equals(line2)) {
                return false;
            }
        }
        return true;
    }

    public static void writeFile(String fileName, FinalList rezultate) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        StringBuilder line = new StringBuilder();

        for (var node : rezultate.getTheList()) {
            line.append(node.id).append(",").append(node.punctaj).append("\n");
            writer.write(line.toString());
            line.setLength(0);
        }
        writer.close();
        FileWriter writer2 = new FileWriter("clasament_tari.txt");
        line.setLength(0);
        var clasamentTariFinal = getClasamentTari(rezultate);
        for (var entry : clasamentTariFinal.entrySet()) {
            line.append(entry.getKey()).append(",").append(entry.getValue()).append("\n");
        }
        writer2.write(line.toString());
        writer2.close();
    }

    public static Map<Integer, Integer> getClasamentTari(FinalList rezultate) {
        List<MyNode> rezultatePartiale = rezultate.getTheList();
        Map<Integer, Integer> clasament = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            clasament.put(i, 0);
        }
        for (var rez : rezultatePartiale) {
            clasament.put(rez.tara, clasament.get(rez.tara) + rez.punctaj);
        }
        return clasament;
    }

//    public static void processClients() throws IOException, InterruptedException {
//        int nrClients = 0;
//        BlockingQueue coadaCitire = new BlockingQueue();
//        FinalList listaRezultate = new FinalList();
//        IdLocksMap idLocksMap = new IdLocksMap();
//        ExecutorService pool = Executors.newFixedThreadPool(producers);
//        ServerSocket serverSocket = new ServerSocket(PORT);
//        Thread[] clients = new Thread[5];
//        while (nrClients < 5) {
//            Socket clientSocket = serverSocket.accept();
//            Thread t = new Thread(new ProcessorClient(clientSocket, coadaCitire, listaRezultate, idLocksMap, pool));
//            clients[nrClients] = t;
//            t.start();
//        }
//        Thread[] consumatoriTh = new Thread[consumers];
//        for (int i = 0; i < consumers; i++) {
//            consumatoriTh[i] = new Consumer(coadaCitire, listaRezultate, idLocksMap);
//            consumatoriTh[i].start();
//        }
//        for (int i = 0; i < 5; i++) {
//            clients[i].join();
//        }
//
//    }

    public static void main(String[] args) {
        try {
            producers = Integer.parseInt(args[0]);
            consumers = Integer.parseInt(args[1]);
            deltaT = Integer.parseInt(args[2]);
//            type = Integer.parseInt(args[3]);
//            if (type == 1) {
//                return;
//            }
            Map<Integer, Integer> clasamentTari = null;
            var lastTime = System.currentTimeMillis();
            ServerSocket serverSocket = new ServerSocket(PORT);
            //System.out.println("Serverul asculta pe portul " + PORT);

            BlockingQueue coadaCitire = new BlockingQueue();
            FinalList listaRezultate = new FinalList();
            IdLocksMap idLocksMap = new IdLocksMap();
            ExecutorService producersPool = Executors.newFixedThreadPool(producers);
            ExecutorService partialResultsPool = Executors.newSingleThreadExecutor();
            ExecutorService finalResultsPool = Executors.newSingleThreadExecutor();
            List<Future<Integer>> futureResultateFinale = new ArrayList<>();

            Thread[] consumatoriTh = new Thread[consumers];
            for (int i = 0; i < consumers; i++) {
                consumatoriTh[i] = new Consumer(coadaCitire, listaRezultate, idLocksMap);
                consumatoriTh[i].start();
            }

            Socket clientSocket = serverSocket.accept();
            //System.out.println("Client conectat la server");
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            Object receivedObject;
            try {
                while ((receivedObject = inputStream.readObject()) != null) {

                    if (receivedObject instanceof List) {
                        List<MyNode> concurentList = (List<MyNode>) receivedObject;
                        producersPool.execute(new Producer(coadaCitire, concurentList, idLocksMap));
                        //concurentList.clear();
                    }
                    if (receivedObject instanceof String command) {
                        if (command.equals("clasament_tari")) {
                            var nowTime = System.currentTimeMillis();
                            if ((nowTime - lastTime < deltaT) && clasamentTari != null) {
                                outputStream.writeObject(clasamentTari);
                            } else {
                                Future<Map<Integer, Integer>> clasamentFuture = partialResultsPool.submit(() -> getClasamentTari(listaRezultate));
                                clasamentTari = clasamentFuture.get();
                                outputStream.writeObject(clasamentTari);
                                lastTime = System.currentTimeMillis();
                            }
                        } else if (command.equals("clasamente_finale")) {
                            Socket newClientSocket = serverSocket.accept();
                            //System.out.println("Socket nou");
                            futureResultateFinale.add(finalResultsPool.submit(() -> {
                                lockFinale.lock();
                                try {
                                    FileInputStream fileInputStream = new FileInputStream("output2.txt");
                                    BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                                    OutputStream binaryStream = newClientSocket.getOutputStream();
                                    InputStream inputBinaryStream = newClientSocket.getInputStream();

                                    byte[] buffer = new byte[1024];
                                    int bytesRead;

                                    while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                                        binaryStream.write(buffer, 0, bytesRead);
                                    }
                                    bufferedInputStream.close();
//                                    inputBinaryStream.read(buffer);
//
//                                    fileInputStream = new FileInputStream("clasament_tari.txt");
//                                    bufferedInputStream = new BufferedInputStream(fileInputStream);
//                                    while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
//                                        binaryStream.write(buffer, 0, bytesRead);
//                                    }
//                                    bufferedInputStream.close();

//                                    ObjectOutputStream outputStreamFinale = new ObjectOutputStream(newClientSocket.getOutputStream());
//                                    if (!Main.done) {
//                                        notDone.await();
//                                    }
//                                    //lista rezultate concurenti
//                                    StringBuilder line = new StringBuilder();
//                                    for (var node : listaRezultate.getTheList()) {
//                                        line.append(node.id).append(",").append(node.punctaj).append("\n");
//                                    }
//                                    outputStreamFinale.writeObject(line.toString());
//
//                                    //lista rezultate tari
//                                    line.setLength(0);
//                                    var clasamentTariFinal = getClasamentTari(listaRezultate);
//                                    for (var entry : clasamentTariFinal.entrySet()) {
//                                        line.append(entry.getKey()).append(",").append(entry.getValue()).append("\n");
//                                    }
//                                    outputStreamFinale.writeObject(line.toString());
                                    inputBinaryStream.close();
                                    binaryStream.close();
                                    newClientSocket.close();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                } finally {
                                    lockFinale.unlock();
                                    return 0;
                                }
                            }));
                        }
                    }
                }
            } catch (EOFException e) {
                inputStream.close();
                clientSocket.close();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
            finalResultsPool.shutdown();
            partialResultsPool.shutdown();
            producersPool.shutdown();
            boolean gata = false;
            while (!gata) {
                gata = producersPool.awaitTermination(10, TimeUnit.SECONDS) &&
                        partialResultsPool.awaitTermination(10, TimeUnit.SECONDS);
            }
            Main.done = true;
            coadaCitire.notifica();
            for (int i = 0; i < consumers; i++) {
                consumatoriTh[i].join();
            }
            writeFile("output2.txt", listaRezultate);

            lockFinale.lock();
            notDone.signalAll();
            lockFinale.unlock();
            gata = false;
            while (!gata) {
                gata = finalResultsPool.awaitTermination(10, TimeUnit.SECONDS);
            }
            for (var f : futureResultateFinale) {
                f.get();
            }

            if (compareFiles()) {
                System.out.println("Files are the same");
            } else {
                System.out.println("Files are NOT the same");
            }

        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
