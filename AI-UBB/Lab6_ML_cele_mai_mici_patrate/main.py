from ML import Machine_Learning

# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    ml = Machine_Learning("input_data/v3_world-happiness-report-2017.csv",
                          ["Economy..GDP.per.Capita.", "Freedom"],
                          ["Happiness.Score"])
    ml.learn_on_the_model()

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
