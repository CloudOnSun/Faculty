class Utils:

    @staticmethod
    def read_data():
        trainIn = []
        trainOut = []
        validIn = []
        validOut = []
        with open('data_emotii/fer2013.csv', newline='') as file:
            for line in file:
                line = line.replace('\n', '')
                d = []
                features = line.split(",")
                if len(features) > 0:
                    if features[len(features)-1] == 'Training':
                        feats = features[:len(features)-1]
                        feats[0] = int(feats[0])
                        pixels = feats[1].split(" ")
                        d = [int(p) for p in pixels]
                        trainIn.append(d)

                        d = [0, 0, 0, 0, 0, 0, 0]
                        d[feats[0]] = 1
                        trainOut.append(d)
                    elif features[len(features)-1] != 'Usage':
                        feats = features[:len(features) - 1]
                        feats[0] = int(feats[0])
                        pixels = feats[1].split(" ")
                        d = [int(p) for p in pixels]
                        validIn.append(d)

                        d = [0, 0, 0, 0, 0, 0, 0]
                        d[feats[0]] = 1
                        validOut.append(d)


        return trainIn, trainOut, validIn, validOut
