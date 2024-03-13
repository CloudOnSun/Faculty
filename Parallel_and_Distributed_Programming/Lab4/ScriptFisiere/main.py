from random import randint, random

for i in range(1,6):
    populatie = randint(80,100)
    for j in range(1, 11):
        file = "Rezultate" + str(i) + "_" + str(j) + ".txt"
        with open(file, "w") as f:
            for k in range (1, populatie + 1):
                p = randint(0, 10)
                prob = random()
                if prob < 0.03:
                    p = -1
                id = i * 1000 + k
                line = str(id) + "," + str(p) + "\n"
                f.write(line)

for i in range(1,6):
    file = "Fisier" + str(i) + ".txt"
    with open(file, "w") as f:
        for j in range(1, 100):
            line = "\n"
            f.write(line)