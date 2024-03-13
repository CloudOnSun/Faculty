from ML import Machine_Learning
from MultiTargetRegression import MLMutliTarget

# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    ml = Machine_Learning("world-happiness-report-2017.csv",
                          ["Economy..GDP.per.Capita.", "Freedom"],
                          ["Happiness.Score"])
    ml.learn_model_tool(2)
    ml.learn_the_model_manual(2)

    ml2 = Machine_Learning("world-happiness-report-2017.csv",
                          ["Economy..GDP.per.Capita."],
                          ["Happiness.Score"])
    ml2.learn_model_tool(1)
    ml2.learn_the_model_manual(1)

    print()
    print("---------- Optional: MultiTarget Independente -----------")
    ml3 = MLMutliTarget()
    ml3.learn_the_model()