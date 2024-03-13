

class DataReader:

    def __init__(self, path: str):
        self.__path = path

    def read_file(self):

        dataIn = []
        dataOut = []
        with open(self.__path, newline='') as file:
            for line in file:
                line = line.replace('\n', '')
                d = []
                features = line.split(",")
                if len(features) > 0:
                    for i in range(len(features) - 1):
                        d.append(float(features[i]))
                    dataIn.append(d)
                    dataOut.append(features[len(features) - 1])

        return dataIn, dataOut