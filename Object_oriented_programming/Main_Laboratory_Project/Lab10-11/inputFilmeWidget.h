#pragma once
#include <QWidget>
#include <QLineEdit>
#include <qspinbox.h>
#include <qlayout.h>


class InputFilmeWidget :public QWidget
{

private:

	QLineEdit* lineTitlu;
	QLineEdit* linieGen;
	QSpinBox* linieAnAparitie;
	QLineEdit* linieActorPrincipal;

public:

	InputFilmeWidget(QWidget* parent = nullptr);

	QLineEdit* getLinieTitlu();
	QLineEdit* getLinieGen();
	QSpinBox* getLinieAnAparitie();
	QLineEdit* getLinieActorPrincipal();
};