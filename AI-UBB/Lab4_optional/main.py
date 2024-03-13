from Ant_Colony import AntColony
from GraphProcessor import GraphProcessor

import matplotlib.pyplot as plt

# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    graphProcessor = GraphProcessor("xqf131.tsp")
    cost, feromon = graphProcessor.create_network()
    antColony = AntColony(cost, feromon)

    best = 1000000
    lengths=[]

    for i in range(1000):
        length = antColony.oneAnt()
        lengths.append(length)
        print(i, length)
        if best > length:
            best = length
    plt.plot(lengths)
    plt.show()
    print(best)
# See PyCharm help at https://www.jetbrains.com/help/pycharm/
