import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    private static final String SERVER_IP = "127.0.0.1"; // Adresa IP a serverului
    private static final int SERVER_PORT = 12345;
    private static final int NUM_THREADS = 5; // Numărul de thread-uri

    private static Lock sendLock = new ReentrantLock();
    private static Lock receiveLock = new ReentrantLock();

    private static Lock lockDone = new ReentrantLock();
    private static Condition notDone = lockDone.newCondition();
    private static AtomicInteger clientsDone = new AtomicInteger(0);
    private static Integer timeToSleep;

    public static void main(String[] args) {
        try {
            var startTime = System.currentTimeMillis();
            timeToSleep = Integer.valueOf(args[0]);
            List<Thread> threads = new ArrayList<>();
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());


            for (int i = 1; i <= NUM_THREADS; i++) {
                Thread thread = new Thread(new ClientThread(i, outputStream, inputStream));
                threads.add(thread);
                thread.start();
            }
            //n
            lockDone.lock();
            while (clientsDone.get() != NUM_THREADS) {
                notDone.await();
            }
            lockDone.unlock();
            inputStream.close();
            outputStream.close();
            socket.close();

            for (Thread thread : threads) {
                thread.join();
            }
            var endTime = System.currentTimeMillis();
            System.out.println(endTime - startTime);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientThread implements Runnable {
        private final int countryNr;
        private final ObjectOutputStream objectOutputStream;
        private final ObjectInputStream objectInputStream;

        public ClientThread(int countryNr, ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream) {
            this.countryNr = countryNr;
            this.objectOutputStream = objectOutputStream;
            this.objectInputStream = objectInputStream;
        }

        @Override
        public void run() {

            try {
                readDataFromFile(countryNr);

                String command = "clasamente_finale";
                sendLock.lock();
                objectOutputStream.writeObject(command);
                sendLock.unlock();
                Main.clientsDone.incrementAndGet();
                lockDone.lock();
                notDone.signalAll();
                lockDone.unlock();

                Socket socketFinal = new Socket(SERVER_IP, SERVER_PORT);
//                ObjectInputStream inputStreamFinal = new ObjectInputStream(socketFinal.getInputStream());
//
//
//                String clasamentFinalConc = (String) inputStreamFinal.readObject();
//                String clasamentFinalTari = (String) inputStreamFinal.readObject();


//                FileWriter writerCountries = new FileWriter("outputCountries" + "_" + countryNr + ".txt");
//                FileWriter writerConcurents = new FileWriter("outputConcurrents" + "_" + countryNr + ".txt");
//
//                writerConcurents.write(clasamentFinalConc);
//                writerCountries.write(clasamentFinalTari);
//
//                writerConcurents.close();
//                writerCountries.close();
//                inputStreamFinal.close();

                InputStream inputStream = socketFinal.getInputStream();
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                        new FileOutputStream("output2" + "_" + countryNr + ".txt"));

                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    bufferedOutputStream.write(buffer, 0, bytesRead);
                }

                System.out.println("File received successfully.");
                bufferedOutputStream.close();
//
//                OutputStream outputStream = socketFinal.getOutputStream();
//                outputStream.write("Done".getBytes());
//
//                bufferedOutputStream = new BufferedOutputStream(
//                        new FileOutputStream("output2_country" + "_" + countryNr + ".txt"));
//
//
//                while ((bytesRead = inputStream.read(buffer)) != -1) {
//                    bufferedOutputStream.write(buffer, 0, bytesRead);
//                }
//
//                System.out.println("File countries successfully.");
//                bufferedOutputStream.close();
//                outputStream.close();
                inputStream.close();

                socketFinal.close();

            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

        private void readDataFromFile(int countryNr) throws IOException, InterruptedException {

            List<String> fileList = new ArrayList<>();
            List<MyNode> concurentList = new ArrayList<>();
            int lastIndex = 0;
            for (int j = 1; j <= 10; j++) {
                String file = "Rezultate" + countryNr + "_" + j + ".txt";
                fileList.add(file);
            }

            for (var currentFile : fileList) {
                try (BufferedReader br = new BufferedReader(new FileReader(currentFile))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (concurentList.size() % 20 == 0) {
                            List<MyNode> listToSend = new ArrayList<>(concurentList.subList(lastIndex, concurentList.size()));
                            sendLock.lock();
                            this.objectOutputStream.writeObject(listToSend);
                            sendLock.unlock();
                            lastIndex = concurentList.size();
                            //concurentList.clear();
                            Thread.sleep(timeToSleep);
                        }
                        MyNode myNode = new MyNode(Integer.parseInt(line.split(",")[0]), Integer.parseInt(line.split(",")[1]), Integer.parseInt(String.valueOf(currentFile.charAt(9))));
                        concurentList.add(myNode);
                    }
                    System.out.println("Thread " + countryNr + " finished file: " + currentFile + "\n");

                    //country tops

                    String command = "clasament_tari";
                    sendLock.lock();
                    receiveLock.lock();
                    objectOutputStream.writeObject(command);


                    Map<Integer, Integer> countryTops = (Map<Integer, Integer>) objectInputStream.readObject();
                    sendLock.unlock();
                    receiveLock.unlock();


                    StringBuilder resultCountryTop = new StringBuilder();
                    for (var pair : countryTops.entrySet()) {
                        resultCountryTop.append("Country id: ").append(pair.getKey()).append(", Score: ").append(pair.getValue()).append(", from thread: ").append(countryNr).append("\n");
                    }

                    System.out.println(resultCountryTop);


                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
            if (concurentList.size() > 0) {
                List<MyNode> listToSend = new ArrayList<>(concurentList.subList(lastIndex, concurentList.size()));
                sendLock.lock();
                this.objectOutputStream.writeObject(listToSend);
                sendLock.unlock();
                //concurentList.clear();
                //Thread.sleep(10000);
            }

        }
    }
}
