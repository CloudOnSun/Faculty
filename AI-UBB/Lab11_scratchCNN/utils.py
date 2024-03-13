class Utils:

    @staticmethod
    def read_data():
        inputs = []
        outputs = []
        i = 0
        with open('data_emotii/fer2013.csv', newline='') as file:
            for line in file:
                line = line.replace('\n', '')
                d = []
                features = line.split(",")
                if len(features) > 0 and features[len(features) - 1] != 'Usage':
                    i += 1
                    if i > 10000:
                        break
                    feats = features[:len(features)-1]
                    feats[0] = int(feats[0])
                    pixels = feats[1].split(" ")
                    d = [int(p) for p in pixels]
                    inputs.append(d)

                    # d = [0, 0, 0, 0, 0, 0, 0]
                    # d[feats[0]] = 1
                    outputs.append(feats[0])

        return inputs, outputs
