#include "inputFilmeWidget.h"
#include <qformlayout.h>


InputFilmeWidget::InputFilmeWidget(QWidget* parent) : QWidget(parent)
{
	this->lineTitlu = new QLineEdit;
	this->linieGen = new QLineEdit;
	this->linieAnAparitie = new QSpinBox;
	linieAnAparitie->setMinimum(1900);
	linieAnAparitie->setMaximum(2022);
	this->linieActorPrincipal = new QLineEdit;
	QFormLayout* formLayout = new QFormLayout;
	formLayout->addRow("Titlu", lineTitlu);
	formLayout->addRow("Gen", linieGen);
	formLayout->addRow("An Aparitie", linieAnAparitie);
	formLayout->addRow("Actor Principal", linieActorPrincipal);
	this->setLayout(formLayout);
}


QLineEdit* InputFilmeWidget::getLinieTitlu()
{
	return this->lineTitlu;
}


QLineEdit* InputFilmeWidget::getLinieGen()
{
	return this->linieGen;
}


QSpinBox* InputFilmeWidget::getLinieAnAparitie()
{
	return this->linieAnAparitie;
}


QLineEdit* InputFilmeWidget::getLinieActorPrincipal()
{
	return this->linieActorPrincipal;
}
