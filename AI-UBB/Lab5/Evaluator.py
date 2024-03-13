class Evaluator:

    def evalClassification(self, realLabels, computedLabels, labelNames=None):
        from sklearn.metrics import confusion_matrix, accuracy_score, precision_score, recall_score

        acc = accuracy_score(realLabels, computedLabels)
        precision = precision_score(realLabels, computedLabels, average=None, labels=labelNames)
        recall = recall_score(realLabels, computedLabels, average=None, labels=labelNames)
        return acc, precision, recall

    def evalRegression(self, realOutputs, computedOutputs):
        errors = []
        for real, computed in zip(realOutputs, computedOutputs):
            errorL1 = sum(abs(r - c) for r, c in zip(real, computed)) / len(real)
            errors.append(errorL1)

        return errors
